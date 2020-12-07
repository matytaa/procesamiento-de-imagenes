package core.action.edgedetector.hough;

import dominio.PuntoXY;
import dominio.customimage.Imagen;
import dominio.hough.LineaRhoTheeta;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.Map;

public class TransformadaHoughLinearAction {

    private final int thetaLimiteInferior = -90;
    private final int thetaLimiteSuperior = 90;
    private double rhoLimiteInferior;
    private double rhoLimiteSuperior;

    private Map<LineaRhoTheeta, Integer> matrizParametro;

    public Imagen ejecutar(Imagen imagenOriginal, Imagen imageConBordesDetectados, int rho, int theeta, double tolerancia) {

        this.rhoLimiteInferior = 0;
        this.rhoLimiteSuperior = this.calcularDiagonal(imageConBordesDetectados.getAncho(), imageConBordesDetectados.getAltura());
        this.crearMatrizParametro(rho, theeta);

        int[][] canal = imageConBordesDetectados.getMatrizRed();
        
        for (int x=0; x < canal.length; x++) {
            for (int y=0; y < canal[x].length; y++) {
                
                if (canal[x][y] == 255) this.evaluarTodasLasLineas(x,y, tolerancia);
                
            }
        }

        int votoMaximo = this.encontrarVotoMaximo();
        int limite = (int)(votoMaximo - (double)votoMaximo*0.10);

        Map<LineaRhoTheeta, Integer> lineasAceptadas = this.encontrarLimitesAceptados(limite);

        return this.dibujarLineas(imagenOriginal, imageConBordesDetectados.getAncho(), imageConBordesDetectados.getAltura(), lineasAceptadas);

    }

    private Imagen dibujarLineas(Imagen originalImage, Integer ancho, Integer alto, Map<LineaRhoTheeta, Integer> lineasAceptadas) {

        WritableImage imagen = new WritableImage(ancho, alto);
        PixelWriter writer = imagen.getPixelWriter();

        for(int x=0; x < ancho; x++) {
            for (int y=0; y < alto; y++) {
                writer.setColor(x,y, Color.rgb(originalImage.getPromedioPixel(x,y), originalImage.getPromedioPixel(x,y), originalImage.getPromedioPixel(x,y)));
            }
        }

        for(Map.Entry<LineaRhoTheeta, Integer>  entry : lineasAceptadas.entrySet()) {

            double rhoActual = entry.getKey().getRho();
            double theetaActual = entry.getKey().getTheta();

            PuntoXY puntoDeInicio = this.getPuntoDeInicioDeLinea(rhoActual,theetaActual, alto);
            PuntoXY puntoDeFin = this.getPuntoDeFinDeLinea(rhoActual, theetaActual, alto, ancho);

            Line linea = new Line(puntoDeInicio.getX(), puntoDeInicio.getY(), puntoDeFin.getX(), puntoDeFin.getY());

            for (int x=0; x < ancho; x++) {
                for (int y=0; y < alto; y++) {
                    if (linea.contains(x,y)) writer.setColor(x,y, Color.RED);
                }
            }

        }

        return new Imagen(imagen, "png");

    }

    private PuntoXY getPuntoDeFinDeLinea(double rho, double theeta, int alto, int ancho) {

        //LINEA VERTICAL
        if (theeta == 0) {
            return new PuntoXY((int)rho, alto-1);
        }

        double intercepcion = this.getIntercepcion(rho, theeta);
        double pendiente = this.getPendiente(theeta);
        double cruceLimiteDerecho = pendiente*ancho + intercepcion;

        //La línea está disminuyendo y el segmento 'termina' en el límite inferior de la imagen, de ahí las fórmulas para x cuando y = L
        //x = (L-b)/m, y=L
        if (cruceLimiteDerecho < 0 && pendiente < 0) {
            return new PuntoXY((int)(-intercepcion/pendiente), alto);
        }

        //La línea está aumentando y el segmento 'termina' en el límite superior de la imagen, de ahí las fórmulas para x cuando y = 0
        //x = -b/m, y=0
        if (cruceLimiteDerecho > alto && pendiente > 0) {
            return new PuntoXY((int)((alto - intercepcion)/pendiente),0);
        }

        //En cualquier otro caso, el segmento 'termina' en el límite de la imagen derecha, e y es el cruce del límite derecho
        return new PuntoXY(ancho, (int)cruceLimiteDerecho);

    }

    private PuntoXY getPuntoDeInicioDeLinea(double rho, double theeta, int alto) {

        //LINEA VERTICAL
        if (theeta == 0) {
            return new PuntoXY((int)rho, 0);
        }

        double intercepcion = this.getIntercepcion(rho, theeta);
        double pendiente = this.getPendiente(theeta);

        //La línea está disminuyendo y el segmento 'comienza' en el límite superior de la imagen, de ahí las fórmulas para x cuando y = 0
        //x = -b/m, y=0
        if (intercepcion > alto && pendiente < 0) {
            return new PuntoXY((int)((alto - intercepcion)/pendiente), 0);
        }

// La línea está aumentando y el segmento 'comienza' en el límite inferior de la imagen, de ahí las fórmulas para x cuando y = L        //x = (L-b)/m, y=L
        if (intercepcion < 0 && pendiente > 0) {
            return new PuntoXY((int)(-intercepcion/pendiente), alto);
        }

        // En cualquier otro caso, el segmento 'comienza' en el límite de la imagen izquierda, e y es la intersección
        return new PuntoXY(0, (int)intercepcion);

    }

    private double getPendiente(double theta) {

        if (Math.abs(theta) == 90) return 0;

        return -1.0/Math.tan(Math.toRadians(theta)); //Recordar que la tangente de theta me da la pendiente de la semirrecta perpendicular a la recta de interes. Entonces, esta tiene pendiente opuesta e inversa.
    }

    private double getIntercepcion(double rho, double theta) {
        return rho/Math.sin(Math.toRadians(theta)); //De esta forma se calcula la ordenada al origen
    }

    private Map<LineaRhoTheeta,Integer> encontrarLimitesAceptados(int limite) {

        Map<LineaRhoTheeta, Integer> limitesAceptados = new HashMap<>();

        for (Map.Entry<LineaRhoTheeta, Integer> entry : this.matrizParametro.entrySet()) {
            if (entry.getValue() >= limite) limitesAceptados.put(entry.getKey(), entry.getValue());
        }

        return limitesAceptados;
    }

    private int encontrarVotoMaximo() {
        int maximo = 0;
        for (Map.Entry<LineaRhoTheeta, Integer> entry : this.matrizParametro.entrySet()) {
            if (entry.getValue() > maximo) maximo = entry.getValue();
        }
        return maximo;
    }

    private void crearMatrizParametro(int rho, int theeta) {

        this.matrizParametro = new HashMap<>();

        double largoParticionRho = (this.rhoLimiteSuperior - this.rhoLimiteInferior)/(double)rho;
        double largoParticionTheeta = (this.thetaLimiteSuperior - this.thetaLimiteInferior)/(double)theeta;

        for (int i=0; i < rho; i++) {
            for (int j=0; j < theeta; j++) {

                LineaRhoTheeta rhoTheetaActual = new LineaRhoTheeta(this.rhoLimiteInferior + i*largoParticionRho, this.thetaLimiteInferior + j*largoParticionTheeta);
                this.matrizParametro.put(rhoTheetaActual, 0);

            }
        }

    }

    private double calcularDiagonal(Integer ancho, Integer alto) {
        return Math.sqrt(Math.pow(ancho,2) + Math.pow(alto,2));
    }

    //Este metodo actualiza la matriz para un cierto punto x y dado
    private void evaluarTodasLasLineas(int x, int y, double tolerancia) {

        Map<LineaRhoTheeta, Integer> matrizActualizada = new HashMap<>();
        
        for(Map.Entry<LineaRhoTheeta, Integer> entry : this.matrizParametro.entrySet()) {

            LineaRhoTheeta rhoTheetaActual = entry.getKey();
            double rhoActual = rhoTheetaActual.getRho();
            double theetaActual = rhoTheetaActual.getTheta();
            int votoActual = this.matrizParametro.get(rhoTheetaActual);

            if (this.evaluarLinea(x,y, rhoActual, theetaActual, tolerancia)) {
                matrizActualizada.put(rhoTheetaActual, votoActual+1);
            } else {
                matrizActualizada.put(rhoTheetaActual, votoActual);
            }
        }
        this.matrizParametro = matrizActualizada;

    }

    private boolean evaluarLinea(int x, int y, double rho, double theeta, double tolerancia) {
        if (Math.abs((x*Math.cos(Math.toRadians(theeta)) + y*Math.sin(Math.toRadians(theeta)) - rho)) < tolerancia) {
            return true;
        }
        return false;
    }
}

package core.action.threshold;

import core.service.AplicarUmbralService;
import core.service.MatrizService;
import dominio.automaticthreshold.GruposDeUmbralesGlobales;
import dominio.automaticthreshold.ResultadoUmbralGlobal;
import dominio.customimage.Imagen;
import dominio.customimage.Pixel;
import javafx.scene.image.Image;

public class AplicarEstimacionDeUmbralGlobalAction {

    private MatrizService matrizService;
    private AplicarUmbralService aplicarUmbralService;
    private int iteraciones;
    private int umbral;


    public AplicarEstimacionDeUmbralGlobalAction(MatrizService matrizService, AplicarUmbralService aplicarUmbralService){
        this.matrizService = matrizService;
        this.aplicarUmbralService = aplicarUmbralService;
        this.iteraciones = 0;
        this.umbral = 0;
    }

    public ResultadoUmbralGlobal ejecutar(Imagen customImage, int umbralInicial, int deltaT){
        int[][] matrizImagen = this.matrizService.aMatrizDeGrises(customImage.toFXImage());
        this.umbral = umbralInicial;
        int[][] imagenTransformada = new int[matrizImagen.length][matrizImagen[0].length];
        int actualDeltaT = 99999;
        this.iteraciones = 0;

        while (actualDeltaT > deltaT){
            imagenTransformada = this.aplicarUmbralService.aplicarUmbral(matrizImagen, umbral);
            GruposDeUmbralesGlobales gruposDeUmbralesGlobales = this.calcularGrupos(imagenTransformada);
            int m1 = this.calcularM1(matrizImagen, gruposDeUmbralesGlobales);
            int m2 = this.calcularM2(matrizImagen, gruposDeUmbralesGlobales);
            int oldUmbral = umbral;
            umbral = this.actualizarUmbral(m1, m2);
            actualDeltaT = Math.abs(oldUmbral - umbral);
            this.iteraciones++;
        }

        Image image = this.matrizService.toImage(imagenTransformada, imagenTransformada, imagenTransformada);
        return new ResultadoUmbralGlobal(image, this.iteraciones, this.umbral);
    }


    private GruposDeUmbralesGlobales calcularGrupos(int[][] imagen){
        int ancho = imagen.length;
        int alto = imagen[0].length;
        GruposDeUmbralesGlobales gruposDeUmbralesGlobales = new GruposDeUmbralesGlobales();

        for (int i=0; i < ancho; i++)
            for(int j=0; j < alto; j++)
                if (imagen[i][j] == 0) {
                    //No necesitamos el color, solo la posicion
                    Pixel pixelG1 = new Pixel(i, j, null);
                    gruposDeUmbralesGlobales.agregarPixelAlGrupo1(pixelG1);
                } else { //255
                    //No necesitamos el color, solo la posicion
                    Pixel pixelG2 = new Pixel(i, j, null);
                    gruposDeUmbralesGlobales.agregarPixelAlGrupo2(pixelG2);
                }

        return gruposDeUmbralesGlobales;
    }

    private int calcularM1(int[][] imagen, GruposDeUmbralesGlobales gruposDeUmbralesGlobales){
        Double m1 = 0.0;
        for (Pixel pixel : gruposDeUmbralesGlobales.getPixelsGrupo1())
            m1 += ((Double.valueOf((double)imagen[pixel.getX()][pixel.getY()])) / gruposDeUmbralesGlobales.getG1());
        return m1.intValue();
    }

    private int calcularM2(int[][] image, GruposDeUmbralesGlobales gruposDeUmbralesGlobales){
        Double m2 = 0.0;
        for (Pixel pixel : gruposDeUmbralesGlobales.getPixelsGrupo2())
            m2 += ((Double.valueOf((double)image[pixel.getX()][pixel.getY()])) / gruposDeUmbralesGlobales.getG2());
        return m2.intValue();
    }

    private int actualizarUmbral(int m1, int m2){
        return ((m1 + m2) / 2);
    }

}

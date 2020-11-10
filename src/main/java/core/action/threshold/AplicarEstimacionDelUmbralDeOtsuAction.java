package core.action.threshold;

import core.service.AplicarUmbralService;
import core.service.MatrizService;
import core.service.generation.HistogramaService;
import dominio.Histograma;
import dominio.automaticthreshold.EstimacionDelUmbralDeOtsuResultante;
import dominio.customimage.Imagen;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;


public class AplicarEstimacionDelUmbralDeOtsuAction {

    private HistogramaService histogramaService;
    private MatrizService matrizService;
    private AplicarUmbralService aplicarUmbralService;

    private int cantidadDeNivelesDeGris = 256;
    private double[] probabilidadesAcumuladas = new double[cantidadDeNivelesDeGris];
    private double[] mediasAcumuladas = new double[cantidadDeNivelesDeGris];
    private double[] varianzas = new double[cantidadDeNivelesDeGris];
    private double mediaGlobalAcumulada = 0;

    public AplicarEstimacionDelUmbralDeOtsuAction(HistogramaService histogramaService, MatrizService matrizService, AplicarUmbralService aplicarUmbralService){
        this.histogramaService = histogramaService;
        this.matrizService = matrizService;
        this.aplicarUmbralService = aplicarUmbralService;
    }

    public EstimacionDelUmbralDeOtsuResultante ejecutar(Imagen customImage){

        Histograma histograma = this.histogramaService.crear(customImage);
        int totalDeValoresEnElHistograma = histograma.getTotalPixeles();

        for (int i = 0; i < probabilidadesAcumuladas.length; i++)
            probabilidadesAcumuladas[i] = this.probabilidadesAcumuladas(histograma, i, totalDeValoresEnElHistograma);

        //para cada t desde 0 a 255 tengo dos grupos C1 (menor al umbral) y C2 (mayor al umbral)

        //calculo las medias acumulativas (similar a las sumas acumulativas) pero multiplico a pi(p sub i) * i, yendo i de 0 a t

        for (int t = 0; t < mediasAcumuladas.length; t++) {
            mediasAcumuladas[t] = this.mediasAcumuladas(histograma, t, totalDeValoresEnElHistograma);
            mediaGlobalAcumulada += mediasAcumuladas[t];
        }
        //calculo la media global igual que la acumulativa
        mediaGlobalAcumulada = mediaGlobalAcumulada/totalDeValoresEnElHistograma;
        CalcularVarianzas(mediaGlobalAcumulada);

        //calculo el umbral final
        int umbral = this.calcularUmbralFinal(varianzas);

        //calculo la forma matricial de la imagen
        int[][] imageMatrix = this.matrizService.aMatrizDeGrises(customImage.toFXImage());

        //le aplico el umbral calculado
        int[][] imagenTransformada = this.aplicarUmbralService.aplicarUmbral(imageMatrix, umbral);

        //creo un objeto con la imagen modificada y el umbral usado, y lo devuelvo
        Image image = this.matrizService.toImage(imagenTransformada, imagenTransformada, imagenTransformada);
        return new EstimacionDelUmbralDeOtsuResultante(image, umbral);
    }

    private void CalcularVarianzas(double mediaGlobalAcumulada) {
        //calculo varianza entre clases para cada t: (mg * P1(t) - m(t))² / P1(t) * (1 - P1(t))
        for (int t = 0; t < varianzas.length; t++){
            varianzas[t] = Math.pow(((mediaGlobalAcumulada * probabilidadesAcumuladas[t]) - mediasAcumuladas[t]), 2)
                            / (probabilidadesAcumuladas[t] * (1 - probabilidadesAcumuladas[t]));
        }
    }


    private Double probabilidadesAcumuladas(Histograma histograma, int limite, int totalDeValoresEnElHistograma) {
        double valor = 0.0;
        for (int i = 0; i <= limite; i++)
            valor += histograma.getValores()[i];

        return valor / totalDeValoresEnElHistograma;
    }

    private double mediasAcumuladas(Histograma histogram, int limit, int totalDeValoresEnElHistograma) {
        double valor = 0.0;
        for (int i = 0; i <= limit; i++)
            valor += ((histogram.getValores()[i]));

        return valor / totalDeValoresEnElHistograma;
    }

    private List<Integer> buscarTQueGeneraLaMaximaVarianza(double[] varianzas){
        List<Integer> t_maximaVarianza = new ArrayList<Integer>();
        double maximaVarianza = 0.0;
        //Buscamos la mayor de las varianzas
        for (int t = 0; t < varianzas.length; t++) {
            if (varianzas[t] >= maximaVarianza)
                maximaVarianza = varianzas[t];
        }

        //agregamos la maxima varianza a la lista
        for (int t = 0; t < varianzas.length; t++) {
            if (varianzas[t] == maximaVarianza)
                t_maximaVarianza.add(t);
        }
        return t_maximaVarianza;
    }

    private int calcularUmbralFinal(double[] varianzas){
        //busco con que t me dio una varianza mas grande. Pueden ser mas de uno
        List<Integer> t_maximaVarianza = this.buscarTQueGeneraLaMaximaVarianza(varianzas);

        //veo si es el t es unico o son varios valores. Si es unico el valor de umbral es t, sino calculo la media de todos los t
        int umbral = 0;
        if(t_maximaVarianza.size() > 1){
            Integer suma = 0;
            for (int i = 0; i < t_maximaVarianza.size(); i++) {
                suma += t_maximaVarianza.get(i);
            }
            double aux = (double) suma / t_maximaVarianza.size();
            umbral = (int) aux;
        }
        else
            umbral = t_maximaVarianza.get(0);

        return umbral;
    }
}

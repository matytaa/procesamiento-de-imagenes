package core.action.threshold;

import core.service.ApplyThresholdService;
import core.service.MatrizService;
import core.service.generation.HistogramaService;
import dominio.Histograma;
import dominio.automaticthreshold.OtsuThresholdResult;
import dominio.customimage.Imagen;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;


public class ApplyOtsuThresholdEstimationAction {

    private HistogramaService histogramService;
    private MatrizService matrizService;
    private ApplyThresholdService applyThresholdService;

    public ApplyOtsuThresholdEstimationAction(HistogramaService histogramService, MatrizService matrizService, ApplyThresholdService applyThresholdService){
        this.histogramService = histogramService;
        this.matrizService = matrizService;
        this.applyThresholdService = applyThresholdService;
    }

    public OtsuThresholdResult execute(Imagen customImage){

        Histograma histogram = this.histogramService.crear(customImage);

        Double[] cumulativeProbabilities = new Double[256];
        for (int i = 0; i <= 255; i++){
            cumulativeProbabilities[i] = this.cumulativeProbability(histogram, i);
        }

        //para cada t desde 0 a 255 tengo dos grupos C1 (menor al umbral) y C2 (mayor al umbral)

        //calculo las medias acumulativas (similar a las sumas acumulativas) pero multiplico a pi(p sub i) * i, yendo i de 0 a t
        Double[] cumulativeMeans = new Double[256];
        for (int t = 0; t <= 255; t++){
            cumulativeMeans[t] = this.cumulativeMean(histogram, t);
        }

        //calculo la media global igual que la acumulativa, pero t sería 255
        Double globalCumulativeMean = cumulativeMeans[255];

        //calculo varianza entre clases para cada t: (mg * P1(t) - m(t))² / P1(t) * (1 - P1(t))
        Double[] variances = new Double[256];
        for (int t = 0; t <= 255; t++){
            Double tCumulativeProbability = cumulativeProbabilities[t];
            Double tCumulativeMean = cumulativeMeans[t];
            variances[t] = Math.pow(((globalCumulativeMean * tCumulativeProbability) - tCumulativeMean), 2)
                            / (tCumulativeProbability * (1 - tCumulativeProbability));
        }

        //calculo el umbral final
        int threshold = this.calculateFinalThreshold(variances);

        //calculo la forma matricial de la imagen
        int[][] imageMatrix = this.matrizService.aMatrizDeGrises(customImage.toFXImage());

        //le aplico el umbral calculado
        int[][] transformedImage = this.applyThresholdService.aplicarUmbral(imageMatrix, threshold);

        //creo un objeto con la imagen modificada y el umbral usado, y lo devuelvo
        Image image = this.matrizService.toImage(transformedImage, transformedImage, transformedImage);
        return new OtsuThresholdResult(image, threshold);
    }


    private Double cumulativeProbability(Histograma histogram, int limit) {
        Double value = 0.0;
        for (int i = 0; i <= limit; i++) {
            value += histogram.getValores()[i];
        }
        return value / histogram.getTotalPixeles();
    }

    private Double cumulativeMean(Histograma histogram, int limit) {
        Double value = 0.0;
        for (int i = 0; i <= limit; i++) {
            value += ((histogram.getValores()[i]) * i);
        }
        return value / histogram.getTotalPixeles();
    }

    private List<Integer> searchTThatGenerateMaximumVariance(Double[] variances){
        List<Integer> tMaxValues = new ArrayList<Integer>();
        Double maximumVariance = 0.0;
        for (int t = 0; t < variances.length; t++){
            if(variances[t] >= maximumVariance){
                maximumVariance = variances[t];
            }
        }

        for (int t = 0; t < variances.length; t++){
            if(variances[t] == maximumVariance){
                tMaxValues.add(t);
            }
        }
        return tMaxValues;
    }

    private int calculateFinalThreshold(Double[] variances){
        //busco con que t me dio una varianza mas grande. Pueden ser mas de uno
        List<Integer> tMaxValues = this.searchTThatGenerateMaximumVariance(variances);

        //veo si es el t es unico o son varios valores. Si es unico el valor de umbral es t, sino calculo la media de todos los t
        int threshold = 0;
        if(tMaxValues.size() > 1){
            Integer sum = 0;
            for (int i = 0; i < tMaxValues.size(); i++){
                sum += tMaxValues.get(i);
            }
            double aux = (double) sum / tMaxValues.size();
            threshold = (int) aux;
        }
        else{
            threshold = tMaxValues.get(0);
        }
        return threshold;
    }

}

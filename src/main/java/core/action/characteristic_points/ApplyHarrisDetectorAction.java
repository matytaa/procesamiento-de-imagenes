package core.action.characteristic_points;

import core.service.MatrizService;
import dominio.PuntoXY;
import dominio.customimage.Imagen;
import dominio.mask.filter.MascaraGaussiana;
import dominio.mask.sobel.MascaraSobelX;
import dominio.mask.sobel.MascaraSobelY;

import java.util.ArrayList;
import java.util.List;

public class ApplyHarrisDetectorAction {

    private final MatrizService matrizService;
    private final double k = 0.04;

    public ApplyHarrisDetectorAction(MatrizService matrizService) {
        this.matrizService = matrizService;
    }

    public List<PuntoXY> execute(Imagen image, double tolerance) {

        int[][] xDeriv = new MascaraSobelX().aplicar(image).getCanalRojo();
        int[][] yDeriv = new MascaraSobelY().aplicar(image).getCanalRojo();

        double[][] xSquareDeriv = this.applyGaussianFilter(this.matrizService.calculateSquare(xDeriv));
        double[][] ySquareDeriv = this.applyGaussianFilter(this.matrizService.calculateSquare(yDeriv));
        double[][] xyCrossProduct = this.applyGaussianFilter(this.matrizService.multiplyPointToPoint(xDeriv, yDeriv));

        double[][] possibleCorners = new double[xDeriv.length][xDeriv[0].length];
        for (int i = 0; i < possibleCorners.length; i++) {
            for (int j = 0; j < possibleCorners[i].length; j++) {
                possibleCorners[i][j] = this.calculateC1(xSquareDeriv[i][j], ySquareDeriv[i][j], xyCrossProduct[i][j]);
            }
        }

        double maximum = this.matrizService.findMaxValue(possibleCorners);

        return this.filterFakeCorners(possibleCorners, maximum, tolerance);
    }

    private List<PuntoXY> filterFakeCorners(double[][] possibleCorners, double maximum, double tolerance) {

        List<PuntoXY> corners = new ArrayList<>();

        for (int i = 0; i < possibleCorners.length; i++) {
            for (int j = 0; j < possibleCorners[i].length; j++) {

                if (possibleCorners[i][j] >= maximum - maximum * tolerance / 100) {
                    corners.add(new PuntoXY(i, j));
                }
            }
        }
        return corners;
    }

    private double calculateC1(double xSquareDeriv, double ySquareDeriv, double xyCrossProduct) {
        double determinant = xSquareDeriv * ySquareDeriv - Math.pow(xyCrossProduct, 2);
        double trace = xSquareDeriv + ySquareDeriv;
        return determinant - k * Math.pow(trace, 2);
    }

    private double[][] applyGaussianFilter(int[][] matrix) {
        return new MascaraGaussiana(2).apply(matrix);
    }

}

package core.action.characteristic_points;

import core.service.MatrizService;
import domain.XYPoint;
import domain.customimage.Imagen;
import domain.mask.filter.MascaraGaussiana;
import domain.mask.sobel.SobelXDerivativeMascara;
import domain.mask.sobel.SobelYDerivativeMascara;

import java.util.ArrayList;
import java.util.List;

public class ApplyHarrisDetectorAction {

    private final MatrizService matrizService;
    private final double k = 0.04;

    public ApplyHarrisDetectorAction(MatrizService matrizService) {
        this.matrizService = matrizService;
    }

    public List<XYPoint> execute(Imagen image, double tolerance) {

        int[][] xDeriv = new SobelXDerivativeMascara().apply(image).getRedChannel();
        int[][] yDeriv = new SobelYDerivativeMascara().apply(image).getRedChannel();

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

    private List<XYPoint> filterFakeCorners(double[][] possibleCorners, double maximum, double tolerance) {

        List<XYPoint> corners = new ArrayList<>();

        for (int i = 0; i < possibleCorners.length; i++) {
            for (int j = 0; j < possibleCorners[i].length; j++) {

                if (possibleCorners[i][j] >= maximum - maximum * tolerance / 100) {
                    corners.add(new XYPoint(i, j));
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

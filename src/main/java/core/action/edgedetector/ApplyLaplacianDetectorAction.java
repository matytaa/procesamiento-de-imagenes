package core.action.edgedetector;

import domain.customimage.MatrizCanales;
import domain.customimage.Imagen;
import domain.mask.Mascara;

public class ApplyLaplacianDetectorAction {

    public Imagen execute(Imagen customImage, Mascara mascara, int slopeThreshold) {
        MatrizCanales maskResult = mascara.apply(customImage);
        MatrizCanales markZeroCrossings = this.markZeroCrossings(maskResult, slopeThreshold);
        return new Imagen(markZeroCrossings, customImage.getFormatString());
    }

    private MatrizCanales markZeroCrossings(MatrizCanales channelMatrix, int slopeThreshold) {

        int edgedRedMatrix[][] = this.markHorizontalZeroCrossings(channelMatrix.getRedChannel(), slopeThreshold);
        int edgedGreenMatrix[][] = this.markHorizontalZeroCrossings(channelMatrix.getGreenChannel(), slopeThreshold);
        int edgedBlueMatrix[][] = this.markHorizontalZeroCrossings(channelMatrix.getBlueChannel(), slopeThreshold);

        int resultantRedMatrix[][] = this.markVerticalZeroCrossings(channelMatrix.getRedChannel(), edgedRedMatrix, slopeThreshold);
        int resultantGreenMatrix[][] = this.markVerticalZeroCrossings(channelMatrix.getGreenChannel(), edgedGreenMatrix, slopeThreshold);
        int resultantBlueMatrix[][] = this.markVerticalZeroCrossings(channelMatrix.getBlueChannel(), edgedBlueMatrix, slopeThreshold);

        return new MatrizCanales(resultantRedMatrix, resultantGreenMatrix, resultantBlueMatrix);
    }

    private int[][] markHorizontalZeroCrossings(int[][] matrix, int slopeThreshold) {

        int edgedMatrix[][] = new int[matrix.length][matrix[0].length];

        for (int x = 0; x < matrix.length - 1; x++) {
            for (int y = 0; y < matrix[x].length; y++) {

                if (signsChangeHorizontally(matrix, x, y)) {
                    //The slope evaluation is always made, but in the case of the standard Laplacian detector, the slope is always 0
                    if (this.evaluateHorizontalSlope(matrix, x, y, slopeThreshold)) {
                        edgedMatrix[x][y] = 255;
                    }
                }
            }
        }

        return edgedMatrix;
    }

    private int[][] markVerticalZeroCrossings(int[][] matrix, int[][] horizontalZeroCrossingMatrix, int slopeThreshold) {

        int[][] resultantMatrix = horizontalZeroCrossingMatrix;

        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length - 1; y++) {

                if (signsChangeVertically(matrix, x, y)) {
                    //The slope evaluation is always made, but in the case of the standard Laplacian detector, the slope is always 0
                    if (this.evaluateVerticalSlope(matrix, x, y, slopeThreshold)) {
                        resultantMatrix[x][y] = 255;
                    }
                }

            }
        }

        return resultantMatrix;
    }

    private boolean signsChangeHorizontally(int[][] matrix, int x, int y) {
        return matrix[x + 1][y] * matrix[x][y] < 0;
    }

    private boolean signsChangeVertically(int[][] matrix, int x, int y) {
        return matrix[x][y + 1] * matrix[x][y] < 0;
    }

    private boolean evaluateHorizontalSlope(int[][] matrix, int x, int y, int slopeThreshold) {
        double a = matrix[x][y];
        double b = matrix[x + 1][y];

        return (Math.abs(a) + Math.abs(b)) >= slopeThreshold;
    }

    private boolean evaluateVerticalSlope(int[][] matrix, int x, int y, int slopeThreshold) {
        double a = matrix[x][y];
        double b = matrix[x][y + 1];

        return (Math.abs(a) + Math.abs(b)) >= slopeThreshold;
    }

}

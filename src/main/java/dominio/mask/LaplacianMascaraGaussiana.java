package dominio.mask;

import dominio.mask.filter.MascaraGaussiana;

public class LaplacianMascaraGaussiana extends MascaraGaussiana {

    public LaplacianMascaraGaussiana(double standardDeviation) {
        super(standardDeviation, createSize(standardDeviation), Mascara.Tipo.GAUSSIAN_LAPLACIAN);
        this.matriz = this.crearMatriz(getTamanio());
        this.factor = this.createFactor();
    }

    //The size is different then the original GaussianMask
    private static int createSize(double standardDeviation) {
        return (int) (4 * standardDeviation + 1);
    }

    @Override
    protected double[][] crearMatriz(int tamanio) {

        double[][] matrix = new double[tamanio][tamanio];

        for (int x = 0; x < tamanio; x++) {
            for (int y = 0; y < tamanio; y++) {

                double xSquare = Math.pow(x - tamanio / 2, 2);
                double ySquare = Math.pow(y - tamanio / 2, 2);
                double standardDeviationSquare = Math.pow(desviacionEstandar, 2);
                double standardDeviationCube = Math.pow(desviacionEstandar, 3);

                double firstTerm = -1.0 / (standardDeviationCube * Math.sqrt(2.0 * Math.PI));
                double secondTerm = 2 - ((xSquare + ySquare) / standardDeviationSquare);
                double thirdTerm = Math.exp(-(xSquare + ySquare) / (standardDeviationSquare * 2.0));

                matrix[x][y] = firstTerm * secondTerm * thirdTerm;
            }
        }

        return matrix;
    }
}

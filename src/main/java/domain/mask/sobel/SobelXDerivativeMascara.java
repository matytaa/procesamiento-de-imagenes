package domain.mask.sobel;

import domain.mask.Mascara;

public class SobelXDerivativeMascara extends Mascara {

    private static final int AVAILABLE_SIZE = 3;

    public SobelXDerivativeMascara() {
        super(Tipo.SOBEL, AVAILABLE_SIZE);
        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                {-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}
        };
    }

    @Override
    public double getValue(int x, int y) {
        return matriz[x][y];
    }
}

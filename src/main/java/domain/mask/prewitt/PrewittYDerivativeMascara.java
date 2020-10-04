package domain.mask.prewitt;

import domain.mask.Mascara;

public class PrewittYDerivativeMascara extends Mascara {

    private static final int AVAILABLE_SIZE = 3;

    public PrewittYDerivativeMascara() {
        super(Tipo.PREWITT, AVAILABLE_SIZE);
        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                {-1, 0, 1},
                {-1, 0, 1},
                {-1, 0, 1}
        };
    }

    @Override
    public double getValue(int x, int y) {
        return matriz[x][y];
    }
}

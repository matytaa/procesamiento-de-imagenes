package domain.mask.prewitt;

import domain.mask.Mascara;

public class PrewittXDerivativeMascara extends Mascara {

    private static final int AVAILABLE_SIZE = 3;

    public PrewittXDerivativeMascara() {
        super(Tipo.PREWITT, AVAILABLE_SIZE);
        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                {-1, -1, -1},
                {0, 0, 0},
                {1, 1, 1}
        };
    }

    @Override
    public double obtenerValor(int x, int y) {
        return matriz[x][y];
    }
}

package domain.mask.sobel;

import domain.mask.Mascara;

public class MascaraSobelX extends Mascara {

    private static final int TAMANIO_MASCARA = 3;

    public MascaraSobelX() {
        super(Tipo.SOBEL, TAMANIO_MASCARA);
        this.matriz = crearMatriz(TAMANIO_MASCARA);
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
    public double obtenerValor(int x, int y) {
        return matriz[x][y];
    }
}

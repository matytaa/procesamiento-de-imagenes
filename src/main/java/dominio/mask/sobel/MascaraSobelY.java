package dominio.mask.sobel;

import dominio.mask.Mascara;

public class MascaraSobelY extends Mascara {

    private static final int TAMANIO_MASCARA = 3;

    public MascaraSobelY() {
        super(Tipo.SOBEL, TAMANIO_MASCARA);
        this.matriz = crearMatriz(TAMANIO_MASCARA);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };
    }

    @Override
    public double obtenerValor(int x, int y) {
        return matriz[x][y];
    }
}

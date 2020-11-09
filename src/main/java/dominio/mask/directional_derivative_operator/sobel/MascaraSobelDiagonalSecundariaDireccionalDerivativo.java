package dominio.mask.directional_derivative_operator.sobel;

import dominio.mask.Mascara;

public class MascaraSobelDiagonalSecundariaDireccionalDerivativo extends Mascara {

    public MascaraSobelDiagonalSecundariaDireccionalDerivativo() {
        super(Tipo.DERIVATE_DIRECTIONAL_OPERATOR_SOBEL, AVAILABLE_SIZE);

        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                {2,  1,  0},
                {1,  0, -1},
                {0, -1, -2}
        };
    }

    @Override
    public double obtenerValor(int x, int y) {
        return matriz[x][y];
    }
}
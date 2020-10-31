package domain.mask.directional_derivative_operator.sobel;

import domain.mask.Mascara;

public class MascaraSobelHorizontalDireccionalDerivativo extends Mascara {

    public MascaraSobelHorizontalDireccionalDerivativo() {
        super(Tipo.DERIVATE_DIRECTIONAL_OPERATOR_SOBEL, AVAILABLE_SIZE);

        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                { 1,  2,  1},
                { 0,  0,  0},
                {-1, -2, -1}
        };
    }

    @Override
    public double obtenerValor(int x, int y) {
        return matriz[x][y];
    }
}
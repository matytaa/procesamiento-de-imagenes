package domain.mask.directional_derivative_operator.sobel;

import domain.mask.Mascara;

public class SobelSecondaryDiagonalMascara extends Mascara {

    public SobelSecondaryDiagonalMascara() {
        super(Tipo.DERIVATE_DIRECTIONAL_OPERATOR_KIRSH, AVAILABLE_SIZE);

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
    public double getValue(int x, int y) {
        return matriz[x][y];
    }
}
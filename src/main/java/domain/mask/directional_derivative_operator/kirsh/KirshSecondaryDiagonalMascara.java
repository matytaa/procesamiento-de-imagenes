package domain.mask.directional_derivative_operator.kirsh;

import domain.mask.Mascara;

public class KirshSecondaryDiagonalMascara extends Mascara {

    public KirshSecondaryDiagonalMascara() {
        super(Tipo.DERIVATE_DIRECTIONAL_OPERATOR_KIRSH, AVAILABLE_SIZE);

        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                {5,   5, -3},
                {5,   0, -3},
                {-3, -3, -3}
        };
    }

    @Override
    public double getValue(int x, int y) {
        return matriz[x][y];
    }
}
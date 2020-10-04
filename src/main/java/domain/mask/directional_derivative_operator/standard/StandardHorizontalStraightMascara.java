package domain.mask.directional_derivative_operator.standard;

import domain.mask.Mascara;

public class StandardHorizontalStraightMascara extends Mascara {

    public StandardHorizontalStraightMascara() {
        super(Tipo.DERIVATE_DIRECTIONAL_OPERATOR_STANDARD, AVAILABLE_SIZE);

        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                {1,   1,  1},
                {1,  -2,  1},
                {-1, -1, -1}
        };
    }

    @Override
    public double getValue(int x, int y) {
        return matriz[x][y];
    }
}

package domain.mask.directional_derivative_operator.prewitt;

import domain.mask.Mascara;

public class PrewittHorizontalStraightMascara extends Mascara {

    public PrewittHorizontalStraightMascara() {
        super(Tipo.DERIVATE_DIRECTIONAL_OPERATOR_KIRSH, AVAILABLE_SIZE);

        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                { 1,  1,  1},
                { 0,  0,  0},
                {-1, -1, -1}
        };
    }

    @Override
    public double getValue(int x, int y) {
        return matriz[x][y];
    }
}
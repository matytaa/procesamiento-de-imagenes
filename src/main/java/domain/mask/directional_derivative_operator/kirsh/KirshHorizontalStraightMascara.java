package domain.mask.directional_derivative_operator.kirsh;

import domain.mask.Mascara;

public class KirshHorizontalStraightMascara extends Mascara {

    public KirshHorizontalStraightMascara() {
        super(Tipo.DERIVATE_DIRECTIONAL_OPERATOR_KIRSH, AVAILABLE_SIZE);

        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                { 5,  5,  5},
                {-3,  0, -3},
                {-3, -3, -3}
        };
    }

    @Override
    public double obtenerValor(int x, int y) {
        return matriz[x][y];
    }
}

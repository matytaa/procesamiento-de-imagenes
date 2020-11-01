package domain.mask.directional_derivative_operator.kirsh;

import domain.mask.Mascara;

public class MascaraKirshDiagonalPrincipalDireccionalDerivativo extends Mascara {

    public MascaraKirshDiagonalPrincipalDireccionalDerivativo() {
        super(Tipo.DERIVATE_DIRECTIONAL_OPERATOR_KIRSH, AVAILABLE_SIZE);

        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                {-3, -3, -3},
                {5,   0, -3},
                {5,   5, -3}
        };
    }

    @Override
    public double obtenerValor(int x, int y) {
        return matriz[x][y];
    }
}
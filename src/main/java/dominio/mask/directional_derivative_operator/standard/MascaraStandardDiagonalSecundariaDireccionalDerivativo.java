package dominio.mask.directional_derivative_operator.standard;

import dominio.mask.Mascara;

public class MascaraStandardDiagonalSecundariaDireccionalDerivativo extends Mascara {

    public MascaraStandardDiagonalSecundariaDireccionalDerivativo() {
        super(Tipo.DERIVATE_DIRECTIONAL_OPERATOR_STANDARD, AVAILABLE_SIZE);

        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                {1,  1,  1},
                {1, -2, -1},
                {1, -1, -1}
        };
    }

    @Override
    public double obtenerValor(int x, int y) {
        return matriz[x][y];
    }
}
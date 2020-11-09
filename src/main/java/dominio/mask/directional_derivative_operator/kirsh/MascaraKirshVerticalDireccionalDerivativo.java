package dominio.mask.directional_derivative_operator.kirsh;

import dominio.mask.Mascara;

public class MascaraKirshVerticalDireccionalDerivativo extends Mascara {

    public MascaraKirshVerticalDireccionalDerivativo() {
        super(Tipo.DERIVATE_DIRECTIONAL_OPERATOR_KIRSH, AVAILABLE_SIZE);

        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                {5, -3, -3},
                {5,  0, -3},
                {5, -3, -3}
        };
    }

    @Override
    public double obtenerValor(int x, int y) {
        return matriz[x][y];
    }
}
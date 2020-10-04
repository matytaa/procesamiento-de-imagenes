package domain.mask.filter;

public class MascaraMedianaPonderada extends MascaraMediana {

    private static final int AVAILABLE_SIZE = 3;

    public MascaraMedianaPonderada() {
        super(AVAILABLE_SIZE);
        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][]{
                {1, 2, 1},
                {2, 4, 2},
                {1, 2, 1}
        };
    }
}
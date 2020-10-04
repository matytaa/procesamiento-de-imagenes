package domain.mask;

public class LaplacianMascara extends Mascara {

    public LaplacianMascara() {
        super(Tipo.LAPLACIAN, AVAILABLE_SIZE);
        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][] {
                { 0, -1, 0 },
                { -1, 4, -1 },
                { 0, -1, 0 }
        };
    }
}

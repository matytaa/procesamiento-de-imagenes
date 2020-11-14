package dominio.mask;

import dominio.mask.filter.MascaraGaussiana;
import dominio.mask.filter.MascaraGaussianaConFactor;

public class MascaraLaplacianoDelGaussiano extends MascaraGaussianaConFactor {

    public MascaraLaplacianoDelGaussiano(double desviacionEstandar) {
        super(desviacionEstandar, createSize(desviacionEstandar), Mascara.Tipo.LAPLACIANO_DEL_GUASSIANO);
        this.matriz = this.crearMatriz(getTamanio());
        this.factor = this.createFactor();
    }

    //El tamaño es diferente al de la mascara gaussiana
    private static int createSize(double desviacionEstandar) {
        return (int) (6 * desviacionEstandar + 1);
    }

    @Override
    protected double[][] crearMatriz(int tamanio) {

        double[][] matriz = new double[tamanio][tamanio];

        for (int x = 0; x < tamanio; x++) {
            for (int y = 0; y < tamanio; y++) {

                double xAlCuadrado = Math.pow(x - tamanio / 2, 2);
                double yAlCuadrado = Math.pow(y - tamanio / 2, 2);
                double desviacionEstandarAlCuadrado = Math.pow(desviacionEstandar, 2);
                double desviacionEstandarAlCubo = Math.pow(desviacionEstandar, 3);

                double primerTermino = -1.0 / (desviacionEstandarAlCubo * Math.sqrt(2.0 * Math.PI));
                double segundoTermino = 2 - ((xAlCuadrado + yAlCuadrado) / desviacionEstandarAlCuadrado);
                double tercerTermino = Math.exp(-(xAlCuadrado + yAlCuadrado) / (desviacionEstandarAlCuadrado * 2.0));

                matriz[x][y] = primerTermino * segundoTermino * tercerTermino;
            }
        }

        return matriz;
    }
}

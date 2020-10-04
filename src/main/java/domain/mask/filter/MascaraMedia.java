package domain.mask.filter;

import domain.mask.Mascara;

public class MascaraMedia extends Mascara {

    public MascaraMedia(int size) {
        super(Tipo.MEDIA, size);
        //EL FACTOR ES 1/CANTIDAD DE ELEMENTOS DE LA MASCARA
        this.factor = 1 / Math.pow(size, 2);
        this.matriz = crearMatriz(size);
    }

    @Override
    //TODOS LOS ELEMENTOS DE LA MATRIZ SON 1/CANTIDAD DE ELEMENTOS DE LA MASCARA
    protected double[][] crearMatriz(int tamanio) {

        double[][] matrix = new double[tamanio][tamanio];

        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                matrix[i][j] = this.factor;
            }
        }

        return matrix;
    }


}

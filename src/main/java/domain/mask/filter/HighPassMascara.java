package domain.mask.filter;

import domain.customimage.Imagen;
import domain.customimage.RGB;
import domain.mask.Mascara;

public class HighPassMascara extends Mascara {

    public HighPassMascara(int size) {
        super(Tipo.HIGH_PASS, size);

        this.factor = (-1) / Math.pow(size, 2);
        this.matriz = this.crearMatriz(size);
    }

    @Override
    public double obtenerValor(int x, int y) {
        return this.matriz[x][y];
    }

    @Override
    protected double[][] crearMatriz(int size) {

        double matrix[][] = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = this.factor;
            }
        }
        //Since it's a square matrix, its core position is just half the size on both dimensions
        int core_position = size / 2;
        matrix[core_position][core_position] = (Math.pow(size, 2) - 1) / (Math.pow(size, 2));
        return matrix;
    }

    @Override
    public RGB aplicarMascaraAPixel(Imagen imagen, int x, int y) {

        int red = 0;
        int green = 0;
        int blue = 0;

        int width = imagen.getAncho();
        int height = imagen.getAltura();

        for (int j = y - (tamanio / 2); j <= y + (tamanio / 2); j++) {
            for (int i = x - (tamanio / 2); i <= x + (tamanio / 2); i++) {

                if (imagen.isPosicionValida(width, height, i, j)) {
                    int maskColumn = j + (tamanio / 2) - y;
                    int maskRow = i + (tamanio / 2) - x;
                    double value = this.matriz[maskRow][maskColumn];

                    red += imagen.getRChannelValue(i, j) * value;
                    green += imagen.getGChannelValue(i, j) * value;
                    blue += imagen.getBChannelValue(i, j) * value;
                }
            }
        }

        return new RGB(red, green, blue);
    }
}

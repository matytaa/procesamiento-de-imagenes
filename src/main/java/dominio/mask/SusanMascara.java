package dominio.mask;

import dominio.customimage.Imagen;
import dominio.customimage.RGB;

public class SusanMascara extends Mascara {

    private static final int AVAILABLE_SIZE = 7;

    public SusanMascara() {
        super(Tipo.SUSAN, AVAILABLE_SIZE);
        this.matriz = crearMatriz(AVAILABLE_SIZE);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        return new double[][] {
                { 0, 0, 1, 1, 1, 0, 0 },
                { 0, 1, 1, 1, 1, 1, 0 },
                { 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1 },
                { 0, 1, 1, 1, 1, 1, 0 },
                { 0, 0, 1, 1, 1, 0, 0 },
        };
    }

    public RGB aplicarMascaraAPixel(Imagen imagen, int x, int y) {
        int width = imagen.getAncho();
        int height = imagen.getAltura();
        int similarPixelsQuantity = 0;
        double coreValue = (255 * imagen.getPixelReader().getColor(x, y).getRed());
        double red;

        for (int j = y - (tamanio / 2); j <= y + (tamanio / 2); j++) {
            for (int i = x - (tamanio / 2); i <= x + (tamanio / 2); i++) {

                if (imagen.isPosicionValida(width, height, i, j)) {

                    int maskColumn = j + (tamanio / 2) - y;
                    int maskRow = i + (tamanio / 2) - x;
                    double value = this.matriz[maskRow][maskColumn];
                    if (value != 0.0) {
                        red = (255 * imagen.getPixelReader().getColor(i, j).getRed()) * value;
                        similarPixelsQuantity += this.evaluateSimilarity(coreValue, red);
                    }

                }
            }
        }

        double similarity = 1 - ((double) similarPixelsQuantity / 37);
        int pixelValue = this.evaluatePixelType(similarity);
        return new RGB(pixelValue, pixelValue, pixelValue);
    }

    private int evaluateSimilarity(double coreValue, double maskValue) {
        return Math.abs(coreValue - maskValue) <= 27 ? 1 : 0;
    }

    private int evaluatePixelType(double similarity) {
        if (0 <= similarity && similarity <= 0.20) {
            return 0; //ignored
        }

        if (0.4 <= similarity && similarity <= 0.6) {
            return 150; //edge
        }

        if (0.65 <= similarity && similarity <= 0.85) {
            return 255; //corner
        }

        return 0;
    }
}

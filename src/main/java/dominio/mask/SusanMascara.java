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
        int ancho = imagen.getAncho();
        int alto = imagen.getAltura();
        int cantidadPixelesSimilares = 0;
        double valorCentral = (255 * imagen.getPixelReader().getColor(x, y).getRed());
        double rojo;

        for (int j = y - (tamanio / 2); j <= y + (tamanio / 2); j++) {
            for (int i = x - (tamanio / 2); i <= x + (tamanio / 2); i++) {

                if (imagen.isPosicionValida(ancho, alto, i, j)) {

                    int columnaMascara = j + (tamanio / 2) - y;
                    int filaMascara = i + (tamanio / 2) - x;
                    double valor = this.matriz[filaMascara][columnaMascara];
                    if (valor != 0.0) {
                        rojo = (255 * imagen.getPixelReader().getColor(i, j).getRed()) * valor;
                        cantidadPixelesSimilares += this.evaluarSimilitud(valorCentral, rojo);
                    }

                }
            }
        }

        double similarity = 1 - ((double) cantidadPixelesSimilares / 37);
        int pixelValue = this.evaluarTipoPixel(similarity);
        return new RGB(pixelValue, pixelValue, pixelValue);
    }

    private int evaluarSimilitud(double valorCentral, double valorMascara) {
        return Math.abs(valorCentral - valorMascara) <= 27 ? 1 : 0;
    }

    private int evaluarTipoPixel(double similitud) {
        if (0 <= similitud && similitud <= 0.20) {
            return 0; //IGNORO
        }

        if (0.4 <= similitud && similitud <= 0.6) {
            return 150; //BORDE
        }

        if (0.65 <= similitud && similitud <= 0.85) {
            return 255; //ESQUINA
        }

        return 0;
    }
}

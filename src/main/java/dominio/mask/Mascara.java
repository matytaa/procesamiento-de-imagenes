package dominio.mask;

import dominio.customimage.MatrizCanales;
import dominio.customimage.Imagen;
import dominio.customimage.RGB;

public abstract class Mascara {

    public static final int AVAILABLE_SIZE = 3;

    protected final Tipo tipo;
    protected final int tamanio;
    protected double[][] matriz;
    protected double factor;

    public Mascara(Tipo tipo, int tamanio) {
        this.tipo = tipo;
        this.tamanio = tamanio;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getTamanio() {
        return tamanio;
    }

    public double obtenerValor(int x, int y) {
        return this.matriz[x][y];
    }

    protected abstract double[][] crearMatriz(int size);

   //CONVOLUCIÓN ENTRE LA MÁSCARA Y LA IMAGEN
    public MatrizCanales aplicar(Imagen image) {
        Integer ancho = image.getAncho();
        Integer alto = image.getAltura();
        MatrizCanales matrizCanales = new MatrizCanales(ancho, alto);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                matrizCanales.setValue(x, y, aplicarMascaraAPixel(image, x, y));
            }
        }

        return matrizCanales;
    }

    public RGB aplicarMascaraAPixel(Imagen imagen, int x, int y) {
        int red = 0;
        int green = 0;
        int blue = 0;

        int ancho = imagen.getAncho();
        int alto = imagen.getAltura();

        for (int j = y - (tamanio / 2); j <= y + (tamanio / 2); j++) {
            for (int i = x - (tamanio / 2); i <= x + (tamanio / 2); i++) {
                // IGNORO LA PARTE DE LA MASCARA QUE QUEDA AFUERA
                if (imagen.isPosicionValida(ancho, alto, i, j)) {

                    int columnaMascara = j + (tamanio / 2) - y;
                    int filaMascara = i + (tamanio / 2) - x;
                    double valor = this.matriz[filaMascara][columnaMascara];

                    red += (255 * imagen.getPixelReader().getColor(i, j).getRed()) * valor;
                    green += (255 * imagen.getPixelReader().getColor(i, j).getGreen()) * valor;
                    blue += (255 * imagen.getPixelReader().getColor(i, j).getBlue()) * valor;
                }

            }
        }

        return new RGB(red, green, blue);
    }

    public enum Tipo {
        MEDIA, MEDIANA_PONDERADA, GAUSSIANO, PREWITT, MEDIANA, SOBEL_VERTICAL, SOBEL_HORIZONTAL, SOBEL, DERIVATE_DIRECTIONAL_OPERATOR_STANDARD, DERIVATE_DIRECTIONAL_OPERATOR_KIRSH, DERIVATE_DIRECTIONAL_OPERATOR_PREWITT, DERIVATE_DIRECTIONAL_OPERATOR_SOBEL, LAPLACIANO, LAPLACIANO_DEL_GUASSIANO, HIGH_PASS, SUSAN
    }
}

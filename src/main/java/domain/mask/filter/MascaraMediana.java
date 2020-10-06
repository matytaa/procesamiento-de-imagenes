package domain.mask.filter;

import domain.customimage.Imagen;
import domain.customimage.RGB;
import domain.mask.Mascara;

import java.util.ArrayList;
import java.util.List;

public class MascaraMediana extends Mascara {

    public MascaraMediana(int size) {
        super(Tipo.MEDIANA, size);
        this.matriz = crearMatriz(size);
    }

    @Override
    protected double[][] crearMatriz(int size) {
        double[][] pesosNormales = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                pesosNormales[i][j] = 1.0;
            }
        }
        return pesosNormales;
    }

    @Override
    public RGB aplicarMascaraAPixel(Imagen imagen, int x, int y) {
        //AGARRO TODOS LOS COLORES DE ALREDEDOR DEL PIXEL, SEGUN EL TAMAÑO DE LA MASCARA
        List<RGB> pixelsWithinMaskRange = this.obtenerPixelesSegunMascara(imagen, x, y);

        //ORDENO ESOS COLORES DE MENOR A MAYOR
        List<Integer> redValues = this.ordenarRed(pixelsWithinMaskRange);
        List<Integer> greenValues = this.ordenarVerde(pixelsWithinMaskRange);
        List<Integer> blueValues = this.ordenarAzul(pixelsWithinMaskRange);

        //AGARRO LA POSICION DE LA MEDIANA, QUE ES EL ELEMENTO DEL MEDIO
        int medianPosition = pixelsWithinMaskRange.size() / 2;
        double redMedian = redValues.get(medianPosition);
        double greenMedian = greenValues.get(medianPosition);
        double blueMedian = blueValues.get(medianPosition);

        //SI NO ES IMPAR, PROMEDIO LOS DOS DEL CENTRO
        if (medianPosition % 2 == 0) {
            redMedian = (redValues.get(medianPosition - 1) + redValues.get(medianPosition)) / 2;
            greenMedian = (greenValues.get(medianPosition - 1) + greenValues.get(medianPosition)) / 2;
            blueMedian = (blueValues.get(medianPosition - 1) + blueValues.get(medianPosition)) / 2;
        }

        //EL COLOR DEL PIXEL TERMINA SIENDO LA MEDIANA DE CADA CANAL EN ESA MÁSCARA
        return new RGB((int) redMedian, (int) greenMedian, (int) blueMedian);
    }

    private List<Integer> ordenarRed(List<RGB> colorValuesWithinMaskRange) {
        List<Integer> redValues = new ArrayList<>();

        for (RGB value : colorValuesWithinMaskRange) {
            redValues.add(value.getRed());
        }

        redValues.sort(Integer::compareTo);
        return redValues;
    }

    private List<Integer> ordenarVerde(List<RGB> colorValuesWithinMaskRange) {
        List<Integer> greenValues = new ArrayList<>();

        for (RGB value : colorValuesWithinMaskRange) {
            greenValues.add(value.getGreen());
        }

        greenValues.sort(Integer::compareTo);
        return greenValues;
    }

    private List<Integer> ordenarAzul(List<RGB> colorValuesWithinMaskRange) {
        List<Integer> blueValues = new ArrayList<>();

        for (RGB value : colorValuesWithinMaskRange) {
            blueValues.add(value.getBlue());
        }

        blueValues.sort(Integer::compareTo);
        return blueValues;
    }

    private List<RGB> obtenerPixelesSegunMascara(Imagen image, int x, int y) {
        List<RGB> pixelesSegunMascara = new ArrayList<>();
        int ancho = image.getAncho();
        int alto = image.getAltura();

        for (int j = y - (tamanio / 2); j <= y + (tamanio / 2); j++) {
            for (int i = x - (tamanio / 2); i <= x + (tamanio / 2); i++) {

                int columnaMascara = j + (tamanio / 2) - y;
                int filaMascara = i + (tamanio / 2) - x;
                double value = this.matriz[filaMascara][columnaMascara];

                for (int n = 0; n < value; n++) {
                    if (image.isPosicionValida(ancho, alto, i, j)) {
                        pixelesSegunMascara.add(new RGB(image.getRChannelValue(i, j), image.getGChannelValue(i, j), image.getBChannelValue(i, j)));
                    } else {
                        pixelesSegunMascara.add(new RGB(0, 0, 0));
                    }
                }
            }
        }

        return pixelesSegunMascara;
    }
}
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
        List<RGB> pixelsWithinMaskRange = this.obtainPixelsWithinMaskRange(imagen, x, y);

        List<Integer> redValues = this.sortRedValues(pixelsWithinMaskRange);
        List<Integer> greenValues = this.sortGreenValues(pixelsWithinMaskRange);
        List<Integer> blueValues = this.sortBlueValues(pixelsWithinMaskRange);

        int medianPosition = pixelsWithinMaskRange.size() / 2;
        double redMedian = redValues.get(medianPosition);
        double greenMedian = greenValues.get(medianPosition);
        double blueMedian = blueValues.get(medianPosition);

        if (medianPosition % 2 == 0) {
            redMedian = (redValues.get(medianPosition - 1) + redValues.get(medianPosition)) / 2;
            greenMedian = (greenValues.get(medianPosition - 1) + greenValues.get(medianPosition)) / 2;
            blueMedian = (blueValues.get(medianPosition - 1) + blueValues.get(medianPosition)) / 2;
        }

        return new RGB((int) redMedian, (int) greenMedian, (int) blueMedian);
    }

    private List<Integer> sortRedValues(List<RGB> colorValuesWithinMaskRange) {
        List<Integer> redValues = new ArrayList<>();

        for (RGB value : colorValuesWithinMaskRange) {
            redValues.add(value.getRed());
        }

        redValues.sort(Integer::compareTo);
        return redValues;
    }

    private List<Integer> sortGreenValues(List<RGB> colorValuesWithinMaskRange) {
        List<Integer> greenValues = new ArrayList<>();

        for (RGB value : colorValuesWithinMaskRange) {
            greenValues.add(value.getGreen());
        }

        greenValues.sort(Integer::compareTo);
        return greenValues;
    }

    private List<Integer> sortBlueValues(List<RGB> colorValuesWithinMaskRange) {
        List<Integer> blueValues = new ArrayList<>();

        for (RGB value : colorValuesWithinMaskRange) {
            blueValues.add(value.getBlue());
        }

        blueValues.sort(Integer::compareTo);
        return blueValues;
    }

    private List<RGB> obtainPixelsWithinMaskRange(Imagen image, int x, int y) {
        List<RGB> pixelsWithinMaskRange = new ArrayList<>();
        int width = image.getAncho();
        int height = image.getAltura();

        for (int j = y - (tamanio / 2); j <= y + (tamanio / 2); j++) {
            for (int i = x - (tamanio / 2); i <= x + (tamanio / 2); i++) {

                int maskColumn = j + (tamanio / 2) - y;
                int maskRow = i + (tamanio / 2) - x;
                double value = this.matriz[maskRow][maskColumn];

                //Apply weights. For regular median mask, every weight is 1
                for (int n = 0; n < value; n++) {
                    if (image.isPosicionValida(width, height, i, j)) {
                        pixelsWithinMaskRange.add(new RGB(image.getRChannelValue(i, j), image.getGChannelValue(i, j), image.getBChannelValue(i, j)));
                    } else {
                        pixelsWithinMaskRange.add(new RGB(0, 0, 0)); //Zero padding
                    }
                }
            }
        }

        return pixelsWithinMaskRange;
    }
}
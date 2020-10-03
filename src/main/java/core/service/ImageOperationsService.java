package core.service;

import core.service.statistics.GrayLevelStatisticsService;
import domain.customimage.MatrizCanales;
import domain.customimage.Imagen;
import domain.customimage.Pixel;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.List;

public class ImageOperationsService {

    static final int MAXIMO_VALOR_DE_UN_PIXEL = 255;
    private GrayLevelStatisticsService grayLevelStatisticsService;

    public ImageOperationsService(GrayLevelStatisticsService grayLevelStatisticsService) {
        this.grayLevelStatisticsService = grayLevelStatisticsService;
    }

    //completa la writableImage recibida, con el valor de cierta imagen (completando con 0 las posiciones en la cual la imagen no tiene valores)
    public WritableImage fillImage(WritableImage writableImage, Imagen imagen) {
        this.completarConCero(writableImage);
        this.setearValoresDePixelesPorCanalesEnImagen(writableImage, imagen);
        return writableImage;
    }

    public int calcularAnchoResultante(Imagen primeraImagen, Imagen segundaImagen) {
        int resultantImageWidth = 1;
        if (primeraImagen.getWidth() > segundaImagen.getWidth())
            resultantImageWidth = primeraImagen.getWidth();
        else if (segundaImagen.getWidth() >= primeraImagen.getWidth())
            resultantImageWidth = segundaImagen.getWidth();
        
        return resultantImageWidth;
    }

    public int calcularAltoResultante(Imagen primeraImagen, Imagen segundaImagen) {
        int resultantImageHeight = 1;
        if (primeraImagen.getHeight() > segundaImagen.getHeight())
            resultantImageHeight = primeraImagen.getHeight();
        else if (segundaImagen.getHeight() >= primeraImagen.getHeight())
            resultantImageHeight = segundaImagen.getHeight();
        
        return resultantImageHeight;
    }

    public void completarConCero(WritableImage imagenANormalizar) {
        PixelWriter pixelAEscribir = imagenANormalizar.getPixelWriter();
        for (int i = 0; i < imagenANormalizar.getWidth(); i++) 
            for (int j = 0; j < imagenANormalizar.getHeight(); j++) {
                Color color = Color.rgb(0, 0, 0);
                pixelAEscribir.setColor(i, j, color);
            }
    }

    public void completarConCero(int[][] imagen) {
        for (int i = 0; i < imagen.length; i++) 
            for (int j = 0; j < imagen[0].length; j++) 
                imagen[i][j] = 0;
    }

    private void setearValoresDePixelesPorCanalesEnImagen(WritableImage imagenANormalizar, Imagen imagen) {
        int valorDelCanalRojo = 0;
        int valorDelCanalVerde = 0;
        int valorDelCanalAzul = 0;
        PixelWriter pixelAEscribir = imagenANormalizar.getPixelWriter();
        for (int i = 0; i < imagen.getWidth(); i++)
            for (int j = 0; j < imagen.getHeight(); j++) {
                valorDelCanalRojo = imagen.getRChannelValue(i, j);
                valorDelCanalVerde = imagen.getGChannelValue(i, j);
                valorDelCanalAzul = imagen.getBChannelValue(i, j);
                Color color = Color.rgb(valorDelCanalRojo, valorDelCanalVerde, valorDelCanalAzul);
                pixelAEscribir.setColor(i, j, color);
            }
    }

    public int[][] ajustarEscalaDeGrises(int[][] valoresDeUnCanal) {
        int imagenResultante = this.grayLevelStatisticsService.calcularMaximoNivelDeGris(valoresDeUnCanal);
        for (int i = 0; i < valoresDeUnCanal.length; i++)
            for (int j = 0; j < valoresDeUnCanal[i].length; j++) {
                int newPixelValue = (int) (valoresDeUnCanal[i][j] * ((double) MAXIMO_VALOR_DE_UN_PIXEL / imagenResultante));
                valoresDeUnCanal[i][j] = newPixelValue;
            }
        return valoresDeUnCanal;
    }

    public int[][] ajustarEscalaDeGrises(int[][] valoresDeUnCanal, List<Pixel> pixelesContaminados) {
        int imagenResultante = this.grayLevelStatisticsService.calcularMaximoNivelDeGris(valoresDeUnCanal);
        for (Pixel pixel : pixelesContaminados) {
            int oldPixelValue = valoresDeUnCanal[pixel.getX()][pixel.getY()];
            int newPixelValue = (int) (oldPixelValue * ((double) MAXIMO_VALOR_DE_UN_PIXEL / imagenResultante));
            valoresDeUnCanal[pixel.getX()][pixel.getY()] = newPixelValue;
        }
        return valoresDeUnCanal;
    }

    public int[][] convertirAImagenContaminadaValida(int[][] valoresDeUnCanal, List<Pixel> pixelesContaminados) {
        return ajustarEscalaDeGrises(desplazarValoresDeLosPixelesHaciaCero(valoresDeUnCanal), pixelesContaminados);
    }

    public Image escribirNuevosValoresDePixelesEnLaImagen(int[][] valoresDelCanalRojo, int[][] valoresDelCanalVerde,
                                                          int[][] valoresDelCanalAzul) {
        int width = valoresDelCanalRojo.length;
        int height = valoresDelCanalRojo[0].length;
        WritableImage imagen = new WritableImage(width, height);
        PixelWriter pixelAEscribir = imagen.getPixelWriter();
        int valorDelPixelRojo = 0;
        int valorDelPixelVerde = 0;
        int valorDelPixelAzul = 0;
        for (int i = 0; i < (int) imagen.getWidth(); i++)
            for (int j = 0; j < (int) imagen.getHeight(); j++) {
                valorDelPixelRojo = valoresDelCanalRojo[i][j];
                valorDelPixelVerde = valoresDelCanalVerde[i][j];
                valorDelPixelAzul = valoresDelCanalAzul[i][j];
                Color color = Color.rgb(valorDelPixelRojo, valorDelPixelVerde, valorDelPixelAzul);
                pixelAEscribir.setColor(i, j, color);
            }
        return imagen;
    }

    public int[][] sumaValoresPixelesRojos(Image primeraImagen, Image segundaImagen) {
        int[][] result = new int[(int) primeraImagen.getWidth()][(int) primeraImagen.getHeight()];
        PixelReader pixelLeidoDeLaImagen1 = primeraImagen.getPixelReader();
        PixelReader pixelLeidoDeLaImagen2 = segundaImagen.getPixelReader();
        for (int i = 0; i < (int) primeraImagen.getWidth(); i++) {
            for (int j = 0; j < (int) primeraImagen.getHeight(); j++) {
                double sumResult = ((pixelLeidoDeLaImagen1.getColor(i, j).getRed() * MAXIMO_VALOR_DE_UN_PIXEL) + (pixelLeidoDeLaImagen2.getColor(i, j).getRed() * MAXIMO_VALOR_DE_UN_PIXEL));
                result[i][j] = (int) Math.round(sumResult);
            }
        }
        return this.ajustarEscalaDeGrises(this.desplazarValoresDeLosPixelesHaciaCero(result));
    }

    public int[][] sumaValoresPixelesVerde(Image primeraImagen, Image segundaImagen) {
        int[][] result = new int[(int) primeraImagen.getWidth()][(int) primeraImagen.getHeight()];
        PixelReader pixelLeidoDeLaImagen1 = primeraImagen.getPixelReader();
        PixelReader pixelLeidoDeLaImagen2 = segundaImagen.getPixelReader();
        for (int i = 0; i < (int) primeraImagen.getWidth(); i++)
            for (int j = 0; j < (int) primeraImagen.getHeight(); j++) {
                double sumResult = ((pixelLeidoDeLaImagen1.getColor(i, j).getGreen() * MAXIMO_VALOR_DE_UN_PIXEL) + (pixelLeidoDeLaImagen2.getColor(i, j).getGreen() * MAXIMO_VALOR_DE_UN_PIXEL));
                result[i][j] = (int) Math.round(sumResult);
            }
        return this.ajustarEscalaDeGrises(this.desplazarValoresDeLosPixelesHaciaCero(result));
    }

    public int[][] sumaValoresPixelesAzul(Image primeraImagen, Image segundaImagen) {
        int[][] result = new int[(int) primeraImagen.getWidth()][(int) primeraImagen.getHeight()];
        PixelReader pixelLeidoDeLaImagen1 = primeraImagen.getPixelReader();
        PixelReader pixelLeidoDeLaImagen2 = segundaImagen.getPixelReader();
        for (int i = 0; i < (int) primeraImagen.getWidth(); i++)
            for (int j = 0; j < (int) primeraImagen.getHeight(); j++) {
                double sumResult = ((pixelLeidoDeLaImagen1.getColor(i, j).getBlue() * MAXIMO_VALOR_DE_UN_PIXEL) + (pixelLeidoDeLaImagen2.getColor(i, j).getBlue() * MAXIMO_VALOR_DE_UN_PIXEL));
                result[i][j] = (int) Math.round(sumResult);
            }
        return this.ajustarEscalaDeGrises(this.desplazarValoresDeLosPixelesHaciaCero(result));
    }

    public int[][] multiplicarValoresPixelesRojos(Image primeraImagen, Image segundaImagen) {
        int[][] result = new int[(int) primeraImagen.getWidth()][(int) primeraImagen.getHeight()];
        PixelReader pixelLeidoDeLaImagen1 = primeraImagen.getPixelReader();
        PixelReader pixelLeidoDeLaImagen2 = segundaImagen.getPixelReader();
        for (int i = 0; i < (int) primeraImagen.getWidth(); i++)
            for (int j = 0; j < (int) primeraImagen.getHeight(); j++) {
                double productResult = ((pixelLeidoDeLaImagen1.getColor(i, j).getRed() * MAXIMO_VALOR_DE_UN_PIXEL) * (pixelLeidoDeLaImagen2.getColor(i, j).getRed() * MAXIMO_VALOR_DE_UN_PIXEL));
                result[i][j] = (int) Math.round(productResult);
            }
        return this.ajustarEscalaDeGrises(this.desplazarValoresDeLosPixelesHaciaCero(result));
    }

    public int[][] multiplicarValoresPixelesVerdes(Image primeraImagen, Image segundaImagen) {
        int[][] result = new int[(int) primeraImagen.getWidth()][(int) primeraImagen.getHeight()];
        PixelReader pixelLeidoDeLaImagen1 = primeraImagen.getPixelReader();
        PixelReader pixelLeidoDeLaImagen2 = segundaImagen.getPixelReader();
        for (int i = 0; i < (int) primeraImagen.getWidth(); i++)
            for (int j = 0; j < (int) primeraImagen.getHeight(); j++) {
                double productResult = ((pixelLeidoDeLaImagen1.getColor(i, j).getGreen() * MAXIMO_VALOR_DE_UN_PIXEL) * (pixelLeidoDeLaImagen2.getColor(i, j).getGreen() * MAXIMO_VALOR_DE_UN_PIXEL));
                result[i][j] = (int) Math.round(productResult);
            }
        return this.ajustarEscalaDeGrises(this.desplazarValoresDeLosPixelesHaciaCero(result));
    }

    public int[][] multiplicarValoresPixelesAzules(Image primeraImagen, Image segundaImagen) {
        int[][] result = new int[(int) primeraImagen.getWidth()][(int) primeraImagen.getHeight()];
        PixelReader pixelLeidoDeLaImagen1 = primeraImagen.getPixelReader();
        PixelReader pixelLeidoDeLaImagen2 = segundaImagen.getPixelReader();
        for (int i = 0; i < (int) primeraImagen.getWidth(); i++)
            for (int j = 0; j < (int) primeraImagen.getHeight(); j++) {
                double productResult = ((pixelLeidoDeLaImagen1.getColor(i, j).getBlue() * MAXIMO_VALOR_DE_UN_PIXEL) * (pixelLeidoDeLaImagen2.getColor(i, j).getBlue() * MAXIMO_VALOR_DE_UN_PIXEL));
                result[i][j] = (int) Math.round(productResult);
            }
        return this.ajustarEscalaDeGrises(this.desplazarValoresDeLosPixelesHaciaCero(result));
    }

    public int[][] restarValoresPixelesRojos(Image primeraImagen, Image segundaImagen) {
        int[][] result = new int[(int) primeraImagen.getWidth()][(int) primeraImagen.getHeight()];
        PixelReader pixelLeidoDeLaImagen1 = primeraImagen.getPixelReader();
        PixelReader pixelLeidoDeLaImagen2 = segundaImagen.getPixelReader();
        for (int i = 0; i < (int) primeraImagen.getWidth(); i++)
            for (int j = 0; j < (int) primeraImagen.getHeight(); j++) {
                double sumResult = ((pixelLeidoDeLaImagen1.getColor(i, j).getRed() * MAXIMO_VALOR_DE_UN_PIXEL) - (pixelLeidoDeLaImagen2.getColor(i, j).getRed() * MAXIMO_VALOR_DE_UN_PIXEL));
                result[i][j] = (int) Math.round(sumResult);
            }
        return this.ajustarEscalaDeGrises(this.desplazarValoresDeLosPixelesHaciaCero(result));
    }

    public int[][] restarValoresPixelesVerdes(Image primeraImagen, Image segundaImagen) {
        int[][] result = new int[(int) primeraImagen.getWidth()][(int) primeraImagen.getHeight()];
        PixelReader pixelLeidoDeLaImagen1 = primeraImagen.getPixelReader();
        PixelReader pixelLeidoDeLaImagen2 = segundaImagen.getPixelReader();
        for (int i = 0; i < (int) primeraImagen.getWidth(); i++)
            for (int j = 0; j < (int) primeraImagen.getHeight(); j++) {
                double sumResult = ((pixelLeidoDeLaImagen1.getColor(i, j).getGreen() * MAXIMO_VALOR_DE_UN_PIXEL) - (pixelLeidoDeLaImagen2.getColor(i, j).getGreen() * MAXIMO_VALOR_DE_UN_PIXEL));
                result[i][j] = (int) Math.round(sumResult);
            }
        return this.ajustarEscalaDeGrises(this.desplazarValoresDeLosPixelesHaciaCero(result));
    }

    public int[][] restarValoresPixelesAzules(Image primeraImagen, Image segundaImagen) {
        int[][] result = new int[(int) primeraImagen.getWidth()][(int) primeraImagen.getHeight()];
        PixelReader pixelLeidoDeLaImagen1 = primeraImagen.getPixelReader();
        PixelReader pixelLeidoDeLaImagen2 = segundaImagen.getPixelReader();
        for (int i = 0; i < (int) primeraImagen.getWidth(); i++)
            for (int j = 0; j < (int) primeraImagen.getHeight(); j++) {
                double sumResult = ((pixelLeidoDeLaImagen1.getColor(i, j).getBlue() * MAXIMO_VALOR_DE_UN_PIXEL) - (pixelLeidoDeLaImagen2.getColor(i, j).getBlue() * MAXIMO_VALOR_DE_UN_PIXEL));
                result[i][j] = (int) Math.round(sumResult);
            }
        return this.ajustarEscalaDeGrises(this.desplazarValoresDeLosPixelesHaciaCero(result));
    }

    public int[][] desplazarValoresDeLosPixelesHaciaCero(int[][] pixelsValues) {
        int minPixelValue = this.grayLevelStatisticsService.calcularMinimoNivelDeGris(pixelsValues);
        for (int i = 0; i < pixelsValues.length; i++)
            for (int j = 0; j < pixelsValues[i].length; j++)
                pixelsValues[i][j] -= minPixelValue;
        return pixelsValues;
    }

    private int[][] toValidImageMatrix(int[][] pixels) {
        return this.ajustarEscalaDeGrises(this.desplazarValoresDeLosPixelesHaciaCero(pixels));
    }

    public MatrizCanales toValidImageMatrix(MatrizCanales channelMatrix) {
        int[][] redChannel = this.toValidImageMatrix(channelMatrix.getRedChannel());
        int[][] greenChannel = this.toValidImageMatrix(channelMatrix.getGreenChannel());
        int[][] blueChannel = this.toValidImageMatrix(channelMatrix.getBlueChannel());
        return new MatrizCanales(redChannel, greenChannel, blueChannel);
    }

    public MatrizCanales multiplyChannelMatrixs(MatrizCanales channelMatrix1, MatrizCanales channelMatrix2) {
        int[][] redChannel = multiplyMatrix(channelMatrix1.getRedChannel(), channelMatrix2.getRedChannel());
        int[][] greenChannel = multiplyMatrix(channelMatrix1.getGreenChannel(), channelMatrix2.getGreenChannel());
        int[][] blueChannel = multiplyMatrix(channelMatrix1.getBlueChannel(), channelMatrix2.getBlueChannel());
        return toValidImageMatrix(new MatrizCanales(redChannel, greenChannel, blueChannel));
    }

    public MatrizCanales sumChannelMatrixs(MatrizCanales channelMatrix1, MatrizCanales channelMatrix2) {
        int[][] redChannel = sumMatrix(channelMatrix1.getRedChannel(), channelMatrix2.getRedChannel());
        int[][] greenChannel = sumMatrix(channelMatrix1.getGreenChannel(), channelMatrix2.getGreenChannel());
        int[][] blueChannel = sumMatrix(channelMatrix1.getBlueChannel(), channelMatrix2.getBlueChannel());
        return toValidImageMatrix(new MatrizCanales(redChannel, greenChannel, blueChannel));
    }

    public MatrizCanales sqrtChannelMatrixs(MatrizCanales channelMatrix) {
        int[][] redChannel = sqrtMatrix(channelMatrix.getRedChannel());
        int[][] greenChannel = sqrtMatrix(channelMatrix.getGreenChannel());
        int[][] blueChannel = sqrtMatrix(channelMatrix.getBlueChannel());
        return toValidImageMatrix(new MatrizCanales(redChannel, greenChannel, blueChannel));
    }

    public int[][] sumMatrix(int[][] matrix1, int[][] matrix2) {
        int width = matrix1.length;
        int height = matrix1[0].length;
        int[][] result = new int[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                result[i][j] = matrix1[i][j] + matrix2[i][j];
        return result;
    }

    public int[][] multiplyMatrix(int[][] matrix1, int[][] matrix2) {
        int width = matrix1.length;
        int height = matrix1[0].length;
        int[][] result = new int[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                double productResult = matrix1[i][j] * matrix2[i][j];
                result[i][j] = (int) Math.round(productResult);
            }
        return result;
    }

    private int[][] sqrtMatrix(int[][] matrix) {
        int width = matrix.length;
        int height = matrix[0].length;

        int[][] result = new int[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                double value = Math.sqrt(matrix[i][j]);
                result[i][j] = (int) Math.round(value);
            }
        return result;
    }

    public MatrizCanales calculateAbsoluteSum(MatrizCanales firstImage, MatrizCanales secondImage) {
        int[][] redChannel = this.calculateAbsoluteSum(firstImage.getRedChannel(), secondImage.getRedChannel());
        int[][] greenChannel = this.calculateAbsoluteSum(firstImage.getGreenChannel(), secondImage.getGreenChannel());
        int[][] blueChannel = this.calculateAbsoluteSum(firstImage.getBlueChannel(), secondImage.getBlueChannel());
        return new MatrizCanales(redChannel, greenChannel, blueChannel);
    }

    private int[][] calculateAbsoluteSum(int[][] firstChannel, int[][] secondChanne1) {

        int[][] absoluteSumChannel = new int[firstChannel.length][firstChannel[0].length];

        for (int x=0; x < firstChannel.length; x++)
            for (int y=0; y < firstChannel[x].length; y++)
                absoluteSumChannel[x][y] = Math.abs(firstChannel[x][y]) + Math.abs(secondChanne1[x][y]);
        return absoluteSumChannel;
    }

}

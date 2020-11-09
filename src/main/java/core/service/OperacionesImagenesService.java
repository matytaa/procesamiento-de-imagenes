package core.service;

import core.service.statistics.GrayLevelStatisticsService;
import dominio.customimage.MatrizCanales;
import dominio.customimage.Imagen;
import dominio.customimage.Pixel;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.List;

public class OperacionesImagenesService {

    static final int MAXIMO_VALOR_DE_UN_PIXEL = 255;
    private GrayLevelStatisticsService grayLevelStatisticsService;

    public OperacionesImagenesService(GrayLevelStatisticsService grayLevelStatisticsService) {
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
        if (primeraImagen.getAncho() > segundaImagen.getAncho())
            resultantImageWidth = primeraImagen.getAncho();
        else if (segundaImagen.getAncho() >= primeraImagen.getAncho())
            resultantImageWidth = segundaImagen.getAncho();
        
        return resultantImageWidth;
    }

    public int calcularAltoResultante(Imagen primeraImagen, Imagen segundaImagen) {
        int resultantImageHeight = 1;
        if (primeraImagen.getAltura() > segundaImagen.getAltura())
            resultantImageHeight = primeraImagen.getAltura();
        else if (segundaImagen.getAltura() >= primeraImagen.getAltura())
            resultantImageHeight = segundaImagen.getAltura();
        
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
        for (int i = 0; i < imagen.getAncho(); i++)
            for (int j = 0; j < imagen.getAltura(); j++) {
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

    public int[][] desplazarValoresDeLosPixelesHaciaCero(int[][] pixeles) {
        int valorMinimoPixel = this.grayLevelStatisticsService.calcularMinimoNivelDeGris(pixeles);
        for (int i = 0; i < pixeles.length; i++)
            for (int j = 0; j < pixeles[i].length; j++)
                pixeles[i][j] -= valorMinimoPixel;
        return pixeles;
    }

    private int[][] aMatrizValida(int[][] pixels) {
        return this.ajustarEscalaDeGrises(this.desplazarValoresDeLosPixelesHaciaCero(pixels));
    }

    public MatrizCanales aMatrizValida(MatrizCanales matrizCanal) {
        int[][] redChannel = this.aMatrizValida(matrizCanal.getCanalRojo());
        int[][] greenChannel = this.aMatrizValida(matrizCanal.getCanalVerde());
        int[][] blueChannel = this.aMatrizValida(matrizCanal.getCanalAzul());
        return new MatrizCanales(redChannel, greenChannel, blueChannel);
    }

    public MatrizCanales multiplicarMatrizPorCanal(MatrizCanales matrizCanal1, MatrizCanales matrizCanal2) {
        int[][] redChannel = multiplicarMatriz(matrizCanal1.getCanalRojo(), matrizCanal2.getCanalRojo());
        int[][] greenChannel = multiplicarMatriz(matrizCanal1.getCanalVerde(), matrizCanal2.getCanalVerde());
        int[][] blueChannel = multiplicarMatriz(matrizCanal1.getCanalAzul(), matrizCanal2.getCanalAzul());
        return aMatrizValida(new MatrizCanales(redChannel, greenChannel, blueChannel));
    }

    public MatrizCanales sumarMatrizCanales(MatrizCanales matrizCanal1, MatrizCanales matrizCanal2) {
        int[][] redChannel = sumarMatriz(matrizCanal1.getCanalRojo(), matrizCanal2.getCanalRojo());
        int[][] greenChannel = sumarMatriz(matrizCanal1.getCanalVerde(), matrizCanal2.getCanalVerde());
        int[][] blueChannel = sumarMatriz(matrizCanal1.getCanalAzul(), matrizCanal2.getCanalAzul());
        return aMatrizValida(new MatrizCanales(redChannel, greenChannel, blueChannel));
    }

    public MatrizCanales raizMatrizCanal(MatrizCanales matrizCanales) {
        int[][] redChannel = raizMatriz(matrizCanales.getCanalRojo());
        int[][] greenChannel = raizMatriz(matrizCanales.getCanalVerde());
        int[][] blueChannel = raizMatriz(matrizCanales.getCanalAzul());
        return aMatrizValida(new MatrizCanales(redChannel, greenChannel, blueChannel));
    }

    public int[][] sumarMatriz(int[][] matriz1, int[][] matriz2) {
        int ancho = matriz1.length;
        int alto = matriz1[0].length;
        int[][] salida = new int[ancho][alto];
        for (int i = 0; i < ancho; i++)
            for (int j = 0; j < alto; j++)
                salida[i][j] = matriz1[i][j] + matriz2[i][j];
        return salida;
    }

    public int[][] multiplicarMatriz(int[][] matriz1, int[][] matriz2) {
        int ancho = matriz1.length;
        int alto = matriz1[0].length;
        int[][] salida = new int[ancho][alto];
        for (int i = 0; i < ancho; i++)
            for (int j = 0; j < alto; j++) {
                double resultado = matriz1[i][j] * matriz2[i][j];
                salida[i][j] = (int) Math.round(resultado);
            }
        return salida;
    }

    private int[][] raizMatriz(int[][] matriz) {
        int ancho = matriz.length;
        int alto = matriz[0].length;

        int[][] salida = new int[ancho][alto];
        for (int i = 0; i < ancho; i++)
            for (int j = 0; j < alto; j++) {
                double raiz = Math.sqrt(matriz[i][j]);
                salida[i][j] = (int) Math.round(raiz);
            }
        return salida;
    }

    public MatrizCanales calcularSumaAbsoluta(MatrizCanales firstImage, MatrizCanales secondImage) {
        int[][] redChannel = this.calcularSumaAbsoluta(firstImage.getCanalRojo(), secondImage.getCanalRojo());
        int[][] greenChannel = this.calcularSumaAbsoluta(firstImage.getCanalVerde(), secondImage.getCanalVerde());
        int[][] blueChannel = this.calcularSumaAbsoluta(firstImage.getCanalAzul(), secondImage.getCanalAzul());
        return new MatrizCanales(redChannel, greenChannel, blueChannel);
    }

    private int[][] calcularSumaAbsoluta(int[][] firstChannel, int[][] secondChanne1) {

        int[][] absoluteSumChannel = new int[firstChannel.length][firstChannel[0].length];

        for (int x=0; x < firstChannel.length; x++)
            for (int y=0; y < firstChannel[x].length; y++)
                absoluteSumChannel[x][y] = Math.abs(firstChannel[x][y]) + Math.abs(secondChanne1[x][y]);
        return absoluteSumChannel;
    }

}

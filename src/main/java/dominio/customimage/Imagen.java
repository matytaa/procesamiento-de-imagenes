package dominio.customimage;

import core.provider.ServiceProvider;
import core.service.MatrizService;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Imagen {

    public static final Imagen EMPTY = new Imagen(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), Format.PNG);
    private static final int INDEX_OUT_OF_BOUND = -1;
    private final PixelReader reader;
    private final BufferedImage bufferedImage;
    private final Format format;
    private final MatrizService matrizService;
    private int[][] matrizRed;
    private int[][] matrizGreen;
    private int[][] matrizBlue;
    private List<Pixel> pixeles;

    public Imagen(MatrizCanales channelMatrix, String formatString) {
        this(channelMatrixToFXImage(channelMatrix.getCanalRojo(), channelMatrix.getCanalVerde(), channelMatrix.getCanalAzul()), formatString);
    }

    public Imagen(Image image, String formatString) {
        this(SwingFXUtils.fromFXImage(image, null), formatString); //See the other constructor
    }

    public Imagen(BufferedImage bufferedImage, String formatString) {
        this.bufferedImage = bufferedImage;
        this.format = new Format(formatString);
        WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
        this.reader = image.getPixelReader();
        this.pixeles = getListOfPixels();
        this.matrizService = ServiceProvider.provideMatrixService();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        this.matrizRed = matrizService.toChannelMatrix((x, y) -> reader.getColor(x, y).getRed(), width, height);
        this.matrizGreen = matrizService.toChannelMatrix((x, y) -> reader.getColor(x, y).getGreen(), width, height);
        this.matrizBlue = matrizService.toChannelMatrix((x, y) -> reader.getColor(x, y).getBlue(), width, height);
    }

    private static Image channelMatrixToFXImage(int[][] red, int[][] green, int[][] blue) {
        int width = red.length;
        int height = red[0].length;
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color color = Color.rgb(red[i][j], green[i][j], blue[i][j]);
                pixelWriter.setColor(i, j, color);
            }
        }

        return image;
    }

    public int[][] getMatrizRed() {
        return this.matrizService.copy(matrizRed);
    }

    public int[][] getMatrizGreen() {
        return this.matrizService.copy(matrizGreen);
    }

    public int[][] getMatrizBlue() {
        return this.matrizService.copy(matrizBlue);
    }

    public int getPixelQuantity() {
        return this.pixeles.size();
    }

    private List<Pixel> getListOfPixels() {
        List<Pixel> total = new ArrayList<>();

        for (int columna = 0; columna < getAncho(); columna++) {
            for (int fila = 0; fila < getAltura(); fila++) {
                total.add(new Pixel(columna, fila, getPixelReader().getColor(columna, fila)));
            }
        }

        return total;
    }

    public List<Pixel> seleccionarNPiexelesRandom(int n) {
        List<Pixel> copy = new LinkedList<>(pixeles);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }

    public String getFormatString() {
        return format.getFormatString();
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public int getPromedioPixel(Integer x, Integer y) {
        RGB pixelValue = getPixelValue(x, y);
        return Math.round((pixelValue.getRed() +
                pixelValue.getGreen() +
                pixelValue.getBlue()) / 3);
    }

    public RGB getPixelValue(Integer x, Integer y) {
        try {
            return new RGB(this.getRChannelValue(x, y), this.getGChannelValue(x, y), this.getBChannelValue(x, y));
        } catch (IndexOutOfBoundsException e) {
            return new RGB(INDEX_OUT_OF_BOUND, INDEX_OUT_OF_BOUND, INDEX_OUT_OF_BOUND);
        }
    }

    public int getRChannelValue(int x, int y) {
        return (int) (reader.getColor(x, y).getRed() * 255);
    }

    public int getGChannelValue(int x, int y) {
        return (int) (reader.getColor(x, y).getGreen() * 255);
    }

    public int getBChannelValue(int x, int y) {
        return (int) (reader.getColor(x, y).getBlue() * 255);
    }

    public Integer getAltura() {
        return this.bufferedImage.getHeight();
    }

    public Integer getAncho() {
        return this.bufferedImage.getWidth();
    }

    public PixelReader getPixelReader() {
        return reader;
    }

    public Image toFXImage() {
        return this.matrizService.toImage(this.getMatrizRed(), this.getMatrizGreen(), this.getMatrizBlue());
    }

    public boolean isPosicionValida(int width, int height, int i, int j) {
        return j >= 0 && j < height && i >= 0 && i < width;
    }

    public Color getColor(int x, int y) {
        return Color.rgb(getRChannelValue(x, y), getGChannelValue(x, y), getBChannelValue(x, y));
    }
}

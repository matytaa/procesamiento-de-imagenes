package core.service;

import domain.customimage.Imagen;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ModifyImageService {

    public Imagen createModifiedImage(Imagen image, Integer pixelX, Integer pixelY, Integer valueR, Integer valueG, Integer valueB) {

        int width = image.getAncho();
        int height = image.getAltura();

        PixelReader pixelReader = image.getPixelReader();

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color argb = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, argb);
            }
        }

        this.modifySinglePixel(pixelX, pixelY, valueR, valueG, valueB, pixelWriter);
        return new Imagen(SwingFXUtils.fromFXImage(writableImage, null), image.getFormatString());
    }

    //Como la imagen es en escala de grises los distintos valores de R, G, B son iguales
    public void modifySinglePixel(Integer pixelX, Integer pixelY, Integer value, PixelWriter pixelWriter) {
        Color modifiedColor = Color.rgb(value,value,value);
        pixelWriter.setColor(pixelX,pixelY,modifiedColor);
    }

    public void modifySinglePixel(Integer pixelX, Integer pixelY, Integer valueR, Integer valueG, Integer valueB, PixelWriter pixelWriter) {
        Color modifiedColor = Color.rgb(valueR, valueG, valueB);
        pixelWriter.setColor(pixelX, pixelY, modifiedColor);
    }

    public void modifySingleGrayPixel(Integer pixelX, Integer pixelY, Integer valueGray, PixelWriter pixelWriter) {
        modifySinglePixel(pixelX, pixelY, valueGray, valueGray, valueGray, pixelWriter);
    }
}

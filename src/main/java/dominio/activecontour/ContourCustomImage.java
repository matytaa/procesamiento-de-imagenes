package dominio.activecontour;

import dominio.customimage.Imagen;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ContourCustomImage {

    private final Imagen customImage;
    private final ActiveContour activeContour;

    public ContourCustomImage(Imagen customImage, ActiveContour activeContour) {
        this.customImage = customImage;
        this.activeContour = activeContour;
    }

    public Imagen getCustomImage() {
        return customImage;
    }

    public ActiveContour getActiveContour() {
        return activeContour;
    }

    public Image drawActiveContour() {
        WritableImage imageWithContour = new WritableImage(customImage.getAncho(), customImage.getAltura());
        PixelWriter pixelWriter = imageWithContour.getPixelWriter();

        for (int i = 0; i < customImage.getAncho(); i++) {
            for (int j = 0; j < customImage.getAltura(); j++) {
                pixelWriter.setColor(i, j, customImage.getColor(i, j));
            }
        }

        activeContour.getlIn().forEach(xyPoint -> pixelWriter.setColor(xyPoint.getX(), xyPoint.getY(), Color.RED));
        activeContour.getlOut().forEach(xyPoint -> pixelWriter.setColor(xyPoint.getX(), xyPoint.getY(), Color.BLUE));

        return new Imagen(imageWithContour, customImage.getFormatString()).toFXImage();
    }
}

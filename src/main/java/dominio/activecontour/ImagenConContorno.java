package dominio.activecontour;

import dominio.customimage.Imagen;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImagenConContorno {

    private final Imagen customImage;
    private final ContornoActivo contornoActivo;

    public ImagenConContorno(Imagen customImage, ContornoActivo contornoActivo) {
        this.customImage = customImage;
        this.contornoActivo = contornoActivo;
    }

    public Imagen getCustomImage() {
        return customImage;
    }

    public ContornoActivo getContornoActivo() {
        return contornoActivo;
    }

    public Image dibujarContornoActivo() {
        WritableImage imageWithContour = new WritableImage(customImage.getAncho(), customImage.getAltura());
        PixelWriter pixelWriter = imageWithContour.getPixelWriter();

        for (int i = 0; i < customImage.getAncho(); i++) {
            for (int j = 0; j < customImage.getAltura(); j++) {
                pixelWriter.setColor(i, j, customImage.getColor(i, j));
            }
        }

        contornoActivo.getBordeInterno().forEach(xyPoint -> pixelWriter.setColor(xyPoint.getX(), xyPoint.getY(), Color.RED));
        contornoActivo.getBordeExterno().forEach(xyPoint -> pixelWriter.setColor(xyPoint.getX(), xyPoint.getY(), Color.BLUE));

        return new Imagen(imageWithContour, customImage.getFormatString()).toFXImage();
    }
}

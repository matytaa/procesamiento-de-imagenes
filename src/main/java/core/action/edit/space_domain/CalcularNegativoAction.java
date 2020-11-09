package core.action.edit.space_domain;

import core.repository.RepositorioImagen;
import core.service.ModifyImageService;
import dominio.customimage.Imagen;
import dominio.customimage.RGB;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.Optional;

public class CalcularNegativoAction {

    private final RepositorioImagen repositorioImagen;
    private final ModifyImageService modifyImageService;

    public CalcularNegativoAction(RepositorioImagen repositorioImagen, ModifyImageService modifyImageService) {
        this.repositorioImagen = repositorioImagen;
        this.modifyImageService = modifyImageService;
    }

    public Image execute() {

        Optional<Imagen> imageOptional = this.repositorioImagen.obtenerImagen();
        if (!imageOptional.isPresent())
            return null;
        Imagen customImage = imageOptional.get();
        WritableImage image = new WritableImage(customImage.getAncho(), customImage.getAltura());
        PixelWriter pixelWriter = image.getPixelWriter();

        for (int i = 0; i < image.getWidth(); i++)
            for (int j = 0; j < image.getHeight(); j++) {
                RGB pixelValue = customImage.getPixelValue(i, j);
                this.modifyImageService.modifySinglePixel(i, j, toNegative(pixelValue.getRed()), toNegative(pixelValue.getGreen()), toNegative(pixelValue.getBlue()), pixelWriter);
            }

        return image;
    }

    // T(r) = (L-1) - r
    private int toNegative(Integer value) {
        return 255 - value;
    }
}

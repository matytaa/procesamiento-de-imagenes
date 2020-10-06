package core.action.edit.space_domain;

import core.repository.ImagenRepository;
import core.service.ModifyImageService;
import domain.customimage.Imagen;
import domain.customimage.RGB;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.Optional;

public class CalculateNegativeImageAction {

    private final ImagenRepository imagenRepository;
    private final ModifyImageService modifyImageService;

    public CalculateNegativeImageAction(ImagenRepository imagenRepository, ModifyImageService modifyImageService) {
        this.imagenRepository = imagenRepository;
        this.modifyImageService = modifyImageService;
    }

    public Image execute() {

        Optional<Imagen> imageOptional = this.imagenRepository.getImagen();
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

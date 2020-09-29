package core.action.gradient;

import core.repository.ImageRepository;
import core.service.generation.ImageGradientService;
import domain.customimage.Imagen;
import domain.customimage.Format;
import domain.generation.Gradient;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class CreateImageWithGradientAction {

    private final ImageGradientService imageGradientService;
    private final ImageRepository imageRepository;

    public CreateImageWithGradientAction(ImageGradientService imageGradientService, ImageRepository imageRepository) {
        this.imageGradientService = imageGradientService;
        this.imageRepository = imageRepository;
    }

    public Imagen execute(int width, int height, Gradient value) {
        switch (value) {
            case GREY:
                Image greyGradient = this.imageGradientService.createGreyGradient(width, height);
                return putOnRepository(SwingFXUtils.fromFXImage(greyGradient, null));
            case COLOR:
                Image colorGradient = this.imageGradientService.createColorGradient(width, height);
                return putOnRepository(SwingFXUtils.fromFXImage(colorGradient, null));
            default:
                return Imagen.EMPTY;
        }
    }

    private Imagen putOnRepository(BufferedImage bufferedImage) {
        return imageRepository.saveImage(new Imagen(bufferedImage, Format.PNG));
    }
}

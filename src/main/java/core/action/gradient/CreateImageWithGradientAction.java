package core.action.gradient;

import core.repository.ImagenRepository;
import core.service.generation.ImageGradientService;
import domain.customimage.Imagen;
import domain.customimage.Format;
import domain.generation.Gradient;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class CreateImageWithGradientAction {

    private final ImageGradientService imageGradientService;
    private final ImagenRepository imagenRepository;

    public CreateImageWithGradientAction(ImageGradientService imageGradientService, ImagenRepository imagenRepository) {
        this.imageGradientService = imageGradientService;
        this.imagenRepository = imagenRepository;
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
        return imagenRepository.salvarImagen(new Imagen(bufferedImage, Format.PNG));
    }
}

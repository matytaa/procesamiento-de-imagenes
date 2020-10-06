package core.action.figure;

import core.repository.ImagenRepository;
import core.service.generation.ImageFigureService;
import domain.customimage.Imagen;
import domain.customimage.Format;
import domain.generation.Figure;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class CreateImageWithFigureAction {

    private final ImageFigureService imageFigureService;
    private final ImagenRepository imagenRepository;

    public CreateImageWithFigureAction(ImageFigureService imageFigureService, ImagenRepository imagenRepository) {
        this.imageFigureService = imageFigureService;
        this.imagenRepository = imagenRepository;
    }

    public Imagen execute(int width, int height, Figure value) {

        switch (value) {
            case CIRCLE:
                Image imageWithCircle = imageFigureService.createImageWithCircle(width, height);
                return putOnRepository(SwingFXUtils.fromFXImage(imageWithCircle, null));
            case QUADRATE:
                Image imageWithQuadrate = imageFigureService.createImageWithQuadrate(width, height);
                return putOnRepository(SwingFXUtils.fromFXImage(imageWithQuadrate, null));
            default:
                return Imagen.EMPTY;
        }
    }

    private Imagen putOnRepository(BufferedImage bufferedImage) {
        return imagenRepository.salvarImagen(new Imagen(bufferedImage, Format.PNG));
    }
}

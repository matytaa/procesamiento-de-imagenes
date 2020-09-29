package core.action.figure;

import core.repository.ImageRepository;
import core.service.generation.ImageFigureService;
import domain.customimage.Imagen;
import domain.customimage.Format;
import domain.generation.Figure;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class CreateImageWithFigureAction {

    private final ImageFigureService imageFigureService;
    private final ImageRepository imageRepository;

    public CreateImageWithFigureAction(ImageFigureService imageFigureService, ImageRepository imageRepository) {
        this.imageFigureService = imageFigureService;
        this.imageRepository = imageRepository;
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
        return imageRepository.saveImage(new Imagen(bufferedImage, Format.PNG));
    }
}

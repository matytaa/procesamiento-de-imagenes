package core.action.image;

import core.repository.ImageRepository;
import domain.customimage.Imagen;


public class UpdateCurrentImageAction {

    private final ImageRepository imageRepository;

    public UpdateCurrentImageAction(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void execute(Imagen customImage) {
        this.imageRepository.saveImage(customImage);
    }
}

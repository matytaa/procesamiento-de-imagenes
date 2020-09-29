package core.action.image;

import core.repository.ImageRepository;
import domain.customimage.Imagen;

public class PutModifiedImageAction {

    private final ImageRepository imageRepository;

    public PutModifiedImageAction(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void execute(Imagen customImage) {
        imageRepository.saveModifiedImage(customImage);
    }
}

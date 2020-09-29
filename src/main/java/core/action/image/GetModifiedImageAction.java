package core.action.image;

import core.repository.ImageRepository;
import domain.customimage.Imagen;

import java.util.Optional;

public class GetModifiedImageAction {

    private final ImageRepository imageRepository;

    public GetModifiedImageAction(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Optional<Imagen> execute() {
        return imageRepository.getModifiedImage();
    }
}

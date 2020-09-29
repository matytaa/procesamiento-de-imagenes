package core.action.image;

import core.repository.ImageRepository;
import domain.customimage.Imagen;

import java.util.Optional;

public class GetImageAction {

    private final ImageRepository imageRepository;

    public GetImageAction(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Optional<Imagen> execute() {
        return imageRepository.getImage();
    }
}

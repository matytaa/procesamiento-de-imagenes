package core.action.edgedetector;

import core.repository.ImageRepository;
import domain.customimage.Imagen;

import java.util.List;
import java.util.Optional;

public class GetImageSequenceAction {

    private final ImageRepository imageRepository;

    public GetImageSequenceAction(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Optional<List<Imagen>> execute() {
        return imageRepository.getImageSequence();
    }
}

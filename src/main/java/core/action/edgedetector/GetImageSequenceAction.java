package core.action.edgedetector;

import core.repository.ImagenRepository;
import domain.customimage.Imagen;

import java.util.List;
import java.util.Optional;

public class GetImageSequenceAction {

    private final ImagenRepository imagenRepository;

    public GetImageSequenceAction(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public Optional<List<Imagen>> execute() {
        return imagenRepository.getImagenes();
    }
}

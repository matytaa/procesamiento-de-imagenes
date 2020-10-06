package core.action.image;

import core.repository.ImagenRepository;
import domain.customimage.Imagen;

import java.util.Optional;

public class GetModifiedImageAction {

    private final ImagenRepository imagenRepository;

    public GetModifiedImageAction(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public Optional<Imagen> execute() {
        return imagenRepository.getImagenModificada();
    }
}

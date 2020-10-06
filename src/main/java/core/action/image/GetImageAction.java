package core.action.image;

import core.repository.ImagenRepository;
import domain.customimage.Imagen;

import java.util.Optional;

public class GetImageAction {

    private final ImagenRepository imagenRepository;

    public GetImageAction(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public Optional<Imagen> execute() {
        return imagenRepository.getImagen();
    }
}

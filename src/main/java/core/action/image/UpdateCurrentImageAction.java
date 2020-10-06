package core.action.image;

import core.repository.ImagenRepository;
import domain.customimage.Imagen;


public class UpdateCurrentImageAction {

    private final ImagenRepository imagenRepository;

    public UpdateCurrentImageAction(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public void execute(Imagen customImage) {
        this.imagenRepository.salvarImagen(customImage);
    }
}

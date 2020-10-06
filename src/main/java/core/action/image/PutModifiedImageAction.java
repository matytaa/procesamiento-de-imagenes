package core.action.image;

import core.repository.ImagenRepository;
import domain.customimage.Imagen;

public class PutModifiedImageAction {

    private final ImagenRepository imagenRepository;

    public PutModifiedImageAction(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public void execute(Imagen customImage) {
        imagenRepository.salvarImagenModificada(customImage);
    }
}

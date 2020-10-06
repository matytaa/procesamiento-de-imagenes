package core.action.image;

import core.repository.ImagenRepository;
import domain.customimage.Imagen;

public class UndoChangesAction {

    private final ImagenRepository imagenRepository;

    public UndoChangesAction(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public Imagen execute() {
        Imagen originalImage = imagenRepository.getImagenOriginalBackup();
        /*Creo una imagen nueva para evitar problemas con las referencias
        Ya que la buffered image nunca la modificamos, no hay problema con utilizarla
        */
        return imagenRepository.salvarImagen(new Imagen(originalImage.getBufferedImage(), originalImage.getFormatString()));
    }

}

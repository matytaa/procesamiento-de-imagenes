package core.action.image;

import core.repository.RepositorioImagen;
import dominio.customimage.Imagen;

public class UndoChangesAction {

    private final RepositorioImagen repositorioImagen;

    public UndoChangesAction(RepositorioImagen repositorioImagen) {
        this.repositorioImagen = repositorioImagen;
    }

    public Imagen execute() {
        Imagen originalImage = repositorioImagen.getImagenOriginalBackup();
        /*Creo una imagen nueva para evitar problemas con las referencias
        Ya que la buffered image nunca la modificamos, no hay problema con utilizarla
        */
        return repositorioImagen.salvarImagen(new Imagen(originalImage.getBufferedImage(), originalImage.getFormatString()));
    }

}

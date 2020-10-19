package core.action.image;

import core.repository.RepositorioImagen;
import domain.customimage.Imagen;

public class PutModifiedImageAction {

    private final RepositorioImagen repositorioImagen;

    public PutModifiedImageAction(RepositorioImagen repositorioImagen) {
        this.repositorioImagen = repositorioImagen;
    }

    public void execute(Imagen customImage) {
        repositorioImagen.salvarImagenModificada(customImage);
    }
}

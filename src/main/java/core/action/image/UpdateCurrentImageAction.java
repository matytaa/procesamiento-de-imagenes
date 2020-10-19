package core.action.image;

import core.repository.RepositorioImagen;
import domain.customimage.Imagen;


public class UpdateCurrentImageAction {

    private final RepositorioImagen repositorioImagen;

    public UpdateCurrentImageAction(RepositorioImagen repositorioImagen) {
        this.repositorioImagen = repositorioImagen;
    }

    public void execute(Imagen customImage) {
        this.repositorioImagen.salvarImagen(customImage);
    }
}

package core.action.image;

import core.repository.RepositorioImagen;
import domain.customimage.Imagen;

import java.util.Optional;

public class GetModifiedImageAction {

    private final RepositorioImagen repositorioImagen;

    public GetModifiedImageAction(RepositorioImagen repositorioImagen) {
        this.repositorioImagen = repositorioImagen;
    }

    public Optional<Imagen> execute() {
        return repositorioImagen.getImagenModificada();
    }
}

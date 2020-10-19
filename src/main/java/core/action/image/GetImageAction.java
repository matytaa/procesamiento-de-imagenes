package core.action.image;

import core.repository.RepositorioImagen;
import domain.customimage.Imagen;

import java.util.Optional;

public class GetImageAction {

    private final RepositorioImagen repositorioImagen;

    public GetImageAction(RepositorioImagen repositorioImagen) {
        this.repositorioImagen = repositorioImagen;
    }

    public Optional<Imagen> execute() {
        return repositorioImagen.obtenerImagen();
    }
}

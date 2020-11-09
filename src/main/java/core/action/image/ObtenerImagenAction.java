package core.action.image;

import core.repository.RepositorioImagen;
import dominio.customimage.Imagen;

import java.util.Optional;

public class ObtenerImagenAction {

    private final RepositorioImagen repositorioImagen;

    public ObtenerImagenAction(RepositorioImagen repositorioImagen) {
        this.repositorioImagen = repositorioImagen;
    }

    public Optional<Imagen> ejecutar() {
        return repositorioImagen.obtenerImagen();
    }
}

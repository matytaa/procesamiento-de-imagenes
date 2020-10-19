package core.action.edgedetector;

import core.repository.RepositorioImagen;
import domain.customimage.Imagen;

import java.util.List;
import java.util.Optional;

public class GetImageSequenceAction {

    private final RepositorioImagen repositorioImagen;

    public GetImageSequenceAction(RepositorioImagen repositorioImagen) {
        this.repositorioImagen = repositorioImagen;
    }

    public Optional<List<Imagen>> execute() {
        return repositorioImagen.getImagenes();
    }
}

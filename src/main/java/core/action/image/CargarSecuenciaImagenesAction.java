package core.action.image;

import core.repository.RepositorioImagen;
import core.service.ServicioImagenCruda;
import core.service.ServicioAbrirArchivo;
import dominio.customimage.Imagen;
import ij.io.Opener;
import org.apache.commons.io.FilenameUtils;
import presentation.util.InsertValuePopup;

import java.util.ArrayList;
import java.util.List;

public class CargarSecuenciaImagenesAction {

    private final RepositorioImagen repositorioImagen;
    private final ServicioAbrirArchivo servicioAbrirArchivo;
    private final Opener opener;
    private final ServicioImagenCruda servicioImagenCruda;

    private List<Imagen> imagenes;

    public CargarSecuenciaImagenesAction(RepositorioImagen repositorioImagen, ServicioAbrirArchivo servicioAbrirArchivo, Opener opener,
                                         ServicioImagenCruda servicioImagenCruda) {
        this.repositorioImagen = repositorioImagen;
        this.servicioAbrirArchivo = servicioAbrirArchivo;
        this.opener = opener;
        this.servicioImagenCruda = servicioImagenCruda;
    }

    public List<Imagen> execute() {

        this.imagenes = new ArrayList<>();

        servicioAbrirArchivo.openMultiple().ifPresent(files -> {

            files.forEach(file -> {
                String path = file.toPath().toString();
                String extension = FilenameUtils.getExtension(path);

                if(extension.equalsIgnoreCase("raw")){
                    int ancho = Integer.parseInt(InsertValuePopup.show("Insert width", "256").get());
                    int alto = Integer.parseInt(InsertValuePopup.show("Insert height", "256").get());
                    imagenes.add(new Imagen(servicioImagenCruda.load(file, ancho, alto), extension));
                } else {
                    imagenes.add(new Imagen(opener.openImage(path).getBufferedImage(), extension));
                }
            });
        });

        return repositorioImagen.guardarSecuenciaImagenes(imagenes);
    }
}

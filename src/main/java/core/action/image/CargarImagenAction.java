package core.action.image;

import core.repository.RepositorioImagen;
import core.service.ServicioImagenCruda;
import core.service.ServicioAbrirArchivo;
import dominio.customimage.Imagen;
import ij.io.Opener;
import org.apache.commons.io.FilenameUtils;
import presentation.util.InsertValuePopup;

import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class CargarImagenAction {

    private final RepositorioImagen repositorioImagen;
    private final ServicioAbrirArchivo servicioAbrirArchivo;
    private final Opener opener;
    private final ServicioImagenCruda servicioImagenCruda;

    private String path = "";
    private Imagen imagen;

    public CargarImagenAction(RepositorioImagen repositorioImagen, ServicioAbrirArchivo servicioAbrirArchivo, Opener opener,
                              ServicioImagenCruda servicioImagenCruda) {
        this.repositorioImagen = repositorioImagen;
        this.servicioAbrirArchivo = servicioAbrirArchivo;
        this.opener = opener;
        this.servicioImagenCruda = servicioImagenCruda;
    }

    public Imagen ejecutar() {

        imagen = new Imagen(new BufferedImage(1, 1, TYPE_INT_ARGB), "png");

        servicioAbrirArchivo.open().ifPresent(file -> {
            path = file.toPath().toString();
            String extension = FilenameUtils.getExtension(path);
            if(extension.equalsIgnoreCase("raw")){
                int ancho = Integer.parseInt(InsertValuePopup.show("Ancho (px)", "256").get());
                int alto = Integer.parseInt(InsertValuePopup.show("Alto (px)", "256").get());
                imagen = ponerEnRepositorio(extension, servicioImagenCruda.load(file, ancho, alto));
            } else {
                imagen = ponerEnRepositorio(extension, opener.openImage(path).getBufferedImage());
            }
        });

        return imagen;
    }

    private Imagen ponerEnRepositorio(String extension, BufferedImage bufferedImage) {
        repositorioImagen.setImagenOriginalBackup(new Imagen(bufferedImage, extension));
        return repositorioImagen.salvarImagen(new Imagen(bufferedImage, extension));
    }
}

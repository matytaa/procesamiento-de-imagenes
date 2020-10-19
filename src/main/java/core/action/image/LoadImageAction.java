package core.action.image;

import core.repository.RepositorioImagen;
import core.service.ImageRawService;
import core.service.OpenFileService;
import domain.customimage.Imagen;
import ij.io.Opener;
import org.apache.commons.io.FilenameUtils;
import presentation.util.InsertValuePopup;

import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class LoadImageAction {

    private final RepositorioImagen repositorioImagen;
    private final OpenFileService openFileService;
    private final Opener opener;
    private final ImageRawService imageRawService;

    private String path = "";
    private Imagen image;

    public LoadImageAction(RepositorioImagen repositorioImagen, OpenFileService openFileService, Opener opener,
                           ImageRawService imageRawService) {
        this.repositorioImagen = repositorioImagen;
        this.openFileService = openFileService;
        this.opener = opener;
        this.imageRawService = imageRawService;
    }

    public Imagen execute() {

        image = new Imagen(new BufferedImage(1, 1, TYPE_INT_ARGB), "png");

        openFileService.open().ifPresent(file -> {
            path = file.toPath().toString();
            String extension = FilenameUtils.getExtension(path);
            if(extension.equalsIgnoreCase("raw")){
                int ancho = Integer.parseInt(InsertValuePopup.show("Ancho (px)", "256").get());
                int alto = Integer.parseInt(InsertValuePopup.show("Alto (px)", "256").get());
                image = putOnRepository(extension, imageRawService.load(file, ancho, alto));
            } else {
                image = putOnRepository(extension, opener.openImage(path).getBufferedImage());
            }
        });

        return image;
    }

    private Imagen putOnRepository(String extension, BufferedImage bufferedImage) {
        repositorioImagen.setImagenOriginalBackup(new Imagen(bufferedImage, extension));
        return repositorioImagen.salvarImagen(new Imagen(bufferedImage, extension));
    }
}

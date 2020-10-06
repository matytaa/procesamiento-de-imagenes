package core.action.image;

import core.repository.ImagenRepository;
import core.service.ImageRawService;
import core.service.OpenFileService;
import domain.customimage.Imagen;
import ij.io.Opener;
import org.apache.commons.io.FilenameUtils;
import presentation.util.InsertValuePopup;

import java.util.ArrayList;
import java.util.List;

public class LoadImageSequenceAction {

    private final ImagenRepository imagenRepository;
    private final OpenFileService openFileService;
    private final Opener opener;
    private final ImageRawService imageRawService;

    private List<Imagen> imagenes;

    public LoadImageSequenceAction(ImagenRepository imagenRepository, OpenFileService openFileService, Opener opener,
                                   ImageRawService imageRawService) {
        this.imagenRepository = imagenRepository;
        this.openFileService = openFileService;
        this.opener = opener;
        this.imageRawService = imageRawService;
    }

    public List<Imagen> execute() {

        this.imagenes = new ArrayList<>();

        openFileService.openMultiple().ifPresent(files -> {

            files.forEach(file -> {
                String path = file.toPath().toString();
                String extension = FilenameUtils.getExtension(path);

                if(extension.equalsIgnoreCase("raw")){
                    int ancho = Integer.parseInt(InsertValuePopup.show("Insert width", "256").get());
                    int alto = Integer.parseInt(InsertValuePopup.show("Insert height", "256").get());
                    imagenes.add(new Imagen(imageRawService.load(file, ancho, alto), extension));
                } else {
                    imagenes.add(new Imagen(opener.openImage(path).getBufferedImage(), extension));
                }
            });
        });

        return imagenRepository.guardarSecuenciaImagenes(imagenes);
    }
}

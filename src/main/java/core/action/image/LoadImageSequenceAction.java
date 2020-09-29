package core.action.image;

import core.repository.ImageRepository;
import core.service.ImageRawService;
import core.service.OpenFileService;
import domain.customimage.Imagen;
import ij.io.Opener;
import org.apache.commons.io.FilenameUtils;
import presentation.util.InsertValuePopup;

import java.util.ArrayList;
import java.util.List;

public class LoadImageSequenceAction {

    private final ImageRepository imageRepository;
    private final OpenFileService openFileService;
    private final Opener opener;
    private final ImageRawService imageRawService;

    private List<Imagen> images;

    public LoadImageSequenceAction(ImageRepository imageRepository, OpenFileService openFileService, Opener opener,
            ImageRawService imageRawService) {
        this.imageRepository = imageRepository;
        this.openFileService = openFileService;
        this.opener = opener;
        this.imageRawService = imageRawService;
    }

    public List<Imagen> execute() {

        this.images = new ArrayList<>();

        openFileService.openMultiple().ifPresent(files -> {

            files.forEach(file -> {
                String path = file.toPath().toString();
                String extension = FilenameUtils.getExtension(path);

                if(extension.equalsIgnoreCase("raw")){
                    int width = Integer.parseInt(InsertValuePopup.show("Insert width", "256").get());
                    int height = Integer.parseInt(InsertValuePopup.show("Insert height", "256").get());
                    images.add(new Imagen(imageRawService.load(file, width, height), extension));
                } else {
                    images.add(new Imagen(opener.openImage(path).getBufferedImage(), extension));
                }
            });
        });

        return imageRepository.saveImageSequence(images);
    }
}

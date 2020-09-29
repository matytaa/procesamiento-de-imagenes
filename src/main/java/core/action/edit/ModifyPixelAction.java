package core.action.edit;

import core.repository.ImageRepository;
import core.service.ModifyImageService;
import domain.customimage.Imagen;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.Optional;

public class ModifyPixelAction {

    private final ImageRepository imageRepository;
    private final ModifyImageService modifyImageService;

    public ModifyPixelAction(ImageRepository imageRepository, ModifyImageService modifyImageService) {
        this.imageRepository = imageRepository;
        this.modifyImageService = modifyImageService;
    }

    public Image execute(Integer pixelX, Integer pixelY, String valueR, String valueG, String valueB) {

        Optional<Imagen> image = this.imageRepository.getImage();

        if (!image.isPresent()) {
            return new WritableImage(1, 1);
        }

        Imagen modifiedImage = modifyImageService.createModifiedImage(image.get(), pixelX, pixelY, Integer.parseInt(valueR), Integer.parseInt(valueG), Integer.parseInt(valueB));
        this.imageRepository.saveModifiedImage(modifiedImage);
        return SwingFXUtils.toFXImage(modifiedImage.getBufferedImage(), null);
    }
}

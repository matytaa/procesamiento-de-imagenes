package core.action.edit;

import core.repository.ImagenRepository;
import core.service.ModifyImageService;
import domain.customimage.Imagen;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.Optional;

public class ModifyPixelAction {

    private final ImagenRepository imagenRepository;
    private final ModifyImageService modifyImageService;

    public ModifyPixelAction(ImagenRepository imagenRepository, ModifyImageService modifyImageService) {
        this.imagenRepository = imagenRepository;
        this.modifyImageService = modifyImageService;
    }

    public Image execute(Integer pixelX, Integer pixelY, String valueR, String valueG, String valueB) {

        Optional<Imagen> image = this.imagenRepository.getImagen();

        if (!image.isPresent()) {
            return new WritableImage(1, 1);
        }

        Imagen modifiedImage = modifyImageService.createModifiedImage(image.get(), pixelX, pixelY, Integer.parseInt(valueR), Integer.parseInt(valueG), Integer.parseInt(valueB));
        this.imagenRepository.salvarImagenModificada(modifiedImage);
        return SwingFXUtils.toFXImage(modifiedImage.getBufferedImage(), null);
    }
}

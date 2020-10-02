
package core.action.edit.space_domain.operations;

import core.service.ImageOperationsService;
import javafx.scene.image.Image;

public class MultiplyImagesAction {

    private final ImageOperationsService imageOperationsService;

    public MultiplyImagesAction(ImageOperationsService imageOperationsService) {
        this.imageOperationsService = imageOperationsService;
    }

    public Image execute(Image image1, Image image2) {
        int[][] redChannelResultantValues = this.imageOperationsService.multiplicarValoresPixelesRojos(image1, image2);
        int[][] greenChannelResultantValues = this.imageOperationsService.multiplicarValoresPixelesVerdes(image1, image2);
        int[][] blueChannelResultantValues = this.imageOperationsService.multiplicarValoresPixelesAzules(image1, image2);
        return this.imageOperationsService.escribirNuevosValoresDePixelesEnLaImagen(redChannelResultantValues,
                greenChannelResultantValues, blueChannelResultantValues);
    }
}


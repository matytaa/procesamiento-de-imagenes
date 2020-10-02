package core.action.edit.space_domain.operations;

import core.service.ImageOperationsService;
import javafx.scene.image.Image;

public class SubstractImagesAction {
    private ImageOperationsService imageOperationsService;

    public SubstractImagesAction(ImageOperationsService imageOperationsService){
        this.imageOperationsService = imageOperationsService;
    }

    public Image execute(Image image1, Image image2) {
        int[][] redChannelResultantValues = this.imageOperationsService.restarValoresPixelesRojos(image1, image2);
        int[][] greenChannelResultantValues = this.imageOperationsService.restarValoresPixelesVerdes(image1, image2);
        int[][] blueChannelResultantValues = this.imageOperationsService.restarValoresPixelesAzules(image1, image2);
        return this.imageOperationsService.escribirNuevosValoresDePixelesEnLaImagen(redChannelResultantValues,
                greenChannelResultantValues, blueChannelResultantValues);
    }
}

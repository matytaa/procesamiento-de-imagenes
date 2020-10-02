package core.action.edit.space_domain.operations;

import core.service.ImageOperationsService;
import javafx.scene.image.Image;

public class SumImagesAction {

    private ImageOperationsService imageOperationsService;

    public SumImagesAction(ImageOperationsService imageOperationsService){
        this.imageOperationsService = imageOperationsService;
    }

    public Image execute(Image image1, Image image2) {
        int[][] redChannelResultantValues = this.imageOperationsService.sumaValoresPixelesRojos(image1, image2);
        int[][] greenChannelResultantValues = this.imageOperationsService.sumaValoresPixelesVerde(image1, image2);
        int[][] blueChannelResultantValues = this.imageOperationsService.sumaValoresPixelesAzul(image1, image2);
        return this.imageOperationsService.escribirNuevosValoresDePixelesEnLaImagen(redChannelResultantValues,
                greenChannelResultantValues, blueChannelResultantValues);
    }

}

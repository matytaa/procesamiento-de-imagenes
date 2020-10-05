package core.action.edit.space_domain.operations;

import core.service.OperacionesImagenesService;
import javafx.scene.image.Image;

public class SumImagesAction {

    private OperacionesImagenesService operacionesImagenesService;

    public SumImagesAction(OperacionesImagenesService operacionesImagenesService){
        this.operacionesImagenesService = operacionesImagenesService;
    }

    public Image execute(Image image1, Image image2) {
        int[][] redChannelResultantValues = this.operacionesImagenesService.sumaValoresPixelesRojos(image1, image2);
        int[][] greenChannelResultantValues = this.operacionesImagenesService.sumaValoresPixelesVerde(image1, image2);
        int[][] blueChannelResultantValues = this.operacionesImagenesService.sumaValoresPixelesAzul(image1, image2);
        return this.operacionesImagenesService.escribirNuevosValoresDePixelesEnLaImagen(redChannelResultantValues,
                greenChannelResultantValues, blueChannelResultantValues);
    }

}

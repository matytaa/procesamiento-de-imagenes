package core.action.edit.space_domain.operations;

import core.service.OperacionesImagenesService;
import javafx.scene.image.Image;

public class SubstractImagesAction {
    private OperacionesImagenesService operacionesImagenesService;

    public SubstractImagesAction(OperacionesImagenesService operacionesImagenesService){
        this.operacionesImagenesService = operacionesImagenesService;
    }

    public Image execute(Image image1, Image image2) {
        int[][] redChannelResultantValues = this.operacionesImagenesService.restarValoresPixelesRojos(image1, image2);
        int[][] greenChannelResultantValues = this.operacionesImagenesService.restarValoresPixelesVerdes(image1, image2);
        int[][] blueChannelResultantValues = this.operacionesImagenesService.restarValoresPixelesAzules(image1, image2);
        return this.operacionesImagenesService.escribirNuevosValoresDePixelesEnLaImagen(redChannelResultantValues,
                greenChannelResultantValues, blueChannelResultantValues);
    }
}

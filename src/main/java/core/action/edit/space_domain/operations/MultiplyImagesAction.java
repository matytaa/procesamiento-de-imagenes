
package core.action.edit.space_domain.operations;

import core.service.OperacionesImagenesService;
import javafx.scene.image.Image;

public class MultiplyImagesAction {

    private final OperacionesImagenesService operacionesImagenesService;

    public MultiplyImagesAction(OperacionesImagenesService operacionesImagenesService) {
        this.operacionesImagenesService = operacionesImagenesService;
    }

    public Image execute(Image image1, Image image2) {
        int[][] redChannelResultantValues = this.operacionesImagenesService.multiplicarValoresPixelesRojos(image1, image2);
        int[][] greenChannelResultantValues = this.operacionesImagenesService.multiplicarValoresPixelesVerdes(image1, image2);
        int[][] blueChannelResultantValues = this.operacionesImagenesService.multiplicarValoresPixelesAzules(image1, image2);
        return this.operacionesImagenesService.escribirNuevosValoresDePixelesEnLaImagen(redChannelResultantValues,
                greenChannelResultantValues, blueChannelResultantValues);
    }
}


package core.action.edit.space_domain;

import core.service.OperacionesImagenesService;
import domain.customimage.Imagen;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class NormalizeImageAction {

    OperacionesImagenesService operacionesImagenesService;

    public NormalizeImageAction(OperacionesImagenesService operacionesImagenesService){
        this.operacionesImagenesService = operacionesImagenesService;
    }

    //la primer imagen es la que se normaliza
    public Image execute(Imagen image1, Imagen image2){
        int resultantImageWidth = this.operacionesImagenesService.calcularAnchoResultante(image1, image2);
        int resultantImageHeight = this.operacionesImagenesService.calcularAltoResultante(image1, image2);
        WritableImage imageToNormalize = new WritableImage(resultantImageWidth, resultantImageHeight);
        imageToNormalize = this.operacionesImagenesService.fillImage(imageToNormalize, image1);
        return imageToNormalize;
    }
}

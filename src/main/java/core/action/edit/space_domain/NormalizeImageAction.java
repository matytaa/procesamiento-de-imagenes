package core.action.edit.space_domain;

import core.service.ImageOperationsService;
import domain.customimage.Imagen;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class NormalizeImageAction {

    ImageOperationsService imageOperationsService;

    public NormalizeImageAction(ImageOperationsService imageOperationsService){
        this.imageOperationsService = imageOperationsService;
    }

    //la primer imagen es la que se normaliza
    public Image execute(Imagen image1, Imagen image2){
        int resultantImageWidth = this.imageOperationsService.calcularAnchoResultante(image1, image2);
        int resultantImageHeight = this.imageOperationsService.calcularAltoResultante(image1, image2);
        WritableImage imageToNormalize = new WritableImage(resultantImageWidth, resultantImageHeight);
        imageToNormalize = this.imageOperationsService.fillImage(imageToNormalize, image1);
        return imageToNormalize;
    }
}

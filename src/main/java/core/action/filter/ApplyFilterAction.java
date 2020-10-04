package core.action.filter;

import core.service.ImageOperationsService;
import domain.customimage.MatrizCanales;
import domain.customimage.Imagen;
import domain.mask.Mascara;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;

public class ApplyFilterAction {

    private final PublishSubject<Image> onModifiedImagePublishSubject;
    private final ImageOperationsService imageOperationsService;

    public ApplyFilterAction(PublishSubject<Image> imagePublishSubject,
                             ImageOperationsService imageOperationsService) {

        this.onModifiedImagePublishSubject = imagePublishSubject;
        this.imageOperationsService = imageOperationsService;
    }

    public Imagen execute(Imagen customImage, Mascara mascara) {
        MatrizCanales mascaraAplicada = mascara.apply(customImage);
        MatrizCanales validImageMatrix = this.imageOperationsService.aMatrizValida(mascaraAplicada);
        Imagen image = new Imagen(validImageMatrix, customImage.getFormatString());

        onModifiedImagePublishSubject.onNext(image.toFXImage());

        return image;
    }
}

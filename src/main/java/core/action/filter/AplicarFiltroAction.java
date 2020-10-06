package core.action.filter;

import core.service.OperacionesImagenesService;
import domain.customimage.MatrizCanales;
import domain.customimage.Imagen;
import domain.mask.Mascara;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;

public class AplicarFiltroAction {

    private final PublishSubject<Image> onModifiedImagePublishSubject;
    private final OperacionesImagenesService operacionesImagenesService;

    public AplicarFiltroAction(PublishSubject<Image> imagePublishSubject,
                               OperacionesImagenesService operacionesImagenesService) {

        this.onModifiedImagePublishSubject = imagePublishSubject;
        this.operacionesImagenesService = operacionesImagenesService;
    }

    public Imagen execute(Imagen customImage, Mascara mascara) {
        MatrizCanales mascaraAplicada = mascara.apply(customImage);
        MatrizCanales validImageMatrix = this.operacionesImagenesService.aMatrizValida(mascaraAplicada);
        Imagen image = new Imagen(validImageMatrix, customImage.getFormatString());

        onModifiedImagePublishSubject.onNext(image.toFXImage());

        return image;
    }
}

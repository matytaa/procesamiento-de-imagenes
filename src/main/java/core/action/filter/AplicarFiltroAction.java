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

    public Imagen execute(Imagen imagen, Mascara mascara) {
        MatrizCanales mascaraAplicada = mascara.apply(imagen);
        MatrizCanales matrizImagenValida = this.operacionesImagenesService.aMatrizValida(mascaraAplicada);
        Imagen imagenFinal = new Imagen(matrizImagenValida, imagen.getFormatString());

        onModifiedImagePublishSubject.onNext(imagenFinal.toFXImage());

        return imagenFinal;
    }
}

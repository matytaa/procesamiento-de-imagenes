package core.action.filter;

import core.service.OperacionesImagenesService;
import dominio.customimage.MatrizCanales;
import dominio.customimage.Imagen;
import dominio.mask.Mascara;
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

    public Imagen aplicar(Imagen imagen, Mascara mascara) {
        MatrizCanales mascaraAplicada = mascara.aplicar(imagen);
        MatrizCanales matrizImagenValida = this.operacionesImagenesService.aMatrizValida(mascaraAplicada);
        Imagen imagenFinal = new Imagen(matrizImagenValida, imagen.getFormatString());

        onModifiedImagePublishSubject.onNext(imagenFinal.toFXImage());

        return imagenFinal;
    }
}

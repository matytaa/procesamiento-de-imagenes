package presentation.presenter;

import core.action.edgedetector.AplicarDetectorDeBordesAction;
import core.action.image.ObtenerImagenAction;
import dominio.SemaforoFiltro;
import dominio.customimage.Imagen;
import dominio.mask.Mascara;
import dominio.mask.prewitt.MascaraDerivativaPrewittX;
import dominio.mask.prewitt.MascaraDerivativaPrewittY;
import dominio.mask.sobel.MascaraSobelX;
import dominio.mask.sobel.MascaraSobelY;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;

public class DetectorBordesPresenter {

    private final ObtenerImagenAction obtenerImagenAction;
    private final AplicarDetectorDeBordesAction aplicarDetectorDeBordesAction;
    private final PublishSubject<Image> imagePublishSubject;

    public DetectorBordesPresenter(ObtenerImagenAction obtenerImagenAction,
                                   AplicarDetectorDeBordesAction aplicarDetectorDeBordesAction,
                                   PublishSubject<Image> imagePublishSubject) {

        this.obtenerImagenAction = obtenerImagenAction;
        this.aplicarDetectorDeBordesAction = aplicarDetectorDeBordesAction;
        this.imagePublishSubject = imagePublishSubject;
    }

    public void onInitialize() {
        this.obtenerImagenAction.ejecutar()
                .ifPresent(imagen -> {
                    if (SemaforoFiltro.is(Mascara.Tipo.PREWITT)) {
                        this.aplicarDetectorBordesPrewitt(imagen);
                    }

                    if (SemaforoFiltro.is(Mascara.Tipo.SOBEL)) {
                        this.aplicarDetectorBordesSobel(imagen);
                    }

                    if (SemaforoFiltro.is(Mascara.Tipo.SOBEL_VERTICAL)) {
                        this.aplicarDetectorBordesSobelVertical(imagen);
                    }

                    if (SemaforoFiltro.is(Mascara.Tipo.SOBEL_HORIZONTAL)) {
                        this.aplicarDetectorBordesSobelHorizontal(imagen);
                    }
                });
    }

    private void aplicarDetectorBordesPrewitt(Imagen customImage) {
        Mascara mascaraDerivativaPrewittX = new MascaraDerivativaPrewittX();
        Mascara mascaraDerivativaPrewittY = new MascaraDerivativaPrewittY();
        Image edgedImage = aplicarDetectorDeBordesAction.ejecutar(customImage, mascaraDerivativaPrewittX, mascaraDerivativaPrewittY);
        imagePublishSubject.onNext(edgedImage);
    }

    private void aplicarDetectorBordesSobelVertical(Imagen imagen) {
        Mascara mascaraSobelY = new MascaraSobelY();
        Image imagenSalida = aplicarDetectorDeBordesAction.ejecutar(imagen, mascaraSobelY);
        imagePublishSubject.onNext(imagenSalida);
    }

    private void aplicarDetectorBordesSobelHorizontal(Imagen imagen) {
        Mascara mascaraSobelX = new MascaraSobelX();
        Image imagenSalida = aplicarDetectorDeBordesAction.ejecutar(imagen, mascaraSobelX);
        imagePublishSubject.onNext(imagenSalida);
    }

    private void aplicarDetectorBordesSobel(Imagen imagen) {
        Mascara mascaraSobelX = new MascaraSobelX();
        Mascara mascaraSobel = new MascaraSobelY();
        Image imagenSalida = aplicarDetectorDeBordesAction.ejecutar(imagen, mascaraSobelX, mascaraSobel);
        imagePublishSubject.onNext(imagenSalida);
    }
}

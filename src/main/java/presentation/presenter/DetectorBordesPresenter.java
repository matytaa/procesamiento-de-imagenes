package presentation.presenter;

import core.action.edgedetector.AplicarDetectorDeBordesAction;
import core.action.image.GetImageAction;
import domain.SemaforoFiltro;
import domain.customimage.Imagen;
import domain.mask.Mascara;
import domain.mask.prewitt.PrewittXDerivativeMascara;
import domain.mask.prewitt.PrewittYDerivativeMascara;
import domain.mask.sobel.MascaraSobelX;
import domain.mask.sobel.MascaraSobelY;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;

public class DetectorBordesPresenter {

    private final GetImageAction getImageAction;
    private final AplicarDetectorDeBordesAction aplicarDetectorDeBordesAction;
    private final PublishSubject<Image> imagePublishSubject;

    public DetectorBordesPresenter(GetImageAction getImageAction,
                                   AplicarDetectorDeBordesAction aplicarDetectorDeBordesAction,
                                   PublishSubject<Image> imagePublishSubject) {

        this.getImageAction = getImageAction;
        this.aplicarDetectorDeBordesAction = aplicarDetectorDeBordesAction;
        this.imagePublishSubject = imagePublishSubject;
    }

    public void onInitialize() {
        this.getImageAction.execute()
                .ifPresent(imagen -> {
                    if (SemaforoFiltro.is(Mascara.Tipo.PREWITT)) {
                        this.applyPrewittEdgeDetector(imagen);
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

    private void applyPrewittEdgeDetector(Imagen customImage) {
        Mascara prewittXDerivativeMascara = new PrewittXDerivativeMascara();
        Mascara prewittYDerivativeMascara = new PrewittYDerivativeMascara();
        Image edgedImage = aplicarDetectorDeBordesAction.ejecutar(customImage, prewittXDerivativeMascara, prewittYDerivativeMascara);
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

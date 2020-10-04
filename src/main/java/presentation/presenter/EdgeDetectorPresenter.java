package presentation.presenter;

import core.action.edgedetector.ApplyEdgeDetectorByGradientAction;
import core.action.image.GetImageAction;
import domain.SemaforoFiltro;
import domain.customimage.Imagen;
import domain.mask.Mascara;
import domain.mask.prewitt.PrewittXDerivativeMascara;
import domain.mask.prewitt.PrewittYDerivativeMascara;
import domain.mask.sobel.SobelXDerivativeMascara;
import domain.mask.sobel.SobelYDerivativeMascara;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;

public class EdgeDetectorPresenter {

    private final GetImageAction getImageAction;
    private final ApplyEdgeDetectorByGradientAction applyEdgeDetectorByGradientAction;
    private final PublishSubject<Image> imagePublishSubject;

    public EdgeDetectorPresenter(GetImageAction getImageAction,
                                 ApplyEdgeDetectorByGradientAction applyEdgeDetectorByGradientAction,
                                 PublishSubject<Image> imagePublishSubject) {

        this.getImageAction = getImageAction;
        this.applyEdgeDetectorByGradientAction = applyEdgeDetectorByGradientAction;
        this.imagePublishSubject = imagePublishSubject;
    }

    public void onInitialize() {
        this.getImageAction.execute()
                .ifPresent(customImage -> {
                    if (SemaforoFiltro.is(Mascara.Tipo.PREWITT)) {
                        this.applyPrewittEdgeDetector(customImage);
                    }

                    if (SemaforoFiltro.is(Mascara.Tipo.SOBEL)) {
                        this.applySobelEdgeDetector(customImage);
                    }
                });
    }

    private void applyPrewittEdgeDetector(Imagen customImage) {
        Mascara prewittXDerivativeMascara = new PrewittXDerivativeMascara();
        Mascara prewittYDerivativeMascara = new PrewittYDerivativeMascara();
        Image edgedImage = applyEdgeDetectorByGradientAction.execute(customImage, prewittXDerivativeMascara, prewittYDerivativeMascara);
        imagePublishSubject.onNext(edgedImage);
    }

    private void applySobelEdgeDetector(Imagen customImage) {
        Mascara sobelXDerivativeMascara = new SobelXDerivativeMascara();
        Mascara sobelYDerivativeMascara = new SobelYDerivativeMascara();
        Image edgedImage = applyEdgeDetectorByGradientAction.execute(customImage, sobelXDerivativeMascara, sobelYDerivativeMascara);
        imagePublishSubject.onNext(edgedImage);
    }
}

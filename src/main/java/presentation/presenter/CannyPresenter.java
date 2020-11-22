package presentation.presenter;

import core.action.edgedetector.AplicarCannyAction;
import core.action.filter.AplicarFiltroAction;
import core.action.image.ObtenerImagenAction;
import dominio.customimage.Imagen;
import dominio.mask.filter.MascaraGaussiana;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;
import presentation.controller.CannySceneController;

public class CannyPresenter {

    private final CannySceneController view;
    private final ObtenerImagenAction obtenerImagenAction;
    private final AplicarFiltroAction aplicarFiltroAction;
    private final PublishSubject<Image> imagePublishSubject;
    private final AplicarCannyAction aplicarCannyAction;
    private final PublishSubject<Image> cannyPublishSubject;

    public CannyPresenter(CannySceneController view, ObtenerImagenAction obtenerImagenAction, AplicarFiltroAction aplicarFiltroAction,
                          PublishSubject<Image> imagePublishSubject, AplicarCannyAction aplicarCannyAction, PublishSubject<Image> cannyPublishSubject) {
        this.view = view;
        this.obtenerImagenAction = obtenerImagenAction;
        this.aplicarFiltroAction = aplicarFiltroAction;
        this.imagePublishSubject = imagePublishSubject;
        this.aplicarCannyAction = aplicarCannyAction;
        this.cannyPublishSubject = cannyPublishSubject;
    }

    public void aplicar() {

        int sigma = Integer.parseInt(view.sigmaTextField.getText());
        int t1 = Integer.parseInt(view.t1TextField.getText());
        int t2 = Integer.parseInt(view.t2TextField.getText());

        if (isSigmaValid(sigma)) {

            if (areThresholdsValid(t1, t2)) {

                this.obtenerImagenAction.ejecutar().ifPresent(imagen -> {

                            //PRIMERO APLICO FILTRO GAUSSEANO
                            Imagen imagenFiltrada = this.aplicarFiltroAction.aplicar(imagen, new MascaraGaussiana(sigma));
                            //LUEGO CANNY
                            Image imagenConCanny = this.aplicarCannyAction.ejecutar(imagenFiltrada, t1, t2).toFXImage();
                            //NO invertir el orden, o rompe Hough (deberiamos buscar una manera de fixear esto)
                            this.imagePublishSubject.onNext(imagenConCanny);
                            this.cannyPublishSubject.onNext(imagenConCanny);

                            this.view.cerrar();
                        }
                );
            }
        }
    }

    private boolean areThresholdsValid(int t1, int t2) {
        if (t1 < 0 || t2 < 0) {
            return false;
        }
        if (t1 > t2) {
            return false;
        }
        return true;
    }

    private boolean isSigmaValid(double sigma) {
        return sigma > 0;
    }

}

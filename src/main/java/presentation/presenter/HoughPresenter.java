package presentation.presenter;

import core.action.edgedetector.AplicarDetectorDeBordesAction;
import core.action.edgedetector.hough.TransformadaHoughCircularAction;
import core.action.edgedetector.hough.TransformadaHoughLinearAction;
import core.action.image.ObtenerImagenAction;
import core.action.threshold.AplicarEstimacionDelUmbralDeOtsuAction;
import dominio.automaticthreshold.EstimacionDelUmbralDeOtsuResultante;
import dominio.customimage.Imagen;
import dominio.mask.prewitt.MascaraDerivativaPrewittX;
import dominio.mask.prewitt.MascaraDerivativaPrewittY;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;
import presentation.controller.HoughSceneController;


public class HoughPresenter {

    private final HoughSceneController vista;
    //Este tiene que ser el mismo publish subject de la modified image para que pueda recibir el resultado de Canny
    private final PublishSubject<Image> imagePublishSubject;
    private final TransformadaHoughLinearAction transformadaHoughLinearAction;
    private final TransformadaHoughCircularAction transformadaHoughCircularAction;
    private final AplicarEstimacionDelUmbralDeOtsuAction aplicarEstimacionDelUmbralDeOtsuAction;
    private final ObtenerImagenAction obtenerImagenAction;
    private final AplicarDetectorDeBordesAction aplicarDetectorDeBordesAction;

    public HoughPresenter(HoughSceneController houghSceneController,
                          PublishSubject<Image> onModifiedImagePublishSubject,
                          TransformadaHoughLinearAction transformadaHoughLinearAction,
                          TransformadaHoughCircularAction transformadaHoughCircularAction,
                          AplicarEstimacionDelUmbralDeOtsuAction aplicarEstimacionDelUmbralDeOtsuAction,
                          ObtenerImagenAction obtenerImagenAction,
                          AplicarDetectorDeBordesAction aplicarDetectorDeBordesAction) {
        this.vista = houghSceneController;
        this.imagePublishSubject = onModifiedImagePublishSubject;
        this.transformadaHoughLinearAction = transformadaHoughLinearAction;
        this.transformadaHoughCircularAction = transformadaHoughCircularAction;
        this.obtenerImagenAction = obtenerImagenAction;
        this.aplicarEstimacionDelUmbralDeOtsuAction = aplicarEstimacionDelUmbralDeOtsuAction;
        this.aplicarDetectorDeBordesAction = aplicarDetectorDeBordesAction;
    }

    public void aplicar() {

        if (vista.lineaRadioButton.isSelected()) this.transformadaLinear();
        else if (vista.circuloRadioButton.isSelected()) this.transformadaCircular();

    }

    private void transformadaCircular() {

        int xCentro = Integer.parseInt(vista.circuloXTextField.getText());
        int yCentro = Integer.parseInt(vista.circuloYTextField.getText());
        int radio = Integer.parseInt(vista.circuloRadioTextField.getText());
        double tolerancia = Double.parseDouble(vista.toleranciaTextField.getText());

        if (esXCentroValido(xCentro) && esYCentroValido(yCentro) && esRadioValido(radio) && esToleranciaValida(tolerancia)) {

            this.obtenerImagenAction.ejecutar().ifPresent(imagen -> {
                //DETECTO BORDES CON PREWITT
                Imagen imagenConBordesDetectados = new Imagen(this.aplicarDetectorDeBordesAction.ejecutar(imagen, new MascaraDerivativaPrewittX(), new MascaraDerivativaPrewittY()), "png");
                //CALCULO UMBRAL CON OTSU
                EstimacionDelUmbralDeOtsuResultante estimacionDelUmbralDeOtsuResultante = this.aplicarEstimacionDelUmbralDeOtsuAction.ejecutar(imagenConBordesDetectados);
                Imagen imagenConLimites = new Imagen(estimacionDelUmbralDeOtsuResultante.getImagen(), "png");
                //CALCULO HOUGH
                Imagen imagenConHough = this.transformadaHoughCircularAction.ejecutar(imagen, imagenConLimites, xCentro, yCentro, radio, tolerancia);
                imagePublishSubject.onNext(imagenConHough.toFXImage());
                this.vista.closeWindow();
                    }
            );

        }

    }

    private boolean esRadioValido(int radio) {
        return radio > 0;
    }

    private boolean esYCentroValido(int yCentro) {
        return yCentro > 0;
    }

    private boolean esXCentroValido(int xCentro) {
        return xCentro > 0;
    }

    private void transformadaLinear() {

        int rho = Integer.parseInt(vista.rhoTextField.getText());
        int theta = Integer.parseInt(vista.thetaTextField.getText());
        double tolerancia = Double.parseDouble(vista.toleranciaTextField.getText());

        if (esRhoValida(rho) && esThetaValida(theta) && esToleranciaValida(tolerancia)) {

            this.obtenerImagenAction.ejecutar().ifPresent(imagen -> {
                //DETECTO BORDES CON PREWITT
                Imagen imagenConBordesDetectados = new Imagen(this.aplicarDetectorDeBordesAction.ejecutar(imagen, new MascaraDerivativaPrewittX(), new MascaraDerivativaPrewittY()), "png");
                //CALCULO UMBRAL CON OTSU
                EstimacionDelUmbralDeOtsuResultante estimacionDelUmbralDeOtsuResultante = this.aplicarEstimacionDelUmbralDeOtsuAction.ejecutar(imagenConBordesDetectados);
                Imagen imagenConLimites = new Imagen(estimacionDelUmbralDeOtsuResultante.getImagen(), "png");
                //CALCULO HOUGH
                Imagen imagenConHough = this.transformadaHoughLinearAction.ejecutar(imagen, imagenConLimites, rho, theta, tolerancia);
                imagePublishSubject.onNext(imagenConHough.toFXImage());
                this.vista.closeWindow();
                    }
            );

        }
    }

    private boolean esThetaValida(int theta) {
        return theta > 0;
    }

    private boolean esRhoValida(int rho) {
        return rho > 0;
    }

    private boolean esToleranciaValida(double tolerancia) {
        return tolerancia > 0;
    }
}

package presentation.controller;

import core.provider.PresenterProvider;
import core.provider.ViewProvider;
import dominio.flags.LaplacianDetector;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import presentation.presenter.MainPresenter;
import presentation.view.CustomImageView;

public class MainSceneController {

    @FXML
    public Group groupImageView;
    @FXML
    public ImageView imageView;
    @FXML
    public ImageView modifiedImageView;
    @FXML
    public Button aceptarBoton;
    @FXML
    public Button resetModifiedImageButton;
    @FXML
    public TextField pixelX;
    @FXML
    public TextField pixelY;
    @FXML
    public TextField valueR;
    @FXML
    public TextField valueG;
    @FXML
    public TextField valueB;
    @FXML
    public Button undoChangesButton;

    public CustomImageView customImageView;

    private final MainPresenter mainPresenter;

    public MainSceneController() {

        this.mainPresenter = PresenterProvider.provideImageSelectionPresenter(this);
        ViewProvider.setMainView(this);
    }

    @FXML
    //JavaFX invoke this method after constructor
    public void initialize() {
        this.mainPresenter.initialize();
    }

    @FXML
    public void abrirImagen() {
        this.mainPresenter.onAbrirImagen();
    }

    @FXML
    public void saveModifiedImage() {
        this.mainPresenter.onSaveImage();
    }

    @FXML
    public void aplicarCambios() {
        this.mainPresenter.onAplicarCambios();
    }

    @FXML
    public void calculateImagesOperations() {
        this.mainPresenter.onCalculateImagesOperations();
    }

    @FXML
    public void calcularNegativo() {
        this.mainPresenter.calcularNegativo();
    }

    @FXML
    public void umbralizar() {
        this.mainPresenter.onUmbralizar();
    }

    @FXML
    public void FuncionDePotenciaGamma() {
        this.mainPresenter.onFuncionDePotenciaGamma();
    }

    @FXML
    public void generarNumeroRandomExponencial() {
        this.mainPresenter.onGenerarNumeroRandomExponencial();
    }

    @FXML
    public void generarNumeroRandomRayleigh() {
        this.mainPresenter.onGenerarNumeroRandomRayleigh();
    }

    @FXML
    public void generarNumeroRandomGaussiano() {
        this.mainPresenter.onGenerarNumeroRandomGaussiano();
    }

    @FXML
    public void cerrar() {
        Platform.exit();
    }

    @FXML
    public void crearHistograma() {
        this.mainPresenter.onCrearHistograma();
    }

    @FXML
    public void aplicarRuidoSalYPimiena() {
        this.mainPresenter.onAplicarRuidoSalYPimienta();
    }

    @FXML
    public void generarImagenSinteticaRuidoExponencial() {
        this.mainPresenter.onGenerarImagenSinteticaRuidoExponencial();
    }

    @FXML
    public void generarImagenSinteticaRuidoRayleigh() {
        this.mainPresenter.onGenerarImagenSinteticaRuidoRayleigh();
    }

    @FXML
    public void generarImagenSinteticaRuidoGaussiano() {
        this.mainPresenter.onGenerarImagenSinteticaRuidoGaussiano();
    }

    @FXML
    public void createEqualizedImageByHistogram() {
        this.mainPresenter.onCreateEqualizedImageByHistogram();
    }

    @FXML
    public void createImageEqualizedTwiceByHistogram() {
        this.mainPresenter.onCreateEqualizedImageTwiceByHistogram();
    }

    @FXML
    public void aplicarRuidoAditivoGaussiano() {
        this.mainPresenter.onAplicarRuidoAditivoGaussiano();
    }

    @FXML
    public void aplicarRuidoMultiplicativoRayleigh() {
        this.mainPresenter.onAplicarRuidoMultiplicativoRayleigh();
    }

    @FXML
    public void aplicarRuidoMultiplicativoExponencial() {
        this.mainPresenter.onAplicarRuidoMultiplicativoExponencial();
    }

    @FXML
    public void onAplicarFiltroMedia() {
        this.mainPresenter.onAplicarFiltroMedia();
    }

    @FXML
    public void onAplicarFiltroMediana() {
        this.mainPresenter.onAplicarFiltroMediana();
    }

    @FXML
    public void onAplicarFiltroMedianaPonderada() {
        this.mainPresenter.onAplicarFiltroMedianaPonderada();
    }

    @FXML
    public void onAplicarFiltroGausseano() {
        this.mainPresenter.onAplicarFiltroGausseano();
    }

    @FXML
    public void OnAplicarDetectorBordesPrewitt() {
        this.mainPresenter.OnAplicarDetectorBordesPrewitt();
    }

    @FXML
    public void onAplicarDetectorBordesSobel() {
        this.mainPresenter.onAplicarDetectorBordesSobel();
    }

    @FXML
    public void onAplicarDetectorBordesSobelVertical() {
        this.mainPresenter.onAplicarDetectorBordesSobelVertical();
    }

    @FXML
    public void onAplicarDetectorBordesSobelHorizontal() {
        this.mainPresenter.onAplicarDetectorBordesSobelHorizontal();
    }

    @FXML
    public void onAplicarMascaraStadardDerivativaDireccional() {
        this.mainPresenter.onAplicarMascaraStadardDerivativaDireccional();
    }

    @FXML
    public void onAplicarMascaraDerivativaDireccionalDeKirsh() {
        this.mainPresenter.onAplicarMascaraDerivativaDireccionalDeKirsh();
    }

    @FXML
    public void onAplicarMascaraDerivativaDireccionalDePrewitt() {
        this.mainPresenter.onAplicarMascaraDerivativaDireccionalDePrewitt();
    }

    @FXML
    public void onAplicarMascaraDerivativaDireccionalDeSobel() {
        this.mainPresenter.onAplicarMascaraDerivativaDireccionalDeSobel();
    }

    @FXML
    public void onAplicarDifusion() {
        this.mainPresenter.onAplicarDifusion();
    }

    public void onApplyEdgeEnhancement() {
        this.mainPresenter.onApplyEdgeEnhancement();
    }

    @FXML
    public void showGreyGradient() {
        this.mainPresenter.onShowGreyGradient();
    }

    @FXML
    public void showColorGradient() {
        this.mainPresenter.onShowColorGradient();
    }

    @FXML
    public void showRGBImageRedChannel() {
        this.mainPresenter.onShowRGBImageRedChannel();
    }

    @FXML
    public void showRGBImageGreenChannel() {
        this.mainPresenter.onShowRGBImageGreenChannel();
    }

    @FXML
    public void showRGBImageBlueChannel() {
        this.mainPresenter.onShowRGBImageBlueChannel();
    }

    @FXML
    public void showImageWithQuadrate() {
        this.mainPresenter.onShowImageWithQuadrate();
    }

    @FXML
    public void showImageWithCircle() {
        this.mainPresenter.onShowImageWithCircle();
    }

    @FXML
    public void showHueHSVChannel() {
        this.mainPresenter.onShowHueHSVChannel();
    }

    @FXML
    public void showSaturationHSVChannel() {
        this.mainPresenter.onShowSaturationHSVChannel();
    }

    @FXML
    public void showValueHSVChannel() {
        this.mainPresenter.onShowValueHSVChannel();
    }

    @FXML
    public void calculatePixelValue() {
        this.mainPresenter.onCalculatePixelValue();
    }

    @FXML
    public void modifyPixelValue() {
        this.mainPresenter.onModifyPixelValue();
    }

    @FXML
    public void showReport() {
        this.mainPresenter.onShowReport();
    }

    @FXML
    public void cutPartialImage() {
        this.mainPresenter.onCutPartialImage();
    }

    @FXML
    public void contrast() {
        this.mainPresenter.onContrast();
    }

    @FXML
    public void compressDynamicRange() {
        this.mainPresenter.onCompressDynamicRange();
    }

    @FXML
    public void applyGlobalThresholdEstimation(){
        this.mainPresenter.onApplyGlobalThresholdEstimation();
    }

    @FXML
    public void applyOtsuThresholdEstimation(){
        this.mainPresenter.onApplyOtsuThresholdEstimation();
    }

    @FXML
    public void onApplyLaplacianEdgeDetector() {
        this.mainPresenter.onApplyLaplacianEdgeDetector(LaplacianDetector.STANDARD);
    }

    @FXML
    public void createEqualizedImage() {
        this.mainPresenter.onCreateEqualizedImageOnce();
    }

    @FXML
    public void createImageEqualizedTwice() {
        this.mainPresenter.onCreateEqualizedImageTwice();
    }

    @FXML
    public void onApplyLaplacianEdgeDetectorWithSlopeEvaluation() {
        this.mainPresenter.onApplyLaplacianEdgeDetector(LaplacianDetector.WITH_SLOPE_EVALUATION);
    }

    @FXML
    public void onApplyMarrHildrethEdgeDetector() {
        this.mainPresenter.onApplyMarrHildrethEdgeDetector();
    }

    @FXML
    public void resetModifiedImage() {
        this.mainPresenter.onResetModifiedImage();
    }

    @FXML
    public void onUndoChanges() {
        this.mainPresenter.onUndoChanges();
    }

    @FXML
    public void onApplyCannyEdgeDetector() {
        this.mainPresenter.onApplyCannyEdgeDetector();
    }

    @FXML
    public void applySusanEdgeDetector(){
        this.mainPresenter.onApplySusanEdgeDetector();
    }

    @FXML
    public void onHoughTransform() {
        this.mainPresenter.onHoughTransform();
    }

    @FXML
    public void onApplyActiveContour() {
        this.mainPresenter.onApplyActiveContour();
    }

    @FXML
    public void onApplyActiveContourOnImageSequence() {
        this.mainPresenter.onApplyActiveContourOnImageSequence();
    }

    @FXML
    public void onApplyHarris() { this.mainPresenter.onApplyHarris(); }

    @FXML
    public void onApplySift() { this.mainPresenter.onApplySift(); }

    @FXML
    public void abrirImagenMultipleProcesamiento() {
        this.mainPresenter.onAbrirImagenMultipleProcesamiento();
    }
}

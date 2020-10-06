package presentation.presenter;

import core.action.channels.ObtainHSVChannelAction;
import core.action.channels.ObtainRGBChannelAction;
import core.action.edgedetector.ApplyLaplacianDetectorAction;
import core.action.edgedetector.ApplySusanDetectorAction;
import core.action.edit.ModifyPixelAction;
import core.action.edit.space_domain.CalculateNegativeImageAction;
import core.action.edit.space_domain.CompressDynamicRangeAction;
import core.action.figure.CreateImageWithFigureAction;
import core.action.filter.ApplyFilterAction;
import core.action.gradient.CreateImageWithGradientAction;
import core.action.histogram.EqualizeGrayImageAction;
import core.action.histogram.utils.EqualizedTimes;
import core.action.image.*;
import core.action.threshold.ApplyGlobalThresholdEstimationAction;
import core.action.threshold.ApplyOtsuThresholdEstimationAction;
import core.action.threshold.AplicarUmbralAction;
import core.provider.PresenterProvider;
import core.semaphore.SemaforosGeneradoresDeRandoms;
import domain.SemaforoFiltro;
import domain.RandomElement;
import domain.activecontour.ActiveContourMode;
import domain.automaticthreshold.GlobalThresholdResult;
import domain.automaticthreshold.ImageLimitValues;
import domain.automaticthreshold.OtsuThresholdResult;
import domain.customimage.Imagen;
import domain.customimage.Format;
import domain.flags.LaplacianDetector;
import domain.generation.Channel;
import domain.generation.Figure;
import domain.generation.Gradient;
import domain.mask.LaplacianMascaraGaussiana;
import domain.mask.LaplacianMascara;
import domain.mask.Mascara;
import domain.mask.SusanMascara;
import domain.mask.filter.HighPassMascara;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import presentation.controller.MainSceneController;
import presentation.scenecreator.*;
import presentation.util.InsertValuePopup;
import presentation.util.ShowResultPopup;
import presentation.view.CustomImageView;

import java.util.List;

public class MainPresenter {

    private static final String EMPTY = "";
    private static final int DEFAULT_WIDTH = 510;
    private static final int DEFAULT_HEIGHT = 510;

    private final MainSceneController view;
    private final LoadImageAction loadImageAction;
    private final LoadImageSequenceAction loadImageSequenceAction;
    private final GetImageAction getImageAction;
    private final ModifyPixelAction modifyPixelAction;
    private final PutModifiedImageAction putModifiedImageAction;
    private final CalculateNegativeImageAction calculateNegativeImageAction;
    private final AplicarUmbralAction aplicarUmbralAction;
    private final CreateImageWithGradientAction createImageWithGradientAction;
    private final ObtainRGBChannelAction obtainRGBChannelAction;
    private final ObtainHSVChannelAction obtainHSVChannelAction;
    private final CreateImageWithFigureAction createImageWithFigureAction;
    private final EqualizeGrayImageAction equalizeGrayImageAction;
    private final Observable<Image> onModifiedImage;
    private final CompressDynamicRangeAction compressDynamicRangeAction;
    private final ApplyFilterAction applyFilterAction;
    private final UpdateCurrentImageAction updateCurrentImageAction;
    private final ApplyGlobalThresholdEstimationAction applyGlobalThresholdEstimationAction;
    private final ApplyOtsuThresholdEstimationAction applyOtsuThresholdEstimationAction;
    private final ApplyLaplacianDetectorAction applyLaplacianDetectorAction;
    private final UndoChangesAction undoChangesAction;
    private final GetImageLimitValuesAction getImageLimitValuesAction;
    private final ApplySusanDetectorAction applySusanDetectorAction;

    public MainPresenter(MainSceneController view,
                         LoadImageAction loadImageAction,
                         LoadImageSequenceAction loadImageSequenceAction, GetImageAction getImageAction,
                         PutModifiedImageAction putModifiedImageAction,
                         ModifyPixelAction modifyPixelAction,
                         CalculateNegativeImageAction calculateNegativeImageAction,
                         AplicarUmbralAction aplicarUmbralAction,
                         CreateImageWithGradientAction createImageWithGradientAction,
                         ObtainRGBChannelAction obtainRGBChannelAction,
                         ObtainHSVChannelAction obtainHSVChannelAction,
                         CreateImageWithFigureAction createImageWithFigureAction,
                         EqualizeGrayImageAction equalizeGrayImageAction,
                         Observable<Image> onModifiedImage,
                         CompressDynamicRangeAction compressDynamicRangeAction,
                         ApplyFilterAction applyFilterAction,
                         UpdateCurrentImageAction updateCurrentImageAction,
                         ApplyGlobalThresholdEstimationAction applyGlobalThresholdEstimationAction,
                         ApplyOtsuThresholdEstimationAction applyOtsuThresholdEstimationAction,
                         ApplyLaplacianDetectorAction applyLaplacianDetectorAction,
                         UndoChangesAction undoChangesAction,
                         GetImageLimitValuesAction getImageLimitValuesAction,
                         ApplySusanDetectorAction applySusanDetectorAction) {

        this.view = view;

        this.loadImageAction = loadImageAction;
        this.loadImageSequenceAction = loadImageSequenceAction;
        this.getImageAction = getImageAction;
        this.modifyPixelAction = modifyPixelAction;
        this.putModifiedImageAction = putModifiedImageAction;
        this.calculateNegativeImageAction = calculateNegativeImageAction;
        this.aplicarUmbralAction = aplicarUmbralAction;
        this.onModifiedImage = onModifiedImage;
        this.createImageWithGradientAction = createImageWithGradientAction;
        this.obtainRGBChannelAction = obtainRGBChannelAction;
        this.obtainHSVChannelAction = obtainHSVChannelAction;
        this.createImageWithFigureAction = createImageWithFigureAction;
        this.equalizeGrayImageAction = equalizeGrayImageAction;
        this.compressDynamicRangeAction = compressDynamicRangeAction;
        this.applyFilterAction = applyFilterAction;
        this.updateCurrentImageAction = updateCurrentImageAction;
        this.applyGlobalThresholdEstimationAction = applyGlobalThresholdEstimationAction;
        this.applyOtsuThresholdEstimationAction = applyOtsuThresholdEstimationAction;
        this.applyLaplacianDetectorAction = applyLaplacianDetectorAction;
        this.undoChangesAction = undoChangesAction;
        this.getImageLimitValuesAction = getImageLimitValuesAction;
        this.applySusanDetectorAction = applySusanDetectorAction;
    }

    public void initialize() {
        view.customImageView = new CustomImageView(view.groupImageView, view.imageView)
                .withSetPickOnBounds(true)
                .withOnPixelClick(this::onPixelClick)
                .withSelectionMode();

        awaitingForNewModifiedImages();
    }

    private Action onPixelClick(Integer x, Integer y) {
        return () -> {
            view.pixelX.setText(x.toString());
            view.pixelY.setText(y.toString());
            onCalculatePixelValue();
        };
    }

    private void awaitingForNewModifiedImages() {
        onModifiedImage.subscribe(image -> {
            putModifiedImageAction.execute(new Imagen(SwingFXUtils.fromFXImage(image, null), Format.PNG));
            view.modifiedImageView.setImage(image);
        });
    }

    public void onOpenImage() {
        setImageOnCustomImageView(this.loadImageAction.execute());
    }

    public void onOpenImageSequence() {
        List<Imagen> customImages = this.loadImageSequenceAction.execute();
        if(!customImages.isEmpty()) {
            setImageOnCustomImageView(customImages.get(0));
        }
    }

    private void setImageOnCustomImageView(Imagen customImage) {
        view.customImageView.setImage(SwingFXUtils.toFXImage(customImage.getBufferedImage(), null));
    }

    public void onSaveImage() {
        new SaveImageSceneCreator().createScene();
    }

    public void onApplyChanges() {
        Imagen modifiedCustomImage = new Imagen(view.modifiedImageView.getImage(), "png");
        view.customImageView.setImage(view.modifiedImageView.getImage());
        updateCurrentImageAction.execute(modifiedCustomImage);
        view.modifiedImageView.setImage(null);
        view.aceptarBoton.setVisible(false);
        view.undoChangesButton.setVisible(true);
    }

    public void onShowGreyGradient() {
        setImageOnCustomImageView(createImageWithGradientAction.execute(DEFAULT_WIDTH, DEFAULT_HEIGHT, Gradient.GREY));
    }

    public void onShowColorGradient() {
        setImageOnCustomImageView(createImageWithGradientAction.execute(DEFAULT_WIDTH, DEFAULT_HEIGHT, Gradient.COLOR));
    }

    public void onShowRGBImageRedChannel() {
        setImageOnModifiedImageView(obtainRGBChannelAction.execute(Channel.RED));
    }

    public void onShowRGBImageGreenChannel() {
        setImageOnModifiedImageView(obtainRGBChannelAction.execute(Channel.GREEN));
    }

    public void onShowRGBImageBlueChannel() {
        setImageOnModifiedImageView(obtainRGBChannelAction.execute(Channel.BLUE));
    }

    public void onShowImageWithQuadrate() {
        setImageOnCustomImageView(createImageWithFigureAction.execute(200, 200, Figure.QUADRATE));
    }

    public void onShowImageWithCircle() {
        setImageOnCustomImageView(createImageWithFigureAction.execute(200, 200, Figure.CIRCLE));
    }

    public void onShowHueHSVChannel() {
        setImageOnModifiedImageView(obtainHSVChannelAction.execute(Channel.HUE));
    }

    public void onShowSaturationHSVChannel() {
        setImageOnModifiedImageView(obtainHSVChannelAction.execute(Channel.SATURATION));
    }

    public void onShowValueHSVChannel() {
        setImageOnModifiedImageView(obtainHSVChannelAction.execute(Channel.VALUE));
    }

    private void updateModifiedImage(Imagen customImage) {
        this.putModifiedImageAction.execute(customImage);
        this.setImageOnModifiedImageView(customImage);
    }

    private void setImageOnModifiedImageView(Imagen customImage) {
        view.modifiedImageView.setImage(SwingFXUtils.toFXImage(customImage.getBufferedImage(), null));
        view.aceptarBoton.setVisible(true);
    }

    public void onCalculatePixelValue() {
        if (this.validatePixelCoordinates()) {

            int pixelX = Integer.parseInt(view.pixelX.getText());
            int pixelY = Integer.parseInt(view.pixelY.getText());

            this.getImageAction.execute()
                               .map(customImage -> customImage.getPixelValue(pixelX, pixelY))
                               .ifPresent(rgb -> {
                                   view.valueR.setText(String.valueOf(rgb.getRed()));
                                   view.valueG.setText(String.valueOf(rgb.getGreen()));
                                   view.valueB.setText(String.valueOf(rgb.getBlue()));
                               });

        } else {
            view.valueR.setText("Error");
        }
    }

    public void onModifyPixelValue() {
        if (this.validatePixelCoordinates()) {

            Integer pixelX = Integer.parseInt(view.pixelX.getText());
            Integer pixelY = Integer.parseInt(view.pixelY.getText());

            String valueR = InsertValuePopup.show("Ingrese valor R", "0").get();
            String valueG = InsertValuePopup.show("Ingrese valor G", "0").get();
            String valueB = InsertValuePopup.show("Ingrese valor B", "0").get();

            Image modifiedImage = modifyPixelAction.execute(pixelX, pixelY, valueR, valueG, valueB);

            view.modifiedImageView.setImage(modifiedImage);

        } else {
            view.valueR.setText("Elegir pixel R");
            view.valueG.setText("Elegir pixel V");
            view.valueB.setText("Elegir pixel B");
        }
    }

    private boolean validatePixelCoordinates() {
        return (!view.pixelX.getText().equals(EMPTY) && !view.pixelY.getText().equals(EMPTY));
    }

    public void onShowReport() {
        new ImageInformSceneCreator().createScene();
    }

    public void onCutPartialImage() {
        Image image = view.customImageView.cutPartialImage();
        view.modifiedImageView.setImage(image);
        this.putModifiedImageAction.execute(new Imagen(SwingFXUtils.fromFXImage(image, null), Format.PNG));
        view.aceptarBoton.setVisible(true);
    }

    public void onCalculateNegativeImage() {
        Image image = this.calculateNegativeImageAction.execute();
        view.modifiedImageView.setImage(image);
        view.aceptarBoton.setVisible(true);
    }

    private void applyThresholdToModifiedImage(Imagen customImage) {
        aplicarUmbralizacion(customImage);
    }

    public void onUmbralizar() {
        this.getImageAction.execute().ifPresent(this::aplicarUmbralizacion);
    }

    private void aplicarUmbralizacion(Imagen customImage) {
        int threshold = Integer.parseInt(InsertValuePopup.show("Umbral", "0").get());
        view.modifiedImageView.setImage(aplicarUmbralAction.execute(customImage, threshold));
        view.aceptarBoton.setVisible(true);
    }

    public void onCreateHistograma() {
        new HistogramaSceneCreator().createScene();
    }

    public void onContrast() {
        new ContrastSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public MainSceneController getView() {
        return this.view;
    }

    public void onCreateEqualizedImageOnce() {
        this.getImageAction.execute()
                           .ifPresent(customImage -> equalizeGrayImageAction.execute(customImage, 1));
        view.aceptarBoton.setVisible(true);
    }

    public void onCreateEqualizedImageTwice() {
        this.getImageAction.execute()
                           .ifPresent(customImage -> equalizeGrayImageAction.execute(customImage, 2));
        view.aceptarBoton.setVisible(true);
    }

    public void onCompressDynamicRange() {
        Image image = compressDynamicRangeAction.execute();
        view.modifiedImageView.setImage(image);
        view.aceptarBoton.setVisible(true);
    }

    public void onGammaPowerFunction() {
        new GammaPowerFunctionSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onCalculateImagesOperations() {
        new ImagesOperationsSceneCreator().createScene();
    }

    public void onGenerarNumeroRandomExponencial() {
        SemaforosGeneradoresDeRandoms.setValue(RandomElement.NUMERO);
        new ExponentialSceneCreator().createScene();
    }

    public void onGenerarNumeroRandomRayleigh() {
        SemaforosGeneradoresDeRandoms.setValue(RandomElement.NUMERO);
        new RayleighSceneCreator().createScene();
    }

    public void onGenerarNumeroRandomGaussiano() {
        SemaforosGeneradoresDeRandoms.setValue(RandomElement.NUMERO);
        new GaussianSceneCreator().createScene();
    }

    public void onAplicarRuidoSalYPimienta() {
        new SaltAndPepperNoiseSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplyEdgeEnhancement() {
        int insertedSize = 0;
        while (insertedSize % 2 == 0 || insertedSize <= 0) {
            insertedSize = Integer.parseInt(InsertValuePopup.show("Insert High Pass mask size (odd)", "3").get());
        }
        //hago esto, porque sino una expresion lambda que la usa despues tiene problemas
        int size = insertedSize;
        this.getImageAction.execute()
                           .ifPresent(customImage -> {
                               Imagen filteredCustomImage = applyFilterAction.execute(customImage, new HighPassMascara(size));
                               view.modifiedImageView.setImage(filteredCustomImage.toFXImage());

                               this.applyThresholdToModifiedImage(filteredCustomImage);
                           });
        view.aceptarBoton.setVisible(true);
    }

    public void onGenerarImagenSinteticaRuidoExponencial() {
        SemaforosGeneradoresDeRandoms.setValue(RandomElement.IMAGEN_SINTETICA_RUIDO);
        new ExponentialSceneCreator().createScene();
    }

    public void onGenerarImagenSinteticaRuidoRayleigh() {
        SemaforosGeneradoresDeRandoms.setValue(RandomElement.IMAGEN_SINTETICA_RUIDO);
        new RayleighSceneCreator().createScene();
    }

    public void onGenerarImagenSinteticaRuidoGaussiano() {
        SemaforosGeneradoresDeRandoms.setValue(RandomElement.IMAGEN_SINTETICA_RUIDO);
        new GaussianSceneCreator().createScene();
    }

    public void onCreateEqualizedImageByHistogram() {
        EqualizedTimes.once();
        new EqualizeImageByHistogramSceneCreator().createScene();
    }

    public void onCreateEqualizedImageTwiceByHistogram() {
        EqualizedTimes.twice();
        new EqualizeImageByHistogramSceneCreator().createScene();
    }

    public void onAplicarRuidoAditivoGaussiano() {
        SemaforosGeneradoresDeRandoms.setValue(RandomElement.RUIDO);
        new GaussianSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarRuidoMultiplicativoRayleigh() {
        SemaforosGeneradoresDeRandoms.setValue(RandomElement.RUIDO);
        new RayleighSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarRuidoMultiplicativoExponencial() {
        SemaforosGeneradoresDeRandoms.setValue(RandomElement.RUIDO);
        new ExponentialSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarFiltroMedia() {
        SemaforoFiltro.setValue(Mascara.Tipo.MEDIA);
        new FilterSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarFiltroMediana() {
        SemaforoFiltro.setValue(Mascara.Tipo.MEDIANA);
        new FilterSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarFiltroMedianaPonderada() {
        SemaforoFiltro.setValue(Mascara.Tipo.MEDIANA_PONDERADA);
        new FilterSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarFiltroGausseano() {
        SemaforoFiltro.setValue(Mascara.Tipo.GAUSSIANO);
        new FilterSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplyPrewittEdgeDetector() {
        SemaforoFiltro.setValue(Mascara.Tipo.PREWITT);
        PresenterProvider.provideEdgeDetectorPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplySobelEdgeDetector() {
        SemaforoFiltro.setValue(Mascara.Tipo.SOBEL);
        PresenterProvider.provideEdgeDetectorPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplyDirectionalDerivativeOperatorStandardMask() {
        SemaforoFiltro.setValue(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_STANDARD);
        PresenterProvider.provideDirectionalDerivativeOperatorPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplyDirectionalDerivativeOperatorKirshMask() {
        SemaforoFiltro.setValue(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_KIRSH);
        PresenterProvider.provideDirectionalDerivativeOperatorPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplyDirectionalDerivativeOperatorPrewittMask() {
        SemaforoFiltro.setValue(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_PREWITT);
        PresenterProvider.provideDirectionalDerivativeOperatorPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplyDirectionalDerivativeOperatorSobelMask() {
        SemaforoFiltro.setValue(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_SOBEL);
        PresenterProvider.provideDirectionalDerivativeOperatorPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplyGlobalThresholdEstimation() {
        this.getImageAction.execute()
                           .ifPresent(customImage -> {
                               ImageLimitValues imageLimitValues = this.getImageLimitValuesAction.execute(customImage);
                               int initialThreshold = Integer.parseInt(InsertValuePopup.show("Initial Threshold " +
                                       "Max = " + String.valueOf(imageLimitValues.getMaxLimit()) +
                                       " ; Min = " + String.valueOf(imageLimitValues.getMinLimit()), "1").get());
                               int deltaT = Integer.parseInt(InsertValuePopup.show("Define Delta T", "1").get());
                               GlobalThresholdResult globalThresholdResult = applyGlobalThresholdEstimationAction
                                       .execute(customImage, initialThreshold, deltaT);
                               view.modifiedImageView.setImage(globalThresholdResult.getImage());
                               ShowResultPopup.show("Global Threshold Estimation",
                                       "Iterations: " + String.valueOf(globalThresholdResult.getIterations()) + "\n" +
                                               "Threshold: " + String.valueOf(globalThresholdResult.getThreshold()));
                           });

        view.aceptarBoton.setVisible(true);
    }

    public void onApplyOtsuThresholdEstimation() {
        this.getImageAction.execute()
                           .ifPresent(customImage -> {
                               OtsuThresholdResult otsuThresholdResult = applyOtsuThresholdEstimationAction.execute(customImage);
                               view.modifiedImageView.setImage(otsuThresholdResult.getImage());
                               ShowResultPopup.show("Otsu Threshold Estimation",
                                       "Threshold: " + String.valueOf(otsuThresholdResult.getThreshold()));
                           });

        view.aceptarBoton.setVisible(true);
    }

    public void onApplyLaplacianEdgeDetector(LaplacianDetector detector) {
        this.getImageAction.execute()
                           .ifPresent(customImage -> {
                               int slopeThreshold = 0;
                               if (detector == LaplacianDetector.WITH_SLOPE_EVALUATION) {
                                   slopeThreshold = Integer.parseInt(InsertValuePopup.show("Insert slope threshold", "0").get());
                               }
                               Imagen edgedImage = this.applyLaplacianDetectorAction.execute(customImage, new LaplacianMascara(), slopeThreshold);
                               this.updateModifiedImage(edgedImage);
                           });
    }

    public void onApplyMarrHildrethEdgeDetector() {
        this.getImageAction.execute()
                           .ifPresent(customImage -> {
                               double sigma = Double.parseDouble(InsertValuePopup.show("Insert standard deviation value", "0").get());
                               int slopeThreshold = Integer.parseInt(InsertValuePopup.show("Insert slope threshold", "0").get());
                               Imagen edgedImage = this.applyLaplacianDetectorAction
                                       .execute(customImage, new LaplacianMascaraGaussiana(sigma), slopeThreshold);
                               this.updateModifiedImage(edgedImage);
                           });
    }

    public void onApplyDiffusion() {
        new DiffusionSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onResetModifiedImage() {
        view.modifiedImageView.setImage(null);
        view.aceptarBoton.setVisible(false);
    }

    public void onUndoChanges() {
        Imagen originalImage = this.undoChangesAction.execute();
        view.undoChangesButton.setVisible(false);
        view.imageView.setImage(originalImage.toFXImage());
    }

    public void onApplyCannyEdgeDetector() {
        new CannySceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplySusanEdgeDetector() {
        this.getImageAction.execute()
                           .ifPresent(customImage -> {
                               Mascara susanMascara = new SusanMascara();
                               Imagen edgedImage = this.applySusanDetectorAction.execute(customImage, susanMascara);
                               this.updateModifiedImage(edgedImage);
                           });
    }

    public void onHoughTransform() {
        new HoughSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplyActiveContour() {
        ActiveContourMode.single();
        new ActiveContourSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplyActiveContourOnImageSequence() {
        ActiveContourMode.sequence();
        new ActiveContourSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void onApplyHarris() {
        new HarrisSceneCreator().createScene();
    }

    public void onApplySift() {
        new SiftSceneCreator().createScene();
    }
}

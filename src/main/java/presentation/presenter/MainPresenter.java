package presentation.presenter;

import core.action.channels.ObtainHSVChannelAction;
import core.action.channels.ObtainRGBChannelAction;
import core.action.edgedetector.AplicarDetectorLaplacianoAction;
import core.action.edgedetector.ApplySusanDetectorAction;
import core.action.edit.ModifyPixelAction;
import core.action.edit.space_domain.CalcularNegativoAction;
import core.action.edit.space_domain.CompressDynamicRangeAction;
import core.action.figure.CrearImagenConFiguraAction;
import core.action.filter.AplicarFiltroAction;
import core.action.gradient.CreateImageWithGradientAction;
import core.action.histogram.EqualizeGrayImageAction;
import core.action.histogram.utils.EqualizedTimes;
import core.action.image.*;
import core.action.threshold.AplicarEstimacionDeUmbralGlobalAction;
import core.action.threshold.AplicarEstimacionDelUmbralDeOtsuAction;
import core.action.threshold.AplicarUmbralAction;
import core.provider.PresenterProvider;
import core.semaphore.SemaforosGeneradoresDeRandoms;
import dominio.SemaforoFiltro;
import dominio.RandomElement;
import dominio.activecontour.ActiveContourMode;
import dominio.automaticthreshold.ResultadoUmbralGlobal;
import dominio.automaticthreshold.ImageLimitValues;
import dominio.automaticthreshold.EstimacionDelUmbralDeOtsuResultante;
import dominio.customimage.Imagen;
import dominio.customimage.Format;
import dominio.flags.LaplacianDetector;
import dominio.generation.Channel;
import dominio.generation.Figura;
import dominio.generation.Gradient;
import dominio.mask.MascaraLaplacianoDelGaussiano;
import dominio.mask.MascaraLaplaciano;
import dominio.mask.Mascara;
import dominio.mask.SusanMascara;
import dominio.mask.filter.HighPassMascara;
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
    private final LoadImageAction cargarImagenAction;
    private final CargarSecuenciaImagenesAction cargarImagenParaMultipleProcesamientoAction;
    private final ObtenerImagenAction obtenerImagenAction;
    private final ModifyPixelAction modifyPixelAction;
    private final PutModifiedImageAction putModifiedImageAction;
    private final CalcularNegativoAction calcularNegativoAction;
    private final AplicarUmbralAction aplicarUmbralAction;
    private final CreateImageWithGradientAction createImageWithGradientAction;
    private final ObtainRGBChannelAction obtainRGBChannelAction;
    private final ObtainHSVChannelAction obtainHSVChannelAction;
    private final CrearImagenConFiguraAction crearImagenConFiguraAction;
    private final EqualizeGrayImageAction equalizeGrayImageAction;
    private final Observable<Image> onModifiedImage;
    private final CompressDynamicRangeAction compressDynamicRangeAction;
    private final AplicarFiltroAction aplicarFiltroAction;
    private final UpdateCurrentImageAction updateCurrentImageAction;
    private final AplicarEstimacionDeUmbralGlobalAction aplicarEstimacionDeUmbralGlobalAction;
    private final AplicarEstimacionDelUmbralDeOtsuAction aplicarEstimacionDelUmbralDeOtsuAction;
    private final AplicarDetectorLaplacianoAction aplicarDetectorLaplacianoAction;
    private final UndoChangesAction undoChangesAction;
    private final GetImageLimitValuesAction getImageLimitValuesAction;
    private final ApplySusanDetectorAction applySusanDetectorAction;

    public MainPresenter(MainSceneController view,
                         LoadImageAction cargarImagenAction,
                         CargarSecuenciaImagenesAction cargarImagenParaMultipleProcesamientoAction, ObtenerImagenAction obtenerImagenAction,
                         PutModifiedImageAction putModifiedImageAction,
                         ModifyPixelAction modifyPixelAction,
                         CalcularNegativoAction calcularNegativoAction,
                         AplicarUmbralAction aplicarUmbralAction,
                         CreateImageWithGradientAction createImageWithGradientAction,
                         ObtainRGBChannelAction obtainRGBChannelAction,
                         ObtainHSVChannelAction obtainHSVChannelAction,
                         CrearImagenConFiguraAction crearImagenConFiguraAction,
                         EqualizeGrayImageAction equalizeGrayImageAction,
                         Observable<Image> onModifiedImage,
                         CompressDynamicRangeAction compressDynamicRangeAction,
                         AplicarFiltroAction aplicarFiltroAction,
                         UpdateCurrentImageAction updateCurrentImageAction,
                         AplicarEstimacionDeUmbralGlobalAction aplicarEstimacionDeUmbralGlobalAction,
                         AplicarEstimacionDelUmbralDeOtsuAction aplicarEstimacionDelUmbralDeOtsuAction,
                         AplicarDetectorLaplacianoAction aplicarDetectorLaplacianoAction,
                         UndoChangesAction undoChangesAction,
                         GetImageLimitValuesAction getImageLimitValuesAction,
                         ApplySusanDetectorAction applySusanDetectorAction) {

        this.view = view;

        this.cargarImagenAction = cargarImagenAction;
        this.cargarImagenParaMultipleProcesamientoAction = cargarImagenParaMultipleProcesamientoAction;
        this.obtenerImagenAction = obtenerImagenAction;
        this.modifyPixelAction = modifyPixelAction;
        this.putModifiedImageAction = putModifiedImageAction;
        this.calcularNegativoAction = calcularNegativoAction;
        this.aplicarUmbralAction = aplicarUmbralAction;
        this.onModifiedImage = onModifiedImage;
        this.createImageWithGradientAction = createImageWithGradientAction;
        this.obtainRGBChannelAction = obtainRGBChannelAction;
        this.obtainHSVChannelAction = obtainHSVChannelAction;
        this.crearImagenConFiguraAction = crearImagenConFiguraAction;
        this.equalizeGrayImageAction = equalizeGrayImageAction;
        this.compressDynamicRangeAction = compressDynamicRangeAction;
        this.aplicarFiltroAction = aplicarFiltroAction;
        this.updateCurrentImageAction = updateCurrentImageAction;
        this.aplicarEstimacionDeUmbralGlobalAction = aplicarEstimacionDeUmbralGlobalAction;
        this.aplicarEstimacionDelUmbralDeOtsuAction = aplicarEstimacionDelUmbralDeOtsuAction;
        this.aplicarDetectorLaplacianoAction = aplicarDetectorLaplacianoAction;
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

    public void onAbrirImagen() {
        setImagenEnVistaPreliminar(this.cargarImagenAction.execute());
    }

    public void onAbrirImagenMultipleProcesamiento() {
        List<Imagen> imagenes = this.cargarImagenParaMultipleProcesamientoAction.execute();
        if(!imagenes.isEmpty()) {
            setImagenEnVistaPreliminar(imagenes.get(0));
        }
    }

    private void setImagenEnVistaPreliminar(Imagen imagen) {
        view.customImageView.setImagen(SwingFXUtils.toFXImage(imagen.getBufferedImage(), null));
    }

    public void onSaveImage() {
        new SaveImageSceneCreator().createScene();
    }

    public void onAplicarCambios() {
        Imagen modifiedCustomImage = new Imagen(view.modifiedImageView.getImage(), "png");
        view.customImageView.setImagen(view.modifiedImageView.getImage());
        updateCurrentImageAction.execute(modifiedCustomImage);
        view.modifiedImageView.setImage(null);
        view.aceptarBoton.setVisible(false);
        //view.undoChangesButton.setVisible(true);
    }

    public void onShowGreyGradient() {
        setImagenEnVistaPreliminar(createImageWithGradientAction.execute(DEFAULT_WIDTH, DEFAULT_HEIGHT, Gradient.GREY));
    }

    public void onShowColorGradient() {
        setImagenEnVistaPreliminar(createImageWithGradientAction.execute(DEFAULT_WIDTH, DEFAULT_HEIGHT, Gradient.COLOR));
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

    public void onCuadradoBlanco() {
        setImagenEnVistaPreliminar(crearImagenConFiguraAction.ejecutar(200, 200, Figura.CUADRADO));
    }

    public void onCirculoBlanco() {
        setImagenEnVistaPreliminar(crearImagenConFiguraAction.ejecutar(200, 200, Figura.CIRCULO));
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

    private void actualizarImagenModificada(Imagen customImage) {
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

            this.obtenerImagenAction.ejecutar()
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

    public void calcularNegativo() {
        Image image = this.calcularNegativoAction.execute();
        view.modifiedImageView.setImage(image);
        view.aceptarBoton.setVisible(true);
    }

    private void applyThresholdToModifiedImage(Imagen customImage) {
        aplicarUmbralizacion(customImage);
    }

    public void onUmbralizar() {
        this.obtenerImagenAction.ejecutar().ifPresent(this::aplicarUmbralizacion);
    }

    private void aplicarUmbralizacion(Imagen customImage) {
        int threshold = Integer.parseInt(InsertValuePopup.show("Umbral", "0").get());
        view.modifiedImageView.setImage(aplicarUmbralAction.ejecutar(customImage, threshold));
        view.aceptarBoton.setVisible(true);
    }

    public void onCrearHistograma() {
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
        this.obtenerImagenAction.ejecutar()
                           .ifPresent(customImage -> equalizeGrayImageAction.execute(customImage, 1));
        view.aceptarBoton.setVisible(true);
    }

    public void onCreateEqualizedImageTwice() {
        this.obtenerImagenAction.ejecutar()
                           .ifPresent(customImage -> equalizeGrayImageAction.execute(customImage, 2));
        view.aceptarBoton.setVisible(true);
    }

    public void onCompressDynamicRange() {
        Image image = compressDynamicRangeAction.execute();
        view.modifiedImageView.setImage(image);
        view.aceptarBoton.setVisible(true);
    }

    public void onFuncionDePotenciaGamma() {
        new FuncionPotenciaGammaSceneCreator().createScene();
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
        this.obtenerImagenAction.ejecutar()
                           .ifPresent(customImage -> {
                               Imagen filteredCustomImage = aplicarFiltroAction.aplicar(customImage, new HighPassMascara(size));
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

    public void onAplicarFiltroBilateral() {
        SemaforoFiltro.setValue(Mascara.Tipo.BILATERAL);
        new FilterSceneCreator().createScene();
        view.aceptarBoton.setVisible(true);
    }

    public void OnAplicarDetectorBordesPrewitt() {
        SemaforoFiltro.setValue(Mascara.Tipo.PREWITT);
        PresenterProvider.providerDetectorBordesPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarDetectorBordesSobel() {
        SemaforoFiltro.setValue(Mascara.Tipo.SOBEL);
        PresenterProvider.providerDetectorBordesPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarDetectorBordesSobelVertical() {
        SemaforoFiltro.setValue(Mascara.Tipo.SOBEL_VERTICAL);
        PresenterProvider.providerDetectorBordesPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarDetectorBordesSobelHorizontal() {
        SemaforoFiltro.setValue(Mascara.Tipo.SOBEL_HORIZONTAL);
        PresenterProvider.providerDetectorBordesPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarMascaraStadardDerivativaDireccional() {
        SemaforoFiltro.setValue(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_STANDARD);
        PresenterProvider.provideDirectionalDerivativeOperatorPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarMascaraDerivativaDireccionalDeKirsh() {
        SemaforoFiltro.setValue(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_KIRSH);
        PresenterProvider.provideDirectionalDerivativeOperatorPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarMascaraDerivativaDireccionalDePrewitt() {
        SemaforoFiltro.setValue(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_PREWITT);
        PresenterProvider.provideDirectionalDerivativeOperatorPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarMascaraDerivativaDireccionalDeSobel() {
        SemaforoFiltro.setValue(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_SOBEL);
        PresenterProvider.provideDirectionalDerivativeOperatorPresenter().onInitialize();
        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarEstimacionDelUmbralGlobal() {
        this.obtenerImagenAction.ejecutar()
                           .ifPresent(customImage -> {
                               ImageLimitValues imageLimitValues = this.getImageLimitValuesAction.execute(customImage);
                               int umbralInicial = Integer.parseInt(InsertValuePopup.show("Umbral Inicial " +
                                       "Max = " + String.valueOf(imageLimitValues.getMaxLimit()) +
                                       " ; Min = " + String.valueOf(imageLimitValues.getMinLimit()), "1").get());
                               int deltaT = Integer.parseInt(InsertValuePopup.show("Ingrese un Delta T", "1").get());
                               ResultadoUmbralGlobal resultadoUmbralGlobal = aplicarEstimacionDeUmbralGlobalAction
                                       .ejecutar(customImage, umbralInicial, deltaT);
                               view.modifiedImageView.setImage(resultadoUmbralGlobal.getImagen());
                               ShowResultPopup.show("Estimacion del Umbral Global",
                                       "Cantidad de iteraciones utilizadas: " + String.valueOf(resultadoUmbralGlobal.getIteraciones()) + "\n" +
                                               "Umbral Global: " + String.valueOf(resultadoUmbralGlobal.getUmbral()));
                           });

        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarEstimacionDelUmbralDeOtsu() {
        this.obtenerImagenAction.ejecutar()
                           .ifPresent(customImage -> {
                               EstimacionDelUmbralDeOtsuResultante estimacionDelUmbralDeOtsuResultante = aplicarEstimacionDelUmbralDeOtsuAction.ejecutar(customImage);
                               view.modifiedImageView.setImage(estimacionDelUmbralDeOtsuResultante.getImagen());
                               ShowResultPopup.show("Estimacion Del Umbral De Otsu",
                                       "Umbral: " + String.valueOf(estimacionDelUmbralDeOtsuResultante.getUmbral()));
                           });

        view.aceptarBoton.setVisible(true);
    }

    public void onAplicarDetectorDeBordeLaplacianoConEvaluacionDeLaPendiente(LaplacianDetector detector) {
        this.obtenerImagenAction.ejecutar()
                           .ifPresent(customImage -> {
                               int pendiente = 0;
                               if (detector == LaplacianDetector.CON_EVALUACION_DE_LA_PENDIENTE)
                                   pendiente = Integer.parseInt(InsertValuePopup.show("Ingrese la pendiente", "0").get());
                               Imagen imagenConBordes = this.aplicarDetectorLaplacianoAction.ejecutar(customImage, new MascaraLaplaciano(), pendiente);
                               this.actualizarImagenModificada(imagenConBordes);
                           });
    }

    public void onAplicarMarrHildrethDetectorDeBorde() {
        this.obtenerImagenAction.ejecutar()
                           .ifPresent(customImage -> {
                               double sigma = Double.parseDouble(InsertValuePopup.show("Ingrese el valor de la desviación estandar", "0").get());
                               int pendiente = Integer.parseInt(InsertValuePopup.show("Ingrese la pendiente", "0").get());
                               Imagen imagenConBordes = this.aplicarDetectorLaplacianoAction
                                       .ejecutar(customImage, new MascaraLaplacianoDelGaussiano(sigma), pendiente);
                               this.actualizarImagenModificada(imagenConBordes);
                           });
    }

    public void onAplicarDifusion() {
        new DifusionSceneCreator().createScene();
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
        this.obtenerImagenAction.ejecutar()
                           .ifPresent(customImage -> {
                               Mascara susanMascara = new SusanMascara();
                               Imagen edgedImage = this.applySusanDetectorAction.execute(customImage, susanMascara);
                               this.actualizarImagenModificada(edgedImage);
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

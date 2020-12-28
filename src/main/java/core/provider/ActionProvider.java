package core.provider;

import core.action.channels.ObtainHSVChannelAction;
import core.action.channels.ObtainRGBChannelAction;
import core.action.characteristic_points.ApplyHarrisDetectorAction;
import core.action.characteristic_points.AplicarDetectorSiftAction;
import core.action.difusion.AplicarDifusionAction;
import core.action.edgedetector.*;
import core.action.edgedetector.hough.TransformadaHoughCircularAction;
import core.action.edgedetector.hough.TransformadaHoughLinearAction;
import core.action.edit.ModifyPixelAction;
import core.action.edit.space_domain.*;
import core.action.edit.space_domain.operations.MultiplyImagesAction;
import core.action.edit.space_domain.operations.SubstractImagesAction;
import core.action.edit.space_domain.operations.SumImagesAction;
import core.action.figure.CrearImagenConFiguraAction;
import core.action.filter.AplicarFiltroAction;
import core.action.gradient.CreateImageWithGradientAction;
import core.action.histogram.CreateImageHistogramAction;
import core.action.histogram.EqualizeGrayImageAction;
import core.action.image.*;
import core.action.noise.AplicarRuidoExponencialAction;
import core.action.noise.AplicarRuidoGaussianoAction;
import core.action.noise.AplicarRuidoRayleighAction;
import core.action.noise.AplicarRuidoSalYPimientaAction;
import core.action.noise.generator.GenerarImagenRuidoSinteticoAction;
import core.action.threshold.AplicarEstimacionDeUmbralGlobalAction;
import core.action.threshold.AplicarEstimacionDelUmbralDeOtsuAction;
import core.action.threshold.AplicarUmbralAction;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;


class ActionProvider {

    private static CargarImagenAction cargarImagenAction;
    private static ObtenerImagenAction obtenerImagenAction;
    private static SaveImageAction saveImageAction;
    private static ModifyPixelAction modifyPixelAction;
    private static PutModifiedImageAction putModifiedImageAction;
    private static GetModifiedImageAction getModifiedImageAction;
    private static CreateImageInformAction createImageInformAction;
    private static ObtainRGBChannelAction obtainRGBChannelAction;
    private static ObtainHSVChannelAction obtainHSVChannelAction;
    private static CrearImagenConFiguraAction crearImagenConFiguraAction;
    private static CreateImageWithGradientAction createImageWithGradientAction;
    private static CalcularNegativoAction calcularNegativoAction;
    private static AplicarUmbralAction aplicarUmbralAction;
    private static CreateImageHistogramAction createImageHistogramAction;
    private static ApplyContrastAction applyContrastAction;
    private static CompressDynamicRangeAction compressDynamicRangeAction;
    private static FuncionGammaAction funcionGammaAction;
    private static MultiplyImagesAction multiplyImagesAction;
    private static SumImagesAction sumImagesAction;
    private static NormalizeImageAction normalizeImageAction;
    private static SubstractImagesAction substractImagesAction;
    private static AplicarRuidoSalYPimientaAction applySaltAndPepperAction;
    private static AplicarFiltroAction aplicarFiltroAction;
    private static GenerarImagenRuidoSinteticoAction generarImagenRuidoSinteticoAction;
    private static AplicarRuidoGaussianoAction aplicarRuidoGaussianoAction;
    private static AplicarRuidoRayleighAction aplicarRuidoRayleighAction;
    private static AplicarRuidoExponencialAction aplicarRuidoExponencialAction;
    private static AplicarDetectorDeBordesAction aplicarDetectorDeBordesAction;
    private static UpdateCurrentImageAction updateCurrentImageAction;
    private static AplicarOperadorDireccionalDerivativoAction aplicarOperadorDireccionalDerivativoAction;
    private static AplicarEstimacionDeUmbralGlobalAction aplicarEstimacionDeUmbralGlobalAction;
    private static AplicarEstimacionDelUmbralDeOtsuAction aplicarEstimacionDelUmbralDeOtsuAction;
    private static AplicarDetectorLaplacianoAction aplicarDetectorLaplacianoAction;
    private static AplicarDifusionAction aplicarDifusionAction;
    private static UndoChangesAction undoChangesAction;
    private static GetImageLimitValuesAction getImageLimitValuesAction;
    private static AplicarCannyAction aplicarCannyAction;
    private static ApplySusanDetectorAction applySusanDetectorAction;
    private static TransformadaHoughLinearAction transformadaHoughLinearAction;
    private static TransformadaHoughCircularAction transformadaHoughCircularAction;
    private static AplicarContornoActivoAction aplicarContornoActivoAction;
    private static CargarSecuenciaImagenesAction cargarSecuenciaImagenesAction;
    private static GetImageSequenceAction getImageSequenceAction;
    private static AplicarContornosActivosEnSecuanciaDeImagesAction aplicarContornosActivosEnSecuanciaDeImagesAction;
    private static ApplyHarrisDetectorAction applyHarrisDetectorAction;
    private static AplicarDetectorSiftAction aplicarDetectorSiftAction;

    public static ObtenerImagenAction provideGetImageAction() {
        if (obtenerImagenAction == null) {
            obtenerImagenAction = new ObtenerImagenAction(RepositoryProvider.provideImageRepository());
        }
        return obtenerImagenAction;
    }

    public static CargarImagenAction provideLoadImageAction() {
        if (cargarImagenAction == null) {
            cargarImagenAction = new CargarImagenAction(
                    RepositoryProvider.provideImageRepository(),
                    ServiceProvider.provideOpenFileService(),
                    CommonProvider.provideOpener(),
                    ServiceProvider.provideImageRawService());
        }
        return cargarImagenAction;
    }

    public static SaveImageAction provideSaveImageAction() {
        if (saveImageAction == null) {
            saveImageAction = new SaveImageAction(ServiceProvider.provideImageRawService());
        }
        return saveImageAction;
    }

    public static ModifyPixelAction provideModifyPixelAction() {
        if (modifyPixelAction == null) {
            modifyPixelAction = new ModifyPixelAction(
                    RepositoryProvider.provideImageRepository(),
                    ServiceProvider.provideModifyImageService()
            );
        }
        return modifyPixelAction;
    }

    public static PutModifiedImageAction providePutModifiedImageAction() {
        if (putModifiedImageAction == null) {
            putModifiedImageAction = new PutModifiedImageAction(RepositoryProvider.provideImageRepository());
        }
        return putModifiedImageAction;
    }

    public static GetModifiedImageAction provideGetModifiedImageAction() {
        if (getModifiedImageAction == null) {
            getModifiedImageAction = new GetModifiedImageAction(RepositoryProvider.provideImageRepository());
        }
        return getModifiedImageAction;
    }

    public static ObtainRGBChannelAction provideObtainRGBChannelAction() {
        if (obtainRGBChannelAction == null) {
            obtainRGBChannelAction = new ObtainRGBChannelAction(RepositoryProvider.provideImageRepository());
            return obtainRGBChannelAction;
        }
        return obtainRGBChannelAction;
    }

    public static CreateImageInformAction provideCreateImageInformAction() {
        if (createImageInformAction == null) {
            createImageInformAction = new CreateImageInformAction();
        }
        return createImageInformAction;
    }

    public static ObtainHSVChannelAction provideObtainHSVChannelAction() {
        if (obtainHSVChannelAction == null) {
            obtainHSVChannelAction = new ObtainHSVChannelAction(RepositoryProvider.provideImageRepository(),
                    ServiceProvider.provideTransformRGBtoHSVImageService());
        }
        return obtainHSVChannelAction;
    }

    public static CrearImagenConFiguraAction provideCreateImageWithFigureAction() {
        if (crearImagenConFiguraAction == null) {
            crearImagenConFiguraAction = new CrearImagenConFiguraAction(
                    ServiceProvider.provideImageFigureService(),
                    RepositoryProvider.provideImageRepository());
        }
        return crearImagenConFiguraAction;
    }

    public static CreateImageWithGradientAction provideCreateGradientAction() {
        if (createImageWithGradientAction == null) {
            createImageWithGradientAction = new CreateImageWithGradientAction(
                    ServiceProvider.provideGradientService(),
                    RepositoryProvider.provideImageRepository());
        }
        return createImageWithGradientAction;
    }

    public static CalcularNegativoAction provideCalculateNegativeImageAction() {
        if (calcularNegativoAction == null) {
            calcularNegativoAction = new CalcularNegativoAction(
                    RepositoryProvider.provideImageRepository(),
                    ServiceProvider.provideModifyImageService());
        }
        return calcularNegativoAction;
    }

    public static AplicarUmbralAction provideApplyThresholdAction() {
        if (aplicarUmbralAction == null) {
            aplicarUmbralAction = new AplicarUmbralAction(ServiceProvider.provideApplyThresholdService());
        }
        return aplicarUmbralAction;
    }

    public static CreateImageHistogramAction provideCreateImageHistogram() {
        if (createImageHistogramAction == null) {
            createImageHistogramAction = new CreateImageHistogramAction(ServiceProvider.provideHistogramService());
        }
        return createImageHistogramAction;
    }

    public static ApplyContrastAction provideApplyContrastAction() {
        if (applyContrastAction == null) {
            applyContrastAction = new ApplyContrastAction(
                    ServiceProvider.provideModifyImageService());
        }
        return applyContrastAction;
    }

    public static EqualizeGrayImageAction provideCreateEqualizedGrayImageAction(PublishSubject<Image> imagePublishSubject) {
        return new EqualizeGrayImageAction(
                ServiceProvider.provideHistogramService(),
                RepositoryProvider.provideImageRepository(),
                imagePublishSubject);
    }

    public static CompressDynamicRangeAction provideCompressDynamicRangeAction() {
        if (compressDynamicRangeAction == null) {
            compressDynamicRangeAction = new CompressDynamicRangeAction(
                    ServiceProvider.provideGrayLevelStatisticsService(),
                    RepositoryProvider.provideImageRepository());
        }
        return compressDynamicRangeAction;
    }

    public static FuncionGammaAction provideGammaFunctionAction() {
        if (funcionGammaAction == null) {
            funcionGammaAction = new FuncionGammaAction(RepositoryProvider.provideImageRepository());
        }
        return funcionGammaAction;
    }

    public static SumImagesAction provideSumImagesAction() {
        if (sumImagesAction == null) {
            sumImagesAction = new SumImagesAction(ServiceProvider.provideImageOperationsService());
        }
        return sumImagesAction;
    }

    public static MultiplyImagesAction provideMultiplyImagesAction() {
        if (multiplyImagesAction == null) {
            multiplyImagesAction = new MultiplyImagesAction(ServiceProvider.provideImageOperationsService());
        }
        return multiplyImagesAction;
    }

    public static SubstractImagesAction provideSubstractImagesAction() {
        if (substractImagesAction == null) {
            substractImagesAction = new SubstractImagesAction(ServiceProvider.provideImageOperationsService());
        }
        return substractImagesAction;
    }

    public static NormalizeImageAction provideNormalizeImageAction() {
        if (normalizeImageAction == null) {
            normalizeImageAction = new NormalizeImageAction(ServiceProvider.provideImageOperationsService());
        }
        return normalizeImageAction;
    }

    public static AplicarRuidoSalYPimientaAction provideApplySaltAndPepperNoiseAction() {
        if (applySaltAndPepperAction == null) {
            applySaltAndPepperAction = new AplicarRuidoSalYPimientaAction(
                    ServiceProvider.provideRandomNumberGenerationService(),
                    PublishSubjectProvider.provideOnModifiedImagePublishSubject());
        }
        return applySaltAndPepperAction;
    }

    public static AplicarFiltroAction provideApplyFilterAction() {
        if (aplicarFiltroAction == null) {
            aplicarFiltroAction = new AplicarFiltroAction(
                    PublishSubjectProvider.provideOnModifiedImagePublishSubject(),
                    ServiceProvider.provideImageOperationsService()
            );
        }
        return aplicarFiltroAction;
    }

    public static GenerarImagenRuidoSinteticoAction provideGenerateSyntheticNoiseImageAction() {
        if (generarImagenRuidoSinteticoAction == null) {
            generarImagenRuidoSinteticoAction = new GenerarImagenRuidoSinteticoAction(
                    ServiceProvider.provideImageOperationsService());
        }
        return generarImagenRuidoSinteticoAction;
    }

    public static AplicarRuidoGaussianoAction provideApplyGaussianNoiseToImageAction() {
        if (aplicarRuidoGaussianoAction == null) {
            aplicarRuidoGaussianoAction = new AplicarRuidoGaussianoAction(RepositoryProvider.provideImageRepository(),
                    ServiceProvider.provideImageOperationsService(),
                    ServiceProvider.provideRandomNumberGenerationService());
        }
        return aplicarRuidoGaussianoAction;
    }

    public static AplicarRuidoRayleighAction provideApplyRayleighNoiseToImageAction() {
        if (aplicarRuidoRayleighAction == null) {
            aplicarRuidoRayleighAction = new AplicarRuidoRayleighAction(RepositoryProvider.provideImageRepository(),
                    ServiceProvider.provideImageOperationsService(),
                    ServiceProvider.provideRandomNumberGenerationService());
        }
        return aplicarRuidoRayleighAction;
    }

    public static AplicarRuidoExponencialAction provideApplyExponentialNoiseToImageAction() {
        if (aplicarRuidoExponencialAction == null) {
            aplicarRuidoExponencialAction = new AplicarRuidoExponencialAction(RepositoryProvider.provideImageRepository(),
                    ServiceProvider.provideImageOperationsService(),
                    ServiceProvider.provideRandomNumberGenerationService());
        }
        return aplicarRuidoExponencialAction;
    }

    public static AplicarDetectorDeBordesAction provideApplyEdgeDetectorByGradient() {
        if (aplicarDetectorDeBordesAction == null) {
            aplicarDetectorDeBordesAction = new AplicarDetectorDeBordesAction(
                    ServiceProvider.provideImageOperationsService(),
                    ServiceProvider.provideMatrixService()
            );
        }
        return aplicarDetectorDeBordesAction;
    }

    public static UpdateCurrentImageAction provideUpdateCurrentImageAction() {
        if (updateCurrentImageAction == null) {
            updateCurrentImageAction = new UpdateCurrentImageAction(RepositoryProvider.provideImageRepository());
        }
        return updateCurrentImageAction;
    }

    public static AplicarOperadorDireccionalDerivativoAction provideApplyDerivateDirectionalOperatorAction() {
        if (aplicarOperadorDireccionalDerivativoAction == null) {
            aplicarOperadorDireccionalDerivativoAction = new AplicarOperadorDireccionalDerivativoAction(
                    ServiceProvider.provideImageOperationsService(),
                    PublishSubjectProvider.provideOnModifiedImagePublishSubject(),
                    ServiceProvider.provideMatrixService()
            );
        }
        return aplicarOperadorDireccionalDerivativoAction;
    }

    public static AplicarEstimacionDeUmbralGlobalAction provideApplyGlobalThresholdEstimation() {
        if (aplicarEstimacionDeUmbralGlobalAction == null) {
            aplicarEstimacionDeUmbralGlobalAction = new AplicarEstimacionDeUmbralGlobalAction(
                    ServiceProvider.provideMatrixService(),
                    ServiceProvider.provideApplyThresholdService());
        }
        return aplicarEstimacionDeUmbralGlobalAction;
    }

    public static AplicarEstimacionDelUmbralDeOtsuAction provideApplyOtsuThresholdEstimation() {
        if (aplicarEstimacionDelUmbralDeOtsuAction == null) {
            aplicarEstimacionDelUmbralDeOtsuAction = new AplicarEstimacionDelUmbralDeOtsuAction(
                    ServiceProvider.provideHistogramService(),
                    ServiceProvider.provideMatrixService(),
                    ServiceProvider.provideApplyThresholdService());
        }
        return aplicarEstimacionDelUmbralDeOtsuAction;
    }

    public static AplicarDetectorLaplacianoAction provideApplyLaplacianDetectorAction() {
        if (aplicarDetectorLaplacianoAction == null) {
            aplicarDetectorLaplacianoAction = new AplicarDetectorLaplacianoAction();
        }
        return aplicarDetectorLaplacianoAction;
    }

    public static AplicarDifusionAction provideApplyDiffusionAction() {
        if (aplicarDifusionAction == null) {
            aplicarDifusionAction = new AplicarDifusionAction(
                    ServiceProvider.provideImageOperationsService(),
                    PublishSubjectProvider.provideOnModifiedImagePublishSubject()
            );
        }
        return aplicarDifusionAction;
    }

    public static UndoChangesAction provideUndoChangesAction() {
        if (undoChangesAction == null) {
            undoChangesAction = new UndoChangesAction(RepositoryProvider.provideImageRepository());
        }
        return undoChangesAction;
    }

    public static GetImageLimitValuesAction provideGetImageLimitValuesAction() {
        if (getImageLimitValuesAction == null) {
            getImageLimitValuesAction = new GetImageLimitValuesAction(
                    ServiceProvider.provideGrayLevelStatisticsService());
        }
        return getImageLimitValuesAction;
    }

    public static AplicarCannyAction provideApplyCannyDetectorAction() {
        if (aplicarCannyAction == null) {
            aplicarCannyAction = new AplicarCannyAction(ServiceProvider.provideImageOperationsService(),
                    ServiceProvider.provideMatrixService());
        }
        return aplicarCannyAction;
    }

    public static ApplySusanDetectorAction provideApplySusanDetectorAction() {
        if (applySusanDetectorAction == null) {
            applySusanDetectorAction = new ApplySusanDetectorAction();
        }
        return applySusanDetectorAction;
    }

    public static TransformadaHoughLinearAction provideLineHoughTransformAction() {
        if (transformadaHoughLinearAction == null) {
            transformadaHoughLinearAction = new TransformadaHoughLinearAction();
        }
        return transformadaHoughLinearAction;
    }

    public static TransformadaHoughCircularAction provideCircleHoughTransformAction() {
        if (transformadaHoughCircularAction == null) {
            transformadaHoughCircularAction = new TransformadaHoughCircularAction();
        }
        return transformadaHoughCircularAction;
    }

    public static AplicarContornoActivoAction provideApplyActiveContourAction() {
        if (aplicarContornoActivoAction == null) {
            aplicarContornoActivoAction = new AplicarContornoActivoAction();
        }
        return aplicarContornoActivoAction;
    }

    public static CargarSecuenciaImagenesAction provideLoadImageSequenceAction() {
        if (cargarSecuenciaImagenesAction == null) {
            cargarSecuenciaImagenesAction = new CargarSecuenciaImagenesAction(
                    RepositoryProvider.provideImageRepository(),
                    ServiceProvider.provideOpenFileService(),
                    CommonProvider.provideOpener(),
                    ServiceProvider.provideImageRawService());
        }
        return cargarSecuenciaImagenesAction;
    }

    public static GetImageSequenceAction provideImageSequenceAcion() {
        if (getImageSequenceAction == null) {
            getImageSequenceAction = new GetImageSequenceAction(RepositoryProvider.provideImageRepository());
        }
        return getImageSequenceAction;
    }

    public static AplicarContornosActivosEnSecuanciaDeImagesAction provideApplyActiveContourOnImageSequenceAction() {
        if (aplicarContornosActivosEnSecuanciaDeImagesAction == null) {
            aplicarContornosActivosEnSecuanciaDeImagesAction = new AplicarContornosActivosEnSecuanciaDeImagesAction(ActionProvider.provideApplyActiveContourAction());
        }
        return aplicarContornosActivosEnSecuanciaDeImagesAction;
    }

    public static ApplyHarrisDetectorAction provideApplyHarrisDetectorAction() {
        if (applyHarrisDetectorAction == null) {
            applyHarrisDetectorAction = new ApplyHarrisDetectorAction(ServiceProvider.provideMatrixService());
        }
        return applyHarrisDetectorAction;
    }

    public static AplicarDetectorSiftAction provideApplySiftDetectorAction() {
        if (aplicarDetectorSiftAction == null) {
            aplicarDetectorSiftAction = new AplicarDetectorSiftAction();
        }
        return aplicarDetectorSiftAction;
    }
}

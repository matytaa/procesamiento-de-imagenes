package core.provider;

import core.action.channels.ObtainHSVChannelAction;
import core.action.channels.ObtainRGBChannelAction;
import core.action.characteristic_points.ApplyHarrisDetectorAction;
import core.action.characteristic_points.ApplySiftDetectorAction;
import core.action.diffusion.ApplyDiffusionAction;
import core.action.edgedetector.*;
import core.action.edgedetector.hough.CircleHoughTransformAction;
import core.action.edgedetector.hough.LineHoughTransformAction;
import core.action.edit.ModifyPixelAction;
import core.action.edit.space_domain.*;
import core.action.edit.space_domain.operations.MultiplyImagesAction;
import core.action.edit.space_domain.operations.SubstractImagesAction;
import core.action.edit.space_domain.operations.SumImagesAction;
import core.action.figure.CreateImageWithFigureAction;
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
import core.action.threshold.ApplyGlobalThresholdEstimationAction;
import core.action.threshold.ApplyOtsuThresholdEstimationAction;
import core.action.threshold.AplicarUmbralAction;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;


class ActionProvider {

    private static LoadImageAction loadImageAction;
    private static GetImageAction getImageAction;
    private static SaveImageAction saveImageAction;
    private static ModifyPixelAction modifyPixelAction;
    private static PutModifiedImageAction putModifiedImageAction;
    private static GetModifiedImageAction getModifiedImageAction;
    private static CreateImageInformAction createImageInformAction;
    private static ObtainRGBChannelAction obtainRGBChannelAction;
    private static ObtainHSVChannelAction obtainHSVChannelAction;
    private static CreateImageWithFigureAction createImageWithFigureAction;
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
    private static ApplyDirectionalDerivativeOperatorAction applyDirectionalDerivativeOperatorAction;
    private static ApplyGlobalThresholdEstimationAction applyGlobalThresholdEstimationAction;
    private static ApplyOtsuThresholdEstimationAction applyOtsuThresholdEstimationAction;
    private static ApplyLaplacianDetectorAction applyLaplacianDetectorAction;
    private static ApplyDiffusionAction applyDiffusionAction;
    private static UndoChangesAction undoChangesAction;
    private static GetImageLimitValuesAction getImageLimitValuesAction;
    private static ApplyCannyDetectorAction applyCannyDetectorAction;
    private static ApplySusanDetectorAction applySusanDetectorAction;
    private static LineHoughTransformAction lineHoughTransformAction;
    private static CircleHoughTransformAction circleHoughTransformAction;
    private static ApplyActiveContourAction applyActiveContourAction;
    private static CargarSecuenciaImagenesAction cargarSecuenciaImagenesAction;
    private static GetImageSequenceAction getImageSequenceAction;
    private static ApplyActiveContourOnImageSequenceAction applyActiveContourOnImageSequenceAction;
    private static ApplyHarrisDetectorAction applyHarrisDetectorAction;
    private static ApplySiftDetectorAction applySiftDetectorAction;

    public static GetImageAction provideGetImageAction() {
        if (getImageAction == null) {
            getImageAction = new GetImageAction(RepositoryProvider.provideImageRepository());
        }
        return getImageAction;
    }

    public static LoadImageAction provideLoadImageAction() {
        if (loadImageAction == null) {
            loadImageAction = new LoadImageAction(
                    RepositoryProvider.provideImageRepository(),
                    ServiceProvider.provideOpenFileService(),
                    CommonProvider.provideOpener(),
                    ServiceProvider.provideImageRawService());
        }
        return loadImageAction;
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

    public static CreateImageWithFigureAction provideCreateImageWithFigureAction() {
        if (createImageWithFigureAction == null) {
            createImageWithFigureAction = new CreateImageWithFigureAction(
                    ServiceProvider.provideImageFigureService(),
                    RepositoryProvider.provideImageRepository());
        }
        return createImageWithFigureAction;
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

    public static ApplyDirectionalDerivativeOperatorAction provideApplyDerivateDirectionalOperatorAction() {
        if (applyDirectionalDerivativeOperatorAction == null) {
            applyDirectionalDerivativeOperatorAction = new ApplyDirectionalDerivativeOperatorAction(
                    ServiceProvider.provideImageOperationsService(),
                    PublishSubjectProvider.provideOnModifiedImagePublishSubject(),
                    ServiceProvider.provideMatrixService()
            );
        }
        return applyDirectionalDerivativeOperatorAction;
    }

    public static ApplyGlobalThresholdEstimationAction provideApplyGlobalThresholdEstimation() {
        if (applyGlobalThresholdEstimationAction == null) {
            applyGlobalThresholdEstimationAction = new ApplyGlobalThresholdEstimationAction(
                    ServiceProvider.provideMatrixService(),
                    ServiceProvider.provideApplyThresholdService());
        }
        return applyGlobalThresholdEstimationAction;
    }

    public static ApplyOtsuThresholdEstimationAction provideApplyOtsuThresholdEstimation() {
        if (applyOtsuThresholdEstimationAction == null) {
            applyOtsuThresholdEstimationAction = new ApplyOtsuThresholdEstimationAction(
                    ServiceProvider.provideHistogramService(),
                    ServiceProvider.provideMatrixService(),
                    ServiceProvider.provideApplyThresholdService());
        }
        return applyOtsuThresholdEstimationAction;
    }

    public static ApplyLaplacianDetectorAction provideApplyLaplacianDetectorAction() {
        if (applyLaplacianDetectorAction == null) {
            applyLaplacianDetectorAction = new ApplyLaplacianDetectorAction();
        }
        return applyLaplacianDetectorAction;
    }

    public static ApplyDiffusionAction provideApplyDiffusionAction() {
        if (applyDiffusionAction == null) {
            applyDiffusionAction = new ApplyDiffusionAction(
                    ServiceProvider.provideImageOperationsService(),
                    PublishSubjectProvider.provideOnModifiedImagePublishSubject()
            );
        }
        return applyDiffusionAction;
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

    public static ApplyCannyDetectorAction provideApplyCannyDetectorAction() {
        if (applyCannyDetectorAction == null) {
            applyCannyDetectorAction = new ApplyCannyDetectorAction(ServiceProvider.provideImageOperationsService(),
                    ServiceProvider.provideMatrixService());
        }
        return applyCannyDetectorAction;
    }

    public static ApplySusanDetectorAction provideApplySusanDetectorAction() {
        if (applySusanDetectorAction == null) {
            applySusanDetectorAction = new ApplySusanDetectorAction();
        }
        return applySusanDetectorAction;
    }

    public static LineHoughTransformAction provideLineHoughTransformAction() {
        if (lineHoughTransformAction == null) {
            lineHoughTransformAction = new LineHoughTransformAction();
        }
        return lineHoughTransformAction;
    }

    public static CircleHoughTransformAction provideCircleHoughTransformAction() {
        if (circleHoughTransformAction == null) {
            circleHoughTransformAction = new CircleHoughTransformAction();
        }
        return circleHoughTransformAction;
    }

    public static ApplyActiveContourAction provideApplyActiveContourAction() {
        if (applyActiveContourAction == null) {
            applyActiveContourAction = new ApplyActiveContourAction();
        }
        return applyActiveContourAction;
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

    public static ApplyActiveContourOnImageSequenceAction provideApplyActiveContourOnImageSequenceAction() {
        if (applyActiveContourOnImageSequenceAction == null) {
            applyActiveContourOnImageSequenceAction = new ApplyActiveContourOnImageSequenceAction(ActionProvider.provideApplyActiveContourAction());
        }
        return applyActiveContourOnImageSequenceAction;
    }

    public static ApplyHarrisDetectorAction provideApplyHarrisDetectorAction() {
        if (applyHarrisDetectorAction == null) {
            applyHarrisDetectorAction = new ApplyHarrisDetectorAction(ServiceProvider.provideMatrixService());
        }
        return applyHarrisDetectorAction;
    }

    public static ApplySiftDetectorAction provideApplySiftDetectorAction() {
        if (applySiftDetectorAction == null) {
            applySiftDetectorAction = new ApplySiftDetectorAction();
        }
        return applySiftDetectorAction;
    }
}

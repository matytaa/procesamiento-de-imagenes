package core.action.edgedetector;

import core.service.ImageOperationsService;
import core.service.MatrixService;
import domain.customimage.MatrizCanales;
import domain.customimage.Imagen;
import domain.mask.Mask;
import javafx.scene.image.Image;

public class ApplyEdgeDetectorByGradientAction {

    private final ImageOperationsService imageOperationsService;
    private final MatrixService matrixService;

    public ApplyEdgeDetectorByGradientAction(ImageOperationsService imageOperationsService, MatrixService matrixService) {

        this.imageOperationsService = imageOperationsService;
        this.matrixService = matrixService;
    }

    public Image execute(Imagen customImage, Mask xDerivativeMask, Mask yDerivativeMask) {
        //We calculate the partial X and Y derivative matrixes
        MatrizCanales xDerivateChannelMatrix = xDerivativeMask.apply(customImage);
        MatrizCanales yDerivateChannelMatrix = yDerivativeMask.apply(customImage);

        //We calculate the gradient by applying the formulae: sqrt(X^2 + Y^2)
        MatrizCanales xDerivativeSquare = this.imageOperationsService.multiplyChannelMatrixs(xDerivateChannelMatrix, xDerivateChannelMatrix);
        MatrizCanales yDerivativeSquare = this.imageOperationsService.multiplyChannelMatrixs(yDerivateChannelMatrix, yDerivateChannelMatrix);
        MatrizCanales gradientMagnitude = imageOperationsService
                .sqrtChannelMatrixs(imageOperationsService.sumChannelMatrixs(xDerivativeSquare, yDerivativeSquare));

        //The result is normalized and written into an image
        Image resultantImage = this.matrixService
                .toImage(gradientMagnitude.getRedChannel(), gradientMagnitude.getGreenChannel(), gradientMagnitude.getBlueChannel());

        return resultantImage;
    }
}

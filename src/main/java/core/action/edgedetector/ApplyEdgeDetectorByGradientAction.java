package core.action.edgedetector;

import core.service.ImageOperationsService;
import core.service.MatrizService;
import domain.customimage.MatrizCanales;
import domain.customimage.Imagen;
import domain.mask.Mascara;
import javafx.scene.image.Image;

public class ApplyEdgeDetectorByGradientAction {

    private final ImageOperationsService imageOperationsService;
    private final MatrizService matrizService;

    public ApplyEdgeDetectorByGradientAction(ImageOperationsService imageOperationsService, MatrizService matrizService) {

        this.imageOperationsService = imageOperationsService;
        this.matrizService = matrizService;
    }

    public Image execute(Imagen customImage, Mascara xDerivativeMascara, Mascara yDerivativeMascara) {
        //We calculate the partial X and Y derivative matrixes
        MatrizCanales xDerivateChannelMatrix = xDerivativeMascara.apply(customImage);
        MatrizCanales yDerivateChannelMatrix = yDerivativeMascara.apply(customImage);

        //We calculate the gradient by applying the formulae: sqrt(X^2 + Y^2)
        MatrizCanales xDerivativeSquare = this.imageOperationsService.multiplyChannelMatrixs(xDerivateChannelMatrix, xDerivateChannelMatrix);
        MatrizCanales yDerivativeSquare = this.imageOperationsService.multiplyChannelMatrixs(yDerivateChannelMatrix, yDerivateChannelMatrix);
        MatrizCanales gradientMagnitude = imageOperationsService
                .sqrtChannelMatrixs(imageOperationsService.sumChannelMatrixs(xDerivativeSquare, yDerivativeSquare));

        //The result is normalized and written into an image
        Image resultantImage = this.matrizService
                .toImage(gradientMagnitude.getRedChannel(), gradientMagnitude.getGreenChannel(), gradientMagnitude.getBlueChannel());

        return resultantImage;
    }
}

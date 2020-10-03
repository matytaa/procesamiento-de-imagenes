package core.action.diffusion;

import core.service.ImageOperationsService;
import domain.customimage.MatrizCanales;
import domain.customimage.Imagen;
import domain.customimage.RGB;
import domain.diffusion.Derivative;
import domain.diffusion.Diffusion;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;

public class ApplyDiffusionAction {

    private final ImageOperationsService imageOperationsService;
    private final PublishSubject<Image> onModifiedImagePublishSubject;

    public ApplyDiffusionAction(ImageOperationsService imageOperationsService,
                                PublishSubject<Image> onModifiedImagePublishSubject) {
        this.imageOperationsService = imageOperationsService;
        this.onModifiedImagePublishSubject = onModifiedImagePublishSubject;
    }

    public Imagen execute(Imagen customImage, Diffusion diffusion, Integer times) {

        Integer width = customImage.getWidth();
        Integer height = customImage.getHeight();
        MatrizCanales channelMatrix = new MatrizCanales(customImage.getMatrizRed(), customImage.getMatrizGreen(), customImage.getMatrizBlue());

        for (int i = 0; i < times; i++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Derivative redDerivative = new Derivative(channelMatrix.getRedChannel(), x, y);
                    Derivative greenDerivative = new Derivative(channelMatrix.getGreenChannel(), x, y);
                    Derivative blueDerivative = new Derivative(channelMatrix.getBlueChannel(), x, y);

                    int red = diffusion.apply(redDerivative);
                    int green = diffusion.apply(greenDerivative);
                    int blue = diffusion.apply(blueDerivative);
                    RGB value = new RGB(red, green, blue);

                    channelMatrix.setValue(x, y, value);
                }
            }

            channelMatrix = this.imageOperationsService.toValidImageMatrix(channelMatrix);
        }

        Imagen image = new Imagen(channelMatrix, customImage.getFormatString());
        onModifiedImagePublishSubject.onNext(image.toFXImage());

        return image;
    }
}

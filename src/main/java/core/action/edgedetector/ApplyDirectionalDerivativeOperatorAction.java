package core.action.edgedetector;

import core.service.ImageOperationsService;
import core.service.MatrizService;
import domain.customimage.MatrizCanales;
import domain.customimage.Imagen;
import domain.customimage.RGB;
import domain.mask.Mascara;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;

public class ApplyDirectionalDerivativeOperatorAction {

    private final ImageOperationsService imageOperationsService;
    private final PublishSubject<Image> imagePublishSubject;
    private final MatrizService matrizService;

    public ApplyDirectionalDerivativeOperatorAction(ImageOperationsService imageOperationsService,
            PublishSubject<Image> imagePublishSubject, MatrizService matrizService) {

        this.imageOperationsService = imageOperationsService;
        this.imagePublishSubject = imagePublishSubject;
        this.matrizService = matrizService;
    }

    public void execute(Imagen customImage,
                        Mascara horizontalStraightMascara,
                        Mascara verticalStraightMascara,
                        Mascara mainDiagonalMascara,
                        Mascara secondaryDiagonalMascara) {

        MatrizCanales channelMatrix = applyMasks(customImage, horizontalStraightMascara, verticalStraightMascara,
                mainDiagonalMascara, secondaryDiagonalMascara);

        int[][] redChannel = channelMatrix.getRedChannel();
        int[][] greenChannel = channelMatrix.getGreenChannel();
        int[][] blueChannel = channelMatrix.getBlueChannel();
        Image resultantImage = this.matrizService.toImage(redChannel, greenChannel, blueChannel);

        imagePublishSubject.onNext(resultantImage);
    }

    private MatrizCanales applyMasks(Imagen image,
                                     Mascara horizontalStraightMascara, Mascara verticalStraightMascara,
                                     Mascara mainDiagonalMascara, Mascara secondaryDiagonalMascara) {

        Integer width = image.getAncho();
        Integer height = image.getAltura();
        MatrizCanales channelMatrix = new MatrizCanales(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                RGB horizontalRGB = horizontalStraightMascara.aplicarMascaraAPixel(image, x, y);
                RGB verticalRGB = verticalStraightMascara.aplicarMascaraAPixel(image, x, y);
                RGB mainDiagonalRGB = mainDiagonalMascara.aplicarMascaraAPixel(image, x, y);
                RGB secondaryDiagonalRGB = secondaryDiagonalMascara.aplicarMascaraAPixel(image, x, y);

                RGB maxRGB = getMaxRGB(horizontalRGB, verticalRGB, mainDiagonalRGB, secondaryDiagonalRGB);

                channelMatrix.setValue(x, y, maxRGB);
            }
        }

        return this.imageOperationsService.aMatrizValida(channelMatrix);
    }

    private RGB getMaxRGB(RGB horizontalRGB, RGB verticalRGB, RGB mainDiagonalRGB, RGB secondaryDiagonalRGB) {
        int maxRed = Integer
                .max(horizontalRGB.getRed(), Integer.max(verticalRGB.getRed(), Integer.max(mainDiagonalRGB.getRed(), secondaryDiagonalRGB.getRed())));
        int maxGreen = Integer.max(horizontalRGB.getGreen(),
                Integer.max(verticalRGB.getGreen(), Integer.max(mainDiagonalRGB.getGreen(), secondaryDiagonalRGB.getGreen())));
        int maxBlue = Integer.max(horizontalRGB.getBlue(),
                Integer.max(verticalRGB.getBlue(), Integer.max(mainDiagonalRGB.getBlue(), secondaryDiagonalRGB.getBlue())));

        return new RGB(maxRed, maxGreen, maxBlue);
    }
}

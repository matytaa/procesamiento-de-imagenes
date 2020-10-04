package core.action.edgedetector;

import domain.customimage.MatrizCanales;
import domain.customimage.Imagen;
import domain.mask.Mascara;

public class ApplySusanDetectorAction {

    public Imagen execute(Imagen customImage, Mascara mascara) {

        MatrizCanales originalImageMatrix = new MatrizCanales(customImage.getMatrizRed(), customImage.getMatrizBlue(), customImage.getMatrizGreen());
        MatrizCanales maskResult = mascara.apply(customImage);

        for (int i = 0; i < originalImageMatrix.getWidth(); i++) {
            for (int j = 0; j < originalImageMatrix.getHeight(); j++) {

                //edge case --> rojo
                if (maskResult.getRedChannel()[i][j] == 255) {
                    originalImageMatrix.getRedChannel()[i][j] = 255;
                    originalImageMatrix.getGreenChannel()[i][j] = 0;
                    originalImageMatrix.getBlueChannel()[i][j] = 0;
                }

                //corner case --> verde
                if (maskResult.getRedChannel()[i][j] == 150) {
                    originalImageMatrix.getRedChannel()[i][j] = 0;
                    originalImageMatrix.getGreenChannel()[i][j] = 255;
                    originalImageMatrix.getBlueChannel()[i][j] = 0;
                }

            }
        }
        return new Imagen(originalImageMatrix, customImage.getFormatString());
    }
}

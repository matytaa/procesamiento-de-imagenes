package core.action.edgedetector;

import dominio.customimage.MatrizCanales;
import dominio.customimage.Imagen;
import dominio.mask.Mascara;

public class ApplySusanDetectorAction {

    public Imagen execute(Imagen customImage, Mascara mascara) {

        MatrizCanales originalImageMatrix = new MatrizCanales(customImage.getMatrizRed(), customImage.getMatrizBlue(), customImage.getMatrizGreen());
        MatrizCanales maskResult = mascara.aplicar(customImage);

        for (int i = 0; i < originalImageMatrix.getWidth(); i++) {
            for (int j = 0; j < originalImageMatrix.getHeight(); j++) {

                //edge case --> rojo
                if (maskResult.getCanalRojo()[i][j] == 255) {
                    originalImageMatrix.getCanalRojo()[i][j] = 255;
                    originalImageMatrix.getCanalVerde()[i][j] = 0;
                    originalImageMatrix.getCanalAzul()[i][j] = 0;
                }

                //corner case --> verde
                if (maskResult.getCanalRojo()[i][j] == 150) {
                    originalImageMatrix.getCanalRojo()[i][j] = 0;
                    originalImageMatrix.getCanalVerde()[i][j] = 255;
                    originalImageMatrix.getCanalAzul()[i][j] = 0;
                }

            }
        }
        return new Imagen(originalImageMatrix, customImage.getFormatString());
    }
}

package core.action.edgedetector;

import core.service.OperacionesImagenesService;
import core.service.MatrizService;
import domain.customimage.MatrizCanales;
import domain.customimage.Imagen;
import domain.customimage.RGB;
import domain.mask.Mascara;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;

public class ApplyDirectionalDerivativeOperatorAction {

    private final OperacionesImagenesService operacionesImagenesService;
    private final PublishSubject<Image> imagePublishSubject;
    private final MatrizService matrizService;

    public ApplyDirectionalDerivativeOperatorAction(OperacionesImagenesService operacionesImagenesService,
                                                    PublishSubject<Image> imagePublishSubject, MatrizService matrizService) {

        this.operacionesImagenesService = operacionesImagenesService;
        this.imagePublishSubject = imagePublishSubject;
        this.matrizService = matrizService;
    }

    public void execute(Imagen imagen,
                        Mascara mascaraHorizontal,
                        Mascara mascaraVertical,
                        Mascara mascaraDiagonalPrincipal,
                        Mascara mascaraDiagonalSecundaria) {

        MatrizCanales channelMatrix = aplicarMascaras(imagen,
                mascaraHorizontal, mascaraVertical,
                mascaraDiagonalPrincipal, mascaraDiagonalSecundaria);

        int[][] redChannel = channelMatrix.getRedChannel();
        int[][] greenChannel = channelMatrix.getGreenChannel();
        int[][] blueChannel = channelMatrix.getBlueChannel();
        Image resultantImage = this.matrizService.toImage(redChannel, greenChannel, blueChannel);

        imagePublishSubject.onNext(resultantImage);
    }

    private MatrizCanales aplicarMascaras(Imagen imagen,
                                          Mascara mascaraHorizontal,
                                          Mascara mascaraVertical,
                                          Mascara mascaraDiagonalPrincipal,
                                          Mascara mascaraDiagonalSecundaria) {

        Integer ancho = imagen.getAncho();
        Integer alto = imagen.getAltura();
        MatrizCanales matrizDeCanales = new MatrizCanales(ancho, alto);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {

                RGB horizontalRGB = mascaraHorizontal.aplicarMascaraAPixel(imagen, x, y);
                RGB verticalRGB = mascaraVertical.aplicarMascaraAPixel(imagen, x, y);
                RGB diagonalPrincipalRGB = mascaraDiagonalPrincipal.aplicarMascaraAPixel(imagen, x, y);
                RGB diagonalSecundariaRGB = mascaraDiagonalSecundaria.aplicarMascaraAPixel(imagen, x, y);

                RGB maximoRGB = obtenerElMaximoRGB(horizontalRGB, verticalRGB, diagonalPrincipalRGB, diagonalSecundariaRGB);

                matrizDeCanales.setValue(x, y, maximoRGB);
            }
        }

        return this.operacionesImagenesService.aMatrizValida(matrizDeCanales);
    }

    private RGB obtenerElMaximoRGB(RGB horizontalRGB, RGB verticalRGB, RGB diagonalPrincipalRGB, RGB diagonalSecundariaRGB) {
        int maxRed = Integer
                .max(horizontalRGB.getRed(), Integer.max(verticalRGB.getRed(), Integer.max(diagonalPrincipalRGB.getRed(), diagonalSecundariaRGB.getRed())));
        int maxGreen = Integer.max(horizontalRGB.getGreen(),
                Integer.max(verticalRGB.getGreen(), Integer.max(diagonalPrincipalRGB.getGreen(), diagonalSecundariaRGB.getGreen())));
        int maxBlue = Integer.max(horizontalRGB.getBlue(),
                Integer.max(verticalRGB.getBlue(), Integer.max(diagonalPrincipalRGB.getBlue(), diagonalSecundariaRGB.getBlue())));

        return new RGB(maxRed, maxGreen, maxBlue);
    }
}

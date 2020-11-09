package core.action.edgedetector;

import core.service.OperacionesImagenesService;
import core.service.MatrizService;
import dominio.customimage.MatrizCanales;
import dominio.customimage.Imagen;
import dominio.customimage.RGB;
import dominio.mask.Mascara;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;

public class AplicarOperadorDireccionalDerivativoAction {

    private final OperacionesImagenesService operacionesImagenesService;
    private final PublishSubject<Image> imagePublishSubject;
    private final MatrizService matrizService;

    public AplicarOperadorDireccionalDerivativoAction(OperacionesImagenesService operacionesImagenesService,
                                                      PublishSubject<Image> imagePublishSubject, MatrizService matrizService) {

        this.operacionesImagenesService = operacionesImagenesService;
        this.imagePublishSubject = imagePublishSubject;
        this.matrizService = matrizService;
    }

    public void executar(Imagen imagen,
                         Mascara mascaraHorizontal,
                         Mascara mascaraVertical,
                         Mascara mascaraDiagonalPrincipal,
                         Mascara mascaraDiagonalSecundaria) {

        MatrizCanales channelMatrix = aplicarMascaras(imagen,
                mascaraHorizontal, mascaraVertical,
                mascaraDiagonalPrincipal, mascaraDiagonalSecundaria);

        int[][] redChannel = channelMatrix.getCanalRojo();
        int[][] greenChannel = channelMatrix.getCanalVerde();
        int[][] blueChannel = channelMatrix.getCanalAzul();
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
        
        //Máscaras direccionales
        //La dirección ortogonal al gradiente es la dirección de máxima variación
        //Para conseguir esto hay que pasar las 4 máscaras y conseguir el máximo valor de un píxel entre las 4 máscaras.

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

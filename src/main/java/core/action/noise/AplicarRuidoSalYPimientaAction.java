package core.action.noise;

import core.service.statistics.GeneradorDeRandomsService;
import dominio.customimage.Imagen;
import io.reactivex.subjects.PublishSubject;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class AplicarRuidoSalYPimientaAction {

    private final GeneradorDeRandomsService generadorDeRandomsService;
    private final PublishSubject<Image> imagePublishSubject;

    public AplicarRuidoSalYPimientaAction(GeneradorDeRandomsService randomNumberGenerationService,
                                          PublishSubject<Image> imagePublishSubject) {
        this.generadorDeRandomsService = randomNumberGenerationService;
        this.imagePublishSubject = imagePublishSubject;
    }

    public Image aplicar(Imagen imagen, Double porcentajeDePixelesAContaminar, Double p0, Double p1) {

        WritableImage imagenConRuido = SwingFXUtils.toFXImage(imagen.getBufferedImage(), null);
        PixelReader originalPixelReader = imagen.getPixelReader();
        PixelWriter imagenSinRuido = imagenConRuido.getPixelWriter();

        int totalPixels = imagen.getAncho() * imagen.getAltura();

        //CALCULO EL TOTAL DE PIXELES A CONTAMINAR SEGÚN EL PORCENTAJE INGESADO POR EL USUARIO
        Integer totalDePixelesAContaminar = (int) (totalPixels * porcentajeDePixelesAContaminar / 100);

        //VOY APLICANDO EL RUIDO A CADA PIXEL RANDOM QUE SE ELIGIO AL AZAR
        imagen.seleccionarNPiexelesRandom(totalDePixelesAContaminar)
                .forEach(pixel -> aplicarRuido(originalPixelReader, imagenSinRuido, p0 * 100, p1 * 100, pixel.getX(), pixel.getY()));

        imagePublishSubject.onNext(imagenConRuido);

        return imagenConRuido;
    }

    private void aplicarRuido(PixelReader pixelReader, PixelWriter pixelWriter, double p0, double p1, int x, int y) {

        //OBTENGO UN RANDOM SEGUN LA DISTRIBUCION UNIFORME
        int valorRandomDistribucionUniforme = generadorDeRandomsService.segunDistribucionUniforme(0, 100);

        //SI EL RANDOM OBTENIDO ES MAYOR O IGUAL AL PARAMETRO P0, PINTO EL PIXEL DE NEGRO
        if (valorRandomDistribucionUniforme <= p0) {
            pixelWriter.setColor(x, y, Color.rgb(0, 0, 0));
        //SI EL RANDOM OBTENIDO ES MENOR O IGUAL AL PARAMETRO P1, PINTO EL PIXEL DE BLANCO
        } else if (valorRandomDistribucionUniforme >= p1) {
            pixelWriter.setColor(x, y, Color.rgb(255, 255, 255));
        //SI NO SE CUMPLE NINGUNA DE LAS DOS ANTERIORES LO DEJO CON EL MISMO COLOR QUE TENIA
        } else {
            pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
        }
    }
}

package core.action.noise;

import core.repository.RepositorioImagen;
import core.service.OperacionesImagenesService;
import core.service.statistics.GeneradorDeRandomsService;
import domain.customimage.Imagen;
import domain.customimage.Pixel;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.List;
import java.util.function.BiFunction;

public class AplicarRuidoExponencialAction {

    private final RepositorioImagen repositorioImagen;
    private final OperacionesImagenesService operacionesImagenesService;
    private final GeneradorDeRandomsService randomNumberGenerationService;

    public AplicarRuidoExponencialAction(RepositorioImagen repositorioImagen, OperacionesImagenesService operacionesImagenesService, GeneradorDeRandomsService randomNumberGenerationService) {
        this.repositorioImagen = repositorioImagen;
        this.operacionesImagenesService = operacionesImagenesService;
        this.randomNumberGenerationService = randomNumberGenerationService;
    }

    public Image execute(double percent, double lambda) {
        if(!this.repositorioImagen.obtenerImagen().isPresent()) {
            return new WritableImage(100,100);
        }

        Imagen customImage = this.repositorioImagen.obtenerImagen().get();
                int numberOfPixelsToContaminate = (int)(percent * customImage.getPixelQuantity());
        List<Pixel> pixelsToContaminate = customImage.seleccionarNPiexelesRandom(numberOfPixelsToContaminate);

        //GENERO LA MATRIZ, CON N ELEMENTOS CON RUIDO, EL RESTO SIN RUIDO.
        int[][] noiseMatrix = this.generarMatrizDeRuido(lambda, customImage, pixelsToContaminate);

        //MULTIPLICO LA MATRIZ DE RUIDO A LA MATRIZ DE CADA CANAL
        int[][] redChannelValues = this.multiplicarMatrizRuidoPorMatrizCanal(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getRed() * 255));
        int[][] greenChannelValues = this.multiplicarMatrizRuidoPorMatrizCanal(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getGreen() * 255));
        int[][] blueChannelValues = this.multiplicarMatrizRuidoPorMatrizCanal(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getBlue() * 255));

        //AJUSTO LA MATRIZ RESULTANTE
        int[][] adjustedRedChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(redChannelValues, pixelsToContaminate);
        int[][] adjustedGreenChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(greenChannelValues, pixelsToContaminate);
        int[][] adjustedBlueChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(blueChannelValues, pixelsToContaminate);

        //ESCRIBO LA MATRIZ RESULTANTE EN UNA NUEVA IMAGEN
        return this.operacionesImagenesService.escribirNuevosValoresDePixelesEnLaImagen(adjustedRedChannelValues, adjustedGreenChannelValues, adjustedBlueChannelValues);

    }

    private int[][] multiplicarMatrizRuidoPorMatrizCanal(Imagen imagen, int[][] matrizDeRuido, BiFunction<Integer, Integer, Integer> channel) {
        int[][] matrizProducto = new int[imagen.getAncho()][imagen.getAltura()];
        for (int i = 0; i < imagen.getAncho(); i ++) {
            for (int j = 0; j < imagen.getAltura(); j++) {
                matrizProducto[i][j] = channel.apply(i, j) * matrizDeRuido[i][j];
            }
        }
        return matrizProducto;
    }

        private int[][] generarMatrizDeRuido(double lambda, Imagen imagen, List<Pixel> pixelesAContaminar) {
        int[][] matrizDeRuido = new int[imagen.getAncho()][imagen.getAltura()];
        for (int i=0; i < matrizDeRuido.length; i++) {
            for (int j=0; j < matrizDeRuido[i].length; j++) {
                matrizDeRuido[i][j] = 1; //
            }
        }
        for (Pixel pixel : pixelesAContaminar) {
            int i = pixel.getX();
            int j = pixel.getY();
            matrizDeRuido[i][j] = (int) (this.randomNumberGenerationService.generarNumeroExponencial(lambda));
        }
        return matrizDeRuido;
    }

}

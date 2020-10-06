package core.action.noise;

import core.repository.ImagenRepository;
import core.service.OperacionesImagenesService;
import core.service.statistics.GeneradorDeRandomsService;
import domain.customimage.Imagen;
import domain.customimage.Pixel;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.List;
import java.util.function.BiFunction;

public class AplicarRuidoGaussianoAction {

    private final OperacionesImagenesService operacionesImagenesService;
    private final ImagenRepository imagenRepository;
    private final GeneradorDeRandomsService randomNumberGenerationService;

    public AplicarRuidoGaussianoAction(ImagenRepository imagenRepository, OperacionesImagenesService operacionesImagenesService, GeneradorDeRandomsService randomNumberGenerationService) {
        this.imagenRepository = imagenRepository;
        this.operacionesImagenesService = operacionesImagenesService;
        this.randomNumberGenerationService = randomNumberGenerationService;
    }

    public Image execute(double percent, double mu, double sigma) {

        if (!this.imagenRepository.getImagen().isPresent()) {
            return new WritableImage(100, 100);
        }

        Imagen customImage = this.imagenRepository.getImagen().get();
        int numberOfPixelsToContaminate = (int) (percent * customImage.getPixelQuantity());
        List<Pixel> pixelsToContaminate = customImage.seleccionarNPiexelesRandom(numberOfPixelsToContaminate);

        //GENERO MARTRIZ DE N ELEMENTOS CON RUIDO, EL RESTO 0
        int[][] noiseMatrix = this.generarMatrizDeRuido(mu, sigma, customImage, pixelsToContaminate);

        //SUMO LA MATRIZ DE RUIDO CON LA MATRIZ DE CADA CANAL
        int[][] redChannelValues = this.sumarMatrizRuidoYMatrizCanal(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getRed() * 255));
        int[][] greenChannelValues = this.sumarMatrizRuidoYMatrizCanal(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getGreen() * 255));
        int[][] blueChannelValues = this.sumarMatrizRuidoYMatrizCanal(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getBlue() * 255));

        //AJUSTO LA MATRIZ RESULTANTE
        int[][] adjustedRedChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(redChannelValues, pixelsToContaminate);
        int[][] adjustedGreenChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(greenChannelValues, pixelsToContaminate);
        int[][] adjustedBlueChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(blueChannelValues, pixelsToContaminate);

        //ESCRIBO LA MATRIZ RESULTANTE EN UNA NUEVA IMAGEN
        return this.operacionesImagenesService.escribirNuevosValoresDePixelesEnLaImagen(adjustedRedChannelValues, adjustedGreenChannelValues, adjustedBlueChannelValues);
    }

    private int[][] sumarMatrizRuidoYMatrizCanal(Imagen customImage, int[][] noiseMatrix, BiFunction<Integer, Integer, Integer> channel) {
        int[][] sumMatrix = new int[customImage.getAncho()][customImage.getAltura()];
        for (int i = 0; i < customImage.getAncho(); i++) {
            for (int j = 0; j < customImage.getAltura(); j++) {
                sumMatrix[i][j] = channel.apply(i, j) + noiseMatrix[i][j];
            }
        }

        return sumMatrix;
    }

    private int[][] generarMatrizDeRuido(double mu, double sigma, Imagen imagen, List<Pixel> pixelesAContaminar) {
        int[][] matrizDeRuido = new int[imagen.getAncho()][imagen.getAltura()];
        for (int i = 0; i < matrizDeRuido.length; i++) {
            for (int j = 0; j < matrizDeRuido[i].length; j++) {
                matrizDeRuido[i][j] = 0;
            }
        }
        for (Pixel pixel : pixelesAContaminar) {
            int i = pixel.getX();
            int j = pixel.getY();
            matrizDeRuido[i][j] = (int) (this.randomNumberGenerationService.generarNumeroGaussiano(mu, sigma));
        }

        return matrizDeRuido;
    }
}

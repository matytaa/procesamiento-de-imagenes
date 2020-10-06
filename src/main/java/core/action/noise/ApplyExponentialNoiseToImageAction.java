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

public class ApplyExponentialNoiseToImageAction {

    private final ImagenRepository imagenRepository;
    private final OperacionesImagenesService operacionesImagenesService;
    private final GeneradorDeRandomsService randomNumberGenerationService;

    public ApplyExponentialNoiseToImageAction(ImagenRepository imagenRepository, OperacionesImagenesService operacionesImagenesService, GeneradorDeRandomsService randomNumberGenerationService) {
        this.imagenRepository = imagenRepository;
        this.operacionesImagenesService = operacionesImagenesService;
        this.randomNumberGenerationService = randomNumberGenerationService;
    }

    public Image execute(double percent, double lambda) {
        if(!this.imagenRepository.getImagen().isPresent()) {
            return new WritableImage(100,100);
        }

        Imagen customImage = this.imagenRepository.getImagen().get();
                int numberOfPixelsToContaminate = (int)(percent * customImage.getPixelQuantity());
        List<Pixel> pixelsToContaminate = customImage.seleccionarNPiexelesRandom(numberOfPixelsToContaminate);

        //Generate a matrix where N cells contain noise, and the rest contain zeros
        int[][] noiseMatrix = this.generateNoiseMatrix(lambda, customImage, pixelsToContaminate);

        //Now, we multiply the noise matrix and the image and normalize the scale (for each channel)
        int[][] redChannelValues = this.multiplyImageChannelAndNoiseMatrix(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getRed() * 255));
        int[][] greenChannelValues = this.multiplyImageChannelAndNoiseMatrix(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getGreen() * 255));
        int[][] blueChannelValues = this.multiplyImageChannelAndNoiseMatrix(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getBlue() * 255));

        int[][] adjustedRedChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(redChannelValues, pixelsToContaminate);
        int[][] adjustedGreenChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(greenChannelValues, pixelsToContaminate);
        int[][] adjustedBlueChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(blueChannelValues, pixelsToContaminate);

        //Finally, we write the resultant matrix to a new image
        return this.operacionesImagenesService.escribirNuevosValoresDePixelesEnLaImagen(adjustedRedChannelValues, adjustedGreenChannelValues, adjustedBlueChannelValues);

    }

    private int[][] multiplyImageChannelAndNoiseMatrix(Imagen customImage, int[][] noiseMatrix, BiFunction<Integer, Integer, Integer> channel) {
        int[][] productMatrix = new int[customImage.getAncho()][customImage.getAltura()];
        for (int i = 0; i < customImage.getAncho(); i ++) {
            for (int j = 0; j < customImage.getAltura(); j++) {
                productMatrix[i][j] = channel.apply(i, j) * noiseMatrix[i][j];
            }
        }
        return productMatrix;
    }

        private int[][] generateNoiseMatrix(double lambda, Imagen customImage, List<Pixel> pixelsToContaminate) {
        int[][] noiseMatrix = new int[customImage.getAncho()][customImage.getAltura()];
        for (int i=0; i < noiseMatrix.length; i++) {
            for (int j=0; j < noiseMatrix[i].length; j++) {
                noiseMatrix[i][j] = 1; //Since the noise is multiplicative, the not-noise cells must contain 1
            }
        }
        for (Pixel pixel : pixelsToContaminate) {
            int i = pixel.getX();
            int j = pixel.getY();
            noiseMatrix[i][j] = (int) (this.randomNumberGenerationService.generarNumeroExponencial(lambda));
        }
        return noiseMatrix;
    }

}

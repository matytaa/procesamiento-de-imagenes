package core.action.noise;

import core.repository.ImageRepository;
import core.service.ImageOperationsService;
import core.service.statistics.GeneradorDeRandoms;
import domain.customimage.Imagen;
import domain.customimage.Pixel;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.List;
import java.util.function.BiFunction;

public class ApplyGaussianNoiseToImageAction {

    private final ImageOperationsService imageOperationsService;
    private final ImageRepository imageRepository;
    private final GeneradorDeRandoms randomNumberGenerationService;

    public ApplyGaussianNoiseToImageAction(ImageRepository imageRepository, ImageOperationsService imageOperationsService, GeneradorDeRandoms randomNumberGenerationService) {
        this.imageRepository = imageRepository;
        this.imageOperationsService = imageOperationsService;
        this.randomNumberGenerationService = randomNumberGenerationService;
    }

    public Image execute(double percent, double mu, double sigma) {

        if (!this.imageRepository.getImage().isPresent()) {
            return new WritableImage(100, 100);
        }

        Imagen customImage = this.imageRepository.getImage().get();
        int numberOfPixelsToContaminate = (int) (percent * customImage.getPixelQuantity());
        List<Pixel> pixelsToContaminate = customImage.seleccionarNPiexelesRandom(numberOfPixelsToContaminate);

        //Generate a matrix where N cells contain noise, and the rest contain zeros
        int[][] noiseMatrix = this.generateNoiseMatrix(mu, sigma, customImage, pixelsToContaminate);

        //Now, we sum the noise matrix to the image and normalize the scale (for each channel)
        int[][] redChannelValues = this.sumImageChannelAndNoiseMatrixes(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getRed() * 255));
        int[][] greenChannelValues = this.sumImageChannelAndNoiseMatrixes(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getGreen() * 255));
        int[][] blueChannelValues = this.sumImageChannelAndNoiseMatrixes(customImage, noiseMatrix, (i, j) -> (int) (customImage.getPixelReader().getColor(i, j).getBlue() * 255));

        int[][] adjustedRedChannelValues = this.imageOperationsService.convertirAImagenContaminadaValida(redChannelValues, pixelsToContaminate);
        int[][] adjustedGreenChannelValues = this.imageOperationsService.convertirAImagenContaminadaValida(greenChannelValues, pixelsToContaminate);
        int[][] adjustedBlueChannelValues = this.imageOperationsService.convertirAImagenContaminadaValida(blueChannelValues, pixelsToContaminate);

        //Finally, we write the resultant matrix to a new image
        return this.imageOperationsService.escribirNuevosValoresDePixelesEnLaImagen(adjustedRedChannelValues, adjustedGreenChannelValues, adjustedBlueChannelValues);
    }

    private int[][] sumImageChannelAndNoiseMatrixes(Imagen customImage, int[][] noiseMatrix, BiFunction<Integer, Integer, Integer> channel) {
        int[][] sumMatrix = new int[customImage.getAncho()][customImage.getAltura()];
        for (int i = 0; i < customImage.getAncho(); i++) {
            for (int j = 0; j < customImage.getAltura(); j++) {
                sumMatrix[i][j] = channel.apply(i, j) + noiseMatrix[i][j];
            }
        }

        return sumMatrix;
    }

    private int[][] generateNoiseMatrix(double mu, double sigma, Imagen customImage, List<Pixel> pixelsToContaminate) {
        int[][] noiseMatrix = new int[customImage.getAncho()][customImage.getAltura()];
        for (int i = 0; i < noiseMatrix.length; i++) {
            for (int j = 0; j < noiseMatrix[i].length; j++) {
                noiseMatrix[i][j] = 0;
            }
        }
        for (Pixel pixel : pixelsToContaminate) {
            int i = pixel.getX();
            int j = pixel.getY();
            noiseMatrix[i][j] = (int) (this.randomNumberGenerationService.generarNumeroGaussiano(mu, sigma));
        }

        return noiseMatrix;
    }
}

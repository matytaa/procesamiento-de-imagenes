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

public class ApplyRayleighNoiseToImageAction {

    private final ImageRepository imageRepository;
    private final ImageOperationsService imageOperationsService;
    private final GeneradorDeRandoms randomNumberGenerationService;

    public ApplyRayleighNoiseToImageAction(ImageRepository imageRepository, ImageOperationsService imageOperationsService, GeneradorDeRandoms randomNumberGenerationService) {
        this.imageRepository = imageRepository;
        this.imageOperationsService = imageOperationsService;
        this.randomNumberGenerationService = randomNumberGenerationService;
    }

    public Image execute(double percent, double psi) {

        if(!this.imageRepository.getImage().isPresent()) {
            return new WritableImage(100,100);
        }

        Imagen imagen = this.imageRepository.getImage().get();
        int numberOfPixelsToContaminate = (int)(percent * imagen.getPixelQuantity());
        List<Pixel> pixelsToContaminate = imagen.seleccionarNPiexelesRandom(numberOfPixelsToContaminate);

        //Generate a matrix where N cells contain noise, and the rest contain zeros
        int[][] noiseMatrix = this.generateNoiseMatrix(psi, imagen, pixelsToContaminate);

        //Now, we sum the noise matrix to the image and normalize the scale (for each channel)
        int[][] redChannelValues = this.multiplyImageChannelAndNoiseMatrix(imagen, noiseMatrix, (i, j) -> (int) (imagen.getPixelReader().getColor(i, j).getRed() * 255));
        int[][] greenChannelValues = this.multiplyImageChannelAndNoiseMatrix(imagen, noiseMatrix, (i, j) -> (int) (imagen.getPixelReader().getColor(i, j).getGreen() * 255));
        int[][] blueChannelValues = this.multiplyImageChannelAndNoiseMatrix(imagen, noiseMatrix, (i, j) -> (int) (imagen.getPixelReader().getColor(i, j).getBlue() * 255));

        //Now, we multiply the noise matrix and the image and normalize the scale
        int[][] adjustedRedChannelValues = this.imageOperationsService.convertirAImagenContaminadaValida(redChannelValues, pixelsToContaminate);
        int[][] adjustedGreenChannelValues = this.imageOperationsService.convertirAImagenContaminadaValida(greenChannelValues, pixelsToContaminate);
        int[][] adjustedBlueChannelValues = this.imageOperationsService.convertirAImagenContaminadaValida(blueChannelValues, pixelsToContaminate);

        //Finally, we write the resultant matrix to a new image
        return this.imageOperationsService.escribirNuevosValoresDePixelesEnLaImagen(adjustedRedChannelValues, adjustedGreenChannelValues, adjustedBlueChannelValues);

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

    private int[][] generateNoiseMatrix(double psi, Imagen customImage, List<Pixel> pixelesAContaminar) {
        int[][] matrizDeRuido = new int[customImage.getAncho()][customImage.getAltura()];
        for (int i=0; i < matrizDeRuido.length; i++) {
            for (int j=0; j < matrizDeRuido[i].length; j++) {
                matrizDeRuido[i][j] = 1; //COMO ES MULTIPLICATIVO LOS PIXELES SIN RIUDO TIENEN QUE VALER 1
            }
        }
        for (Pixel pixel : pixelesAContaminar) {
            int i = pixel.getX();
            int j = pixel.getY();
            matrizDeRuido[i][j] = (int) (this.randomNumberGenerationService.generarNumeroRayleigh(psi));
        }
        return matrizDeRuido;
    }

}

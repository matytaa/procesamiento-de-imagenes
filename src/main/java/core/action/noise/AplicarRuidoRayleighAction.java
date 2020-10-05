package core.action.noise;

import core.repository.ImageRepository;
import core.service.OperacionesImagenesService;
import core.service.statistics.GeneradorDeRandomsService;
import domain.customimage.Imagen;
import domain.customimage.Pixel;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.List;
import java.util.function.BiFunction;

public class AplicarRuidoRayleighAction {

    private final ImageRepository imageRepository;
    private final OperacionesImagenesService operacionesImagenesService;
    private final GeneradorDeRandomsService randomNumberGenerationService;

    public AplicarRuidoRayleighAction(ImageRepository imageRepository, OperacionesImagenesService operacionesImagenesService, GeneradorDeRandomsService randomNumberGenerationService) {
        this.imageRepository = imageRepository;
        this.operacionesImagenesService = operacionesImagenesService;
        this.randomNumberGenerationService = randomNumberGenerationService;
    }

    public Image execute(double percent, double psi) {

        if(!this.imageRepository.getImage().isPresent()) {
            return new WritableImage(100,100);
        }

        Imagen imagen = this.imageRepository.getImage().get();
        int cantidadDePixelesAContaminar = (int)(percent * imagen.getPixelQuantity());
        List<Pixel> pixelesAContaminar = imagen.seleccionarNPiexelesRandom(cantidadDePixelesAContaminar);

        //GENERO LA MATRIZ, CON N ELEMENTOS CON RUIDO, EL RESTO SIN RUIDO.
        int[][] matrizDeRuido = this.generarMatrizDeRuido(psi, imagen, pixelesAContaminar);

        //MULTIPLICO LA MATRIZ DE RUIDO A LA MATRIZ DE CADA CANAL
        int[][] canalRed = this.multiplicarMatrizRuidoPorMatrizCanal(imagen, matrizDeRuido, (i, j) -> (int) (imagen.getPixelReader().getColor(i, j).getRed() * 255));
        int[][] canalGreen = this.multiplicarMatrizRuidoPorMatrizCanal(imagen, matrizDeRuido, (i, j) -> (int) (imagen.getPixelReader().getColor(i, j).getGreen() * 255));
        int[][] canalBlue = this.multiplicarMatrizRuidoPorMatrizCanal(imagen, matrizDeRuido, (i, j) -> (int) (imagen.getPixelReader().getColor(i, j).getBlue() * 255));

        //AJUSTO LA MATRIZ RESULTANTE
        int[][] adjustedRedChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(canalRed, pixelesAContaminar);
        int[][] adjustedGreenChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(canalGreen, pixelesAContaminar);
        int[][] adjustedBlueChannelValues = this.operacionesImagenesService.convertirAImagenContaminadaValida(canalBlue, pixelesAContaminar);

        //ESCRIBO LA MATRIZ RESULTANTE EN UNA NUEVA IMAGEN
        return this.operacionesImagenesService.escribirNuevosValoresDePixelesEnLaImagen(adjustedRedChannelValues, adjustedGreenChannelValues, adjustedBlueChannelValues);

    }

    private int[][] multiplicarMatrizRuidoPorMatrizCanal(Imagen imagen, int[][] matrizDeRuido, BiFunction<Integer, Integer, Integer> canal) {
        int[][] matrizProducto = new int[imagen.getAncho()][imagen.getAltura()];
        for (int i = 0; i < imagen.getAncho(); i ++) {
            for (int j = 0; j < imagen.getAltura(); j++) {
                matrizProducto[i][j] = canal.apply(i, j) * matrizDeRuido[i][j];
            }
        }
        return matrizProducto;

    }

    private int[][] generarMatrizDeRuido(double psi, Imagen imagen, List<Pixel> pixelesAContaminar) {
        int[][] matrizDeRuido = new int[imagen.getAncho()][imagen.getAltura()];
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

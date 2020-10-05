package core.action.noise.generator;

import core.service.OperacionesImagenesService;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class GenerarImagenRuidoSinteticoAction {

    private final OperacionesImagenesService operacionesImagenesService;

    public GenerarImagenRuidoSinteticoAction(OperacionesImagenesService operacionesImagenesService) {
        this.operacionesImagenesService = operacionesImagenesService;
    }

    public Image execute(int[][] matrizNumerosRandom) {

        //CREO LA MATRIZ Y LA AJUSTO PARA QUE VAYA DE 0 A 255
        int[][] matrizPixeles = this.operacionesImagenesService.ajustarEscalaDeGrises(this.operacionesImagenesService.desplazarValoresDeLosPixelesHaciaCero((matrizNumerosRandom)));

        //CREO IMAGEN EN BLANCO DE 100 x 100
        WritableImage imagen = new WritableImage(100, 100);
        PixelWriter writer = imagen.getPixelWriter();

        //ASIGNO LOS COLORES A CADA PIXEL DE LA IMAGEN EN BLANCO
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                int nivelDeGris = matrizPixeles[i][j];
                writer.setColor(i, j, Color.rgb(nivelDeGris, nivelDeGris, nivelDeGris));
            }
        }

        return imagen;
    }

}

package core.action.edgedetector;

import core.service.OperacionesImagenesService;
import core.service.MatrizService;
import domain.customimage.MatrizCanales;
import domain.customimage.Imagen;
import domain.mask.Mascara;
import javafx.scene.image.Image;

public class AplicarDetectorDeBordesAction {

    private final OperacionesImagenesService operacionesImagenesService;
    private final MatrizService matrizService;

    public AplicarDetectorDeBordesAction(OperacionesImagenesService operacionesImagenesService, MatrizService matrizService) {

        this.operacionesImagenesService = operacionesImagenesService;
        this.matrizService = matrizService;
    }

    public Image ejecutar(Imagen imagen, Mascara mascaraDerivativaX, Mascara mascaraDerivativaY) {
        //Se calcula la derivadas con la máscaras
        MatrizCanales matrizCanalesDerivadaX = mascaraDerivativaX.apply(imagen);
        MatrizCanales matrizCanalesDerivadaY = mascaraDerivativaY.apply(imagen);

        //Calculamos el gradiente usando: sqrt(X^2 + Y^2)
        MatrizCanales derivadaAlCuadradoX = this.operacionesImagenesService.multiplyChannelMatrixs(matrizCanalesDerivadaX, matrizCanalesDerivadaX);
        MatrizCanales derivadaAlCuadradoY = this.operacionesImagenesService.multiplyChannelMatrixs(matrizCanalesDerivadaY, matrizCanalesDerivadaY);
        MatrizCanales gradiente = operacionesImagenesService
                .cuadradoMatrizCanal(operacionesImagenesService.sumarMatrizCanales(derivadaAlCuadradoX, derivadaAlCuadradoY));

        //Se normaliza y se devuelve la imagen resultante
        Image imagenResultante = this.matrizService
                .toImage(gradiente.getRedChannel(), gradiente.getGreenChannel(), gradiente.getBlueChannel());

        return imagenResultante;
    }

    public Image ejecutar(Imagen imagen, Mascara mascaraDerivativa) {
        //Se calcula la derivada con la máscara
        MatrizCanales matrizCanalesDerivada = this.operacionesImagenesService.aMatrizValida(mascaraDerivativa.apply(imagen));

        //Se normaliza y se devuelve la imagen resultante
        Image imagenResultante = this.matrizService
                .toImage(matrizCanalesDerivada.getRedChannel(), matrizCanalesDerivada.getGreenChannel(), matrizCanalesDerivada.getBlueChannel());

        return imagenResultante;
    }
}

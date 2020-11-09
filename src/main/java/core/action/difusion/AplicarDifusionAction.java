package core.action.difusion;

import core.service.OperacionesImagenesService;
import dominio.customimage.MatrizCanales;
import dominio.customimage.Imagen;
import dominio.customimage.RGB;
import dominio.difusion.Derivada;
import dominio.difusion.Difusion;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;

public class AplicarDifusionAction {

    private final OperacionesImagenesService operacionesImagenesService;
    private final PublishSubject<Image> onModifiedImagePublishSubject;

    public AplicarDifusionAction(OperacionesImagenesService operacionesImagenesService,
                                 PublishSubject<Image> onModifiedImagePublishSubject) {
        this.operacionesImagenesService = operacionesImagenesService;
        this.onModifiedImagePublishSubject = onModifiedImagePublishSubject;
    }

    public Imagen ejecutar(Imagen imagen, Difusion difusion, Integer iteraciones) {

        Integer ancho = imagen.getAncho();
        Integer alto = imagen.getAltura();
        MatrizCanales matrizCanales = new MatrizCanales(imagen.getMatrizRed(), imagen.getMatrizGreen(), imagen.getMatrizBlue());

        for (int i = 0; i < iteraciones; i++) {
            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    Derivada redDerivada = new Derivada(matrizCanales.getCanalRojo(), x, y);
                    Derivada greenDerivada = new Derivada(matrizCanales.getCanalVerde(), x, y);
                    Derivada blueDerivada = new Derivada(matrizCanales.getCanalAzul(), x, y);

                    int red = difusion.aplicar(redDerivada);
                    int green = difusion.aplicar(greenDerivada);
                    int blue = difusion.aplicar(blueDerivada);
                    RGB value = new RGB(red, green, blue);

                    matrizCanales.setValue(x, y, value);
                }
            }

            matrizCanales = this.operacionesImagenesService.aMatrizValida(matrizCanales);
        }

        Imagen image = new Imagen(matrizCanales, imagen.getFormatString());
        onModifiedImagePublishSubject.onNext(image.toFXImage());

        return image;
    }
}

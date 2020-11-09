package core.action.difusion;

import core.service.OperacionesImagenesService;
import dominio.customimage.MatrizCanales;
import dominio.customimage.Imagen;
import dominio.customimage.RGB;
import dominio.difusion.Derivativo;
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
                    Derivativo redDerivativo = new Derivativo(matrizCanales.getCanalRojo(), x, y);
                    Derivativo greenDerivativo = new Derivativo(matrizCanales.getCanalVerde(), x, y);
                    Derivativo blueDerivativo = new Derivativo(matrizCanales.getCanalAzul(), x, y);

                    int red = difusion.aplicar(redDerivativo);
                    int green = difusion.aplicar(greenDerivativo);
                    int blue = difusion.aplicar(blueDerivativo);
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

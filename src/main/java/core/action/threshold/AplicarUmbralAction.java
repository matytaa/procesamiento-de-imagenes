package core.action.threshold;

import core.service.AplicarUmbralService;
import dominio.customimage.Imagen;
import javafx.scene.image.Image;


public class AplicarUmbralAction {

    private final AplicarUmbralService aplicarUmbralService;

    public AplicarUmbralAction(AplicarUmbralService aplicarUmbralService) {
        this.aplicarUmbralService = aplicarUmbralService;
    }

    public Image ejecutar(Imagen imagen, int umbral) {
        return this.aplicarUmbralService.aplicarUmbral(imagen, umbral);
    }

}

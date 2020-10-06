package core.action.threshold;

import core.service.ApplyThresholdService;
import domain.customimage.Imagen;
import javafx.scene.image.Image;


public class AplicarUmbralAction {

    private final ApplyThresholdService applyThresholdService;

    public AplicarUmbralAction(ApplyThresholdService applyThresholdService) {
        this.applyThresholdService = applyThresholdService;
    }

    public Image execute(Imagen imagen, int umbral) {
        return this.applyThresholdService.aplicarUmbral(imagen, umbral);
    }

}

package core.action.threshold;

import core.service.ApplyThresholdService;
import domain.customimage.Imagen;
import javafx.scene.image.Image;


public class ApplyThresholdAction {

    private final ApplyThresholdService applyThresholdService;

    public ApplyThresholdAction(ApplyThresholdService applyThresholdService) {
        this.applyThresholdService = applyThresholdService;
    }

    public Image execute(Imagen customImage, int threshold) {
        return this.applyThresholdService.applyThreshold(customImage, threshold);
    }

}

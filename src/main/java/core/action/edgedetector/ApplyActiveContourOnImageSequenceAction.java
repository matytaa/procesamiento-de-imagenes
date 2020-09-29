package core.action.edgedetector;

import domain.activecontour.ActiveContour;
import domain.activecontour.ContourCustomImage;
import domain.customimage.Imagen;

import java.util.ArrayList;
import java.util.List;

public class ApplyActiveContourOnImageSequenceAction {

    private final ApplyActiveContourAction applyActiveContourAction;

    public ApplyActiveContourOnImageSequenceAction(ApplyActiveContourAction applyActiveContourAction) {
        this.applyActiveContourAction = applyActiveContourAction;
    }

    public List<ContourCustomImage> execute(List<Imagen> customImages, ActiveContour activeContour, int steps, double epsilon) {

        List<ContourCustomImage> contourCustomImages = new ArrayList<>();

        Imagen first = customImages.get(0);
        ContourCustomImage contourCustomImage = applyActiveContourAction.execute(first, activeContour, steps, epsilon);
        contourCustomImages.add(contourCustomImage);

        List<Imagen> list = new ArrayList<>(customImages);
        list.remove(0);

        for (Imagen customImage : list) {
            ActiveContour previousActiveContour = ActiveContour.copy(contourCustomImage.getActiveContour());
            contourCustomImage = applyActiveContourAction.execute(customImage, previousActiveContour, steps, epsilon);
            contourCustomImages.add(contourCustomImage);
        }

        return contourCustomImages;
    }

}

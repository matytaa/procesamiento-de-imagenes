package presentation.presenter;

import core.action.characteristic_points.ApplySiftDetectorAction;
import core.action.image.LoadImageAction;
import domain.customimage.Imagen;
import domain.sift.SiftResult;
import org.openimaj.image.DisplayUtilities;
import presentation.controller.SiftSceneController;
import presentation.util.ShowResultPopup;

public class SiftPresenter {

    private final SiftSceneController view;
    private final LoadImageAction loadImageAction;
    private final ApplySiftDetectorAction applySiftDetectorAction;
    private Imagen image1;
    private Imagen image2;

    public SiftPresenter(SiftSceneController siftSceneController, LoadImageAction loadImageAction, ApplySiftDetectorAction applySiftDetectorAction) {
        this.view = siftSceneController;
        this.loadImageAction = loadImageAction;
        this.applySiftDetectorAction = applySiftDetectorAction;
    }

    public void onSelectImage1() {
        this.image1 = this.loadImageAction.execute();
        this.view.imageView1.setImage(this.image1.toFXImage());
    }

    public void onSelectImage2() {
        this.image2 = this.loadImageAction.execute();
        this.view.imageView2.setImage(this.image2.toFXImage());
    }

    public void onApply() {
        SiftResult siftResult = this.applySiftDetectorAction.execute(this.image1, this.image2);
        DisplayUtilities.display(siftResult.getConsistentMatches());
        ShowResultPopup.show("Sift",
                "Image 1 descriptors " + String.valueOf(siftResult.getQueryKeypointsQuantity()) + "\n"
                        + "Image 2 descriptors: " + String.valueOf(siftResult.getTargetKeypointsQuantity()) + "\n"
                        + "Matches: " + String.valueOf(siftResult.getMatchesQuantity()));
    }
}

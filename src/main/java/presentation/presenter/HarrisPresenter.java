package presentation.presenter;

import core.action.characteristic_points.ApplyHarrisDetectorAction;
import core.action.image.LoadImageAction;
import dominio.PuntoXY;
import dominio.customimage.Imagen;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import presentation.controller.HarrisSceneController;
import presentation.util.InsertValuePopup;

import java.text.DecimalFormat;
import java.util.List;

public class HarrisPresenter {

    private final HarrisSceneController view;
    private final LoadImageAction loadImageAction;
    private final ApplyHarrisDetectorAction applyHarrisDetectorAction;
    private Imagen image1;
    private Imagen image2;

    public HarrisPresenter(HarrisSceneController harrisSceneController, LoadImageAction loadImageAction,
            ApplyHarrisDetectorAction applyHarrisDetectorAction) {
        this.view = harrisSceneController;
        this.loadImageAction = loadImageAction;
        this.applyHarrisDetectorAction = applyHarrisDetectorAction;
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

        if (bothImagesArePresent()) {

            double tolerance = Double.parseDouble(InsertValuePopup.show("Insert maximum-percent for filtering fake corners", "0").get());

            List<PuntoXY> image1Corners = this.applyHarrisDetectorAction.execute(this.image1, tolerance);
            List<PuntoXY> image2Corners = this.applyHarrisDetectorAction.execute(this.image2, tolerance);

            this.markImage1Corners(image1Corners);
            this.markImage2Corners(image2Corners);

            int largerSize = Math.max(image1Corners.size(), image2Corners.size());
            int shorterSize = Math.min(image1Corners.size(), image2Corners.size());

            String diffPercentString = new DecimalFormat("#.##").format((largerSize - shorterSize) * 100 / (double) largerSize);
            this.view.resultLabel.setText("Images differ on a " + diffPercentString + "% of characteristic points");

        }
    }

    private void markImage1Corners(List<PuntoXY> image1Corners) {

        WritableImage image = new WritableImage(image1.getAncho(), image1.getAltura());
        PixelWriter writer = image.getPixelWriter();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                PuntoXY puntoXy = new PuntoXY(i, j);
                if (image1Corners.contains(puntoXy)) {
                    this.markPoint(writer, i, j);
                } else {
                    writer.setColor(i, j, image1.getColor(i, j));
                }

            }
        }
        this.view.imageView1.setImage(image);
    }

    private void markImage2Corners(List<PuntoXY> image2Corners) {
        WritableImage image = new WritableImage(image2.getAncho(), image2.getAltura());
        PixelWriter writer = image.getPixelWriter();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                PuntoXY puntoXy = new PuntoXY(i, j);
                if (image2Corners.contains(puntoXy)) {
                    this.markPoint(writer, i, j);
                } else {
                    writer.setColor(i, j, image2.getColor(i, j));
                }

            }
        }

        this.view.imageView2.setImage(image);
    }

    private void markPoint(PixelWriter writer, int i, int j) {
        //TODO: Estaria bueno que aca pintara un circulo rojo que sea mas visible que los puntintos chotos
        writer.setColor(i, j, Color.RED);
    }

    private boolean bothImagesArePresent() {
        return this.image1 != null && this.image2 != null;
    }
}

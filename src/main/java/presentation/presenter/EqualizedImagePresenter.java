package presentation.presenter;

import core.action.histogram.CreateImageHistogramAction;
import core.action.histogram.EqualizeGrayImageAction;
import core.action.histogram.utils.EqualizedTimes;
import core.action.image.ObtenerImagenAction;
import dominio.Histograma;
import dominio.customimage.Imagen;
import io.reactivex.subjects.PublishSubject;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import presentation.controller.EqualizedImageSceneController;

public class EqualizedImagePresenter {

    private final EqualizedImageSceneController view;
    private final EqualizeGrayImageAction equalizeGrayImageAction;
    private final PublishSubject<Image> imagePublishSubject;
    private final ObtenerImagenAction obtenerImagenAction;
    private final CreateImageHistogramAction createImageHistogramAction;

    public EqualizedImagePresenter(EqualizedImageSceneController view,
                                   ObtenerImagenAction obtenerImagenAction,
                                   EqualizeGrayImageAction equalizeGrayImageAction,
                                   CreateImageHistogramAction createImageHistogramAction,
                                   PublishSubject<Image> imagePublishSubject) {
        this.view = view;
        this.obtenerImagenAction = obtenerImagenAction;
        this.equalizeGrayImageAction = equalizeGrayImageAction;
        this.createImageHistogramAction = createImageHistogramAction;
        this.imagePublishSubject = imagePublishSubject;
    }

    public void onInitializeView() {

        imagePublishSubject.subscribe(image -> view.equalizedImageView.setImage(image));

        obtenerImagenAction.ejecutar().ifPresent(imagenAEcualizar -> {
            Image image = equalizeGrayImageAction.execute(imagenAEcualizar, EqualizedTimes.getValue());
            Histograma histogram = createImageHistogramAction.execute(new Imagen(image, "png"));
            setHistogramData(histogram.getValores());
        });
    }

    private void setHistogramData(double[] value) {
        for (int i = 0; i < value.length; i++) {
            view.data.getData().add(createNode(String.valueOf(i), value[i]));
        }
        view.barChart.getData().add(view.data);
    }

    private XYChart.Data createNode(String x, Double y) {
        XYChart.Data chartData = new XYChart.Data(x, y);
        ChangeListener<Node> nodeChangeListener = (ov, oldNode, newNode) -> newNode.setStyle("-fx-bar-fill: black;");
        chartData.nodeProperty().addListener(nodeChangeListener);
        return chartData;
    }

}

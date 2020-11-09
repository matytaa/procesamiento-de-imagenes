package presentation.presenter.space_domain_operations;

import core.action.histogram.CreateImageHistogramAction;
import core.action.image.ObtenerImagenAction;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import presentation.controller.ImageHistogramSceneController;

public class ImageHistogramPresenter {

    private final ImageHistogramSceneController view;
    private final ObtenerImagenAction obtenerImagenAction;
    private final CreateImageHistogramAction createImageHistogramAction;

    public ImageHistogramPresenter(ImageHistogramSceneController view,
                                   ObtenerImagenAction obtenerImagenAction,
                                   CreateImageHistogramAction createImageHistogramAction) {

        this.view = view;
        this.obtenerImagenAction = obtenerImagenAction;
        this.createImageHistogramAction = createImageHistogramAction;
    }

    public void initialize() {
        this.obtenerImagenAction.ejecutar()
                .ifPresent(customImage -> this.setData(this.createImageHistogramAction.execute(customImage).getValores()));
    }

    private void setData(double[] value) {
        for (int i = 0; i < value.length; i++) {
            view.data.getData().add(createNode(String.valueOf(i), value[i]));
        }
        view.barChart.getData().add(view.data);
    }

    private XYChart.Data createNode(String x, Double y) {
        XYChart.Data chartData = new XYChart.Data(x, y);
        chartData.nodeProperty().addListener((ChangeListener<Node>) (ov, oldNode, newNode) -> newNode.setStyle("-fx-bar-fill: black;"));
        return chartData;
    }
}

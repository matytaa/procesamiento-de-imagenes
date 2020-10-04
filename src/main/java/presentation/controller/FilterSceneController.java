package presentation.controller;

import core.provider.PresenterProvider;
import domain.SemaforoFiltro;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presentation.presenter.FilterPresenter;

public class FilterSceneController {

    private final FilterPresenter filterPresenter;

    @FXML
    public TextField textField;
    @FXML
    public Label label;

    public FilterSceneController() {
        this.filterPresenter = PresenterProvider.provideFilterPresenter(this);
    }

    @FXML
    public void initialize() {
        update();
    }

    public void update() {

        switch(SemaforoFiltro.getValue()) {

            case MEDIA:
                label.setText("Tamaño de la máscara (Impar)");
                break;

            case MEDIANA:
                label.setText("Tamaño de la máscara (Impar)");
                break;

            case MEDIANA_PONDERADA:
                label.setText("OTamaño de la máscara fijo 3x3");
                textField.setDisable(true);
                break;

            case GAUSSIANO:
                label.setText("Desviación Estandar");
                break;

        }

    }

    @FXML
    public void aplicar() {
        this.filterPresenter.onAplicarFiltro();
    }

    public void closeWindow() {
        Stage stage = (Stage) this.textField.getScene().getWindow();
        stage.close();
    }
}

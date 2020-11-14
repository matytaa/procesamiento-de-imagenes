package presentation.controller;

import core.provider.PresenterProvider;
import dominio.SemaforoFiltro;
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
    public TextField textField2;
    @FXML
    public Label label;
    @FXML
    public Label label2;

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
                textField2.setDisable(true);
                break;

            case MEDIANA:
                label.setText("Tamaño de la máscara");
                textField2.setDisable(true);
                break;

            case MEDIANA_PONDERADA:
                label.setText("Tamaño de la máscara fijo 3x3");
                textField.setDisable(true);
                textField2.setDisable(true);
                break;

            case GAUSSIANO:
                label.setText("Desviación Estandar");
                textField2.setDisable(true);
                break;

            case BILATERAL:
                label.setText("Desviación Estandar S");
                label2.setText("Desviación Estandar R");
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

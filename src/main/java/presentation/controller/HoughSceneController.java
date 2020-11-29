package presentation.controller;

import core.provider.PresenterProvider;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presentation.presenter.HoughPresenter;

public class HoughSceneController {

    private final HoughPresenter houghPresenter;
    @FXML
    public RadioButton lineaRadioButton;
    @FXML
    public RadioButton circuloRadioButton;
    @FXML
    public TextField toleranciaTextField;

    @FXML
    public TextField thetaTextField;
    @FXML
    public TextField rhoTextField;

    @FXML
    public TextField circuloXTextField;
    @FXML
    public TextField circuloYTextField;
    @FXML
    public TextField circuloRadioTextField;

    public HoughSceneController() {
        this.houghPresenter = PresenterProvider.provideHoughPresenter(this);
    }

    @FXML
    public void initialize() {
        this.deshabilitarInputsCirculo();
        this.deshabilitarInputsLinea();
    }

    private void habilitarInputsLinea() {
        this.rhoTextField.setDisable(false);
        this.thetaTextField.setDisable(false);
    }

    private void deshabilitarInputsLinea() {
        this.rhoTextField.setDisable(true);
        this.thetaTextField.setDisable(true);
    }

    private void habilitarInputsCirculo() {
        this.circuloXTextField.setDisable(false);
        this.circuloYTextField.setDisable(false);
        this.circuloRadioTextField.setDisable(false);
    }

    private void deshabilitarInputsCirculo() {
        this.circuloXTextField.setDisable(true);
        this.circuloYTextField.setDisable(true);
        this.circuloRadioTextField.setDisable(true);
    }

    @FXML
    public void linea() {
        this.habilitarInputsLinea();
        this.deshabilitarInputsCirculo();
    }

    @FXML
    public void circulo() {
        this.habilitarInputsCirculo();
        this.deshabilitarInputsLinea();
    }

    @FXML
    public void aplicar() {
        this.houghPresenter.aplicar();
    }

    public void closeWindow() {
        Stage stage = (Stage) this.thetaTextField.getScene().getWindow();
        stage.close();
    }
}

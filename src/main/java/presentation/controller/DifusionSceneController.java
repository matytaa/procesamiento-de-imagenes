package presentation.controller;

import core.provider.PresenterProvider;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presentation.presenter.DifusionPresenter;

public class DifusionSceneController {

    private final DifusionPresenter difusionPresenter;

    @FXML
    public RadioButton isotropica;
    @FXML
    public RadioButton anisotropica;
    @FXML
    public RadioButton lorentz;
    @FXML
    public RadioButton leclerc;
    @FXML
    public TextField iteracionesTextField;
    @FXML
    public TextField sigmaTextField;

    public DifusionSceneController() {
        this.difusionPresenter = PresenterProvider.provideDiffusionPresenter(this);
    }

    public void initialize() {
        deshabilitarAnisotropica();
        if (anisotropica.isSelected()) {
            this.onAnisotropicaSelection();
        }
    }

    @FXML
    public void onIsotropicaSelection() {
        this.deshabilitarAnisotropica();
    }

    @FXML
    public void onAnisotropicaSelection() {
        this.deshabilitarIsotropica();
    }

    private void deshabilitarIsotropica() {
        lorentz.setDisable(false);
        leclerc.setDisable(false);
        sigmaTextField.setDisable(false);
        sigmaTextField.setDisable(false);
    }

    @FXML
    public void aplicar() {
        this.difusionPresenter.aplicarDifusion();
    }

    private void deshabilitarAnisotropica() {
        lorentz.setDisable(true);
        leclerc.setDisable(true);
        sigmaTextField.setDisable(true);
        sigmaTextField.setDisable(true);
        lorentz.selectedProperty().setValue(false);
        leclerc.selectedProperty().setValue(false);
    }

    public void closeWindow() {
        Stage stage = (Stage) this.sigmaTextField.getScene().getWindow();
        stage.close();
    }

    public boolean isIsotropicSeleccionada() {
        return isotropica.isSelected();
    }

    public boolean isAnisotropicSeleccionada() {
        return anisotropica.isSelected();
    }

    public boolean isLorentzSeleccionado() {
        return lorentz.isSelected();
    }

    public boolean isLeclercSeleccionado() {
        return leclerc.isSelected();
    }

    public double getSigma() {
        return Double.parseDouble(sigmaTextField.getText());
    }

    public int getIteraciones() {
        return Integer.parseInt(iteracionesTextField.getText());
    }
}

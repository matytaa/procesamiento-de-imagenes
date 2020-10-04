package presentation.controller;

import core.provider.PresenterProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presentation.presenter.RuidoSalYPimientaPresenter;

public class SaltAndPepperNoiseController {

    private final RuidoSalYPimientaPresenter ruidoSalYPimientaPresenter;

    @FXML
    public TextField p0Field;
    @FXML
    public TextField p1Field;
    @FXML
    public TextField percentField;
    @FXML
    public Label p0ValidationLabel;
    @FXML
    public Label p1ValidationLabel;
    @FXML
    public Label percentValidationLabel;

    public SaltAndPepperNoiseController() {
        this.ruidoSalYPimientaPresenter = PresenterProvider.provideSaltAndPepperNoisePresenter(this);
    }

    @FXML
    public void initialize() {
        this.ruidoSalYPimientaPresenter.onInitializeView();
    }

    @FXML
    public void aplicarRuido() {
        this.ruidoSalYPimientaPresenter.onAplicarRuido();
    }

    public void closeWindow() {
        Stage stage = (Stage) this.p0Field.getScene().getWindow();
        stage.close();
    }
}

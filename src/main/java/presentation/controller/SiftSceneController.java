package presentation.controller;

import core.provider.PresenterProvider;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import presentation.presenter.SiftPresenter;

public class SiftSceneController {

    @FXML
    public ImageView imageView1;

    @FXML
    public ImageView imageView2;

    private SiftPresenter siftPresenter;

    public SiftSceneController() {
        this.siftPresenter = PresenterProvider.provideSiftPresenter(this);
    }


    @FXML
    public void aplicar() {
        this.siftPresenter.aplicar();
    }

    @FXML
    public void seleccionarImagen1() {
        this.siftPresenter.seleccionarImagen1();
    }

    @FXML
    public void seleccionarImagen2() {
        this.siftPresenter.seleccionarImagen2();
    }
}

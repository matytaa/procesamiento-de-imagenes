package presentation.presenter.random_generators;

import core.action.noise.AplicarRuidoGaussianoAction;
import core.action.noise.generator.GenerarImagenRuidoSinteticoAction;
import core.semaphore.SemaforosGeneradoresDeRandoms;
import core.service.statistics.GeneradorDeRandomsService;
import dominio.RandomElement;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;
import presentation.controller.GaussianSceneController;
import presentation.scenecreator.NoiseImageSceneCreator;
import presentation.util.InsertValuePopup;
import presentation.util.ShowResultPopup;

public class GaussianScenePresenter {

    private final GaussianSceneController view;
    private final GeneradorDeRandomsService generadorDeRandomsService;
    private final GenerarImagenRuidoSinteticoAction generarImagenRuidoSinteticoAction;
    private final PublishSubject<Image> onNoiseImage;
    private final AplicarRuidoGaussianoAction aplicarRuidoGaussianoAction;
    private final PublishSubject<Image> onModifiedImage;

    public GaussianScenePresenter(GaussianSceneController gaussianSceneController, GeneradorDeRandomsService generadorDeRandomsService, GenerarImagenRuidoSinteticoAction generarImagenRuidoSinteticoAction, PublishSubject<Image> imagePublishSubject, AplicarRuidoGaussianoAction aplicarRuidoGaussianoAction, PublishSubject<Image> onModifiedImage) {
        this.view = gaussianSceneController;
        this.generadorDeRandomsService = generadorDeRandomsService;
        this.generarImagenRuidoSinteticoAction = generarImagenRuidoSinteticoAction;
        this.onNoiseImage = imagePublishSubject;
        this.aplicarRuidoGaussianoAction = aplicarRuidoGaussianoAction;
        this.onModifiedImage = onModifiedImage;

    }

    public void onGenerate() {

        double mu = Double.parseDouble(this.view.muTextField.getText());
        double sigma = Double.parseDouble(this.view.sigmaTextField.getText());

        if (isSigmaValido(sigma)) {

            if (SemaforosGeneradoresDeRandoms.getValue() == RandomElement.NUMERO) {

                double number = this.generadorDeRandomsService.generarNumeroGaussiano(mu, sigma);
                this.mostrarNumero(number);
                this.view.closeWindow();

            } else if (SemaforosGeneradoresDeRandoms.getValue() == RandomElement.IMAGEN_SINTETICA_RUIDO){

                int randomNumberMatrix[][] = this.generadorDeRandomsService.generarMatrizRandomGaussianos(100, 100, mu, sigma);
                Image image = this.generarImagenRuidoSinteticoAction.execute(randomNumberMatrix);
                this.enviarImagenAUnaVentanaNueva(image);
                this.view.closeWindow();

            } else { //Noise generator to apply to an existing image
                double percent = (Double.parseDouble(InsertValuePopup.show("Porcentaje de contaminación", "0").get()))/100.00;
                Image image = this.aplicarRuidoGaussianoAction.execute(percent, mu, sigma);
                this.onModifiedImage.onNext(image);
                this.view.closeWindow();
            }
        }
    }

    private void enviarImagenAUnaVentanaNueva(Image image) {
        new NoiseImageSceneCreator().createScene();
        onNoiseImage.onNext(image);
    }

    private boolean isSigmaValido(double sigma) {
        return sigma > 0;
    }

    private void mostrarNumero(double number) {
        ShowResultPopup.show("Generación de número random Gaussiano", "Número generado: " + number);

    }

}

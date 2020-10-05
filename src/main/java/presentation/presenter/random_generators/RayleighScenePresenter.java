package presentation.presenter.random_generators;

import core.action.noise.AplicarRuidoRayleighAction;
import core.action.noise.generator.GenerarImagenRuidoSinteticoAction;
import core.semaphore.SemaforosGeneradoresDeRandoms;
import core.service.statistics.GeneradorDeRandomsService;
import domain.RandomElement;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;
import presentation.controller.RayleighSceneController;
import presentation.scenecreator.NoiseImageSceneCreator;
import presentation.util.InsertValuePopup;
import presentation.util.ShowResultPopup;

public class RayleighScenePresenter {


    private final RayleighSceneController view;
    private final GeneradorDeRandomsService randomNumberGenerationService;
    private final GenerarImagenRuidoSinteticoAction generarImagenRuidoSinteticoAction;
    private final PublishSubject<Image> onNoiseImage;
    private final AplicarRuidoRayleighAction aplicarRuidoRayleighAction;
    private final PublishSubject<Image> onModifiedImage;

    public RayleighScenePresenter(RayleighSceneController rayleighSceneController, GeneradorDeRandomsService randomNumberGenerationService, GenerarImagenRuidoSinteticoAction generarImagenRuidoSinteticoAction, PublishSubject<Image> imagePublishSubject, AplicarRuidoRayleighAction aplicarRuidoRayleighAction, PublishSubject<Image> onModifiedImage) {
        this.view = rayleighSceneController;
        this.randomNumberGenerationService = randomNumberGenerationService;
        this.generarImagenRuidoSinteticoAction = generarImagenRuidoSinteticoAction;
        this.onNoiseImage = imagePublishSubject;
        this.aplicarRuidoRayleighAction = aplicarRuidoRayleighAction;
        this.onModifiedImage = onModifiedImage;
    }

    public void onGenerate() {

        double psi = Double.parseDouble(this.view.psiTextField.getText());

        if (SemaforosGeneradoresDeRandoms.getValue() == RandomElement.NUMERO) {

            double numbero = this.randomNumberGenerationService.generarNumeroRayleigh(psi);
            this.mostrarNumero(numbero);
            this.view.closeWindow();

        } else if (SemaforosGeneradoresDeRandoms.getValue() == RandomElement.IMAGEN_SINTETICA_RUIDO){

            int matrizNumerosRandom[][] = this.randomNumberGenerationService.generarMatrizRandomRayleigh(100,100, psi);
            Image image = this.generarImagenRuidoSinteticoAction.execute(matrizNumerosRandom);
            this.enviarImagenAUnaVentanaNueva(image);
            this.view.closeWindow();

        } else { //GENERADOR DE RUIDO PARA UNA IMAGEN EXISTENTE
            double percent = (Double.parseDouble(InsertValuePopup.show("Porcentaje de contaminación", "0").get()))/100.00;
            Image image = this.aplicarRuidoRayleighAction.execute(percent, psi);
            this.onModifiedImage.onNext(image);
            this.view.closeWindow();
        }
    }

    private void enviarImagenAUnaVentanaNueva(Image image) {
        new NoiseImageSceneCreator().createScene();
        onNoiseImage.onNext(image);
    }

    private void mostrarNumero(double number) {
        ShowResultPopup.show("Generación número random Rayleigh", "Número generado: " + number);
    }

}

package presentation.presenter.random_generators;

import core.action.noise.ApplyExponentialNoiseToImageAction;
import core.action.noise.generator.GenerarImagenRuidoSinteticoAction;
import core.semaphore.SemaforosGeneradoresDeRandoms;
import core.service.statistics.GeneradorDeRandomsService;
import domain.RandomElement;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;
import presentation.controller.ExponentialSceneController;
import presentation.scenecreator.NoiseImageSceneCreator;
import presentation.util.InsertValuePopup;
import presentation.util.ShowResultPopup;

public class ExponentialScenePresenter {

    private final GeneradorDeRandomsService generadorDeRandomsService;
    private final ExponentialSceneController view;
    private final GenerarImagenRuidoSinteticoAction generarImagenRuidoSinteticoAction;
    private final PublishSubject<Image> onNoiseImage;
    private final ApplyExponentialNoiseToImageAction applyExponentialNoiseToImageAction;
    private final PublishSubject<Image> onModifiedImage;

    public ExponentialScenePresenter(ExponentialSceneController exponentialSceneController, GeneradorDeRandomsService generadorDeRandomsService, GenerarImagenRuidoSinteticoAction generarImagenRuidoSinteticoAction, PublishSubject<Image> noiseImagePublishSubject, ApplyExponentialNoiseToImageAction applyExponentialNoiseToImageAction, PublishSubject<Image> onModifiedImage) {
        this.view = exponentialSceneController;
        this.generadorDeRandomsService = generadorDeRandomsService;
        this.generarImagenRuidoSinteticoAction = generarImagenRuidoSinteticoAction;
        this.onNoiseImage = noiseImagePublishSubject;
        this.applyExponentialNoiseToImageAction = applyExponentialNoiseToImageAction;
        this.onModifiedImage = onModifiedImage;
    }

    public void onGenerate() {
        
        double lambda = Double.parseDouble(this.view.lambdaTextField.getText());

        if (isLambdaValida(lambda)) {

            if (SemaforosGeneradoresDeRandoms.getValue() == RandomElement.NUMBER) {

                double number = this.generadorDeRandomsService.generarNumeroExponencial(lambda);
                this.mostrarNumero(number);
                this.view.closeWindow();

            } else if (SemaforosGeneradoresDeRandoms.getValue() == RandomElement.SYNTHETIC_NOISE_IMAGE){

                int matrizRandom[][] = this.generadorDeRandomsService.generarMatrizRandomExponencial(100, 100, lambda);
                Image image = this.generarImagenRuidoSinteticoAction.execute(matrizRandom);
                this.enviarImagenAUnaVentanaNueva(image);
                this.view.closeWindow();

            } else { //GENERADOR DE RUIDO PARA UNA IMAGEN EXISTENTE
                double percent = (Double.parseDouble(InsertValuePopup.show("Porcentaje de contaminación", "0").get()))/100.00;
                Image image = this.applyExponentialNoiseToImageAction.execute(percent, lambda);
                this.onModifiedImage.onNext(image);
                this.view.closeWindow();
            }
        }
    }

    public void enviarImagenAUnaVentanaNueva(Image image) {
        new NoiseImageSceneCreator().createScene();
        onNoiseImage.onNext(image);
    }

    private void mostrarNumero(double number) {
        ShowResultPopup.show("Generación de número random exponencial", "Número generado: " + number);
    }

    private boolean isLambdaValida(double lambda) {
        return lambda > 0;
    }
}

package presentation.presenter.space_domain_operations;

import core.action.edit.space_domain.FuncionGammaAction;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;
import presentation.controller.GammaSceneController;

public class GammaScenePresenter {

    private static final String EMPTY = "";
    private final GammaSceneController view;
    private PublishSubject<Image> modifiedImagePublishSubject;
    private FuncionGammaAction funcionGammaAction;


    public GammaScenePresenter(GammaSceneController gammaSceneController, FuncionGammaAction funcionGammaAction, PublishSubject<Image> imagePublishSubject) {
        this.view = gammaSceneController;
        this.modifiedImagePublishSubject = imagePublishSubject;
        this.funcionGammaAction = funcionGammaAction;
    }

    public void onAplicarFuncion() {
        
        if (esUnGammaValido()) {
            double gamma = Double.parseDouble(this.view.gammaTextField.getText());
            this.sendModifiedImageToMainView(this.funcionGammaAction.ejecutar(gamma));
            this.view.closeWindow();
        }
        
    }

    private boolean esUnGammaValido() {
        String text = this.view.gammaTextField.getText();
        if (text.equals(EMPTY)) return false;
        else if ((Double.parseDouble(text) == 0) || (Double.parseDouble(text) == 1) || (Double.parseDouble(text) > 2) ) return false;
        else return true;
    }

    private void sendModifiedImageToMainView(Image image) {
        this.modifiedImagePublishSubject.onNext(image);
    }
}

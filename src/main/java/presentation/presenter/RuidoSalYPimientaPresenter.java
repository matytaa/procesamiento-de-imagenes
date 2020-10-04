package presentation.presenter;

import core.action.image.GetImageAction;
import core.action.noise.AplicarRuidoSalYPimientaAction;
import domain.customimage.Imagen;
import presentation.controller.SaltAndPepperNoiseController;

import java.util.Optional;

public class RuidoSalYPimientaPresenter {

    private static final String EMPTY = "";
    private final SaltAndPepperNoiseController view;
    private final GetImageAction getImageAction;
    private final AplicarRuidoSalYPimientaAction aplicarRuidoSalYPimientaAction;

    public RuidoSalYPimientaPresenter(
            SaltAndPepperNoiseController view,
            GetImageAction getImageAction,
            AplicarRuidoSalYPimientaAction applySaltAndPepperNoiseAction) {

        this.view = view;
        this.getImageAction = getImageAction;
        this.aplicarRuidoSalYPimientaAction = applySaltAndPepperNoiseAction;
    }

    public void onInitializeView() {
        view.p0ValidationLabel.setText("P0 tiene que ser < 1");
        view.p1ValidationLabel.setText("P1 tiene que ser mayor o igual P0 ");
        view.percentValidationLabel.setText("Porcentaje (0 a 100)");
    }

    public void onAplicarRuido() {

        Optional<Imagen> imagenOpcional = getImageAction.execute();

        if (!imagenOpcional.isPresent()) {
            view.closeWindow();
            return;
        }

        Imagen imagen = imagenOpcional.get();

        if (validarCampos()) {
            Double porcentajeAContaminar = Double.parseDouble(view.percentField.getText());
            Double p0 = Double.parseDouble(view.p0Field.getText());
            Double p1 = Double.parseDouble(view.p1Field.getText());

            if (porcentajeAContaminar >= 0 && porcentajeAContaminar <= 100) {

                if (p0 < 1 && p1 < 1 && (p0 + p1 == 1)) {

                    if (p1 >= p0) {

                        aplicarRuidoSalYPimientaAction.aplicar(imagen, porcentajeAContaminar, p0, p1);
                        view.closeWindow();

                    } else {
                        view.p0ValidationLabel.setText("Error: p1 tiene que ser > p0");
                    }

                } else if (p0 > 1){
                    view.p0ValidationLabel.setText("Error: p0 tiene que ser < 1");
                } else if (p1 > 1){
                    view.p0ValidationLabel.setText("Error: p1 tiene que ser < 1");
                } else if (p0 + p1 != 1){
                    view.p0ValidationLabel.setText("Error: p0 + p1 tienen que ser = 1");
                }

            } else {
                view.p0ValidationLabel.setText("Error: porcentaje inválido");
            }
        }
    }

    private boolean validarCampos() {
        return !view.p0Field.getText().equals(EMPTY) &&
                !view.p1Field.getText().equals(EMPTY) &&
                !view.percentField.getText().equals(EMPTY);
    }
}

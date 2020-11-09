package presentation.presenter;

import core.action.difusion.AplicarDifusionAction;
import core.action.image.ObtenerImagenAction;
import dominio.difusion.DifusionAnisotropicaConLeclerc;
import dominio.difusion.DifusionAnisotropicaConLorentz;
import dominio.difusion.Difusion;
import dominio.difusion.DifusionIsotropica;
import presentation.controller.DifusionSceneController;

public class DifusionPresenter {

    private final DifusionSceneController view;
    private final ObtenerImagenAction obtenerImagenAction;
    private final AplicarDifusionAction aplicarDifusionAction;

    public DifusionPresenter(DifusionSceneController difusionSceneController, ObtenerImagenAction obtenerImagenAction,
                             AplicarDifusionAction aplicarDifusionAction) {

        this.view = difusionSceneController;
        this.obtenerImagenAction = obtenerImagenAction;
        this.aplicarDifusionAction = aplicarDifusionAction;
    }

    public void aplicarDifusion() {

        if (nroIteracionesValido(view.getIteraciones())) {

            Difusion difusion;
            if (view.isIsotropicSeleccionada()) {

                difusion = crearDifusionIsotropica();
                this.ejecutarDifusion(difusion);
                this.view.closeWindow();

            } else if (view.isAnisotropicSeleccionada()) {

                if (sigmaValido(view.getSigma())) {

                    if (view.isLeclercSeleccionado()) {
                        difusion = crearDifusionAnisotropicaLeclerc(view.getSigma());
                    } else if (view.isLorentzSeleccionado()) {
                        difusion = crearDifusionAnisotropicaLorentz(view.getSigma());
                    } else {
                        throw new RuntimeException("Falta seleccionar opción");
                    }

                    this.ejecutarDifusion(difusion);
                    this.view.closeWindow();
                }
            }
        }
    }

    private void ejecutarDifusion(Difusion difusion) {
        this.obtenerImagenAction.ejecutar()
                           .ifPresent(imagen -> {
                               this.aplicarDifusionAction.ejecutar(imagen, difusion, view.getIteraciones());
                           });
    }

    private Difusion crearDifusionAnisotropicaLorentz(double sigma) {
        return new DifusionAnisotropicaConLorentz(sigma);
    }

    private Difusion crearDifusionAnisotropicaLeclerc(double sigma) {
        return new DifusionAnisotropicaConLeclerc(sigma);
    }

    private Difusion crearDifusionIsotropica() {
        return new DifusionIsotropica();
    }

    private boolean sigmaValido(double sigma) {
        return sigma > 0;
    }

    private boolean nroIteracionesValido(int iteraciones) {
        return iteraciones > 0;
    }
}

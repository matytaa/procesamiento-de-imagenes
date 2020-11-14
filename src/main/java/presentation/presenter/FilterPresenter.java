package presentation.presenter;

import core.action.filter.AplicarFiltroAction;
import core.action.image.ObtenerImagenAction;
import dominio.SemaforoFiltro;
import dominio.mask.Mascara;
import dominio.mask.filter.*;
import presentation.controller.FilterSceneController;

public class FilterPresenter {

    private final FilterSceneController view;
    private final ObtenerImagenAction obtenerImagenAction;
    private final AplicarFiltroAction aplicarFiltroAction;

    public FilterPresenter(FilterSceneController view,
                           ObtenerImagenAction obtenerImagenAction,
                           AplicarFiltroAction aplicarFiltroAction) {
        this.view = view;
        this.obtenerImagenAction = obtenerImagenAction;
        this.aplicarFiltroAction = aplicarFiltroAction;
    }

    public void onAplicarFiltro() {
        if (SemaforoFiltro.is(Mascara.Tipo.MEDIA)) {
            this.aplicarFiltroMedia();
        }

        if (SemaforoFiltro.is(Mascara.Tipo.MEDIANA)) {
            this.aplicarFiltroMediana();
        }

        if (SemaforoFiltro.is(Mascara.Tipo.MEDIANA_PONDERADA)) {
            this.aplicarFiltroMedianaPonderada();
        }

        if (SemaforoFiltro.is(Mascara.Tipo.GAUSSIANO)) {
            this.aplicarFiltroGaussiano();
        }

        if (SemaforoFiltro.is(Mascara.Tipo.BILATERAL)) {
            this.aplicarFiltroBilateral();
        }

        view.closeWindow();
    }

    private void aplicarFiltroMedia() {
        int tamanio = Integer.parseInt(view.textField.getText());
        if (isImpar(tamanio)) return;
        aplicarConMascara(new MascaraMedia(tamanio));
    }

    private void aplicarFiltroMediana() {
        int size = Integer.parseInt(view.textField.getText());
        aplicarConMascara(new MascaraMediana(size));
    }

    private void aplicarFiltroMedianaPonderada() {
        aplicarConMascara(new MascaraMedianaPonderada());
    }

    private void aplicarFiltroGaussiano() {
        int desviacionEstandar = Integer.parseInt(view.textField.getText());
        aplicarConMascara(new MascaraGaussiana(desviacionEstandar));
    }

    private void aplicarFiltroBilateral() {
        int desviacionEstandarS = Integer.parseInt(view.textField.getText());
        int desviacionEstandarR = Integer.parseInt(view.textField2.getText());
        aplicarConMascara(new MascaraBilateral(desviacionEstandarS, desviacionEstandarR));
    }

    private void aplicarConMascara(Mascara mascara) {
        this.obtenerImagenAction.ejecutar()
                .ifPresent(imagen -> {
                    aplicarFiltroAction.aplicar(imagen, mascara);
                    view.closeWindow();
                });
    }

    private boolean isImpar(int tamanio) {
        return tamanio % 2 == 0;
    }
}

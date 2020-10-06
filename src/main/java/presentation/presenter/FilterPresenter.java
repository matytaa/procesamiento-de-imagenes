package presentation.presenter;

import core.action.filter.AplicarFiltroAction;
import core.action.image.GetImageAction;
import domain.SemaforoFiltro;
import domain.mask.Mascara;
import domain.mask.filter.MascaraGaussiana;
import domain.mask.filter.MascaraMedia;
import domain.mask.filter.MascaraMediana;
import domain.mask.filter.MascaraMedianaPonderada;
import presentation.controller.FilterSceneController;

public class FilterPresenter {

    private final FilterSceneController view;
    private final GetImageAction getImageAction;
    private final AplicarFiltroAction aplicarFiltroAction;

    public FilterPresenter(FilterSceneController view,
                           GetImageAction getImageAction,
                           AplicarFiltroAction aplicarFiltroAction) {
        this.view = view;
        this.getImageAction = getImageAction;
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

    private void aplicarConMascara(Mascara mascara) {
        this.getImageAction.execute()
                .ifPresent(imagen -> {
                    aplicarFiltroAction.execute(imagen, mascara);
                    view.closeWindow();
                });
    }

    private boolean isImpar(int tamanio) {
        return tamanio % 2 == 0;
    }
}

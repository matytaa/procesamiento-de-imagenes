package core.action.histogram;

import core.service.generation.HistogramaService;
import dominio.Histograma;
import dominio.customimage.Imagen;

public class CreateImageHistogramAction {

    private HistogramaService histogramService;

    public CreateImageHistogramAction(HistogramaService histogramService) {
        this.histogramService = histogramService;
    }

    public Histograma execute(Imagen customImage) {
        return histogramService.crear(customImage);
    }
}

package core.action.histogram;

import core.service.generation.HistogramaService;
import domain.Histograma;
import domain.customimage.Imagen;

public class CreateImageHistogramAction {

    private HistogramaService histogramService;

    public CreateImageHistogramAction(HistogramaService histogramService) {
        this.histogramService = histogramService;
    }

    public Histograma execute(Imagen customImage) {
        return histogramService.crear(customImage);
    }
}

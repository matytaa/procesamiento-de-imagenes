package core.action.histogram;

import core.repository.ImagenRepository;
import core.service.generation.HistogramaService;
import domain.Histograma;
import domain.customimage.Imagen;
import io.reactivex.subjects.PublishSubject;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class EqualizeGrayImageAction {

    private final HistogramaService histogramService;
    private final ImagenRepository imagenRepository;
    private final PublishSubject<Image> imagePublishSubject;

    public EqualizeGrayImageAction(HistogramaService histogramService,
                                   ImagenRepository imagenRepository,
                                   PublishSubject<Image> imagePublishSubject) {
        this.histogramService = histogramService;
        this.imagenRepository = imagenRepository;
        this.imagePublishSubject = imagePublishSubject;
    }

    public Image execute(Imagen imagenAEcualizar, int cantidadDeVecesQueSeEjecutaLaEcualización) {

        Image equalizedImage = recursivo(imagenAEcualizar, cantidadDeVecesQueSeEjecutaLaEcualización);

        this.imagePublishSubject.onNext(equalizedImage);

        return equalizedImage;
    }

    private Image recursivo(Imagen imagenAEcualizar, int cantidadDeVecesQueSeEjecutaLaEcualización) {

        Histograma histogram = this.histogramService.crear(imagenAEcualizar).getAbsoluto();
        Image imagenEcualizada = ecualizarImagen(imagenAEcualizar, histogram);

        cantidadDeVecesQueSeEjecutaLaEcualización--;

        if (cantidadDeVecesQueSeEjecutaLaEcualización == 0) return imagenEcualizada;

        return recursivo(new Imagen(imagenEcualizada, imagenAEcualizar.getFormatString()), cantidadDeVecesQueSeEjecutaLaEcualización);
    }

    private Image ecualizarImagen(Imagen imagenAEcualizar, Histograma histogram) {
        WritableImage imagen = new WritableImage(imagenAEcualizar.getAncho(), imagenAEcualizar.getAltura());
        PixelWriter pixelWriter = imagen.getPixelWriter();

        for (int i = 0; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {

                Double probabilidad = funcionDeDistribucionAcumuladaPorPixel(imagenAEcualizar, histogram, i, j);
                Double sMin = histogram.getValorMinimo();

                Integer transformacion = aplicarTransformacionANivelDeGris(probabilidad, sMin);

                Color escalaEnGrises = Color.rgb(transformacion, transformacion, transformacion);

                pixelWriter.setColor(i, j, escalaEnGrises);
            }
        }

        Imagen updated = new Imagen(SwingFXUtils.fromFXImage(imagen, null), imagenAEcualizar.getFormatString());
        this.imagenRepository.salvarImagenModificada(updated);

        return imagen;
    }

    private Double funcionDeDistribucionAcumuladaPorPixel(Imagen imagenAEcualizar, Histograma histogram, int x, int y) {
        Double valorAcumulado = 0.0;
        Integer promedioPixel = imagenAEcualizar.getPromedioPixel(x, y);
        for (int indice = 0; indice <= promedioPixel; indice++) {
            valorAcumulado += histogram.getValores()[indice];
        }

        return valorAcumulado / histogram.getTotalPixeles();
    }

    private Integer aplicarTransformacionANivelDeGris(Double sK, Double sMin) {
        return (int) (255 * (((sK - sMin) / (1 - sMin))));
    }

}

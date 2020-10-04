package core.action.histogram;

import core.repository.ImageRepository;
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
    private final ImageRepository imageRepository;
    private final PublishSubject<Image> imagePublishSubject;

    public EqualizeGrayImageAction(HistogramaService histogramService,
                                   ImageRepository imageRepository,
                                   PublishSubject<Image> imagePublishSubject) {
        this.histogramService = histogramService;
        this.imageRepository = imageRepository;
        this.imagePublishSubject = imagePublishSubject;
    }

    public Image execute(Imagen customImage, int times) {

        Image equalizedImage = recursive(customImage, times);

        this.imagePublishSubject.onNext(equalizedImage);

        return equalizedImage;
    }

    private Image recursive(Imagen customImage, int times) {

        Histograma histogram = this.histogramService.crear(customImage);
        Image equalizedImage = equalizeImage(customImage, histogram);

        times--;

        if (times == 0) return equalizedImage;

        return recursive(new Imagen(equalizedImage, customImage.getFormatString()), times);
    }

    private Image equalizeImage(Imagen customImage, Histograma histogram) {
        WritableImage image = new WritableImage(customImage.getAncho(), customImage.getAltura());
        PixelWriter pixelWriter = image.getPixelWriter();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                Double sK = cumulativeProbability(customImage, histogram, i, j);
                Double sMin = histogram.getValorMinimo();
                Integer sHat = applyTransform(sK, sMin);

                Color greyValue = Color.rgb(sHat, sHat, sHat);

                pixelWriter.setColor(i, j, greyValue);
            }
        }

        Imagen updated = new Imagen(SwingFXUtils.fromFXImage(image, null), customImage.getFormatString());
        this.imageRepository.saveModifiedImage(updated);

        return image;
    }

    private Double cumulativeProbability(Imagen customImage, Histograma histogram, int x, int y) {
        Double value = 0.0;
        Integer limit = customImage.getPromedioPixel(x, y);
        for (int i1 = 0; i1 <= limit; i1++) {
            value += histogram.getValores()[i1];
        }

        return value / histogram.getTotalPixeles();
    }

    private Integer applyTransform(Double s, Double smin) {
        return (int) (255 * (((s - smin) / (1 - smin))));
    }

}

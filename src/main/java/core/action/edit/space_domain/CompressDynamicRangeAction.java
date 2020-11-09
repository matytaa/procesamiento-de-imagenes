package core.action.edit.space_domain;

import core.repository.RepositorioImagen;
import core.service.statistics.GrayLevelStatisticsService;
import dominio.customimage.Imagen;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Optional;

public class CompressDynamicRangeAction {

    private final GrayLevelStatisticsService grayLevelStatisticsService;
    private final RepositorioImagen repositorioImagen;

    public CompressDynamicRangeAction(GrayLevelStatisticsService grayLevelStatisticsService, RepositorioImagen repositorioImagen) {
        this.grayLevelStatisticsService = grayLevelStatisticsService;
        this.repositorioImagen = repositorioImagen;
    }

    public Image execute() {

        Optional<Imagen> optional = this.repositorioImagen.obtenerImagen();
        if (!optional.isPresent()) {
            return new WritableImage(300, 300);
        }

        Image image = SwingFXUtils.toFXImage(optional.get().getBufferedImage(), null);
        PixelReader reader = image.getPixelReader();
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter writer = writableImage.getPixelWriter();

        int max = this.grayLevelStatisticsService.calcularMaximoNivelDeGris(image);
        double c = this.calculateC(max);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                int oldGrayLevel = (int) (reader.getColor(i, j).getRed() * 255);
                int newGrayLevel = (int) (c * Math.log(1 + oldGrayLevel));
                Color newColor = Color.rgb(newGrayLevel, newGrayLevel, newGrayLevel);
                writer.setColor(i, j, newColor);

            }
        }

        return writableImage;

    }

    public int[][] execute(int[][] channel) {
        int max = this.grayLevelStatisticsService.calcularMaximoNivelDeGris(channel);
        double c = this.calculateC(max);
        for (int i = 0; i < channel.length; i++) {
            for (int j = 0; j < channel[i].length; j++) {
                int oldGrayLevel = channel[i][j];
                int newGrayLevel = (int) (c * Math.log(1 + oldGrayLevel));
                channel[i][j] = newGrayLevel;
            }
        }
        return channel;
    }

    private double calculateC(int maxGrayLevel) {
        return (255 / (Math.log(1 + maxGrayLevel)));
    }

}

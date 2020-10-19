package core.action.channels;

import core.repository.RepositorioImagen;
import core.service.transformations.TransformRGBtoHSVImageService;
import domain.customimage.Imagen;
import domain.customimage.Format;
import domain.generation.Channel;
import domain.hsvimage.HSVImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.BiFunction;

public class ObtainHSVChannelAction {

    private final RepositorioImagen repositorioImagen;
    private final TransformRGBtoHSVImageService transformRGBtoHSVImageService;

    public ObtainHSVChannelAction(RepositorioImagen repositorioImagen, TransformRGBtoHSVImageService transformRGBtoHSVImageService) {
        this.repositorioImagen = repositorioImagen;
        this.transformRGBtoHSVImageService = transformRGBtoHSVImageService;
    }

    public Imagen execute(Channel channel) {

        Optional<Imagen> currentImage = this.repositorioImagen.obtenerImagen();
        if (!currentImage.isPresent()) {
            return new Imagen(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), Format.PNG);
        }

        Imagen image = currentImage.get();

        int width = image.getAncho();
        int height = image.getAltura();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        HSVImage hsvfromRGB = transformRGBtoHSVImageService.createHSVfromRGB(image);

        switch (channel) {
            case HUE:
                getChannel(width, height, pixelWriter, functionHue(hsvfromRGB));
                break;
            case SATURATION:
                getChannel(width, height, pixelWriter, functionSaturation(hsvfromRGB));
                break;
            case VALUE:
                getChannel(width, height, pixelWriter, functionValue(hsvfromRGB));
                break;
        }

        return putOnRepository(SwingFXUtils.fromFXImage(writableImage, null));
    }

    private Imagen putOnRepository(BufferedImage bufferedImage) {
        return repositorioImagen.salvarImagenModificada(new Imagen(bufferedImage, Format.PNG));
    }

    private void getChannel(int width, int height, PixelWriter pixelWriter, BiFunction<Integer, Integer, Color> channel) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixelWriter.setColor(x, y, channel.apply(x, y));
            }
        }
    }

    private BiFunction<Integer, Integer, Color> functionHue(HSVImage hsvImage) {
        return (x, y) -> {
            double hue = hsvImage.getHue(x, y);
            return Color.hsb(hue, 1, 1);
        };
    }

    private BiFunction<Integer, Integer, Color> functionSaturation(HSVImage hsvImage) {
        return (x, y) -> {
            double saturation = hsvImage.getSaturation(x, y);
            return Color.hsb(0, saturation, 1);
        };
    }

    private BiFunction<Integer, Integer, Color> functionValue(HSVImage hsvImage) {
        return (x, y) -> {
            double value = hsvImage.getValue(x, y);
            return Color.hsb(0, 0, value);
        };
    }
}

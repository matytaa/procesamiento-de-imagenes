package core.action.channels;

import core.repository.RepositorioImagen;
import dominio.customimage.Imagen;
import dominio.customimage.Format;
import dominio.generation.Channel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.BiFunction;

public class ObtainRGBChannelAction {

    private final RepositorioImagen repositorioImagen;

    public ObtainRGBChannelAction(RepositorioImagen repositorioImagen) {
        this.repositorioImagen = repositorioImagen;
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

        switch (channel) {
            case RED:
                getChannel(width, height, pixelWriter, functionRed(image));
                break;
            case GREEN:
                getChannel(width, height, pixelWriter, functionGreen(image));
                break;
            case BLUE:
                getChannel(width, height, pixelWriter, functionBlue(image));
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

    private BiFunction<Integer, Integer, Color> functionRed(Imagen image) {
        return (x, y) -> {
            int red = image.getRChannelValue(x, y);
            return Color.rgb(red, 0, 0);
        };
    }

    private BiFunction<Integer, Integer, Color> functionGreen(Imagen image) {
        return (x, y) -> {
            int green = image.getGChannelValue(x, y);
            return Color.rgb(0, green, 0);
        };
    }

    private BiFunction<Integer, Integer, Color> functionBlue(Imagen image) {
        return (x, y) -> {
            int blue = image.getBChannelValue(x, y);
            return Color.rgb(0, 0, blue);
        };
    }

}

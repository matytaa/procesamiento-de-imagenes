package core.action.figure;

import core.repository.RepositorioImagen;
import core.service.generation.FiguraImagenService;
import dominio.customimage.Imagen;
import dominio.customimage.Format;
import dominio.generation.Figura;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class CrearImagenConFiguraAction {

    private final FiguraImagenService figuraImagenService;
    private final RepositorioImagen repositorioImagen;

    public CrearImagenConFiguraAction(FiguraImagenService figuraImagenService, RepositorioImagen repositorioImagen) {
        this.figuraImagenService = figuraImagenService;
        this.repositorioImagen = repositorioImagen;
    }

    public Imagen ejecutar(int ancho, int alto, Figura figura) {

        switch (figura) {
            case CIRCULO:
                Image circulo = figuraImagenService.crearCirculo(ancho, alto);
                return putOnRepository(SwingFXUtils.fromFXImage(circulo, null));
            case CUADRADO:
                Image cuadrado = figuraImagenService.crearCuadrado(ancho, alto);
                return putOnRepository(SwingFXUtils.fromFXImage(cuadrado, null));
            default:
                return Imagen.EMPTY;
        }
    }

    private Imagen putOnRepository(BufferedImage bufferedImage) {
        return repositorioImagen.salvarImagen(new Imagen(bufferedImage, Format.PNG));
    }
}

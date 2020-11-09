package core.action.edit.space_domain;

import core.repository.RepositorioImagen;
import dominio.customimage.Imagen;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Optional;

public class FuncionGammaAction {


    private final RepositorioImagen repositorioImagen;

    public FuncionGammaAction(RepositorioImagen repositorioImagen) {
        this.repositorioImagen = repositorioImagen;
    }

    public Image ejecutar(double gamma) {

        Optional<Imagen> imagenAlmacenada = this.repositorioImagen.obtenerImagen();
        if (!imagenAlmacenada.isPresent()) {
            return new WritableImage(300,300);
        }

        Image imagen = SwingFXUtils.toFXImage(imagenAlmacenada.get().getBufferedImage(),null);
        PixelReader pixelDeLectura = imagen.getPixelReader();
        WritableImage imagenDeEscritura = new WritableImage((int)imagen.getWidth(), (int)imagen.getHeight());
        PixelWriter pixelDeEscritura = imagenDeEscritura.getPixelWriter();

        double c = this.calcularC(gamma);
        for (int i=0; i < imagen.getWidth(); i++)
            for (int j=0; j < imagen.getHeight(); j++) {
                int nivelDeGris = (int) (pixelDeLectura.getColor(i, j).getRed() * 255);
                int nuevoNivelDeGris = AplicarFuncionGamma(gamma, c, nivelDeGris);
                Color nuevoColor = Color.rgb(nuevoNivelDeGris, nuevoNivelDeGris, nuevoNivelDeGris);
                pixelDeEscritura.setColor(i, j, nuevoColor);
            }
        return imagenDeEscritura;
    }

    //T(r) = c.rγ
    private int AplicarFuncionGamma(double gamma, double c, int nivelDeGris) {
        return (int) (c * (Math.pow(nivelDeGris, gamma)));
    }

    //c = L−1 / (L−1)γ
    private double calcularC(double gamma) {
        return 255/(Math.pow(255,gamma));
    }

}

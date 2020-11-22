package core.service.generation;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Circle;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class FiguraImagenService {

    public Image crearCirculo(int ancho, int alto) {
        WritableImage writableImage = new WritableImage(ancho, alto);
        rellenarConNegro(ancho, alto, writableImage);
        crearCirculo(ancho, alto, writableImage);
        return writableImage;
    }

    public Image crearCuadrado(int ancho, int alto) {
        WritableImage imagen = new WritableImage(ancho, alto);
        rellenarConNegro(ancho, alto, imagen);
        crearCuadrado(ancho, alto, imagen);
        return imagen;
    }

    private void rellenarConNegro(int ancho, int alto, WritableImage imagen) {
        for (int fila = 0; fila < alto; fila++) {
            for (int columna = 0; columna < ancho; columna++) {
                imagen.getPixelWriter().setColor(columna, fila, BLACK);
            }
        }
    }

    private void crearCirculo(int ancho, int alto, WritableImage imagen) {
        int columnaCentral = ancho / 2;
        int filaCentral = alto / 2;
        Circle circulo = new Circle(filaCentral, columnaCentral, alto / 6);
        for (int fila = 0; fila < alto; fila++) {
            for (int columna = 0; columna < ancho; columna++) {
                if (circulo.contains(fila, columna)) {
                    imagen.getPixelWriter().setColor(columna, fila, WHITE);
                }
            }
        }
    }

    private void crearCuadrado(int ancho, int alto, WritableImage imagen) {
        for (int fila = alto / 3; fila <= alto * 2 / 3; fila++) {
            for (int columna = ancho / 3; columna <= ancho * 2 / 3; columna++) {
                imagen.getPixelWriter().setColor(columna, fila, WHITE);
            }
        }
    }
}

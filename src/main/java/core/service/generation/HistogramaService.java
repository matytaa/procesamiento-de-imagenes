package core.service.generation;

import domain.Histograma;
import domain.customimage.Imagen;

import java.util.Arrays;

public class HistogramaService {

    public Histograma crear(Imagen imagen) {

        Integer total = 0;
        double[] valores = new double[257];
        //recorro cada pixel, y le calculo el promedio entre los 3 canales RGB. Ese es mi nivel de gris.
        //si vuelvo a obtener otro promedio igual, le voy sumando uno para registrar las apariciones.
        for (int i = 0; i < imagen.getAncho(); i++) {
            for (int j = 0; j < imagen.getAltura(); j++) {
                valores[imagen.getPromedioPixel(i, j)] += 1;
                total++;
            }
        }
        for (int i = 0; i < valores.length; i++) {
            valores[i] = valores[i] / total;
        }

        Double valorMinimo = Arrays.stream(valores).min().orElse(0.0);

        return new Histograma(valores, total, valorMinimo);
    }
}

package core.service.generation;

import dominio.Histograma;
import dominio.customimage.Imagen;

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
        obtenerValoresRelativos(total, valores);
        Double valorMinimo = Arrays.stream(valores).min().orElse(0.0);

        return new Histograma(valores, total, valorMinimo);
    }

    public Histograma crearHistograma(Imagen imagen) {

        Integer total = 0;
        double[] values = new double[257];

        for (int i = 0; i < imagen.getAncho(); i++) {
            for (int j = 0; j < imagen.getAltura(); j++) {
                values[imagen.getPromedioPixel(i, j)] += 1;
                total++;
            }
        }

        Double minValue = Arrays.stream(values).min().orElse(0.0);

        return new Histograma(values, total, minValue);
    }

    private void obtenerValoresRelativos(Integer total, double[] valores) {
        for (int i = 0; i < valores.length; i++) {
            valores[i] = valores[i] / total;
        }
    }
}

package dominio.sift;

import org.openimaj.image.MBFImage;

public class ResultadoSift {
    private int cantidadPuntosOrigen;
    private int cantidadPuntosDestino;
    private int cantidadDeCoincidencias;
    private MBFImage coincidencias;



    public ResultadoSift(int cantidadPuntosOrigen, int cantidadPuntosDestino,
                         int cantidadDeCoincidencias, MBFImage coincidencias){
        this.cantidadPuntosOrigen = cantidadPuntosOrigen;
        this.cantidadPuntosDestino = cantidadPuntosDestino;
        this.cantidadDeCoincidencias = cantidadDeCoincidencias;
        this.coincidencias = coincidencias;
    }

    public int getCantidadPuntosOrigen() {
        return cantidadPuntosOrigen;
    }

    public int getCantidadPuntosDestino() {
        return cantidadPuntosDestino;
    }

    public int getCantidadDeCoincidencias() {
        return cantidadDeCoincidencias;
    }

    public MBFImage getCoincidencias() {
        return coincidencias;
    }
}

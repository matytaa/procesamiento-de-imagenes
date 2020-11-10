package dominio.automaticthreshold;

import javafx.scene.image.Image;

public class ResultadoUmbralGlobal {

    private int iteraciones;
    private int umbral;
    private Image imagen;

    public ResultadoUmbralGlobal(Image imagen, int iteraciones, int umbral){
        this.imagen = imagen;
        this.iteraciones = iteraciones;
        this.umbral = umbral;
    }

    public int getIteraciones() {
        return iteraciones;
    }

    public int getUmbral() {
        return umbral;
    }

    public Image getImagen() {
        return imagen;
    }

}

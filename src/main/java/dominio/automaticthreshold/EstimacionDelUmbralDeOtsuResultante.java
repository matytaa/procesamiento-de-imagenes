package dominio.automaticthreshold;

import javafx.scene.image.Image;

public class EstimacionDelUmbralDeOtsuResultante {

    private Image imagen;
    private int umbral;

    public EstimacionDelUmbralDeOtsuResultante(Image imagen, int umbral){
        this.imagen = imagen;
        this.umbral = umbral;
    }

    public Image getImagen() {
        return imagen;
    }

    public int getUmbral() {
        return umbral;
    }
}

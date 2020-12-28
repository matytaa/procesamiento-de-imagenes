package presentation.presenter;

import core.action.characteristic_points.AplicarDetectorSiftAction;
import core.action.image.CargarImagenAction;
import dominio.customimage.Imagen;
import dominio.sift.ResultadoSift;
import org.openimaj.image.DisplayUtilities;
import presentation.controller.SiftSceneController;
import presentation.util.ShowResultPopup;

public class SiftPresenter {

    private final SiftSceneController vista;
    private final CargarImagenAction cargarImagenAction;
    private final AplicarDetectorSiftAction aplicarDetectorSiftAction;
    private Imagen imagen1;
    private Imagen imagen2;

    public SiftPresenter(SiftSceneController siftSceneController, CargarImagenAction cargarImagenAction, AplicarDetectorSiftAction aplicarDetectorSiftAction) {
        this.vista = siftSceneController;
        this.cargarImagenAction = cargarImagenAction;
        this.aplicarDetectorSiftAction = aplicarDetectorSiftAction;
    }

    public void seleccionarImagen1() {
        this.imagen1 = this.cargarImagenAction.ejecutar();
        this.vista.imageView1.setImage(this.imagen1.toFXImage());
    }

    public void seleccionarImagen2() {
        this.imagen2 = this.cargarImagenAction.ejecutar();
        this.vista.imageView2.setImage(this.imagen2.toFXImage());
    }

    public void aplicar() {
        ResultadoSift resultadoSift = this.aplicarDetectorSiftAction.ejecutar(this.imagen1, this.imagen2);
        DisplayUtilities.display(resultadoSift.getCoincidencias());
        ShowResultPopup.show("Sift",
                "Descriptores Imagen 1: " + String.valueOf(resultadoSift.getCantidadPuntosOrigen()) + "\n"
                        + "Descriptores Imagen 2: " + String.valueOf(resultadoSift.getCantidadPuntosDestino()) + "\n"
                        + "Coincidencias: " + String.valueOf(resultadoSift.getCantidadDeCoincidencias()));
    }
}

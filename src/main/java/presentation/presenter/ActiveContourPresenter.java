package presentation.presenter;

import core.action.edgedetector.AplicarContornoActivoAction;
import core.action.edgedetector.ApplyActiveContourOnImageSequenceAction;
import core.action.edgedetector.GetImageSequenceAction;
import core.action.image.ObtenerImagenAction;
import dominio.activecontour.*;
import dominio.customimage.Imagen;
import dominio.customimage.RGB;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.image.Image;
import presentation.controller.ActiveContourSceneController;
import presentation.view.CustomImageView;

import java.util.ArrayList;
import java.util.List;

public class ActiveContourPresenter {

    private static final int PASOS_DEFAULT = 1;
    private static final double EPSILON_DEFAULT = 0;

    private final ActiveContourSceneController view;
    private final ObtenerImagenAction obtenerImagenAction;
    private final GetImageSequenceAction getImageSequenceAction;
    private final AplicarContornoActivoAction contourActivoAction;
    private final ApplyActiveContourOnImageSequenceAction applyActiveContourOnImageSequenceAction;
    private final PublishSubject<Image> onModifiedImagePublishSubject;

    private Integer tita1;
    private Integer tita0;
    private Imagen currentCustomImage;
    private ContornoActivo contornoActivo;
    private Image modifiedImage;
    private List<Imagen> currentImages;
    private List<ImagenConContorno> contours;
    private int contourIndex = 0;

    public ActiveContourPresenter(ActiveContourSceneController view,
                                  AplicarContornoActivoAction contourActivoAction, ObtenerImagenAction obtenerImagenAction,
                                  GetImageSequenceAction getImageSequenceAction,
                                  ApplyActiveContourOnImageSequenceAction applyActiveContourOnImageSequenceAction,
                                  PublishSubject<Image> onModifiedImagePublishSubject) {
        this.view = view;
        this.contourActivoAction = contourActivoAction;
        this.obtenerImagenAction = obtenerImagenAction;
        this.getImageSequenceAction = getImageSequenceAction;
        this.applyActiveContourOnImageSequenceAction = applyActiveContourOnImageSequenceAction;
        this.onModifiedImagePublishSubject = onModifiedImagePublishSubject;
        this.currentImages = new ArrayList<>();
        this.contours = new ArrayList<>();
    }

    public void initialize() {
        view.setInitializeCustomImageView(new CustomImageView(view.groupImageView, view.imageView)
                .withSetPickOnBounds(true)
                .withSelectionMode());

        onInitializeContours();
        FdFunctionMode.classic();
    }

    public void onInitializeContours() {
        if (ActiveContourMode.esSimple()) {
            this.obtenerImagenAction.ejecutar().ifPresent(customImage -> {
                currentCustomImage = customImage;
                modifiedImage = customImage.toFXImage();
                view.setImage(modifiedImage);
            });
        } else {
            this.contourIndex = 0;
            this.getImageSequenceAction.execute().ifPresent(customImages -> {
                if (!customImages.isEmpty()) {
                    currentCustomImage = customImages.get(0);
                    modifiedImage = currentCustomImage.toFXImage();
                    currentImages = customImages;
                    view.setImage(modifiedImage);
                }
            });
        }

        view.disablePrevButton();
        view.disableNextButton();
        view.disableApplyButton();
        view.enableStartButton();
    }

    public void onComenzar() {
        SelectionSquare rectanguloSeleccionado = view.getRectanguloSeleccionado();
        if (rectanguloSeleccionado.isValid() && tita0 != null) {
            if (ActiveContourMode.esSimple()) {
                onComenzarModoSimple(rectanguloSeleccionado);
                view.enableApplyButton();
            } else {
                onStartSequenceMode(rectanguloSeleccionado);
            }

            view.disableStartButton();
        } else {
            view.mustSelectArea();
        }
    }

    private void onComenzarModoSimple(SelectionSquare rectanguloSeleccionado) {
        if (currentCustomImage != null) {
            contornoActivo = crearContornoActivo(rectanguloSeleccionado, currentCustomImage);
            setearElContornoInicialDelObjeto(contourActivoAction.execute(currentCustomImage, contornoActivo, PASOS_DEFAULT, EPSILON_DEFAULT));
        }
    }

    private void onStartSequenceMode(SelectionSquare selectionSquare) {
        if (currentImages != null && !currentImages.isEmpty()) {
            contornoActivo = crearContornoActivo(selectionSquare, currentCustomImage);
            contours = applyActiveContourOnImageSequenceAction.execute(currentImages, contornoActivo, view.getCantidadDeIteraciones(), view.getEpsilon());
            view.setImage(contours.get(contourIndex).dibujarContornoActivo());
            contourIndex++;
        }

        view.enableNextButton();
    }

    private ContornoActivo crearContornoActivo(SelectionSquare rectanguloSeleccionado, Imagen imagen) {
        return new ContornoActivo(imagen.getAncho(), imagen.getAltura(), rectanguloSeleccionado, tita0, tita1);
    }

    public void onApply() {
        if (view.getCantidadDeIteraciones() > 0) {
            if (tita0 != null && currentCustomImage != null) {
                setearElContornoInicialDelObjeto(contourActivoAction.execute(currentCustomImage, contornoActivo, view.getCantidadDeIteraciones(), view.getEpsilon()));
            }
        } else {
            view.stepsMustBeGreaterThanZero();
        }
    }

    public void onPrev() {
        if (!contours.isEmpty() && contourIndex > 0) {
            contourIndex--;
            Image image = contours.get(contourIndex).dibujarContornoActivo();
            modifiedImage = image;
            view.setImage(image);
            view.enableNextButton();
        } else {
            view.disablePrevButton();
        }
    }

    public void onNext() {
        if (!contours.isEmpty() && contourIndex < contours.size()) {
            Image image = contours.get(contourIndex).dibujarContornoActivo();
            modifiedImage = image;
            view.setImage(image);
            view.enablePrevButton();
            contourIndex++;
        } else {
            view.disableNextButton();
        }
    }

    public void onGetInsidePressed() {
        SelectionSquare selectionSquare = view.getRectanguloSeleccionado();
        if (selectionSquare.isValid()) {
            tita1 = obtenerPromedioDePixelesDeLaSeleccion();
            view.disableGetObjectButton();
        } else {
            view.mustSelectArea();
        }
    }

    private int obtenerPromedioDePixelesDeLaSeleccion() {
        Imagen imagen = new Imagen(view.getPartialImage(), "png");
        RGB promedioRGB = imagen.getPromedioPorCanal();
        return (promedioRGB.getRed()+promedioRGB.getBlue()+promedioRGB.getGreen())/3;
    }

    public void onGetOutsidePressed() {
        tita0 = obtenerPromedioDePixelesDeLaSeleccion();
        view.disableGetBackgroundButton();
    }

    public void onFinish() {
        this.onModifiedImagePublishSubject.onNext(modifiedImage);
        view.closeWindow();
    }

    private void setearElContornoInicialDelObjeto(ImagenConContorno imagenConContorno) {
        contornoActivo = imagenConContorno.getContornoActivo();
        modifiedImage = imagenConContorno.dibujarContornoActivo();
        view.setImage(modifiedImage);
    }
}

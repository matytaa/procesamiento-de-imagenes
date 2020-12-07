package presentation.presenter;

import core.action.edgedetector.AplicarContornoActivoAction;
import core.action.edgedetector.AplicarContornosActivosEnSecuanciaDeImagesAction;
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

    private static final int ITERACIONES_DEFAULT = 1;
    private static final double EPSILON_DEFAULT = 0;

    private final ActiveContourSceneController view;
    private final ObtenerImagenAction obtenerImagenAction;
    private final GetImageSequenceAction getImageSequenceAction;
    private final AplicarContornoActivoAction contourActivoAction;
    private final AplicarContornosActivosEnSecuanciaDeImagesAction aplicarContornosActivosEnSecuanciaDeImagesAction;
    private final PublishSubject<Image> onModifiedImagePublishSubject;

    private Integer tita1;
    private Integer tita0;
    private Imagen imagenActual;
    private ContornoActivo contornoActivo;
    private Image modifiedImage;
    private List<Imagen> secuenciaDeImagenes;
    private List<ImagenConContorno> contours;
    private int contourIndex = 0;

    public ActiveContourPresenter(ActiveContourSceneController view,
                                  AplicarContornoActivoAction contourActivoAction, ObtenerImagenAction obtenerImagenAction,
                                  GetImageSequenceAction getImageSequenceAction,
                                  AplicarContornosActivosEnSecuanciaDeImagesAction aplicarContornosActivosEnSecuanciaDeImagesAction,
                                  PublishSubject<Image> onModifiedImagePublishSubject) {
        this.view = view;
        this.contourActivoAction = contourActivoAction;
        this.obtenerImagenAction = obtenerImagenAction;
        this.getImageSequenceAction = getImageSequenceAction;
        this.aplicarContornosActivosEnSecuanciaDeImagesAction = aplicarContornosActivosEnSecuanciaDeImagesAction;
        this.onModifiedImagePublishSubject = onModifiedImagePublishSubject;
        this.secuenciaDeImagenes = new ArrayList<>();
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
        if (ContornosActivosModo.esSimple()) {
            this.obtenerImagenAction.ejecutar().ifPresent(customImage -> {
                imagenActual = customImage;
                modifiedImage = customImage.toFXImage();
                view.setImage(modifiedImage);
            });
        } else {
            this.contourIndex = 0;
            this.getImageSequenceAction.execute().ifPresent(customImages -> {
                if (!customImages.isEmpty()) {
                    imagenActual = customImages.get(0);
                    modifiedImage = imagenActual.toFXImage();
                    secuenciaDeImagenes = customImages;
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
            if (ContornosActivosModo.esSimple()) {
                onComenzarModoSimple(rectanguloSeleccionado);
                view.enableApplyButton();
            } else {
                onComenzarModoSecuenciaDeImagenes(rectanguloSeleccionado);
            }

            view.disableStartButton();
        } else {
            view.mustSelectArea();
        }
    }

    private void onComenzarModoSimple(SelectionSquare rectanguloSeleccionado) {
        if (imagenActual != null) {
            contornoActivo = crearContornoActivo(rectanguloSeleccionado, imagenActual);
            setearElContornoInicialDelObjeto(contourActivoAction.execute(imagenActual, contornoActivo, ITERACIONES_DEFAULT, EPSILON_DEFAULT));
        }
    }

    private void onComenzarModoSecuenciaDeImagenes(SelectionSquare rectanguloSeleccionado) {
        if (secuenciaDeImagenes != null && !secuenciaDeImagenes.isEmpty()) {
            contornoActivo = crearContornoActivo(rectanguloSeleccionado, imagenActual);
            contours = aplicarContornosActivosEnSecuanciaDeImagesAction.execute(secuenciaDeImagenes, contornoActivo, view.getCantidadDeIteraciones(), view.getEpsilon());
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
            if (tita0 != null && imagenActual != null) {
                setearElContornoInicialDelObjeto(contourActivoAction.execute(imagenActual, contornoActivo, view.getCantidadDeIteraciones(), view.getEpsilon()));
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
        SelectionSquare rectanguloSeleccionado = view.getRectanguloSeleccionado();
        if (rectanguloSeleccionado.isValid()) {
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

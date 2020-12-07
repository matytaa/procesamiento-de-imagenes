package core.action.edgedetector;

import dominio.activecontour.ContornoActivo;
import dominio.activecontour.ImagenConContorno;
import dominio.customimage.Imagen;

import java.util.ArrayList;
import java.util.List;

public class AplicarContornosActivosEnSecuanciaDeImagesAction {

    private final AplicarContornoActivoAction aplicarContornoActivoAction;

    public AplicarContornosActivosEnSecuanciaDeImagesAction(AplicarContornoActivoAction aplicarContornoActivoAction) {
        this.aplicarContornoActivoAction = aplicarContornoActivoAction;
    }

    public List<ImagenConContorno> execute(List<Imagen> secuenciaDeImagenes, ContornoActivo contornoActivo, int iteraciones, double epsilon) {

        List<ImagenConContorno> imagenConContornos = new ArrayList<>();

        Imagen primeraImagen = secuenciaDeImagenes.get(0);
        ImagenConContorno imagenConContorno = aplicarContornoActivoAction.execute(primeraImagen, contornoActivo, iteraciones, epsilon);
        imagenConContornos.add(imagenConContorno);

        List<Imagen> list = new ArrayList<>(secuenciaDeImagenes);
        list.remove(0);

        for (Imagen imagen : list) {
            ContornoActivo anterior = ContornoActivo.copiar(imagenConContorno.getContornoActivo());
            imagenConContorno = aplicarContornoActivoAction.execute(imagen, anterior, iteraciones, epsilon);
            imagenConContornos.add(imagenConContorno);
        }

        return imagenConContornos;
    }

}

package core.action.edgedetector;

import dominio.activecontour.ContornoActivo;
import dominio.activecontour.ImagenConContorno;
import dominio.customimage.Imagen;

import java.util.ArrayList;
import java.util.List;

public class ApplyActiveContourOnImageSequenceAction {

    private final ApplyActiveContourAction applyActiveContourAction;

    public ApplyActiveContourOnImageSequenceAction(ApplyActiveContourAction applyActiveContourAction) {
        this.applyActiveContourAction = applyActiveContourAction;
    }

    public List<ImagenConContorno> execute(List<Imagen> customImages, ContornoActivo contornoActivo, int steps, double epsilon) {

        List<ImagenConContorno> imagenConContornos = new ArrayList<>();

        Imagen first = customImages.get(0);
        ImagenConContorno imagenConContorno = applyActiveContourAction.execute(first, contornoActivo, steps, epsilon);
        imagenConContornos.add(imagenConContorno);

        List<Imagen> list = new ArrayList<>(customImages);
        list.remove(0);

        for (Imagen customImage : list) {
            ContornoActivo previousContornoActivo = ContornoActivo.copy(imagenConContorno.getContornoActivo());
            imagenConContorno = applyActiveContourAction.execute(customImage, previousContornoActivo, steps, epsilon);
            imagenConContornos.add(imagenConContorno);
        }

        return imagenConContornos;
    }

}

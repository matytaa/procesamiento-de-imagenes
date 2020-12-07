package core.action.edgedetector;

import dominio.PuntoXY;
import dominio.activecontour.ContornoActivo;
import dominio.activecontour.ImagenConContorno;
import dominio.activecontour.FdFunction;
import dominio.activecontour.FdFunctionMode;
import dominio.customimage.Imagen;
import dominio.mask.filter.MascaraGaussiana;

import java.util.ArrayList;
import java.util.List;

public class AplicarContornoActivoAction {

    public static final int GAUSSIAN_STANDARD_DEVIATION = 1;

    public ImagenConContorno execute(Imagen imagen, ContornoActivo contornoActivo, int pasos, double epsilon) {
        return recursive(imagen, contornoActivo, pasos, epsilon);
    }

    private ImagenConContorno recursive(Imagen imagen, ContornoActivo contornoActivo, int pasos, double epsilon) {

        ImagenConContorno imagenConContorno = new ImagenConContorno(imagen, contornoActivo);

        ContornoActivo cicloDeUnCortorno = imagenConContorno.getContornoActivo();
        if (hemosEncontradoUnObjeto(imagen, cicloDeUnCortorno, epsilon)) {
            return new ImagenConContorno(imagen, aplicarSegundoCiclo(cicloDeUnCortorno));
        } else if (pasos == 0) {
            return imagenConContorno;
        } else {
            imagenConContorno = aplicarContornoActivo(imagenConContorno.getCustomImage(), imagenConContorno.getContornoActivo(), epsilon);
        }

        pasos--;
        return recursive(imagenConContorno.getCustomImage(), imagenConContorno.getContornoActivo(), pasos, epsilon);
    }

    private boolean hemosEncontradoUnObjeto(Imagen image, ContornoActivo contornoActivo, double epsilon) {

        int promedioDeGrisesFueraDelObjeto = contornoActivo.getPromedioDeGrisesFueraDelObjeto();
        int promedioDeGrisesDentroDelObjeto = contornoActivo.getPromedioDeGrisesDentroDelObjeto();

        for (PuntoXY puntoExterior : contornoActivo.getBordeExterno()) {
            if (!chequearSiFuncionFdEsMenorQueEpsilon(puntoExterior, image, promedioDeGrisesFueraDelObjeto, promedioDeGrisesDentroDelObjeto, epsilon)) {
                return false;
            }
        }

        for (PuntoXY puntoInterior : contornoActivo.getBordeInterno()) {
            if (chequearSiFuncionFdEsMenorQueEpsilon(puntoInterior, image, promedioDeGrisesFueraDelObjeto, promedioDeGrisesDentroDelObjeto, epsilon)) {
                return false;
            }
        }

        return true;
    }

    private ImagenConContorno aplicarContornoActivo(Imagen imagen, ContornoActivo contornoActivo, double epsilon) {
        ContornoActivo cicloUnoDelContorno = aplicarCicloUno(imagen, contornoActivo, epsilon);
        return new ImagenConContorno(imagen, cicloUnoDelContorno);
    }

    private ContornoActivo aplicarCicloUno(Imagen imagen, ContornoActivo contornoActivo, double epsilon) {

        ContornoActivo cicloUnoDelContorno = ContornoActivo.copy(contornoActivo);

        // Paso 1
        List<PuntoXY> lOut = cicloUnoDelContorno.getBordeExterno();
        List<PuntoXY> lIn = cicloUnoDelContorno.getBordeInterno();
        int tita0 = cicloUnoDelContorno.getPromedioDeGrisesFueraDelObjeto();
        int tita1 = cicloUnoDelContorno.getPromedioDeGrisesDentroDelObjeto();

        // Paso 2
        interacambiarEnLIn(imagen, cicloUnoDelContorno, lOut, tita0, tita1, epsilon);

        // Paso 3
        cicloUnoDelContorno.moverInvalidoLInATita1();

        // Paso 4
        intercambiarEnLOut(imagen, cicloUnoDelContorno, lIn, tita0, tita1, epsilon);

        // Paso 5
        cicloUnoDelContorno.moverInvalidoLOutATita0();

        return cicloUnoDelContorno;
    }

    private ContornoActivo aplicarSegundoCiclo(ContornoActivo cicloUnoDelContorno) {

        ContornoActivo segunddoCicloDelContorno = ContornoActivo.copy(cicloUnoDelContorno);

        // Step 0
        List<PuntoXY> lOut = segunddoCicloDelContorno.getBordeExterno();
        List<PuntoXY> lIn = segunddoCicloDelContorno.getBordeInterno();

        //Step 1
        segundoCicloIntercambiarBordesInternos(segunddoCicloDelContorno, lOut);

        // Step 2
        segunddoCicloDelContorno.moverInvalidoLInATita1();

        //Step 3
        segundoCicloIntercambiarBordesExternos(segunddoCicloDelContorno, lIn);

        //Step 4
        segunddoCicloDelContorno.moverInvalidoLOutATita0();

        return segunddoCicloDelContorno;
    }

    private void segundoCicloIntercambiarBordesInternos(ContornoActivo segundoCicloDelContorno, List<PuntoXY> lOut) {

        List<PuntoXY> puntosParaRemoverDeLOut = new ArrayList<>();
        List<PuntoXY> puntosParaAgregarEnLIn = new ArrayList<>();
        List<PuntoXY> puntosParaAgregarEnLOut = new ArrayList<>();

        for (PuntoXY puntoXy : lOut) {

            double nuevoFi = new MascaraGaussiana(GAUSSIAN_STANDARD_DEVIATION)
                    .aplicarMascaraPorPixel(segundoCicloDelContorno.getContent(), puntoXy.getX(), puntoXy.getY());
            if (nuevoFi < 0) {
                rellenarIntercambioEnLIn(segundoCicloDelContorno, puntosParaRemoverDeLOut, puntosParaAgregarEnLIn, puntosParaAgregarEnLOut, puntoXy);
            }
        }

        segundoCicloDelContorno.agregarEnLIn(puntosParaAgregarEnLIn);
        segundoCicloDelContorno.removerEnLOut(puntosParaRemoverDeLOut);
        segundoCicloDelContorno.agregarEnLOut(puntosParaAgregarEnLOut);
    }

    private void segundoCicloIntercambiarBordesExternos(ContornoActivo segundoCicloDelContorno, List<PuntoXY> lIn) {

        List<PuntoXY> puntosParaAgregarEnLOut2 = new ArrayList<>();
        List<PuntoXY> puntosParaAgregarEnLIn2 = new ArrayList<>();
        List<PuntoXY> toRemoveFromLIn2 = new ArrayList<>();

        for (PuntoXY puntoXy : lIn) {

            double newFiValue = new MascaraGaussiana(GAUSSIAN_STANDARD_DEVIATION)
                    .aplicarMascaraPorPixel(segundoCicloDelContorno.getContent(), puntoXy.getX(), puntoXy.getY());
            if (newFiValue > 0) {
                rellenarIntercambioEnLOut(segundoCicloDelContorno, puntosParaAgregarEnLOut2, puntosParaAgregarEnLIn2, toRemoveFromLIn2, puntoXy);
            }
        }

        segundoCicloDelContorno.removerEnLIn(toRemoveFromLIn2);
        segundoCicloDelContorno.agregarEnLOut(puntosParaAgregarEnLOut2);
        segundoCicloDelContorno.agregarEnLIn(puntosParaAgregarEnLIn2);
    }

    private void interacambiarEnLIn(Imagen imagen, ContornoActivo cicloUnoDelContorno, List<PuntoXY> lOut, int tita0,
                                    int tita1, double epsilon) {
        List<PuntoXY> puntosParaRemoverDeLOut = new ArrayList<>();
        List<PuntoXY> puntosParaAgregarEnLIn = new ArrayList<>();
        List<PuntoXY> puntosParaAgregarEnLOut = new ArrayList<>();

        for (PuntoXY puntoXy : lOut) {
            if (!chequearSiFuncionFdEsMenorQueEpsilon(puntoXy, imagen, tita0, tita1, epsilon)) {
                rellenarIntercambioEnLIn(cicloUnoDelContorno, puntosParaRemoverDeLOut, puntosParaAgregarEnLIn, puntosParaAgregarEnLOut, puntoXy);
            }
        }

        cicloUnoDelContorno.agregarEnLIn(puntosParaAgregarEnLIn);
        cicloUnoDelContorno.removerEnLOut(puntosParaRemoverDeLOut);
        cicloUnoDelContorno.agregarEnLOut(puntosParaAgregarEnLOut);
    }

    private void intercambiarEnLOut(Imagen imagen, ContornoActivo cicloUnoDelContorno, List<PuntoXY> lIn, int tita0,
                                    int tita1, double epsilon) {
        List<PuntoXY> puntosParaAgregarEnLOut2 = new ArrayList<>();
        List<PuntoXY> puntosParaAgregarEnLIn2 = new ArrayList<>();
        List<PuntoXY> toRemoveFromLIn2 = new ArrayList<>();

        for (PuntoXY puntoXy : lIn) {
            if (chequearSiFuncionFdEsMenorQueEpsilon(puntoXy, imagen, tita0, tita1, epsilon)) {
                rellenarIntercambioEnLOut(cicloUnoDelContorno, puntosParaAgregarEnLOut2, puntosParaAgregarEnLIn2, toRemoveFromLIn2, puntoXy);
            }
        }

        cicloUnoDelContorno.removerEnLIn(toRemoveFromLIn2);
        cicloUnoDelContorno.agregarEnLOut(puntosParaAgregarEnLOut2);
        cicloUnoDelContorno.agregarEnLIn(puntosParaAgregarEnLIn2);
    }

    private void rellenarIntercambioEnLIn(ContornoActivo cicloDeContorno,
                                          List<PuntoXY> puntosParaRemoverDeLOut,
                                          List<PuntoXY> puntosParaAgregarEnLIn,
                                          List<PuntoXY> puntosParaAgregarEnLOut,
                                          PuntoXY puntoXy) {
        intercambiarLInDelPrimerCiclo(cicloDeContorno, puntosParaRemoverDeLOut, puntosParaAgregarEnLIn, puntoXy);
        intercambiarLInDelSegundoCiclo(cicloDeContorno, puntosParaAgregarEnLOut, puntoXy);
    }

    private void rellenarIntercambioEnLOut(ContornoActivo cicloUnoDelContorno,
                                           List<PuntoXY> puntosParaAgregarEnLOut2,
                                           List<PuntoXY> puntosParaAgregarEnLIn2,
                                           List<PuntoXY> toRemoveFromLIn2,
                                           PuntoXY puntoXy) {
        intercambiarLOutDelPrimerCiclo(cicloUnoDelContorno, puntosParaAgregarEnLOut2, toRemoveFromLIn2, puntoXy);
        intercambiarLOutDelSegundoCiclo(cicloUnoDelContorno, puntosParaAgregarEnLIn2, puntoXy);
    }

    private void intercambiarLInDelPrimerCiclo(ContornoActivo cicloUnoDelContorno, List<PuntoXY> puntosParaRemoverDeLOut, List<PuntoXY> puntosParaAgregarEnLIn, PuntoXY puntoXy) {
        puntosParaRemoverDeLOut.add(puntoXy);
        puntosParaAgregarEnLIn.add(puntoXy);
        cicloUnoDelContorno.updateFiValueForLInPoint(puntoXy);
    }

    private void intercambiarLOutDelPrimerCiclo(ContornoActivo cicloUnoDelContorno, List<PuntoXY> puntosParaAgregarEnLOut2, List<PuntoXY> toRemoveFromLIn2, PuntoXY puntoXy) {
        toRemoveFromLIn2.add(puntoXy);
        puntosParaAgregarEnLOut2.add(puntoXy);
        cicloUnoDelContorno.updateFiValueForLOutPoint(puntoXy);
    }

    private void intercambiarLInDelSegundoCiclo(ContornoActivo cicloUnoDelContorno, List<PuntoXY> puntosParaAgregarEnLOut, PuntoXY puntoXy) {
        cicloUnoDelContorno.getVecinos(puntoXy).stream()
                       .filter(vecino -> cicloUnoDelContorno.esUnaPosicionValida(vecino.getX(), vecino.getY()))
                       .filter(vecino -> cicloUnoDelContorno.noPerteneceAlObjeto(vecino))
                       .forEach(vecino -> {
                           puntosParaAgregarEnLOut.add(vecino);
                           cicloUnoDelContorno.updateFiValueForLOutPoint(vecino);
                       });
    }

    private void intercambiarLOutDelSegundoCiclo(ContornoActivo cicloUnoDelContorno, List<PuntoXY> puntosParaAgregarEnLIn2, PuntoXY puntoXy) {
        cicloUnoDelContorno.getVecinos(puntoXy).stream()
                       .filter(vecino -> cicloUnoDelContorno.esUnaPosicionValida(vecino.getX(), vecino.getY()))
                       .filter(vecino -> cicloUnoDelContorno.perteneceAlObjeto(vecino))
                       .forEach(vecino -> {
                           puntosParaAgregarEnLIn2.add(vecino);
                           cicloUnoDelContorno.updateFiValueForLInPoint(vecino);
                       });
    }

    private boolean chequearSiFuncionFdEsMenorQueEpsilon(PuntoXY puntoXy, Imagen imagen,
                                                         int tita0, int tita1, double epsilon) {

        int promedio = imagen.getPromedioPixel(puntoXy.getX(), puntoXy.getY());
        if (FdFunctionMode.isClassic())
            return FdFunction.esMenorQueCero(promedio, tita0, tita1, 0);

        return FdFunction.esMayorQueCero(promedio, tita1, epsilon);
    }
}

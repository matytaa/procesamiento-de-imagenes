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

    public ImagenConContorno execute(Imagen imagen, ContornoActivo contornoActivo, int iteraciones, double epsilon) {
        return recursive(imagen, contornoActivo, iteraciones, epsilon);
    }

    private ImagenConContorno recursive(Imagen imagen, ContornoActivo contornoActivo, int iteraciones, double epsilon) {

        ImagenConContorno imagenConContorno = new ImagenConContorno(imagen, contornoActivo);

        ContornoActivo primerCiclo = imagenConContorno.getContornoActivo();
        if (hemosEncontradoUnObjeto(imagen, primerCiclo, epsilon)) {
            return new ImagenConContorno(imagen, aplicarSegundoCiclo(primerCiclo));
        } else if (iteraciones == 0) {
            return imagenConContorno;
        } else {
            imagenConContorno = aplicarContornoActivo(imagenConContorno.getCustomImage(), imagenConContorno.getContornoActivo(), epsilon);
        }

        iteraciones--;
        return recursive(imagenConContorno.getCustomImage(), imagenConContorno.getContornoActivo(), iteraciones, epsilon);
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
        ContornoActivo primerCiclo = aplicarPrimerCiclo(imagen, contornoActivo, epsilon);
        return new ImagenConContorno(imagen, primerCiclo);
    }

    private ContornoActivo aplicarPrimerCiclo(Imagen imagen, ContornoActivo contornoActivo, double epsilon) {

        ContornoActivo primerCiclo = ContornoActivo.copiar(contornoActivo);

        // Paso 1
        List<PuntoXY> lOut = primerCiclo.getBordeExterno();
        List<PuntoXY> lIn = primerCiclo.getBordeInterno();
        int tita0 = primerCiclo.getPromedioDeGrisesFueraDelObjeto();
        int tita1 = primerCiclo.getPromedioDeGrisesDentroDelObjeto();

        // Paso 2
        interacambiarEnLIn(imagen, primerCiclo, lOut, tita0, tita1, epsilon);

        // Paso 3
        primerCiclo.moverInvalidoLInATita1();

        // Paso 4
        intercambiarEnLOut(imagen, primerCiclo, lIn, tita0, tita1, epsilon);

        // Paso 5
        primerCiclo.moverInvalidoLOutATita0();

        return primerCiclo;
    }

    private ContornoActivo aplicarSegundoCiclo(ContornoActivo primerCiclo) {

        ContornoActivo segunddoCicloDelContorno = ContornoActivo.copiar(primerCiclo);

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

    private void interacambiarEnLIn(Imagen imagen, ContornoActivo primerCiclo, List<PuntoXY> lOut, int tita0,
                                    int tita1, double epsilon) {
        List<PuntoXY> puntosParaRemoverDeLOut = new ArrayList<>();
        List<PuntoXY> puntosParaAgregarEnLIn = new ArrayList<>();
        List<PuntoXY> puntosParaAgregarEnLOut = new ArrayList<>();

        for (PuntoXY puntoXy : lOut) {
            if (!chequearSiFuncionFdEsMenorQueEpsilon(puntoXy, imagen, tita0, tita1, epsilon)) {
                rellenarIntercambioEnLIn(primerCiclo, puntosParaRemoverDeLOut, puntosParaAgregarEnLIn, puntosParaAgregarEnLOut, puntoXy);
            }
        }

        primerCiclo.agregarEnLIn(puntosParaAgregarEnLIn);
        primerCiclo.removerEnLOut(puntosParaRemoverDeLOut);
        primerCiclo.agregarEnLOut(puntosParaAgregarEnLOut);
    }

    private void intercambiarEnLOut(Imagen imagen, ContornoActivo primerCiclo, List<PuntoXY> lIn, int tita0,
                                    int tita1, double epsilon) {
        List<PuntoXY> puntosParaAgregarEnLOut2 = new ArrayList<>();
        List<PuntoXY> puntosParaAgregarEnLIn2 = new ArrayList<>();
        List<PuntoXY> toRemoveFromLIn2 = new ArrayList<>();

        for (PuntoXY puntoXy : lIn) {
            if (chequearSiFuncionFdEsMenorQueEpsilon(puntoXy, imagen, tita0, tita1, epsilon)) {
                rellenarIntercambioEnLOut(primerCiclo, puntosParaAgregarEnLOut2, puntosParaAgregarEnLIn2, toRemoveFromLIn2, puntoXy);
            }
        }

        primerCiclo.removerEnLIn(toRemoveFromLIn2);
        primerCiclo.agregarEnLOut(puntosParaAgregarEnLOut2);
        primerCiclo.agregarEnLIn(puntosParaAgregarEnLIn2);
    }

    private void rellenarIntercambioEnLIn(ContornoActivo cicloDeContorno,
                                          List<PuntoXY> puntosParaRemoverDeLOut,
                                          List<PuntoXY> puntosParaAgregarEnLIn,
                                          List<PuntoXY> puntosParaAgregarEnLOut,
                                          PuntoXY puntoXy) {
        intercambiarLInDelPrimerCiclo(cicloDeContorno, puntosParaRemoverDeLOut, puntosParaAgregarEnLIn, puntoXy);
        intercambiarLInDelSegundoCiclo(cicloDeContorno, puntosParaAgregarEnLOut, puntoXy);
    }

    private void rellenarIntercambioEnLOut(ContornoActivo primerCiclo,
                                           List<PuntoXY> puntosParaAgregarEnLOut2,
                                           List<PuntoXY> puntosParaAgregarEnLIn2,
                                           List<PuntoXY> toRemoveFromLIn2,
                                           PuntoXY puntoXy) {
        intercambiarLOutDelPrimerCiclo(primerCiclo, puntosParaAgregarEnLOut2, toRemoveFromLIn2, puntoXy);
        intercambiarLOutDelSegundoCiclo(primerCiclo, puntosParaAgregarEnLIn2, puntoXy);
    }

    private void intercambiarLInDelPrimerCiclo(ContornoActivo primerCiclo, List<PuntoXY> puntosParaRemoverDeLOut, List<PuntoXY> puntosParaAgregarEnLIn, PuntoXY puntoXy) {
        puntosParaRemoverDeLOut.add(puntoXy);
        puntosParaAgregarEnLIn.add(puntoXy);
        primerCiclo.updateFiValueForLInPoint(puntoXy);
    }

    private void intercambiarLOutDelPrimerCiclo(ContornoActivo primerCiclo, List<PuntoXY> puntosParaAgregarEnLOut2, List<PuntoXY> toRemoveFromLIn2, PuntoXY puntoXy) {
        toRemoveFromLIn2.add(puntoXy);
        puntosParaAgregarEnLOut2.add(puntoXy);
        primerCiclo.updateFiValueForLOutPoint(puntoXy);
    }

    private void intercambiarLInDelSegundoCiclo(ContornoActivo primerCiclo, List<PuntoXY> puntosParaAgregarEnLOut, PuntoXY puntoXy) {
        primerCiclo.getVecinos(puntoXy).stream()
                       .filter(vecino -> primerCiclo.esUnaPosicionValida(vecino.getX(), vecino.getY()))
                       .filter(vecino -> primerCiclo.noPerteneceAlObjeto(vecino))
                       .forEach(vecino -> {
                           puntosParaAgregarEnLOut.add(vecino);
                           primerCiclo.updateFiValueForLOutPoint(vecino);
                       });
    }

    private void intercambiarLOutDelSegundoCiclo(ContornoActivo primerCiclo, List<PuntoXY> puntosParaAgregarEnLIn2, PuntoXY puntoXy) {
        primerCiclo.getVecinos(puntoXy).stream()
                       .filter(vecino -> primerCiclo.esUnaPosicionValida(vecino.getX(), vecino.getY()))
                       .filter(vecino -> primerCiclo.perteneceAlObjeto(vecino))
                       .forEach(vecino -> {
                           puntosParaAgregarEnLIn2.add(vecino);
                           primerCiclo.updateFiValueForLInPoint(vecino);
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

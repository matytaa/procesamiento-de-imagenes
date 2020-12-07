package core.action.edgedetector;

import dominio.puntoXY;
import dominio.activecontour.ContornoActivo;
import dominio.activecontour.ImagenConContorno;
import dominio.activecontour.FdFunction;
import dominio.activecontour.FdFunctionMode;
import dominio.customimage.Imagen;
import dominio.mask.filter.MascaraGaussiana;

import java.util.ArrayList;
import java.util.List;

public class ApplyActiveContourAction {

    public static final int GAUSSIAN_STANDARD_DEVIATION = 1;

    public ImagenConContorno execute(Imagen imagen, ContornoActivo contornoActivo, int pasos, double epsilon) {
        return recursive(imagen, contornoActivo, pasos, epsilon);
    }

    private ImagenConContorno recursive(Imagen imagen, ContornoActivo contornoActivo, int pasos, double epsilon) {

        ImagenConContorno imagenConContorno = new ImagenConContorno(imagen, contornoActivo);

        ContornoActivo cicloDeUnCortorno = imagenConContorno.getContornoActivo();
        if (hemosEncontradoUnObjeto(imagen, cicloDeUnCortorno, epsilon)) {
            return new ImagenConContorno(imagen, applyCycleTwo(cicloDeUnCortorno));
        } else if (pasos == 0) {
            return imagenConContorno;
        } else {
            imagenConContorno = applyActiveContour(imagenConContorno.getCustomImage(), imagenConContorno.getContornoActivo(), epsilon);
        }

        pasos--;
        return recursive(imagenConContorno.getCustomImage(), imagenConContorno.getContornoActivo(), pasos, epsilon);
    }

    private boolean hemosEncontradoUnObjeto(Imagen image, ContornoActivo contornoActivo, double epsilon) {

        int promedioDeGrisesFueraDelObjeto = contornoActivo.getPromedioDeGrisesFueraDelObjeto();
        int promedioDeGrisesDentroDelObjeto = contornoActivo.getPromedioDeGrisesDentroDelObjeto();

        for (puntoXY puntoExterior : contornoActivo.getBordeExterno()) {
            if (!checkFdFunctionIsLowerThanEpsilon(puntoExterior, image, promedioDeGrisesFueraDelObjeto, promedioDeGrisesDentroDelObjeto, epsilon)) {
                return false;
            }
        }

        for (puntoXY puntoInterior : contornoActivo.getBordeInterno()) {
            if (checkFdFunctionIsLowerThanEpsilon(puntoInterior, image, promedioDeGrisesFueraDelObjeto, promedioDeGrisesDentroDelObjeto, epsilon)) {
                return false;
            }
        }

        return true;
    }

    private ImagenConContorno applyActiveContour(Imagen imagen, ContornoActivo contornoActivo, double epsilon) {
        ContornoActivo cycleOneContour = applyCycleOne(imagen, contornoActivo, epsilon);
        return new ImagenConContorno(imagen, cycleOneContour);
    }

    private ContornoActivo applyCycleOne(Imagen imagen, ContornoActivo contornoActivo, double epsilon) {

        ContornoActivo cycleOneContour = ContornoActivo.copy(contornoActivo);

        // Step 1
        List<puntoXY> lOut = cycleOneContour.getBordeExterno();
        List<puntoXY> lIn = cycleOneContour.getBordeInterno();
        int backgroundGrayAverage = cycleOneContour.getPromedioDeGrisesFueraDelObjeto();
        int objectGrayAverage = cycleOneContour.getPromedioDeGrisesDentroDelObjeto();

        // Step 2
        switchIn(imagen, cycleOneContour, lOut, backgroundGrayAverage, objectGrayAverage, epsilon);

        // Step 3
        cycleOneContour.moveInvalidLInToObject();

        // Step 4
        switchOut(imagen, cycleOneContour, lIn, backgroundGrayAverage, objectGrayAverage, epsilon);

        // Step 5
        cycleOneContour.moveInvalidLOutToBackground();

        return cycleOneContour;
    }

    private ContornoActivo applyCycleTwo(ContornoActivo cycleOneContour) {

        ContornoActivo cycleTwoContour = ContornoActivo.copy(cycleOneContour);

        // Step 0
        List<puntoXY> lOut = cycleTwoContour.getBordeExterno();
        List<puntoXY> lIn = cycleTwoContour.getBordeInterno();

        //Step 1
        cycleTwoSwitchIn(cycleTwoContour, lOut);

        // Step 2
        cycleTwoContour.moveInvalidLInToObject();

        //Step 3
        cycleTwoSwitchOut(cycleTwoContour, lIn);

        //Step 4
        cycleTwoContour.moveInvalidLOutToBackground();

        return cycleTwoContour;
    }

    private void cycleTwoSwitchIn(ContornoActivo cycleTwoContour, List<puntoXY> lOut) {

        List<puntoXY> removeFromLOut = new ArrayList<>();
        List<puntoXY> addToLIn = new ArrayList<>();
        List<puntoXY> addToLOut = new ArrayList<>();

        for (puntoXY puntoXy : lOut) {

            double newFiValue = new MascaraGaussiana(GAUSSIAN_STANDARD_DEVIATION)
                    .applyMaskToPixel(cycleTwoContour.getContent(), puntoXy.getX(), puntoXy.getY());
            if (newFiValue < 0) {
                fillSwitchInLists(cycleTwoContour, removeFromLOut, addToLIn, addToLOut, puntoXy);
            }
        }

        cycleTwoContour.addLIn(addToLIn);
        cycleTwoContour.removeLOut(removeFromLOut);
        cycleTwoContour.addLOut(addToLOut);
    }

    private void cycleTwoSwitchOut(ContornoActivo cycleTwoContour, List<puntoXY> lIn) {

        List<puntoXY> addToLOut2 = new ArrayList<>();
        List<puntoXY> addToLIn2 = new ArrayList<>();
        List<puntoXY> toRemoveFromLIn2 = new ArrayList<>();

        for (puntoXY puntoXy : lIn) {

            double newFiValue = new MascaraGaussiana(GAUSSIAN_STANDARD_DEVIATION)
                    .applyMaskToPixel(cycleTwoContour.getContent(), puntoXy.getX(), puntoXy.getY());
            if (newFiValue > 0) {
                fillSwitchOutLists(cycleTwoContour, addToLOut2, addToLIn2, toRemoveFromLIn2, puntoXy);
            }
        }

        cycleTwoContour.removeLIn(toRemoveFromLIn2);
        cycleTwoContour.addLOut(addToLOut2);
        cycleTwoContour.addLIn(addToLIn2);
    }

    private void switchIn(Imagen imagen, ContornoActivo cycleOneContour, List<puntoXY> lOut, int backgroundGrayAverage,
                          int objectGrayAverage, double epsilon) {
        List<puntoXY> removeFromLOut = new ArrayList<>();
        List<puntoXY> addToLIn = new ArrayList<>();
        List<puntoXY> addToLOut = new ArrayList<>();

        for (puntoXY puntoXy : lOut) {
            if (!checkFdFunctionIsLowerThanEpsilon(puntoXy, imagen, backgroundGrayAverage, objectGrayAverage, epsilon)) {
                fillSwitchInLists(cycleOneContour, removeFromLOut, addToLIn, addToLOut, puntoXy);
            }
        }

        cycleOneContour.addLIn(addToLIn);
        cycleOneContour.removeLOut(removeFromLOut);
        cycleOneContour.addLOut(addToLOut);
    }

    private void switchOut(Imagen imagen, ContornoActivo cycleOneContour, List<puntoXY> lIn, int backgroundGrayAverage,
                           int objectGrayAverage, double epsilon) {
        List<puntoXY> addToLOut2 = new ArrayList<>();
        List<puntoXY> addToLIn2 = new ArrayList<>();
        List<puntoXY> toRemoveFromLIn2 = new ArrayList<>();

        for (puntoXY puntoXy : lIn) {
            if (checkFdFunctionIsLowerThanEpsilon(puntoXy, imagen, backgroundGrayAverage, objectGrayAverage, epsilon)) {
                fillSwitchOutLists(cycleOneContour, addToLOut2, addToLIn2, toRemoveFromLIn2, puntoXy);
            }
        }

        cycleOneContour.removeLIn(toRemoveFromLIn2);
        cycleOneContour.addLOut(addToLOut2);
        cycleOneContour.addLIn(addToLIn2);
    }

    private void fillSwitchInLists(ContornoActivo cycleOneContour, List<puntoXY> removeFromLOut, List<puntoXY> addToLIn, List<puntoXY> addToLOut,
                                   puntoXY puntoXy) {
        switchInStepOne(cycleOneContour, removeFromLOut, addToLIn, puntoXy);
        switchInStepTwo(cycleOneContour, addToLOut, puntoXy);
    }

    private void fillSwitchOutLists(ContornoActivo cycleOneContour, List<puntoXY> addToLOut2, List<puntoXY> addToLIn2, List<puntoXY> toRemoveFromLIn2,
                                    puntoXY puntoXy) {
        switchOutStepOne(cycleOneContour, addToLOut2, toRemoveFromLIn2, puntoXy);
        switchOutStepTwo(cycleOneContour, addToLIn2, puntoXy);
    }

    private void switchInStepOne(ContornoActivo cycleOneContour, List<puntoXY> removeFromLOut, List<puntoXY> addToLIn, puntoXY puntoXy) {
        removeFromLOut.add(puntoXy);
        addToLIn.add(puntoXy);
        cycleOneContour.updateFiValueForLInPoint(puntoXy);
    }

    private void switchOutStepOne(ContornoActivo cycleOneContour, List<puntoXY> addToLOut2, List<puntoXY> toRemoveFromLIn2, puntoXY puntoXy) {
        toRemoveFromLIn2.add(puntoXy);
        addToLOut2.add(puntoXy);
        cycleOneContour.updateFiValueForLOutPoint(puntoXy);
    }

    private void switchInStepTwo(ContornoActivo cycleOneContour, List<puntoXY> addToLOut, puntoXY puntoXy) {
        cycleOneContour.getNeighbors(puntoXy).stream()
                       .filter(neighbor -> cycleOneContour.hasValidPosition(neighbor.getX(), neighbor.getY()))
                       .filter(neighbor -> cycleOneContour.belongToBackground(neighbor))
                       .forEach(neighbor -> {
                           addToLOut.add(neighbor);
                           cycleOneContour.updateFiValueForLOutPoint(neighbor);
                       });
    }

    private void switchOutStepTwo(ContornoActivo cycleOneContour, List<puntoXY> addToLIn2, puntoXY puntoXy) {
        cycleOneContour.getNeighbors(puntoXy).stream()
                       .filter(neighbor -> cycleOneContour.hasValidPosition(neighbor.getX(), neighbor.getY()))
                       .filter(neighbor -> cycleOneContour.belongToObject(neighbor))
                       .forEach(neighbor -> {
                           addToLIn2.add(neighbor);
                           cycleOneContour.updateFiValueForLInPoint(neighbor);
                       });
    }

    private boolean checkFdFunctionIsLowerThanEpsilon(puntoXY puntoXy, Imagen imagen,
                                                      int backgroundGrayAverage, int objectGrayAverage, double epsilon) {

        int imageAverageValue = imagen.getPromedioPixel(puntoXy.getX(), puntoXy.getY());
        if (FdFunctionMode.isClassic()) {
            return FdFunction.lowerThanZero(imageAverageValue, backgroundGrayAverage, objectGrayAverage, 0);
        }

        return FdFunction.greaterThanEpsilon(imageAverageValue, objectGrayAverage, epsilon);
    }
}

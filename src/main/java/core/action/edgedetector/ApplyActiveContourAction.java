package core.action.edgedetector;

import dominio.puntoXY;
import dominio.activecontour.ActiveContour;
import dominio.activecontour.ContourCustomImage;
import dominio.activecontour.FdFunction;
import dominio.activecontour.FdFunctionMode;
import dominio.customimage.Imagen;
import dominio.mask.filter.MascaraGaussiana;

import java.util.ArrayList;
import java.util.List;

public class ApplyActiveContourAction {

    public static final int GAUSSIAN_STANDARD_DEVIATION = 1;

    public ContourCustomImage execute(Imagen customImage, ActiveContour activeContour, int steps, double epsilon) {
        return recursive(customImage, activeContour, steps, epsilon);
    }

    private ContourCustomImage recursive(Imagen customImage, ActiveContour activeContour, int steps, double epsilon) {

        ContourCustomImage contourCustomImage = new ContourCustomImage(customImage, activeContour);

        ActiveContour cycleOneContour = contourCustomImage.getActiveContour();
        if (objectHasBeenFound(customImage, cycleOneContour, epsilon)) {
            return new ContourCustomImage(customImage, applyCycleTwo(cycleOneContour));
        } else if (steps == 0) {
            return contourCustomImage;
        } else {
            contourCustomImage = applyActiveContour(contourCustomImage.getCustomImage(), contourCustomImage.getActiveContour(), epsilon);
        }

        steps--;
        return recursive(contourCustomImage.getCustomImage(), contourCustomImage.getActiveContour(), steps, epsilon);
    }

    private boolean objectHasBeenFound(Imagen image, ActiveContour activeContour, double epsilon) {

        int backgroundGrayAverage = activeContour.getBackgroundGrayAverage();
        int objectGrayAverage = activeContour.getObjectGrayAverage();

        for (puntoXY lOutPoint : activeContour.getlOut()) {
            if (!checkFdFunctionIsLowerThanEpsilon(lOutPoint, image, backgroundGrayAverage, objectGrayAverage, epsilon)) {
                return false;
            }
        }

        for (puntoXY lInPoint : activeContour.getlIn()) {
            if (checkFdFunctionIsLowerThanEpsilon(lInPoint, image, backgroundGrayAverage, objectGrayAverage, epsilon)) {
                return false;
            }
        }

        return true;
    }

    private ContourCustomImage applyActiveContour(Imagen customImage, ActiveContour activeContour, double epsilon) {
        ActiveContour cycleOneContour = applyCycleOne(customImage, activeContour, epsilon);
        return new ContourCustomImage(customImage, cycleOneContour);
    }

    private ActiveContour applyCycleOne(Imagen customImage, ActiveContour activeContour, double epsilon) {

        ActiveContour cycleOneContour = ActiveContour.copy(activeContour);

        // Step 1
        List<puntoXY> lOut = cycleOneContour.getlOut();
        List<puntoXY> lIn = cycleOneContour.getlIn();
        int backgroundGrayAverage = cycleOneContour.getBackgroundGrayAverage();
        int objectGrayAverage = cycleOneContour.getObjectGrayAverage();

        // Step 2
        switchIn(customImage, cycleOneContour, lOut, backgroundGrayAverage, objectGrayAverage, epsilon);

        // Step 3
        cycleOneContour.moveInvalidLInToObject();

        // Step 4
        switchOut(customImage, cycleOneContour, lIn, backgroundGrayAverage, objectGrayAverage, epsilon);

        // Step 5
        cycleOneContour.moveInvalidLOutToBackground();

        return cycleOneContour;
    }

    private ActiveContour applyCycleTwo(ActiveContour cycleOneContour) {

        ActiveContour cycleTwoContour = ActiveContour.copy(cycleOneContour);

        // Step 0
        List<puntoXY> lOut = cycleTwoContour.getlOut();
        List<puntoXY> lIn = cycleTwoContour.getlIn();

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

    private void cycleTwoSwitchIn(ActiveContour cycleTwoContour, List<puntoXY> lOut) {

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

    private void cycleTwoSwitchOut(ActiveContour cycleTwoContour, List<puntoXY> lIn) {

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

    private void switchIn(Imagen customImage, ActiveContour cycleOneContour, List<puntoXY> lOut, int backgroundGrayAverage,
                          int objectGrayAverage, double epsilon) {
        List<puntoXY> removeFromLOut = new ArrayList<>();
        List<puntoXY> addToLIn = new ArrayList<>();
        List<puntoXY> addToLOut = new ArrayList<>();

        for (puntoXY puntoXy : lOut) {
            if (!checkFdFunctionIsLowerThanEpsilon(puntoXy, customImage, backgroundGrayAverage, objectGrayAverage, epsilon)) {
                fillSwitchInLists(cycleOneContour, removeFromLOut, addToLIn, addToLOut, puntoXy);
            }
        }

        cycleOneContour.addLIn(addToLIn);
        cycleOneContour.removeLOut(removeFromLOut);
        cycleOneContour.addLOut(addToLOut);
    }

    private void switchOut(Imagen customImage, ActiveContour cycleOneContour, List<puntoXY> lIn, int backgroundGrayAverage,
                           int objectGrayAverage, double epsilon) {
        List<puntoXY> addToLOut2 = new ArrayList<>();
        List<puntoXY> addToLIn2 = new ArrayList<>();
        List<puntoXY> toRemoveFromLIn2 = new ArrayList<>();

        for (puntoXY puntoXy : lIn) {
            if (checkFdFunctionIsLowerThanEpsilon(puntoXy, customImage, backgroundGrayAverage, objectGrayAverage, epsilon)) {
                fillSwitchOutLists(cycleOneContour, addToLOut2, addToLIn2, toRemoveFromLIn2, puntoXy);
            }
        }

        cycleOneContour.removeLIn(toRemoveFromLIn2);
        cycleOneContour.addLOut(addToLOut2);
        cycleOneContour.addLIn(addToLIn2);
    }

    private void fillSwitchInLists(ActiveContour cycleOneContour, List<puntoXY> removeFromLOut, List<puntoXY> addToLIn, List<puntoXY> addToLOut,
                                   puntoXY puntoXy) {
        switchInStepOne(cycleOneContour, removeFromLOut, addToLIn, puntoXy);
        switchInStepTwo(cycleOneContour, addToLOut, puntoXy);
    }

    private void fillSwitchOutLists(ActiveContour cycleOneContour, List<puntoXY> addToLOut2, List<puntoXY> addToLIn2, List<puntoXY> toRemoveFromLIn2,
                                    puntoXY puntoXy) {
        switchOutStepOne(cycleOneContour, addToLOut2, toRemoveFromLIn2, puntoXy);
        switchOutStepTwo(cycleOneContour, addToLIn2, puntoXy);
    }

    private void switchInStepOne(ActiveContour cycleOneContour, List<puntoXY> removeFromLOut, List<puntoXY> addToLIn, puntoXY puntoXy) {
        removeFromLOut.add(puntoXy);
        addToLIn.add(puntoXy);
        cycleOneContour.updateFiValueForLInPoint(puntoXy);
    }

    private void switchOutStepOne(ActiveContour cycleOneContour, List<puntoXY> addToLOut2, List<puntoXY> toRemoveFromLIn2, puntoXY puntoXy) {
        toRemoveFromLIn2.add(puntoXy);
        addToLOut2.add(puntoXy);
        cycleOneContour.updateFiValueForLOutPoint(puntoXy);
    }

    private void switchInStepTwo(ActiveContour cycleOneContour, List<puntoXY> addToLOut, puntoXY puntoXy) {
        cycleOneContour.getNeighbors(puntoXy).stream()
                       .filter(neighbor -> cycleOneContour.hasValidPosition(neighbor.getX(), neighbor.getY()))
                       .filter(neighbor -> cycleOneContour.belongToBackground(neighbor))
                       .forEach(neighbor -> {
                           addToLOut.add(neighbor);
                           cycleOneContour.updateFiValueForLOutPoint(neighbor);
                       });
    }

    private void switchOutStepTwo(ActiveContour cycleOneContour, List<puntoXY> addToLIn2, puntoXY puntoXy) {
        cycleOneContour.getNeighbors(puntoXy).stream()
                       .filter(neighbor -> cycleOneContour.hasValidPosition(neighbor.getX(), neighbor.getY()))
                       .filter(neighbor -> cycleOneContour.belongToObject(neighbor))
                       .forEach(neighbor -> {
                           addToLIn2.add(neighbor);
                           cycleOneContour.updateFiValueForLInPoint(neighbor);
                       });
    }

    private boolean checkFdFunctionIsLowerThanEpsilon(puntoXY puntoXy, Imagen customImage,
                                                      int backgroundGrayAverage, int objectGrayAverage, double epsilon) {

        int imageAverageValue = customImage.getPromedioPixel(puntoXy.getX(), puntoXy.getY());
        if (FdFunctionMode.isClassic()) {
            return FdFunction.lowerThanZero(imageAverageValue, backgroundGrayAverage, objectGrayAverage, 0);
        }

        return FdFunction.greaterThanEpsilon(imageAverageValue, objectGrayAverage, epsilon);
    }
}

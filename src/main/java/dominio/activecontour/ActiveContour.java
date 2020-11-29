package dominio.activecontour;

import dominio.puntoXY;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ActiveContour {

    private static final int OBJECT_VALUE = -3;
    private static final int BACKGROUND_VALUE = 3;
    private static final int L_IN_VALUE = -1;
    private static final int L_OUT_VALUE = 1;

    private final Integer width;
    private final Integer height;
    private final int backgroundGrayAverage;
    private final int objectGrayAverage;
    private final List<puntoXY> lOut;
    private final List<puntoXY> lIn;
    private double[][] content;

    public ActiveContour(Integer width, Integer height, SelectionSquare selectionSquare, int backgroundGrayAverage, int objectGrayAverage) {
        this.width = width;
        this.height = height;
        this.backgroundGrayAverage = backgroundGrayAverage;
        this.objectGrayAverage = objectGrayAverage;

        int firstRow = selectionSquare.getFirstRow();
        int secondRow = selectionSquare.getSecondRow();
        int firstColumn = selectionSquare.getFirstColumn();
        int secondColumn = selectionSquare.getSecondColumn();

        this.lOut = addPoints(firstRow, secondRow, firstColumn, secondColumn);
        this.lIn = addPoints(firstRow + 1, secondRow - 1, firstColumn + 1, secondColumn - 1);
        this.content = initializeContent(firstRow + 2, secondRow - 2, firstColumn + 2, secondColumn - 2);
    }

    private ActiveContour(Integer width, Integer height, int backgroundGrayAverage, int objectGrayAverage,
                          List<puntoXY> lOut, List<puntoXY> lIn, double[][] content) {
        this.width = width;
        this.height = height;
        this.backgroundGrayAverage = backgroundGrayAverage;
        this.objectGrayAverage = objectGrayAverage;
        this.lOut = lOut;
        this.lIn = lIn;
        this.content = content;
    }

    public static ActiveContour copy(ActiveContour activeContour) {
        return new ActiveContour(
                activeContour.getWidth(),
                activeContour.getHeight(),
                activeContour.getBackgroundGrayAverage(),
                activeContour.getObjectGrayAverage(),
                new ArrayList<>(activeContour.getlOut()),
                new ArrayList<>(activeContour.getlIn()),
                Arrays.copyOf(activeContour.getContent(), activeContour.getContent().length));
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public List<puntoXY> getlOut() {
        return lOut;
    }

    public List<puntoXY> getlIn() {
        return lIn;
    }

    public int getBackgroundGrayAverage() {
        return backgroundGrayAverage;
    }

    public int getObjectGrayAverage() {
        return objectGrayAverage;
    }

    private List<puntoXY> addPoints(int firstRow, int secondRow, int firstColumn, int secondColumn) {
        List<puntoXY> positions = new CopyOnWriteArrayList<>();

        for (int i = firstRow; i <= secondRow; i++) {
            positions.add(new puntoXY(i, firstColumn));
            positions.add(new puntoXY(i, secondColumn));
        }

        for (int i = firstColumn; i <= secondColumn; i++) {
            positions.add(new puntoXY(firstRow, i));
            positions.add(new puntoXY(secondRow, i));
        }

        return positions;
    }

    private double[][] initializeContent(int firstRowObject, int secondRowObject, int firstColumnObject, int secondColumnObject) {
        double matrix[][] = new double[width][height];

        // Fill matrix with background value
        for (int row = 0; row < width; row++) {
            for (int column = 0; column < height; column++) {
                matrix[row][column] = BACKGROUND_VALUE;
            }
        }

        // Set edges
        lIn.forEach(xyPoint -> matrix[xyPoint.getX()][xyPoint.getY()] = L_IN_VALUE);
        lOut.forEach(xyPoint -> matrix[xyPoint.getX()][xyPoint.getY()] = L_OUT_VALUE);

        // Set object
        for (int row = firstRowObject; row <= secondRowObject; row++) {
            for (int column = firstColumnObject; column <= secondColumnObject; column++) {
                matrix[row][column] = OBJECT_VALUE;
            }
        }
        return matrix;
    }

    public void moveInvalidLInToObject() {
        for (int i = 0; i < lIn.size(); i++) {
            puntoXY puntoXy = lIn.get(i);
            if (hasAllNeighborsWithValueLowerThanZero(puntoXy)) {
                lIn.remove(puntoXy);
                content[puntoXy.getX()][puntoXy.getY()] = OBJECT_VALUE;
            }
        }
    }

    public void moveInvalidLOutToBackground() {
        for (int i = 0; i < lOut.size(); i++) {
            puntoXY puntoXy = lOut.get(i);
            if (hasAllNeighborsWithValueHigherThanZero(puntoXy)) {
                lOut.remove(puntoXy);
                content[puntoXy.getX()][puntoXy.getY()] = BACKGROUND_VALUE;
            }
        }
    }

    private boolean hasAllNeighborsWithValueLowerThanZero(puntoXY puntoXy) {
        int row = puntoXy.getX();
        int column = puntoXy.getY();
        return hasValueLowerThanZero(row - 1, column) &&
                hasValueLowerThanZero(row + 1, column) &&
                hasValueLowerThanZero(row, column - 1) &&
                hasValueLowerThanZero(row, column + 1);
    }

    private boolean hasAllNeighborsWithValueHigherThanZero(puntoXY puntoXy) {
        int row = puntoXy.getX();
        int column = puntoXy.getY();
        return hasValueHigherThanZero(row - 1, column) &&
                hasValueHigherThanZero(row + 1, column) &&
                hasValueHigherThanZero(row, column - 1) &&
                hasValueHigherThanZero(row, column + 1);
    }

    private boolean hasValueLowerThanZero(int row, int column) {
        return hasValidPosition(row, column) && content[row][column] < 0;
    }

    private boolean hasValueHigherThanZero(int row, int column) {
        return hasValidPosition(row, column) && content[row][column] > 0;
    }

    public Set<puntoXY> getNeighbors(puntoXY puntoXy) {
        Set<puntoXY> neighbors = new HashSet<>();
        int row = puntoXy.getX();
        int column = puntoXy.getY();
        neighbors.add(new puntoXY(row - 1, column));
        neighbors.add(new puntoXY(row + 1, column));
        neighbors.add(new puntoXY(row, column - 1));
        neighbors.add(new puntoXY(row, column + 1));
        return neighbors;
    }

    public boolean hasValidPosition(int row, int column) {
        boolean rowIsValid = row < this.width && 0 <= row;
        boolean columnIsValid = column < this.height && 0 <= column;
        return rowIsValid && columnIsValid;
    }

    //fi(x) = 3
    public boolean belongToBackground(puntoXY puntoXy) {
        return hasValue(puntoXy, BACKGROUND_VALUE);
    }

    //fi(x) = -3
    public boolean belongToObject(puntoXY puntoXy) {
        return hasValue(puntoXy, OBJECT_VALUE);
    }

    private boolean hasValue(puntoXY puntoXy, int value) {
        return content[puntoXy.getX()][puntoXy.getY()] == value;
    }

    //Set fi(x) = -1
    public void updateFiValueForLInPoint(puntoXY puntoXy) {
        content[puntoXy.getX()][puntoXy.getY()] = L_IN_VALUE;
    }

    //Set fi(x) = 1
    public void updateFiValueForLOutPoint(puntoXY puntoXy) {
        content[puntoXy.getX()][puntoXy.getY()] = L_OUT_VALUE;
    }

    public void addLIn(List<puntoXY> toAddToLIn) {
        lIn.addAll(toAddToLIn);
    }

    public void removeLIn(List<puntoXY> toRemoveFromLIn) {
        lIn.removeAll(toRemoveFromLIn);
    }

    public void addLOut(List<puntoXY> toAddToLOut) {
        lOut.addAll(toAddToLOut);
    }

    public void removeLOut(List<puntoXY> toRemoveFromLOut) {
        lOut.removeAll(toRemoveFromLOut);
    }

    public double[][] getContent() {
        return content;
    }

    public void setFiFunction(double[][] fiFunction) {
        this.content = fiFunction;
    }

    public void setFi(int x, int y, double newFiValue) {
        this.content[x][y] = newFiValue;
    }
}

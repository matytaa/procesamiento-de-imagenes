package dominio.activecontour;

import dominio.puntoXY;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContornoActivo {

    private static final int OBJECT_VALUE = -3;
    private static final int BACKGROUND_VALUE = 3;
    private static final int L_IN_VALUE = -1;
    private static final int L_OUT_VALUE = 1;

    private final Integer width;
    private final Integer height;
    private final int tita0;
    private final int tita1;
    private final List<puntoXY> bordeExterno;
    private final List<puntoXY> bordeInterno;
    private double[][] content;

    public ContornoActivo(Integer width, Integer height, SelectionSquare selectionSquare, int tita0, int tita1) {
        this.width = width;
        this.height = height;
        this.tita0 = tita0;
        this.tita1 = tita1;

        int primeraFila = selectionSquare.getFirstRow();
        int segundaFila = selectionSquare.getSecondRow();
        int primeraColumna = selectionSquare.getFirstColumn();
        int segundaColumna = selectionSquare.getSecondColumn();

        this.bordeExterno = addPoints(primeraFila, segundaFila, primeraColumna, segundaColumna);
        this.bordeInterno = addPoints(primeraFila + 1, segundaFila - 1, primeraColumna + 1, segundaColumna - 1);
        this.content = initializeContent(primeraFila + 2, segundaFila - 2, primeraColumna + 2, segundaColumna - 2);
    }

    private ContornoActivo(Integer width, Integer height, int tita0, int tita1,
                           List<puntoXY> bordeExterno, List<puntoXY> bordeInterno, double[][] content) {
        this.width = width;
        this.height = height;
        this.tita0 = tita0;
        this.tita1 = tita1;
        this.bordeExterno = bordeExterno;
        this.bordeInterno = bordeInterno;
        this.content = content;
    }

    public static ContornoActivo copy(ContornoActivo contornoActivo) {
        return new ContornoActivo(
                contornoActivo.getWidth(),
                contornoActivo.getHeight(),
                contornoActivo.getPromedioDeGrisesFueraDelObjeto(),
                contornoActivo.getPromedioDeGrisesDentroDelObjeto(),
                new ArrayList<>(contornoActivo.getBordeExterno()),
                new ArrayList<>(contornoActivo.getBordeInterno()),
                Arrays.copyOf(contornoActivo.getContent(), contornoActivo.getContent().length));
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public List<puntoXY> getBordeExterno() {
        return bordeExterno;
    }

    public List<puntoXY> getBordeInterno() {
        return bordeInterno;
    }

    public int getPromedioDeGrisesFueraDelObjeto() {
        return tita0;
    }

    public int getPromedioDeGrisesDentroDelObjeto() {
        return tita1;
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
        bordeInterno.forEach(xyPoint -> matrix[xyPoint.getX()][xyPoint.getY()] = L_IN_VALUE);
        bordeExterno.forEach(xyPoint -> matrix[xyPoint.getX()][xyPoint.getY()] = L_OUT_VALUE);

        // Set object
        for (int row = firstRowObject; row <= secondRowObject; row++) {
            for (int column = firstColumnObject; column <= secondColumnObject; column++) {
                matrix[row][column] = OBJECT_VALUE;
            }
        }
        return matrix;
    }

    public void moveInvalidLInToObject() {
        for (int i = 0; i < bordeInterno.size(); i++) {
            puntoXY puntoXy = bordeInterno.get(i);
            if (hasAllNeighborsWithValueLowerThanZero(puntoXy)) {
                bordeInterno.remove(puntoXy);
                content[puntoXy.getX()][puntoXy.getY()] = OBJECT_VALUE;
            }
        }
    }

    public void moveInvalidLOutToBackground() {
        for (int i = 0; i < bordeExterno.size(); i++) {
            puntoXY puntoXy = bordeExterno.get(i);
            if (hasAllNeighborsWithValueHigherThanZero(puntoXy)) {
                bordeExterno.remove(puntoXy);
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
        bordeInterno.addAll(toAddToLIn);
    }

    public void removeLIn(List<puntoXY> toRemoveFromLIn) {
        bordeInterno.removeAll(toRemoveFromLIn);
    }

    public void addLOut(List<puntoXY> toAddToLOut) {
        bordeExterno.addAll(toAddToLOut);
    }

    public void removeLOut(List<puntoXY> toRemoveFromLOut) {
        bordeExterno.removeAll(toRemoveFromLOut);
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

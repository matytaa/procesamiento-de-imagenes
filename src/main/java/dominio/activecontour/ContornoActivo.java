package dominio.activecontour;

import dominio.PuntoXY;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContornoActivo {

    private static final int VALOR_TITA_1 = -3;
    private static final int VALOR_TITA_0 = 3;
    private static final int VALOR_L_IN = -1;
    private static final int VALOR_L_OUT = 1;

    private final Integer ancho;
    private final Integer alto;
    private final int tita0;
    private final int tita1;
    private final List<PuntoXY> bordeExterno;
    private final List<PuntoXY> bordeInterno;
    private double[][] content;

    public ContornoActivo(Integer ancho, Integer alto, SelectionSquare selectionSquare, int tita0, int tita1) {
        this.ancho = ancho;
        this.alto = alto;
        this.tita0 = tita0;
        this.tita1 = tita1;

        int primeraFila = selectionSquare.getFirstRow();
        int segundaFila = selectionSquare.getSecondRow();
        int primeraColumna = selectionSquare.getFirstColumn();
        int segundaColumna = selectionSquare.getSecondColumn();

        this.bordeExterno = addPoints(primeraFila, segundaFila, primeraColumna, segundaColumna);
        this.bordeInterno = addPoints(primeraFila + 1, segundaFila - 1, primeraColumna + 1, segundaColumna - 1);
        this.content = inicializarContenido(primeraFila + 2, segundaFila - 2, primeraColumna + 2, segundaColumna - 2);
    }

    private ContornoActivo(Integer ancho, Integer alto, int tita0, int tita1,
                           List<PuntoXY> bordeExterno, List<PuntoXY> bordeInterno, double[][] content) {
        this.ancho = ancho;
        this.alto = alto;
        this.tita0 = tita0;
        this.tita1 = tita1;
        this.bordeExterno = bordeExterno;
        this.bordeInterno = bordeInterno;
        this.content = content;
    }

    public static ContornoActivo copy(ContornoActivo contornoActivo) {
        return new ContornoActivo(
                contornoActivo.getAncho(),
                contornoActivo.getAlto(),
                contornoActivo.getPromedioDeGrisesFueraDelObjeto(),
                contornoActivo.getPromedioDeGrisesDentroDelObjeto(),
                new ArrayList<>(contornoActivo.getBordeExterno()),
                new ArrayList<>(contornoActivo.getBordeInterno()),
                Arrays.copyOf(contornoActivo.getContent(), contornoActivo.getContent().length));
    }

    public Integer getAncho() {
        return ancho;
    }

    public Integer getAlto() {
        return alto;
    }

    public List<PuntoXY> getBordeExterno() {
        return bordeExterno;
    }

    public List<PuntoXY> getBordeInterno() {
        return bordeInterno;
    }

    public int getPromedioDeGrisesFueraDelObjeto() {
        return tita0;
    }

    public int getPromedioDeGrisesDentroDelObjeto() {
        return tita1;
    }

    private List<PuntoXY> addPoints(int firstRow, int secondRow, int firstColumn, int secondColumn) {
        List<PuntoXY> positions = new CopyOnWriteArrayList<>();

        for (int i = firstRow; i <= secondRow; i++) {
            positions.add(new PuntoXY(i, firstColumn));
            positions.add(new PuntoXY(i, secondColumn));
        }

        for (int i = firstColumn; i <= secondColumn; i++) {
            positions.add(new PuntoXY(firstRow, i));
            positions.add(new PuntoXY(secondRow, i));
        }

        return positions;
    }

    private double[][] inicializarContenido(int primeraFilaDelObjeto, int ultimaFilaDelObjeto, int primeraColumnaDelObjeto, int ultimaColumnaDelObjeto) {
        double matrix[][] = new double[ancho][alto];

        // Rellenamos la matriz con los valores del borde externo
        for (int fila = 0; fila < ancho; fila++)
            for (int columna = 0; columna < alto; columna++)
                matrix[fila][columna] = VALOR_TITA_0;

        // Seteamos los bordes
        bordeInterno.forEach(xyPoint -> matrix[xyPoint.getX()][xyPoint.getY()] = VALOR_L_IN);
        bordeExterno.forEach(xyPoint -> matrix[xyPoint.getX()][xyPoint.getY()] = VALOR_L_OUT);

        // Seteamos en la matriz los valores del borde interno
        for (int fila = primeraFilaDelObjeto; fila <= ultimaFilaDelObjeto; fila++)
            for (int columna = primeraColumnaDelObjeto; columna <= ultimaColumnaDelObjeto; columna++)
                matrix[fila][columna] = VALOR_TITA_1;
        return matrix;
    }

    public void moverInvalidoLInATita1() {
        for (int i = 0; i < bordeInterno.size(); i++) {
            PuntoXY puntoXy = bordeInterno.get(i);
            if (todosSusVecinosTienenValorInferiorACero(puntoXy)) {
                bordeInterno.remove(puntoXy);
                content[puntoXy.getX()][puntoXy.getY()] = VALOR_TITA_1;
            }
        }
    }

    public void moverInvalidoLOutATita0() {
        for (int i = 0; i < bordeExterno.size(); i++) {
            PuntoXY puntoXy = bordeExterno.get(i);
            if (todosSusVecinosTienenValorSuperiorACero(puntoXy)) {
                bordeExterno.remove(puntoXy);
                content[puntoXy.getX()][puntoXy.getY()] = VALOR_TITA_0;
            }
        }
    }

    private boolean todosSusVecinosTienenValorInferiorACero(PuntoXY puntoXy) {
        int fila = puntoXy.getX();
        int columna = puntoXy.getY();
        return tieneUnValorInfeeriorACero(fila - 1, columna) &&
                tieneUnValorInfeeriorACero(fila + 1, columna) &&
                tieneUnValorInfeeriorACero(fila, columna - 1) &&
                tieneUnValorInfeeriorACero(fila, columna + 1);
    }

    private boolean todosSusVecinosTienenValorSuperiorACero(PuntoXY puntoXy) {
        int fila = puntoXy.getX();
        int columna = puntoXy.getY();
        return tieneUnValorSuperiorACero(fila - 1, columna) &&
                tieneUnValorSuperiorACero(fila + 1, columna) &&
                tieneUnValorSuperiorACero(fila, columna - 1) &&
                tieneUnValorSuperiorACero(fila, columna + 1);
    }

    private boolean tieneUnValorInfeeriorACero(int fila, int columna) {
        return esUnaPosicionValida(fila, columna) && content[fila][columna] < 0;
    }

    private boolean tieneUnValorSuperiorACero(int fila, int columna) {
        return esUnaPosicionValida(fila, columna) && content[fila][columna] > 0;
    }

    public Set<PuntoXY> getVecinos(PuntoXY puntoXy) {
        Set<PuntoXY> vecinos = new HashSet<>();
        int fila = puntoXy.getX();
        int columna = puntoXy.getY();
        vecinos.add(new PuntoXY(fila - 1, columna));
        vecinos.add(new PuntoXY(fila + 1, columna));
        vecinos.add(new PuntoXY(fila, columna - 1));
        vecinos.add(new PuntoXY(fila, columna + 1));
        return vecinos;
    }

    public boolean esUnaPosicionValida(int fila, int columna) {
        boolean esUnaFilaValida = fila < this.ancho && 0 <= fila;
        boolean esUnaColumnaValida = columna < this.alto && 0 <= columna;
        return esUnaFilaValida && esUnaColumnaValida;
    }

    //fi(x) = 3
    public boolean noPerteneceAlObjeto(PuntoXY puntoXy) {
        return tieneValor(puntoXy, VALOR_TITA_0);
    }

    //fi(x) = -3
    public boolean perteneceAlObjeto(PuntoXY puntoXy) {
        return tieneValor(puntoXy, VALOR_TITA_1);
    }

    private boolean tieneValor(PuntoXY puntoXy, int valor) {
        return content[puntoXy.getX()][puntoXy.getY()] == valor;
    }

    //Set fi(x) = -1
    public void updateFiValueForLInPoint(PuntoXY puntoXy) {
        content[puntoXy.getX()][puntoXy.getY()] = VALOR_L_IN;
    }

    //Set fi(x) = 1
    public void updateFiValueForLOutPoint(PuntoXY puntoXy) {
        content[puntoXy.getX()][puntoXy.getY()] = VALOR_L_OUT;
    }

    public void agregarEnLIn(List<PuntoXY> toAddToLIn) {
        bordeInterno.addAll(toAddToLIn);
    }

    public void removerEnLIn(List<PuntoXY> toRemoveFromLIn) {
        bordeInterno.removeAll(toRemoveFromLIn);
    }

    public void agregarEnLOut(List<PuntoXY> toAddToLOut) {
        bordeExterno.addAll(toAddToLOut);
    }

    public void removerEnLOut(List<PuntoXY> toRemoveFromLOut) {
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

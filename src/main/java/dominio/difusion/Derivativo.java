package dominio.difusion;

public class Derivativo {

    private final int valor;
    private final float norte;
    private final float sur;
    private final float este;
    private final float oeste;

    public Derivativo(int[][] matriz, int x, int y) {
        valor = matriz[x][y];
        norte = calcularNorte(matriz, x, y);
        sur = calcularSur(matriz, x, y);
        este = calcularEste(matriz, x, y);
        oeste = calcularOeste(matriz, x, y);
    }

    public int getValor() {
        return valor;
    }

    public float getNorte() {
        return norte;
    }

    public float getSur() {
        return sur;
    }

    public float getEste() {
        return este;
    }

    public float getOeste() {
        return oeste;
    }

    private float calcularNorte(int[][] matriz, int x, int y) {
        return onY(matriz, x, y, y - 1);
    }

    private float calcularSur(int[][] matriz, int x, int y) {
        return onY(matriz, x, y, y + 1);
    }

    private float calcularOeste(int[][] matriz, int x, int y) {
        return onX(matriz, x, y, x - 1);
    }

    private float calcularEste(int[][] matriz, int x, int y) {
        return onX(matriz, x, y, x + 1);
    }

    private float onX(int[][] matriz, int x, int y, int coordenada) {
        if (coordenada < 0 || coordenada >= matriz.length) {
            return 0;
        }

        return matriz[coordenada][y] - matriz[x][y];
    }

    private float onY(int[][] matriz, int x, int y, int coordenada) {
        if (coordenada < 0 || coordenada >= matriz[0].length) {
            return 0;
        }

        return matriz[x][coordenada] - matriz[x][y];
    }
}

package dominio.customimage;

public class MatrizCanales {

    private int[][] canalRojo;
    private int[][] canalVerde;
    private int[][] canalAzul;

    public MatrizCanales(int ancho, int alto) {
        this.canalRojo = new int[ancho][alto];
        this.canalVerde = new int[ancho][alto];
        this.canalAzul = new int[ancho][alto];
    }

    public MatrizCanales(int[][] canalRojo, int[][] canalVerde, int[][] canalAzul) {
        this.canalRojo = canalRojo;
        this.canalVerde = canalVerde;
        this.canalAzul = canalAzul;
    }

    public void setValue(int x, int y, RGB value) {
        this.canalRojo[x][y] = value.getRed();
        this.canalVerde[x][y] = value.getGreen();
        this.canalAzul[x][y] = value.getBlue();
    }

    public int[][] getCanalRojo() {
        return canalRojo;
    }

    public int[][] getCanalVerde() {
        return canalVerde;
    }

    public int[][] getCanalAzul() {
        return canalAzul;
    }

    public int getWidth() {
        return this.canalRojo.length;
    }

    public int getHeight() {
        return this.canalRojo[0].length;
    }
}

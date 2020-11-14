package dominio.mask.filter;

import core.provider.ServiceProvider;
import core.service.MatrizService;
import dominio.customimage.Imagen;
import dominio.customimage.RGB;
import dominio.mask.Mascara;

public class MascaraBilateral extends Mascara {

    protected final double desviacionEstandarS;
    protected final double desviacionEstandarR;
    private MatrizService matrizService;

    public MascaraBilateral(double desviacionEstandarS, double desviacionEstandarR) {
        //EL TAMAÑO DE LA VENTANA ES 2 VECES LA DESVIACIÓN ESTÁNDAR + 1
        super(Tipo.GAUSSIANO, calcularTamanio(desviacionEstandarS));
        this.desviacionEstandarS = desviacionEstandarS;
        this.desviacionEstandarR = desviacionEstandarR;
        this.matriz = crearMatriz(getTamanio());
        this.factor = createFactor();

        this.matrizService = ServiceProvider.provideMatrixService();
    }

    public MascaraBilateral(double desviacionEstandarS, double desviacionEstandarR, int size, Tipo type) {
        super(type, size);
        this.desviacionEstandarS = desviacionEstandarS;
        this.desviacionEstandarR = desviacionEstandarR;
    }

    private static int calcularTamanio(double desviacionEstandar) {
        return (int) (2 * desviacionEstandar + 1);
    }

    //El FACTOR ES 1 / LA SUMATORIA DE LOS ELEMENTOS DE LA MATRIZ
    protected double createFactor() {
        double divisor = 0;
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                divisor += this.matriz[i][j];
            }
        }

        return 1 / divisor;
    }

    @Override
    protected double[][] crearMatriz(int tamanio) {

        double[][] matriz = new double[tamanio][tamanio];

        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {

                double xCuadrado = Math.pow(i - tamanio / 2, 2);
                double yCuadrado = Math.pow(j - tamanio / 2, 2);
                double cuadradoDesviacionEstandar = Math.pow(desviacionEstandarR, 2);

                double exp = Math.exp(-(xCuadrado + yCuadrado) / (cuadradoDesviacionEstandar*2.0));

                matriz[i][j] = (1.0 / (desviacionEstandarR * Math.sqrt(2.0 * Math.PI))) * exp;
            }
        }

        return matriz;
    }

    protected double[][] crearMatriz(int tamanio, Imagen imagen, int x, int y) {

        double[][] matriz = new double[tamanio][tamanio];

        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {

                double xCuadrado = Math.pow(i - tamanio / 2, 2);
                double yCuadrado = Math.pow(j - tamanio / 2, 2);
                double cuadradoDesviacionEstandar = Math.pow(desviacionEstandarS, 2);

                double red = imagen.getPixelReader().getColor(x, y).getRed();
                double green = imagen.getPixelReader().getColor(x, y).getGreen();
                double blue = imagen.getPixelReader().getColor(x, y).getBlue() ;
                double color = (red + green + blue) / 3;
                double redVecino = 0;
                double greenVecino = 0;
                double blueVecino = 0;
                if (x < imagen.getAncho()-1) {
                    redVecino = imagen.getPixelReader().getColor(x + 1, y).getRed();
                    greenVecino = imagen.getPixelReader().getColor(x + 1, y).getGreen();
                    blueVecino = imagen.getPixelReader().getColor(x + 1, y).getBlue();
                }else {
                    redVecino = imagen.getPixelReader().getColor(x, y).getRed();
                    greenVecino = imagen.getPixelReader().getColor(x, y).getGreen();
                    blueVecino = imagen.getPixelReader().getColor(x, y).getBlue();
                }
                double colorVecino = (redVecino + greenVecino + blueVecino) / 3;

                double resta = Math.abs(color - colorVecino);
                double cuadradoResta = Math.pow(resta, 2);
                double cuadradoDesviacionEstandarR = Math.pow(desviacionEstandarR, 2) * 2.0;

                double segundoTermino = cuadradoResta / cuadradoDesviacionEstandarR;
                double exp = Math.exp(-(xCuadrado + yCuadrado) / (cuadradoDesviacionEstandar*2.0) - segundoTermino);

                matriz[i][j] = (1.0 / (desviacionEstandarS * Math.sqrt(2.0 * Math.PI))) * exp;
            }
        }

        return matriz;
    }

    @Override
    public RGB aplicarMascaraAPixel(Imagen imagen, int x, int y) {

        int red = 0;
        int green = 0;
        int blue = 0;

        int ancho = imagen.getAncho();
        int alto = imagen.getAltura();
        this.crearMatriz(getTamanio(), imagen, x, y);
        for (int j = y - (tamanio / 2); j <= y + (tamanio / 2); j++) {
            for (int i = x - (tamanio / 2); i <= x + (tamanio / 2); i++) {

                if (imagen.isPosicionValida(ancho, alto, i, j)) {

                    int column = j + (tamanio / 2) - y;
                    int row = i + (tamanio / 2) - x;
                    double value = this.matriz[row][column];

                    red += 255 * imagen.getPixelReader().getColor(i, j).getRed() * value;
                    green += 255 * imagen.getPixelReader().getColor(i, j).getGreen() * value;
                    blue += 255 * imagen.getPixelReader().getColor(i, j).getBlue() * value;
                }
            }
        }

        return new RGB(red, green, blue);
    }

    //Apply mask to a simple matrix
    public double[][] apply(double[][] targetMatrix) {

        Integer width = targetMatrix.length;
        Integer height = targetMatrix[0].length;
        double[][] newMatrix = new double[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                newMatrix[x][y] = applyMaskToPixel(targetMatrix, x, y);
            }
        }

        return newMatrix;
    }

    public double[][] apply(int[][] targetMatrix) {

        Integer width = targetMatrix.length;
        Integer height = targetMatrix[0].length;
        double[][] newMatrix = new double[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                newMatrix[x][y] = applyMaskToPixel(targetMatrix, x, y);
            }
        }

        return newMatrix;
    }

    //Basic convolution algorithm with a simple matrix
    public double applyMaskToPixel(double[][] targetMatrix, int x, int y) {

        int width = targetMatrix.length;
        int height = targetMatrix[0].length;

        int newValue = 0;

        for (int j = y - (tamanio / 2); j <= y + (tamanio / 2); j++) {
            for (int i = x - (tamanio / 2); i <= x + (tamanio / 2); i++) {

                if (this.matrizService.isPositionValid(width, height, i, j)) {

                    int column = j + (tamanio / 2) - y;
                    int row = i + (tamanio / 2) - x;
                    double value = this.matriz[row][column];

                    newValue += targetMatrix[i][j] * value * factor;

                }
            }
        }

        return newValue;
    }

    public double applyMaskToPixel(int[][] targetMatrix, int x, int y) {

        int width = targetMatrix.length;
        int height = targetMatrix[0].length;

        int newValue = 0;

        for (int j = y - (tamanio / 2); j <= y + (tamanio / 2); j++) {
            for (int i = x - (tamanio / 2); i <= x + (tamanio / 2); i++) {

                if (this.matrizService.isPositionValid(width, height, i, j)) {

                    int column = j + (tamanio / 2) - y;
                    int row = i + (tamanio / 2) - x;
                    double value = this.matriz[row][column];

                    newValue += targetMatrix[i][j] * value * factor;

                }
            }
        }

        return newValue;
    }

}

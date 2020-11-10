package core.action.edgedetector;

import dominio.customimage.MatrizCanales;
import dominio.customimage.Imagen;
import dominio.mask.Mascara;

public class AplicarDetectorLaplacianoAction {

    public Imagen ejecutar(Imagen customImage, Mascara mascara, int pendiente) {
        MatrizCanales resultadoDeLaMascara = mascara.aplicar(customImage);
        MatrizCanales marcaDeCrucesPorCero = this.marcaDeCrucesPorCero(resultadoDeLaMascara, pendiente);
        return new Imagen(marcaDeCrucesPorCero, customImage.getFormatString());
    }

    private MatrizCanales marcaDeCrucesPorCero(MatrizCanales matrizDeCanales, int pendiente) {

        int matrizDeBordesRed[][] = this.marcaDeCrucesPorCeroHorizontales(matrizDeCanales.getCanalRojo(), pendiente);
        int matrizDeBordesGreen[][] = this.marcaDeCrucesPorCeroHorizontales(matrizDeCanales.getCanalVerde(), pendiente);
        int matrizDeBordesBlue[][] = this.marcaDeCrucesPorCeroHorizontales(matrizDeCanales.getCanalAzul(), pendiente);

        int matrixResultanteRed[][] = this.marcaDeCrucesPorCeroVerticales(matrizDeCanales.getCanalRojo(), matrizDeBordesRed, pendiente);
        int matrixResultanteGreen[][] = this.marcaDeCrucesPorCeroVerticales(matrizDeCanales.getCanalVerde(), matrizDeBordesGreen, pendiente);
        int matrixResultanteBlue[][] = this.marcaDeCrucesPorCeroVerticales(matrizDeCanales.getCanalAzul(), matrizDeBordesBlue, pendiente);

        return new MatrizCanales(matrixResultanteRed, matrixResultanteGreen, matrixResultanteBlue);
    }

    private int[][] marcaDeCrucesPorCeroHorizontales(int[][] matriz, int pendiente) {

        int matrizDeBordes[][] = new int[matriz.length][matriz[0].length];

        for (int x = 0; x < matriz.length - 1; x++)
            for (int y = 0; y < matriz[x].length; y++)
                if (hayCambioDeSignoHorizontal(matriz, x, y))
                    //La evaluación de la pendiente se realiza siempre. En el caso del detector laplaciano estándar, la pendiente es siempre 0
                    if (this.esElValorEsMayorALaPendienteHorizontal(matriz, x, y, pendiente))
                        matrizDeBordes[x][y] = 255;

        return matrizDeBordes;
    }

    private int[][] marcaDeCrucesPorCeroVerticales(int[][] matriz, int[][] matrizDeCrucesPorCeroHorizontales, int pendiente) {

        int[][] matrizResultante = matrizDeCrucesPorCeroHorizontales;

        for (int x = 0; x < matriz.length; x++)
            for (int y = 0; y < matriz[x].length - 1; y++)
                if (hayCambioDeSignoVertical(matriz, x, y))
                    //La evaluación de la pendiente se realiza siempre. En el caso del detector laplaciano estándar, la pendiente es siempre 0
                    if (this.esElValorEsMayorALaPendienteVertical(matriz, x, y, pendiente))
                        matrizResultante[x][y] = 255;
        return matrizResultante;
    }

    private boolean hayCambioDeSignoHorizontal(int[][] matriz, int x, int y) {
        return matriz[x + 1][y] * matriz[x][y] < 0;
    }

    private boolean hayCambioDeSignoVertical(int[][] matriz, int x, int y) {
        return matriz[x][y + 1] * matriz[x][y] < 0;
    }

    private boolean esElValorEsMayorALaPendienteHorizontal(int[][] matriz, int x, int y, int pendiente) {
        double a = matriz[x][y];
        double b = matriz[x + 1][y];

        return (Math.abs(a) + Math.abs(b)) >= pendiente;
    }

    private boolean esElValorEsMayorALaPendienteVertical(int[][] matriz, int x, int y, int pendiente) {
        double a = matriz[x][y];
        double b = matriz[x][y + 1];

        return (Math.abs(a) + Math.abs(b)) >= pendiente;
    }

}

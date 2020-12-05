package core.action.edgedetector;

import core.service.OperacionesImagenesService;
import core.service.MatrizService;
import dominio.customimage.MatrizCanales;
import dominio.customimage.Imagen;
import dominio.customimage.RGB;
import dominio.mask.sobel.MascaraSobelX;
import dominio.mask.sobel.MascaraSobelY;

import java.util.List;

public class AplicarCannyAction {

    private final OperacionesImagenesService operacionesImagenesService;
    private final MatrizService matrizService;

    public AplicarCannyAction(OperacionesImagenesService operacionesImagenesService, MatrizService matrizService) {
        this.operacionesImagenesService = operacionesImagenesService;
        this.matrizService = matrizService;
    }

    public Imagen ejecutar(Imagen imagenFiltrada, int t1, int t2) {

        MatrizCanales sobelXDerivada = new MascaraSobelX().aplicar(imagenFiltrada);
        MatrizCanales sobelYDerivada = new MascaraSobelY().aplicar(imagenFiltrada);

        int[][] anguloDelGradienteMatriz = this.calcularAnguloGradiente(sobelXDerivada, sobelYDerivada);
        int[][] sumaAbsolutaDerivada = this.operacionesImagenesService.calcularSumaAbsoluta(sobelXDerivada, sobelYDerivada)
                                                                          .getCanalRojo();

        //APLICO SUPRESION NO MAXIMO
        int[][] matrizConSupresionNoMaximo = this.aplicoSupresionNoMaximo(sumaAbsolutaDerivada, anguloDelGradienteMatriz);

        //APLICO HISTERESIS
        int[][] imagenFinal = this.aplicarHisteresis(matrizConSupresionNoMaximo, t1, t2);

        return new Imagen(
                this.operacionesImagenesService.aMatrizValida(new MatrizCanales(imagenFinal, imagenFinal, imagenFinal)),
                imagenFiltrada.getFormatString());

    }

    private int[][] aplicarHisteresis(int[][] matrizConSupresionNoMaximo, int t1, int t2) {

        int[][] matrizConBordes = matrizConSupresionNoMaximo;

        for (int x = 0; x < matrizConBordes.length; x++) {
            for (int y = 0; y < matrizConBordes[x].length; y++) {

                if (matrizConSupresionNoMaximo[x][y] > t2) {
                    matrizConBordes[x][y] = 255;
                } else if (matrizConSupresionNoMaximo[x][y] < t1) {
                    matrizConBordes[x][y] = 0;
                }
            }
        }

        //CONECTO LOS BORDES CON OTROS BORDES
        for (int x = 0; x < matrizConBordes.length; x++) {
            for (int y = 0; y < matrizConBordes[x].length; y++) {

                if (matrizConSupresionNoMaximo[x][y] >= t1 && matrizConSupresionNoMaximo[x][y] <= t2) {
                    matrizConBordes[x][y] = this.marcarBordeSiEstaConectadoAOtroBorde(matrizConBordes, x, y);
                }

            }
        }
        return matrizConBordes;
    }

    private int marcarBordeSiEstaConectadoAOtroBorde(int[][] matrizConSupresionNoMaximo, int x, int y) {

        List<RGB> vecinos = this.matrizService.obtenerVecinos(matrizConSupresionNoMaximo, x, y);

        for (RGB neighbor : vecinos) {
            if (neighbor.getRed() == 255) {
                return 255;
            }
        }
        return 0;

    }

    private int[][] aplicoSupresionNoMaximo(int[][] matrizDerivadaSumaAbsoluta, int[][] matrizAnguloGradiente) {

        int[][] matrizSuprimida = new int[matrizDerivadaSumaAbsoluta.length][matrizDerivadaSumaAbsoluta[0].length];

        for (int x = 1; x < matrizDerivadaSumaAbsoluta.length - 1; x++) {
            for (int y = 1; y < matrizDerivadaSumaAbsoluta[x].length - 1; y++) {

                matrizSuprimida[x][y] = this.evaluoBordes(matrizDerivadaSumaAbsoluta, x, y, matrizAnguloGradiente[x][y]);

            }
        }
        return matrizSuprimida;

    }

    private int evaluoBordes(int[][] sumaAbsolutaDerivada, int x, int y, int angulo) {

        if (sumaAbsolutaDerivada[x][y] != 0) {

            switch (angulo) {

                case 0:
                    return suprimoOesteYEsteNoMaximos(sumaAbsolutaDerivada, x, y);
                case 45:
                    return suprimoNoresteYSudoesteNoMaximos(sumaAbsolutaDerivada, x, y);
                case 90:
                    return suprimoNorteYSurNoMaximos(sumaAbsolutaDerivada, x, y);

                case 135:
                    return suprimoNoroesteYSudesteNoMaximos(sumaAbsolutaDerivada, x, y);

                default:
                    return 0;
            }

        }
        return 0;

    }

    private int suprimoNoresteYSudoesteNoMaximos(int[][] sumaAbsolutaDerivada, int x, int y) {
        if ((sumaAbsolutaDerivada[x][y] < sumaAbsolutaDerivada[x - 1][y - 1]) || (sumaAbsolutaDerivada[x][y]
                < sumaAbsolutaDerivada[x + 1][y + 1])) {
            return 0;
        } else {
            return sumaAbsolutaDerivada[x][y];
        }
    }

    private int suprimoNorteYSurNoMaximos(int[][] sumaAbsolutaDerivada, int x, int y) {
        if ((sumaAbsolutaDerivada[x][y] < sumaAbsolutaDerivada[x][y - 1]) || (sumaAbsolutaDerivada[x][y]
                < sumaAbsolutaDerivada[x][y + 1])) {
            return 0;
        } else {
            return sumaAbsolutaDerivada[x][y];
        }
    }

    private int suprimoNoroesteYSudesteNoMaximos(int[][] sumaAbsolutaDerivada, int x, int y) {
        if ((sumaAbsolutaDerivada[x][y] < sumaAbsolutaDerivada[x + 1][y - 1]) || (sumaAbsolutaDerivada[x][y]
                < sumaAbsolutaDerivada[x - 1][y + 1])) {
            return 0;
        } else {
            return sumaAbsolutaDerivada[x][y];
        }
    }

    private int suprimoOesteYEsteNoMaximos(int[][] sumaAbsolutaDerivada, int x, int y) {
        if ((sumaAbsolutaDerivada[x][y] < sumaAbsolutaDerivada[x - 1][y]) || (sumaAbsolutaDerivada[x][y]
                < sumaAbsolutaDerivada[x + 1][y])) {
            return 0;
        } else {
            return sumaAbsolutaDerivada[x][y];
        }
    }

    private int[][] calcularAnguloGradiente(MatrizCanales sobelXDerivada, MatrizCanales sobelYDerivada) {

       //TODO: EXTENDER A LOS OTROS CANALES

        int[][] xDerivadaCanalRojo = sobelXDerivada.getCanalRojo();
        int[][] yDerivadaCanalRojo = sobelYDerivada.getCanalRojo();

        int[][] anguloGradienteMatriz = new int[sobelXDerivada.getWidth()][sobelXDerivada.getHeight()];

        for (int x = 0; x < anguloGradienteMatriz.length; x++) {
            for (int y = 0; y < anguloGradienteMatriz[x].length; y++) {

                if (xDerivadaCanalRojo[x][y] == 0) {
                    anguloGradienteMatriz[x][y] = 90;
                } else {
                    //PASO A GRADOS REALES EL ANGULO
                    double anguloReal = Math.toDegrees(Math.atan2( yDerivadaCanalRojo[x][y] , xDerivadaCanalRojo[x][y]));
                    anguloGradienteMatriz[x][y] = this.elegirAnguloImagen(anguloReal);
                }

            }

        }
        return anguloGradienteMatriz;

    }

    //ELIJO EL ANGULO SEGUN EL RANGO EN EL QUE CAE EL ANGULO REAL
    private int elegirAnguloImagen(double anguloReal) {

        //SI ESTA EN EL 4 CUADRANTE LO CORRIJO
        anguloReal = this.aplicarCorreccionCuadrante(anguloReal);

        if (anguloReal < 22.5 || anguloReal >= 157.5) {
            return 0;
        } else if (anguloReal >= 22.5 && anguloReal < 67.5) {
            return 45;
        } else if (anguloReal >= 67.5 && anguloReal < 112.5) {
            return 90;
        } else if (anguloReal >= 112.5 && anguloReal < 157.5) {
            return 135;
        } else {
            throw new RuntimeException(
                    "Ángulo no encontrado");
        }

    }

    private double aplicarCorreccionCuadrante(double anguloReal) {
        if (anguloReal < 0) {
            anguloReal += 180;
        }
        return anguloReal;
    }

}

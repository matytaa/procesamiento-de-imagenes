package core.service.statistics;

import java.util.Random;

public class GeneradorDeRandomsService {

    private Random random;

    public GeneradorDeRandomsService(Random random) {
        this.random = random;
    }

    // y = (-1/lambda) * log(x)
    public double generarNumeroExponencial(double lambda) {
        return (-1 / lambda) * (Math.log(randomEntre0Y1())); // NUMERO EXPONENCIAL
    }

    // https://en.wikipedia.org/wiki/Rayleigh_distribution
    public double generarNumeroRayleigh(double psi) {
        return psi * Math.sqrt(-2 * Math.log(1 - randomEntre0Y1())); // NUMERO RAYLEIGH
    }

    // y = (((y1 + y2) / 2) * sigma) + mu
    public double generarNumeroGaussiano(double mu, double sigma) {

        double primerNumeroUniforme = randomEntre0Y1();
        double segundoNumeroUniforme = randomEntre0Y1();

        //ASEGURO QUE NINGUNO DE LOS NUMEROS SEAN 0, SINO EL LOGARITMO NO EXISTE
        while (primerNumeroUniforme * segundoNumeroUniforme == 0) {
            primerNumeroUniforme = Math.random();
            segundoNumeroUniforme = Math.random();
        }

        // y1 = (-2 * log(x1) * cos(2 * pi * x2))^(1/2)
        double primerNumeroGaussiano = (Math.sqrt(-2 * Math.log(primerNumeroUniforme))) * Math.cos(2 * Math.PI * segundoNumeroUniforme);

        // y1 = (-2 * log(x1) * sen(2 * pi * x2))^(1/2)
        double segundoNumeroGaussiano = (Math.sqrt(-2 * Math.log(primerNumeroUniforme))) * Math.sin(2 * Math.PI * segundoNumeroUniforme);

        //LA SUMA DE DOS NUMEROS GAUSSIANOS ES UN NUMERO GAUSSIANO TAMBIEN
        double standardGaussianNumber = (primerNumeroGaussiano + segundoNumeroGaussiano) / 2;
        return standardGaussianNumber * sigma + mu;
    }

    public Integer segunDistribucionUniforme(Integer origen, Integer limite) {
        int delta = limite - origen;
        return (int) (randomEntre0Y1() * delta) + origen;
    }

    private double randomEntre0Y1() {
        return this.random.nextDouble();
    }

    public int[][] generarMatrizRandomGaussianos(int width, int height, double mu, double sigma) {
        int[][] matrizRandom = new int[width][height];
        for(int i=0; i < matrizRandom.length; i++) {
            for (int j=0; j < matrizRandom[i].length; j++) {

                double number = this.generarNumeroGaussiano(mu, sigma);
                matrizRandom[i][j] = (int) (number*100); //AJUSTE PARA EVITAR TODOS CEROS SI EL NRO < 1

            }
        }
        return matrizRandom;
    }

    public int [][] generarMatrizRandomRayleigh(int width, int height, double psi) {
        int matrizRandom[][] = new int[width][height];
        for(int i=0; i < matrizRandom.length; i++) {
            for (int j=0; j < matrizRandom[i].length; j++) {

                double number = this.generarNumeroRayleigh(psi);
                matrizRandom[i][j] = (int) (number*100); //AJUSTE PARA EVITAR TODOS CEROS SI EL NRO < 1

            }
        }
        return matrizRandom;
    }

    public int[][] generarMatrizRandomExponencial(int width, int height, double lambda) {
        int matrizRandom[][] = new int[100][100];
        for(int i=0; i < matrizRandom.length; i++) {
            for (int j=0; j < matrizRandom[i].length; j++) {

                double number = this.generarNumeroExponencial(lambda);
                matrizRandom[i][j] = (int) (number*100); //AJUSTE PARA EVITAR TODOS CEROS SI EL NRO < 1

            }
        }
        return matrizRandom;
    }
}

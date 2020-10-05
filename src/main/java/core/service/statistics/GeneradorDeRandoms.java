package core.service.statistics;

import java.util.Random;

public class GeneradorDeRandoms {

    private Random random;

    public GeneradorDeRandoms(Random random) {
        this.random = random;
    }

    // y = (-1/lambda) * log(x)
    public double generateExponentialNumber(double lambda) {
        return (-1 / lambda) * (Math.log(randomEntre0Y1())); // Exponential Number
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

    public int[][] generateRandomGaussianMatrix(int width, int height, double mu, double sigma) {
        int[][] randomNumberMatrix = new int[width][height];
        for(int i=0; i < randomNumberMatrix.length; i++) {
            for (int j=0; j < randomNumberMatrix[i].length; j++) {

                double number = this.generarNumeroGaussiano(mu, sigma);
                randomNumberMatrix[i][j] = (int) (number*100); //This is a scale adjustment, just to avoid getting all zeros, in case the random number generated is < 1

            }
        }
        return randomNumberMatrix;
    }

    public int [][] generateRandomRayleighMatrix(int width, int height, double psi) {
        int randomNumberMatrix[][] = new int[width][height];
        for(int i=0; i < randomNumberMatrix.length; i++) {
            for (int j=0; j < randomNumberMatrix[i].length; j++) {

                double number = this.generarNumeroRayleigh(psi);
                randomNumberMatrix[i][j] = (int) (number*100); //This is a scale adjustment, just to avoid getting all zeros, in case the random number generated is < 1

            }
        }
        return randomNumberMatrix;
    }

    public int[][] generateRandomExponentialMatrix(int width, int height, double lambda) {
        int randomNumberMatrix[][] = new int[100][100];
        for(int i=0; i < randomNumberMatrix.length; i++) {
            for (int j=0; j < randomNumberMatrix[i].length; j++) {

                double number = this.generateExponentialNumber(lambda);
                randomNumberMatrix[i][j] = (int) (number*100); //This is a scale adjustment, just to avoid getting all zeros, in case the random number generated is < 1

            }
        }
        return randomNumberMatrix;
    }
}

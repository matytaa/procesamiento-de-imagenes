package dominio.difusion;

public abstract class AnisotropicaDifusion implements Difusion {

    private final double sigma;

    public AnisotropicaDifusion(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public int aplicar(Derivativo derivativo) {
        float northDerivativeG = aplicarFuncionG(derivativo.getNorte(), sigma);
        float southDerivativeG = aplicarFuncionG(derivativo.getSur(), sigma);
        float eastDerivativeG = aplicarFuncionG(derivativo.getEste(), sigma);
        float westDerivativeG = aplicarFuncionG(derivativo.getOeste(), sigma);

        float northIij = derivativo.getNorte() * northDerivativeG;
        float southIij = derivativo.getSur() * southDerivativeG;
        float eastIij = derivativo.getEste() * eastDerivativeG;
        float westIij = derivativo.getOeste() * westDerivativeG;

        float lambda = 0.25f;

        //DISCRETIZACION
        return (int) (derivativo.getValor() + lambda * (northIij + southIij + eastIij + westIij));
    }

    public abstract float aplicarFuncionG(float derivative, double sigma);
}

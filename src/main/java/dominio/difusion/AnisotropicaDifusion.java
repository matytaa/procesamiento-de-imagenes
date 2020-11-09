package dominio.difusion;

public abstract class AnisotropicaDifusion implements Difusion {

    private final double sigma;

    public AnisotropicaDifusion(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public int aplicar(Derivada derivada) {
        float derivadaNorteG = aplicarFuncionG(derivada.getNorte(), sigma);
        float derivadaSurG = aplicarFuncionG(derivada.getSur(), sigma);
        float derivadaEsteG = aplicarFuncionG(derivada.getEste(), sigma);
        float derivadaOesteG = aplicarFuncionG(derivada.getOeste(), sigma);

        float norteIij = derivada.getNorte() * derivadaNorteG;
        float surIij = derivada.getSur() * derivadaSurG;
        float esteIij = derivada.getEste() * derivadaEsteG;
        float oesteIij = derivada.getOeste() * derivadaOesteG;

        float lambda = 0.25f;

        //DISCRETIZACION
        return (int) (derivada.getValor() + lambda * (norteIij + surIij + esteIij + oesteIij));
    }

    public abstract float aplicarFuncionG(float derivative, double sigma);
}

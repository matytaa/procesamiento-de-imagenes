package dominio.difusion;

public class DifusionAnisotropicaConLorentz extends AnisotropicaDifusion {

    public DifusionAnisotropicaConLorentz(double sigma) {
        super(sigma);
    }

    @Override
    public float aplicarFuncionG(float derivativo, double sigma) {
        return (float) (1 / ((Math.pow(Math.abs(derivativo), 2) / Math.pow(sigma, 2)) + 1));
    }
}

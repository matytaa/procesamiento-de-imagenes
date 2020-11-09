package dominio.difusion;

public class DifusionAnisotropicaConLeclerc extends AnisotropicaDifusion {

    public DifusionAnisotropicaConLeclerc(double sigma) {
        super(sigma);
    }

    @Override
    public float aplicarFuncionG(float derivative, double sigma) {
        return (float) Math.exp((-Math.pow(Math.abs(derivative), 2)) / Math.pow(sigma, 2));
    }
}

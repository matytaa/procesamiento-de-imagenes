package dominio.difusion;

public interface Difusion {

    int aplicar(Derivada derivada);

    enum Type {
        ISOTROPICA,
        LORENTZ_ANISOTROPICA,
        LECLERC_ANISOTROPICA
    }
}

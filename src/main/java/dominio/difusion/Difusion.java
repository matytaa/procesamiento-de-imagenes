package dominio.difusion;

public interface Difusion {

    int aplicar(Derivativo derivativo);

    enum Type {
        ISOTROPICA,
        LORENTZ_ANISOTROPICA,
        LECLERC_ANISOTROPICA
    }
}

package domain;

import domain.mask.Mascara;

public class SemaforoFiltro {

    private static Mascara.Tipo tipo;

    public static void setValue(Mascara.Tipo newTipo) {
        tipo = newTipo;
    }

    public static Mascara.Tipo getValue() {
        return tipo;
    }

    public static boolean is(Mascara.Tipo value) {
        return tipo.equals(value);
    }
}

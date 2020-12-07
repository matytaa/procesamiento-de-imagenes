package dominio.activecontour;

public class ContornosActivosModo {

    private static boolean esModoSimple;

    public static boolean esSimple() {
        return esModoSimple;
    }

    public static void simple() {
        esModoSimple = true;
    }

    public static void secuenciaDeImagenes() {
        esModoSimple = false;
    }
}

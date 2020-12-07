package dominio.activecontour;

public class ActiveContourMode {

    private static boolean mode;

    public static boolean isSingle() {
        return mode;
    }

    public static void simple() {
        mode = true;
    }

    public static void sequence() {
        mode = false;
    }
}

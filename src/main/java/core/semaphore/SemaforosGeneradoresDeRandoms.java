package core.semaphore;

import dominio.RandomElement;

public class SemaforosGeneradoresDeRandoms {

    private static RandomElement value;

    public static void setValue(RandomElement newValue) { value = newValue;}

    public static RandomElement getValue() { return value; }

}

package dominio;

import java.util.Objects;

public class PuntoXY {

    private final int x;
    private final int y;

    public PuntoXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object o) {
        PuntoXY another = (PuntoXY)o;
        return another.getX() == this.x && another.getY() == this.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}

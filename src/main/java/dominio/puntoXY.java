package dominio;

import java.util.Objects;

public class puntoXY {

    private final int x;
    private final int y;

    public puntoXY(int x, int y) {
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
        puntoXY another = (puntoXY)o;
        return another.getX() == this.x && another.getY() == this.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}

package dominio.hough;

import java.util.Objects;

public class LineaRhoTheeta {

    private double rho;
    private double theta;

    public LineaRhoTheeta(double rho, double theta) {
        this.rho = rho;
        this.theta = theta;
    }

    public double getRho() {return this.rho;}

    public double getTheta() {return this.theta;}

    @Override
    public boolean equals(Object o) {
        LineaRhoTheeta rhoTheta = (LineaRhoTheeta)o;
        return ((rhoTheta.getRho() == this.rho) && (rhoTheta.getTheta() == this.theta));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rho, this.theta);
    }
}

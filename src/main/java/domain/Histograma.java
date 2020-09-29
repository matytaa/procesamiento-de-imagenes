package domain;

public class Histograma {

    private final double[] valores;
    private final Integer totalPixeles;
    private final Double valorMinimo;

    public Histograma(double[] valores, Integer totalPixeles, Double valorMinimo) {
        this.valores = valores;
        this.totalPixeles = totalPixeles;
        this.valorMinimo = valorMinimo;
    }

    public double[] getValores() {
        return valores;
    }

    public Integer getTotalPixeles() {
        return totalPixeles;
    }

    public Double getValorMinimo() {
        return valorMinimo;
    }
}

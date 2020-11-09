package dominio.difusion;

public class DifusionIsotropica implements Difusion {
    
    @Override
    public int aplicar(Derivada derivada) {
        float lambda = 0.25f;
        float suma = derivada.getNorte() + derivada.getSur() + derivada.getEste() + derivada.getOeste();
        return (int)(derivada.getValor() + lambda * suma);
    }
}
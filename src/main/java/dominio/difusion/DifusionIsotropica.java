package dominio.difusion;

public class DifusionIsotropica implements Difusion {
    
    @Override
    public int aplicar(Derivativo derivativo) {
        float lambda = 0.25f;
        float suma = derivativo.getNorte() + derivativo.getSur() + derivativo.getEste() + derivativo.getOeste();
        return (int)(derivativo.getValor() + lambda * suma);
    }
}
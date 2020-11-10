package dominio.automaticthreshold;

import dominio.customimage.Pixel;

import java.util.ArrayList;
import java.util.List;

public class GruposDeUmbralesGlobales {

    private List<Pixel> pixelsGrupo1;
    private List<Pixel> pixelsGrupo2;

    public GruposDeUmbralesGlobales(){
        this.pixelsGrupo1 = new ArrayList<Pixel>();
        this.pixelsGrupo2 = new ArrayList<Pixel>();
    }

    public int getG1(){
        return this.pixelsGrupo1.size();
    }

    public int getG2(){
        return this.pixelsGrupo2.size();
    }

    public void agregarPixelAlGrupo1(Pixel pixel){
        this.pixelsGrupo1.add(pixel);
    }

    public void agregarPixelAlGrupo2(Pixel pixel){
        this.pixelsGrupo2.add(pixel);
    }

    public List<Pixel> getPixelsGrupo1(){
        return this.pixelsGrupo1;
    }

    public List<Pixel> getPixelsGrupo2(){
        return this.pixelsGrupo2;
    }
}

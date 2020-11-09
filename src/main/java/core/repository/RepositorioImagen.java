package core.repository;

import dominio.customimage.Imagen;

import java.util.List;
import java.util.Optional;

public class RepositorioImagen {

    private Imagen imagen;
    private Imagen imagenModificada;
    private Imagen imagenOriginalBackup;
    private List<Imagen> imagenes;

    public Imagen salvarImagen(Imagen imagen) {
        this.imagen = imagen;
        return this.imagen;
    }

    public Optional<Imagen> obtenerImagen() {
        return Optional.ofNullable(this.imagen);
    }

    public Imagen salvarImagenModificada(Imagen imagen) {
        this.imagenModificada = imagen;
        return imagenModificada;
    }

    public Optional<Imagen> getImagenModificada() {
        return Optional.ofNullable(this.imagenModificada);
    }

    public void setImagenOriginalBackup(Imagen imagenOriginalBackup) {
        this.imagenOriginalBackup = imagenOriginalBackup;
    }

    public Imagen getImagenOriginalBackup(){
        return this.imagenOriginalBackup;
    }

    public List<Imagen> guardarSecuenciaImagenes(List<Imagen> secuenciaImagenes) {
        if(!secuenciaImagenes.isEmpty()) this.imagen = secuenciaImagenes.get(0);
        this.imagenes = secuenciaImagenes;
        return secuenciaImagenes;
    }

    public Optional<List<Imagen>> getImagenes() {
        return Optional.ofNullable(imagenes);
    }
}

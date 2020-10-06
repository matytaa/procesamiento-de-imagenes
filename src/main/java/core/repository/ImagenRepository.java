package core.repository;

import domain.customimage.Imagen;

import java.util.List;
import java.util.Optional;

public class ImageRepository {

    private Imagen image;
    private Imagen modifiedImage;
    private Imagen originalImageBackup;
    private List<Imagen> imageSequence;

    public Imagen saveImage(Imagen image) {
        this.image = image;
        return this.image;
    }

    public Optional<Imagen> getImage() {
        return Optional.ofNullable(this.image);
    }

    public Imagen saveModifiedImage(Imagen image) {
        this.modifiedImage = image;
        return modifiedImage;
    }

    public Optional<Imagen> getModifiedImage() {
        return Optional.ofNullable(this.modifiedImage);
    }

    public void setOriginalImageBackup(Imagen originalImageBackup) {
        this.originalImageBackup = originalImageBackup;
    }

    public Imagen getOriginalImageBackup(){
        return this.originalImageBackup;
    }

    public List<Imagen> guardarSecuenciaImagenes(List<Imagen> secuenciaImagenes) {
        if(!secuenciaImagenes.isEmpty()) this.image = secuenciaImagenes.get(0);
        this.imageSequence = secuenciaImagenes;
        return secuenciaImagenes;
    }

    public Optional<List<Imagen>> getImageSequence() {
        return Optional.ofNullable(imageSequence);
    }
}

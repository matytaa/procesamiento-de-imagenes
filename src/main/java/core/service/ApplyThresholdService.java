package core.service;

import domain.customimage.Imagen;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ApplyThresholdService {

    private ModifyImageService modifyImageService;

    public ApplyThresholdService(ModifyImageService modifyImageService){
        this.modifyImageService = modifyImageService;
    }

    public Image aplicarUmbral(Imagen imagenAUmbralizar, int umbral) {
        WritableImage imagenUmbralizada = new WritableImage(imagenAUmbralizar.getAncho(), imagenAUmbralizar.getAltura());
        PixelWriter pixelWriter = imagenUmbralizada.getPixelWriter();

        for (int i=0; i < imagenUmbralizada.getWidth(); i++)
            for(int j=0; j < imagenUmbralizada.getHeight(); j++)
                if (imagenAUmbralizar.getPromedioPixel(i,j) >= umbral)
                    this.modifyImageService.modifySinglePixel(i, j, 255, pixelWriter);
                else
                    this.modifyImageService.modifySinglePixel(i, j, 0, pixelWriter);

        return imagenUmbralizada;
    }

    public int[][] aplicarUmbral(int[][] imagenAUmbralizar, int umbral){
        int width = imagenAUmbralizar.length;
        int height = imagenAUmbralizar[0].length;
        int[][] transformedMatrix = new int[width][height];

        for (int i=0; i < width; i++) {
            for(int j=0; j < height; j++) {
                if (imagenAUmbralizar[i][j] >= umbral) {
                    transformedMatrix[i][j] = 255;
                } else {
                    transformedMatrix[i][j] = 0;
                }
            }
        }
        return transformedMatrix;
    }
}

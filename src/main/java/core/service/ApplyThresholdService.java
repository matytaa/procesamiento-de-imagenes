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

    public Image applyThreshold(Imagen customImage, int threshold) {
        WritableImage image = new WritableImage(customImage.getAncho(), customImage.getAltura());
        PixelWriter pixelWriter = image.getPixelWriter();

        for (int i=0; i < image.getWidth(); i++) {
            for(int j=0; j < image.getHeight(); j++) {
                if (customImage.getPromedioPixel(i,j) >= threshold) {
                    this.modifyImageService.modifySinglePixel(i, j, 255, pixelWriter);
                } else {
                    this.modifyImageService.modifySinglePixel(i, j, 0, pixelWriter);
                }
            }
        }

        return image;
    }

    public int[][] applyThreshold(int[][] image, int threshold){
        int width = image.length;
        int height = image[0].length;
        int[][] transformedMatrix = new int[width][height];

        for (int i=0; i < width; i++) {
            for(int j=0; j < height; j++) {
                if (image[i][j] >= threshold) {
                    transformedMatrix[i][j] = 255;
                } else {
                    transformedMatrix[i][j] = 0;
                }
            }
        }
        return transformedMatrix;
    }
}

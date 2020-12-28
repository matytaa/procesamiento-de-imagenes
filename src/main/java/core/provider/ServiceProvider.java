package core.provider;

import core.service.*;
import core.service.generation.HistogramaService;
import core.service.generation.FiguraImagenService;
import core.service.generation.ImageGradientService;
import core.service.statistics.GrayLevelStatisticsService;
import core.service.statistics.GeneradorDeRandomsService;
import core.service.transformations.TransformRGBtoHSVImageService;
import javafx.stage.FileChooser;

import java.util.Random;

public class ServiceProvider {

    public static ServicioAbrirArchivo provideOpenFileService() {
        return new ServicioAbrirArchivo(createFileChooser());
    }

    private static FileChooser createFileChooser() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open customImage");
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
                        new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                        new FileChooser.ExtensionFilter("PNG", "*.png"),
                        new FileChooser.ExtensionFilter("RAW", "*.raw"),
                        new FileChooser.ExtensionFilter("PGM", "*.pgm"),
                        new FileChooser.ExtensionFilter("PPM", "*.ppm"),
                        new FileChooser.ExtensionFilter("BMP", "*.bmp"));
        return fileChooser;
    }

    public static ServicioImagenCruda provideImageRawService() {
        return new ServicioImagenCruda();
    }

    public static ModifyImageService provideModifyImageService() {
        return new ModifyImageService();
    }

    public static FiguraImagenService provideImageFigureService() {
        return new FiguraImagenService();
    }

    public static ImageGradientService provideGradientService() {
        return new ImageGradientService();
    }

    public static OperacionesImagenesService provideImageOperationsService() {
        return new OperacionesImagenesService(provideGrayLevelStatisticsService());
    }

    public static TransformRGBtoHSVImageService provideTransformRGBtoHSVImageService() {
        return new TransformRGBtoHSVImageService();
    }

    public static GrayLevelStatisticsService provideGrayLevelStatisticsService() {
        return new GrayLevelStatisticsService();
    }

    public static HistogramaService provideHistogramService() {
        return new HistogramaService();
    }

    public static GeneradorDeRandomsService provideRandomNumberGenerationService() {
        return new GeneradorDeRandomsService(new Random());
    }

    public static MatrizService provideMatrixService() {
        return new MatrizService();
    }

    public static AplicarUmbralService provideApplyThresholdService(){
        return new AplicarUmbralService(provideModifyImageService());
    }
}

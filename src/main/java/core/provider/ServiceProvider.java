package core.provider;

import core.service.OpenFileService;
import core.service.*;
import javafx.stage.FileChooser;

public class ServiceProvider {
    public static OpenFileService provideOpenFileService() {
        return new OpenFileService(createFileChooser());
    }

    private static FileChooser createFileChooser() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open customImage");
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("RAW", "*.raw"),
                        new FileChooser.ExtensionFilter("PGM", "*.pgm"),
                        new FileChooser.ExtensionFilter("PPM", "*.ppm"));
        return fileChooser;
    }

    public static MatrixService provideMatrixService() {
        return new MatrixService();
    }

    public static ImageRawService provideImageRawService() {
        return new ImageRawService();
    }
}

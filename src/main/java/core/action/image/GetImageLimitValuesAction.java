package core.action.image;

import core.service.statistics.GrayLevelStatisticsService;
import dominio.automaticthreshold.ImageLimitValues;
import dominio.customimage.Imagen;


public class GetImageLimitValuesAction {

    private GrayLevelStatisticsService grayLevelStatisticsService;

    public GetImageLimitValuesAction(GrayLevelStatisticsService grayLevelStatisticsService){
        this.grayLevelStatisticsService = grayLevelStatisticsService;
    }

    public ImageLimitValues execute(Imagen customImage){
        int redChannelMin = this.grayLevelStatisticsService.calcularMinimoNivelDeGris(customImage.getMatrizRed());
        int redChannelMax = this.grayLevelStatisticsService.calcularMaximoNivelDeGris(customImage.getMatrizRed());

        int greenChannelMin = this.grayLevelStatisticsService.calcularMinimoNivelDeGris(customImage.getMatrizGreen());
        int greenChannelMax = this.grayLevelStatisticsService.calcularMaximoNivelDeGris(customImage.getMatrizGreen());

        int blueChannelMin = this.grayLevelStatisticsService.calcularMinimoNivelDeGris(customImage.getMatrizBlue());
        int blueChannelMax = this.grayLevelStatisticsService.calcularMaximoNivelDeGris(customImage.getMatrizBlue());

        int[] min = {redChannelMin, greenChannelMin, blueChannelMin};
        int[] max = {redChannelMax, greenChannelMax, blueChannelMax};

        int finalMin = 255;
        int finalMax = 0;

        for (int i = 0; i < 3; i++){
            if (max[i] >= finalMax){
                finalMax = max[i];
            }

            if (min[i] <= finalMin){
                finalMin = min[i];
            }
        }

        ImageLimitValues imageLimitValues = new ImageLimitValues(finalMin, finalMax);

        return imageLimitValues;
    }
}

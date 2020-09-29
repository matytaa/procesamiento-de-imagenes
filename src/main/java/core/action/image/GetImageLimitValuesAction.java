package core.action.image;

import core.service.statistics.GrayLevelStatisticsService;
import domain.automaticthreshold.ImageLimitValues;
import domain.customimage.Imagen;


public class GetImageLimitValuesAction {

    private GrayLevelStatisticsService grayLevelStatisticsService;

    public GetImageLimitValuesAction(GrayLevelStatisticsService grayLevelStatisticsService){
        this.grayLevelStatisticsService = grayLevelStatisticsService;
    }

    public ImageLimitValues execute(Imagen customImage){
        int redChannelMin = this.grayLevelStatisticsService.calculateMinGrayLevel(customImage.getMatrizRed());
        int redChannelMax = this.grayLevelStatisticsService.calculateMaxGrayLevel(customImage.getMatrizRed());

        int greenChannelMin = this.grayLevelStatisticsService.calculateMinGrayLevel(customImage.getMatrizGreen());
        int greenChannelMax = this.grayLevelStatisticsService.calculateMaxGrayLevel(customImage.getMatrizGreen());

        int blueChannelMin = this.grayLevelStatisticsService.calculateMinGrayLevel(customImage.getMatrizBlue());
        int blueChannelMax = this.grayLevelStatisticsService.calculateMaxGrayLevel(customImage.getMatrizBlue());

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

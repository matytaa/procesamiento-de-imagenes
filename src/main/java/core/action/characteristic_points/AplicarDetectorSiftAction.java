package core.action.characteristic_points;

import dominio.customimage.Imagen;
import dominio.sift.ResultadoSift;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;

public class AplicarDetectorSiftAction {

    // https://es.coursera.org/lecture/clasificacion-imagenes/deteccion-de-caracteristicas-locales-sift-NC6SJ


    private static int ITERACIONES = 2000;
    private static Double LIMITE = 5.0;
    private static Double PORCENTAJE = 0.5;

    public ResultadoSift ejecutar(Imagen origen, Imagen destino) {

        //CONVERTIR A IMAGEN MBF
        boolean alpha = true;
        MBFImage imagenOrigen = ImageUtilities.createMBFImage(origen.getBufferedImage(), alpha);
        MBFImage imagenDestino = ImageUtilities.createMBFImage(destino.getBufferedImage(), alpha);

        //CALCULAR DESCRIPTORES SIFT
        DoGSIFTEngine motorSIFT = new DoGSIFTEngine();
        LocalFeatureList<Keypoint> puntosInteresOrigen = motorSIFT.findFeatures(imagenOrigen.flatten());
        LocalFeatureList<Keypoint> puntosInteresDestino = motorSIFT.findFeatures(imagenDestino.flatten());

        /*
          Basic keypoint matcher. Matches keypoints by finding closest Two keypoints to
          target and checking whether the distance between the two matches is
          sufficiently large.
          This is the method for determining matches suggested by Lowe in the original
          SIFT papers.
         */

        //apply basic matcher
//        LocalFeatureMatcher<Keypoint> matcher = new BasicMatcher<>(80);
//        matcher.setModelFeatures(queryKeypoints);
//        matcher.findMatches(targetKeypoints);

        FastBasicKeypointMatcher<Keypoint> keypointMatcher = new FastBasicKeypointMatcher<>();
        RANSAC.PercentageInliersStoppingCondition stoppingCondition = new RANSAC.PercentageInliersStoppingCondition(PORCENTAJE);

        RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(LIMITE, ITERACIONES, stoppingCondition);
        LocalFeatureMatcher<Keypoint> matcher = new ConsistentLocalFeatureMatcher2d<>(keypointMatcher, modelFitter);

        matcher.setModelFeatures(puntosInteresOrigen);
        matcher.findMatches(puntosInteresDestino);

        //OBTENER LAS COINCIDENCIAS CONSISTENTES
        MBFImage consistentMatches = MatchingUtilities.drawMatches(imagenOrigen, imagenDestino, matcher.getMatches(), RGBColour.BLUE);

        //PONER LA INFORMACION EN UNA CLASE PARA MOSTRARLA
        return new ResultadoSift(puntosInteresOrigen.size(), puntosInteresDestino.size(), matcher.getMatches().size(), consistentMatches);
    }

}

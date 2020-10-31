package presentation.presenter;

import core.action.edgedetector.ApplyDirectionalDerivativeOperatorAction;
import core.action.image.GetImageAction;
import domain.SemaforoFiltro;
import domain.customimage.Imagen;
import domain.mask.Mascara;
import domain.mask.directional_derivative_operator.kirsh.KirshHorizontalStraightMascara;
import domain.mask.directional_derivative_operator.kirsh.KirshMainDiagonalMascara;
import domain.mask.directional_derivative_operator.kirsh.KirshSecondaryDiagonalMascara;
import domain.mask.directional_derivative_operator.kirsh.KirshVerticalStraightMascara;
import domain.mask.directional_derivative_operator.prewitt.PrewittHorizontalStraightMascara;
import domain.mask.directional_derivative_operator.prewitt.PrewittMainDiagonalMascara;
import domain.mask.directional_derivative_operator.prewitt.PrewittSecondaryDiagonalMascara;
import domain.mask.directional_derivative_operator.prewitt.PrewittVerticalStraightMascara;
import domain.mask.directional_derivative_operator.sobel.MascaraSobelHorizontalDireccionalDerivativo;
import domain.mask.directional_derivative_operator.sobel.MascaraSobelDiagonalPrincipalDireccionalDerivativo;
import domain.mask.directional_derivative_operator.sobel.MascaraSobelDiagonalSecundariaDireccionalDerivativo;
import domain.mask.directional_derivative_operator.sobel.MascaraSobelVerticalDireccionalDerivativo;
import domain.mask.directional_derivative_operator.standard.StandardHorizontalStraightMascara;
import domain.mask.directional_derivative_operator.standard.StandardMainDiagonalMascara;
import domain.mask.directional_derivative_operator.standard.StandardSecondaryDiagonalMascara;
import domain.mask.directional_derivative_operator.standard.StandardVerticalStraightMascara;

public class DirectionalDerivativeOperatorPresenter {

    private final GetImageAction getImageAction;
    private final ApplyDirectionalDerivativeOperatorAction applyDirectionalDerivativeOperatorAction;

    public DirectionalDerivativeOperatorPresenter(GetImageAction getImageAction,
                                                  ApplyDirectionalDerivativeOperatorAction applyDirectionalDerivativeOperatorAction) {

        this.getImageAction = getImageAction;
        this.applyDirectionalDerivativeOperatorAction = applyDirectionalDerivativeOperatorAction;
    }

    public void onInitialize() {
        this.getImageAction.execute()
                .ifPresent(customImage -> {
                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_SOBEL))
                        this.aplicarMascaraDerivativaDireccionalDeSobel(customImage);
                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_STANDARD)) {
                        this.applyStandardMask(customImage);
                    }

                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_KIRSH)) {
                        this.applyKirshMask(customImage);
                    }

                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_PREWITT)) {
                        this.applyPrewittMask(customImage);
                    }
                });
    }

    private void applyStandardMask(Imagen customImage) {
        Mascara horizontalStraightMascara = new StandardHorizontalStraightMascara();
        Mascara verticalStraightMascara = new StandardVerticalStraightMascara();
        Mascara mainDiagonalMascara = new StandardMainDiagonalMascara();
        Mascara secondaryDiagonalMascara = new StandardSecondaryDiagonalMascara();

        applyDirectionalDerivativeOperatorAction.execute(customImage,
                horizontalStraightMascara, verticalStraightMascara,
                mainDiagonalMascara, secondaryDiagonalMascara);
    }

    private void applyKirshMask(Imagen customImage) {
        Mascara horizontalStraightMascara = new KirshHorizontalStraightMascara();
        Mascara verticalStraightMascara = new KirshVerticalStraightMascara();
        Mascara mainDiagonalMascara = new KirshMainDiagonalMascara();
        Mascara secondaryDiagonalMascara = new KirshSecondaryDiagonalMascara();

        applyDirectionalDerivativeOperatorAction.execute(customImage,
                horizontalStraightMascara, verticalStraightMascara,
                mainDiagonalMascara, secondaryDiagonalMascara);
    }

    private void applyPrewittMask(Imagen customImage) {
        Mascara horizontalStraightMascara = new PrewittHorizontalStraightMascara();
        Mascara verticalStraightMascara = new PrewittVerticalStraightMascara();
        Mascara mainDiagonalMascara = new PrewittMainDiagonalMascara();
        Mascara secondaryDiagonalMascara = new PrewittSecondaryDiagonalMascara();

        applyDirectionalDerivativeOperatorAction.execute(customImage,
                horizontalStraightMascara, verticalStraightMascara,
                mainDiagonalMascara, secondaryDiagonalMascara);
    }

    private void aplicarMascaraDerivativaDireccionalDeSobel(Imagen customImage) {
        Mascara mascaraSobelHorizontalDireccionalDerivativo = new MascaraSobelHorizontalDireccionalDerivativo();
        Mascara mascaraSobelVerticalDireccionalDerivativo = new MascaraSobelVerticalDireccionalDerivativo();
        Mascara mascaraSobelDiagonalPrincipalDireccionalDerivativo = new MascaraSobelDiagonalPrincipalDireccionalDerivativo();
        Mascara mascaraSobelDiagonalSecundariaDireccionalDerivativo = new MascaraSobelDiagonalSecundariaDireccionalDerivativo();

        applyDirectionalDerivativeOperatorAction.execute(customImage,
                mascaraSobelHorizontalDireccionalDerivativo, mascaraSobelVerticalDireccionalDerivativo,
                mascaraSobelDiagonalPrincipalDireccionalDerivativo, mascaraSobelDiagonalSecundariaDireccionalDerivativo);
    }
}

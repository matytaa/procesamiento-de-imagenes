package presentation.presenter;

import core.action.edgedetector.AplicarOperadorDireccionalDerivativoAction;
import core.action.image.GetImageAction;
import domain.SemaforoFiltro;
import domain.customimage.Imagen;
import domain.mask.Mascara;
import domain.mask.directional_derivative_operator.kirsh.MascaraKirshHorizontalDireccionalDerivativo;
import domain.mask.directional_derivative_operator.kirsh.MascaraKirshDiagonalPrincipalDireccionalDerivativo;
import domain.mask.directional_derivative_operator.kirsh.MascaraKirshDiagonalSecundariaDireccionalDerivativo;
import domain.mask.directional_derivative_operator.kirsh.MascaraKirshVerticalDireccionalDerivativo;
import domain.mask.directional_derivative_operator.prewitt.MascaraPrewittHorizontalDireccionalDerivativo;
import domain.mask.directional_derivative_operator.prewitt.MascaraPrewittDiagonalPrincipalDireccionalDerivativo;
import domain.mask.directional_derivative_operator.prewitt.MascaraPrewittDiagonalSecundariaDireccionalDerivativo;
import domain.mask.directional_derivative_operator.prewitt.MascaraPrewittVerticalDireccionalDerivativo;
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
    private final AplicarOperadorDireccionalDerivativoAction aplicarOperadorDireccionalDerivativoAction;

    public DirectionalDerivativeOperatorPresenter(GetImageAction getImageAction,
                                                  AplicarOperadorDireccionalDerivativoAction aplicarOperadorDireccionalDerivativoAction) {

        this.getImageAction = getImageAction;
        this.aplicarOperadorDireccionalDerivativoAction = aplicarOperadorDireccionalDerivativoAction;
    }

    public void onInitialize() {
        this.getImageAction.execute()
                .ifPresent(customImage -> {
                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_SOBEL))
                        this.aplicarMascaraDerivativaDireccionalDeSobel(customImage);
                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_PREWITT))
                        this.aplicarMascaraDerivativaDireccionalDePrewitt(customImage);
                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_KIRSH))
                        this.aplicarMascaraDerivativaDireccionalDeKirsh(customImage);
                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_STANDARD)) {
                        this.applyStandardMask(customImage);
                    }


                });
    }

    private void applyStandardMask(Imagen customImage) {
        Mascara horizontalStraightMascara = new StandardHorizontalStraightMascara();
        Mascara verticalStraightMascara = new StandardVerticalStraightMascara();
        Mascara mainDiagonalMascara = new StandardMainDiagonalMascara();
        Mascara secondaryDiagonalMascara = new StandardSecondaryDiagonalMascara();

        aplicarOperadorDireccionalDerivativoAction.executar(customImage,
                horizontalStraightMascara, verticalStraightMascara,
                mainDiagonalMascara, secondaryDiagonalMascara);
    }

    private void aplicarMascaraDerivativaDireccionalDeKirsh(Imagen customImage) {
        Mascara horizontalStraightMascara = new MascaraKirshHorizontalDireccionalDerivativo();
        Mascara verticalStraightMascara = new MascaraKirshVerticalDireccionalDerivativo();
        Mascara mainDiagonalMascara = new MascaraKirshDiagonalPrincipalDireccionalDerivativo();
        Mascara secondaryDiagonalMascara = new MascaraKirshDiagonalSecundariaDireccionalDerivativo();

        aplicarOperadorDireccionalDerivativoAction.executar(customImage,
                horizontalStraightMascara, verticalStraightMascara,
                mainDiagonalMascara, secondaryDiagonalMascara);
    }

    private void aplicarMascaraDerivativaDireccionalDePrewitt(Imagen customImage) {
        Mascara mascaraPrewittHorizontalDireccionalDerivativo = new MascaraPrewittHorizontalDireccionalDerivativo();
        Mascara mascaraPrewittVerticalDireccionalDerivativo = new MascaraPrewittVerticalDireccionalDerivativo();
        Mascara mascaraPrewittDiagonalPrincipalDireccionalDerivativo = new MascaraPrewittDiagonalPrincipalDireccionalDerivativo();
        Mascara mascaraPrewittDiagonalSecundariaDireccionalDerivativo = new MascaraPrewittDiagonalSecundariaDireccionalDerivativo();

        aplicarOperadorDireccionalDerivativoAction.executar(customImage,
                mascaraPrewittHorizontalDireccionalDerivativo,
                mascaraPrewittVerticalDireccionalDerivativo,
                mascaraPrewittDiagonalPrincipalDireccionalDerivativo,
                mascaraPrewittDiagonalSecundariaDireccionalDerivativo);
    }

    private void aplicarMascaraDerivativaDireccionalDeSobel(Imagen customImage) {
        Mascara mascaraSobelHorizontalDireccionalDerivativo = new MascaraSobelHorizontalDireccionalDerivativo();
        Mascara mascaraSobelVerticalDireccionalDerivativo = new MascaraSobelVerticalDireccionalDerivativo();
        Mascara mascaraSobelDiagonalPrincipalDireccionalDerivativo = new MascaraSobelDiagonalPrincipalDireccionalDerivativo();
        Mascara mascaraSobelDiagonalSecundariaDireccionalDerivativo = new MascaraSobelDiagonalSecundariaDireccionalDerivativo();

        aplicarOperadorDireccionalDerivativoAction.executar(customImage,
                mascaraSobelHorizontalDireccionalDerivativo,
                mascaraSobelVerticalDireccionalDerivativo,
                mascaraSobelDiagonalPrincipalDireccionalDerivativo,
                mascaraSobelDiagonalSecundariaDireccionalDerivativo);
    }
}

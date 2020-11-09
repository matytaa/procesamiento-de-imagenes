package presentation.presenter;

import core.action.edgedetector.AplicarOperadorDireccionalDerivativoAction;
import core.action.image.ObtenerImagenAction;
import dominio.SemaforoFiltro;
import dominio.customimage.Imagen;
import dominio.mask.Mascara;
import dominio.mask.directional_derivative_operator.kirsh.MascaraKirshHorizontalDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.kirsh.MascaraKirshDiagonalPrincipalDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.kirsh.MascaraKirshDiagonalSecundariaDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.kirsh.MascaraKirshVerticalDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.prewitt.MascaraPrewittHorizontalDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.prewitt.MascaraPrewittDiagonalPrincipalDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.prewitt.MascaraPrewittDiagonalSecundariaDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.prewitt.MascaraPrewittVerticalDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.sobel.MascaraSobelHorizontalDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.sobel.MascaraSobelDiagonalPrincipalDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.sobel.MascaraSobelDiagonalSecundariaDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.sobel.MascaraSobelVerticalDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.standard.MascaraStandardHorizontalDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.standard.MascaraStandardDiagonalPrincipalDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.standard.MascaraStandardDiagonalSecundariaDireccionalDerivativo;
import dominio.mask.directional_derivative_operator.standard.MascaraStandardVerticalDireccionalDerivativo;

public class DirectionalDerivativeOperatorPresenter {

    private final ObtenerImagenAction obtenerImagenAction;
    private final AplicarOperadorDireccionalDerivativoAction aplicarOperadorDireccionalDerivativoAction;

    public DirectionalDerivativeOperatorPresenter(ObtenerImagenAction obtenerImagenAction,
                                                  AplicarOperadorDireccionalDerivativoAction aplicarOperadorDireccionalDerivativoAction) {

        this.obtenerImagenAction = obtenerImagenAction;
        this.aplicarOperadorDireccionalDerivativoAction = aplicarOperadorDireccionalDerivativoAction;
    }

    public void onInitialize() {
        this.obtenerImagenAction.ejecutar()
                .ifPresent(customImage -> {
                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_SOBEL))
                        this.aplicarMascaraDerivativaDireccionalDeSobel(customImage);
                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_PREWITT))
                        this.aplicarMascaraDerivativaDireccionalDePrewitt(customImage);
                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_KIRSH))
                        this.aplicarMascaraDerivativaDireccionalDeKirsh(customImage);
                    if (SemaforoFiltro.is(Mascara.Tipo.DERIVATE_DIRECTIONAL_OPERATOR_STANDARD))
                        this.aplicarMascaraStadardDerivativaDireccional(customImage);
                });
    }

    private void aplicarMascaraStadardDerivativaDireccional(Imagen customImage) {
        Mascara mascaraStandardHorizontalDireccionalDerivativo = new MascaraStandardHorizontalDireccionalDerivativo();
        Mascara mascaraStandardVerticalDireccionalDerivativo = new MascaraStandardVerticalDireccionalDerivativo();
        Mascara mascaraStandardDiagonalPrincipalDireccionalDerivativo = new MascaraStandardDiagonalPrincipalDireccionalDerivativo();
        Mascara mascaraStandardDiagonalSecundariaDireccionalDerivativo = new MascaraStandardDiagonalSecundariaDireccionalDerivativo();

        aplicarOperadorDireccionalDerivativoAction.executar(customImage,
                mascaraStandardHorizontalDireccionalDerivativo,
                mascaraStandardVerticalDireccionalDerivativo,
                mascaraStandardDiagonalPrincipalDireccionalDerivativo,
                mascaraStandardDiagonalSecundariaDireccionalDerivativo);
    }

    private void aplicarMascaraDerivativaDireccionalDeKirsh(Imagen customImage) {
        Mascara mascaraKirshHorizontalDireccionalDerivativo = new MascaraKirshHorizontalDireccionalDerivativo();
        Mascara mascaraKirshVerticalDireccionalDerivativo = new MascaraKirshVerticalDireccionalDerivativo();
        Mascara mascaraKirshDiagonalPrincipalDireccionalDerivativo = new MascaraKirshDiagonalPrincipalDireccionalDerivativo();
        Mascara mascaraKirshDiagonalSecundariaDireccionalDerivativo = new MascaraKirshDiagonalSecundariaDireccionalDerivativo();

        aplicarOperadorDireccionalDerivativoAction.executar(customImage,
                mascaraKirshHorizontalDireccionalDerivativo,
                mascaraKirshVerticalDireccionalDerivativo,
                mascaraKirshDiagonalPrincipalDireccionalDerivativo,
                mascaraKirshDiagonalSecundariaDireccionalDerivativo);
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

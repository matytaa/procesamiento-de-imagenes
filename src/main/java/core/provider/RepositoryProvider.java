package core.provider;

import core.repository.RepositorioImagen;

class RepositoryProvider {

    private static RepositorioImagen repositorioImagen;

    public static RepositorioImagen provideImageRepository() {
        if (repositorioImagen == null) {
            repositorioImagen = new RepositorioImagen();
        }
        return repositorioImagen;
    }

}

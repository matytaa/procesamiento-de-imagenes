package core.provider;

import core.repository.ImagenRepository;

class RepositoryProvider {

    private static ImagenRepository imagenRepository;

    public static ImagenRepository provideImageRepository() {
        if (imagenRepository == null) {
            imagenRepository = new ImagenRepository();
        }
        return imagenRepository;
    }

}

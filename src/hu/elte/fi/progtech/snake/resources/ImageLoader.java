package hu.elte.fi.progtech.snake.resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public final class ImageLoader {

    /**
     * A bufferbe helyezi a megadott képet
     * @param path a kép elérési útja
     * @return a bebufferelt képpel.
     */
    public static BufferedImage readImage(String path) throws IOException {
        try (final InputStream inputStream = ImageLoader.class.getResourceAsStream(path)) {
            assert inputStream != null;
            final BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IOException("No registered ImageReader claims to be able to read the resulting stream." +
                        "(ImageIO.read(...) returns null)");
            }
            return image;
        }
    }

    private ImageLoader() {
        throw new AssertionError("Instances must NOT be constructed from utility classes!");
    }
}
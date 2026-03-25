/*
    While prototype means something different in software development, here it does not mean shitty version of code you will write
    Prototype pattern provides a refernce or base for object creation, which can be used to initiate object with a template state.
    Here, state of object consists of parameters and method an object contains.
    Rather than just cloning, you can also use dymanic parameters to keep count of some sort or recursive value. But better to not use it in interview.
    There is default Cloneable interface for objects but community hate it as they should.
    In nutshell, this is a template loader for classes with added feature of abstraction.
 */

import java.util.HashMap;
import java.util.Map;

public class Prototype {
    public static void main(String[] args) {
        PluginLoader loader = new PluginLoader();
        loader.registerPlugin("Vignette", new VignetteFilter(0.5f, 0.25f, 0));
        Image originalImage = new Image(512, 512, 0.1, 0.25, 0.8);
        System.out.println("Original Image Pixel: " + originalImage.pixels[10][10]);
        ImageFilter vignetteFilter = loader.getFilter("Vignette");
        vignetteFilter.applyFilter(originalImage);
        System.out.println("Filtered Image Pixel: " + originalImage.pixels[10][10]);
    }
}

interface ImageFilter {
    public void applyFilter(Image img);
    public ImageFilter copy();
}

class VignetteFilter implements ImageFilter {
    private float darkIntensity;
    private float width;
    private final Integer filterCount;

    public VignetteFilter(float darkIntensity, float width, Integer filterCount) {
        this.darkIntensity = darkIntensity;
        this.width = width;
        this.filterCount = filterCount;
    }

    public void setDarkIntensity(float darkIntensity) {
        this.darkIntensity = darkIntensity;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public void applyFilter(Image img) {
        double centerX = img.width / 2.0;
        double centerY = img.height / 2.0;
        double maxDistance = Math.sqrt(centerX * centerX + centerY * centerY);
        double startThreshold = maxDistance * this.width;
        for (int y = 0; y < img.height; y++) {
            for (int x = 0; x < img.width; x++) {
                double dx = x - centerX;
                double dy = y - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance > startThreshold) {
                    double factor = (distance - startThreshold) / (maxDistance - startThreshold);
                    if (factor > 1.0) factor = 1.0;
                    double multiplier = 1.0 - (factor * this.darkIntensity);
                    img.pixels[y][x].R *= multiplier;
                    img.pixels[y][x].G *= multiplier;
                    img.pixels[y][x].B *= multiplier;
                }
            }
        }
    }

    @Override
    public ImageFilter copy() {
        //We are using constructor call directly cause this has no mutable parameters otherwise make it properly here
        return new VignetteFilter(darkIntensity, width, filterCount+1);
    }
}

class NoFilter implements ImageFilter {
    @Override
    public void applyFilter(Image img) {
        System.out.println("Nothing done to image");
    }

    @Override
    public ImageFilter copy() {
        return new NoFilter();
    }
}

class Image {
    public final Pixel[][] pixels;
    public final Integer width;
    public final Integer height;

    public Image(Integer width, Integer height, Double R, Double G, Double B) {
        this.width = width;
        this.height = height;
        pixels = new Pixel[width][height];
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                pixels[i][j] = new Pixel(R, G, B);
            }
        }
    }
}

class Pixel {
    public Double R;
    public Double G;
    public Double B;

    public Pixel(Double R, Double G, Double B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }

    @Override
    public String toString() {
        return "R:" + this.R + " G:" + this.G + " B:" + this.B;
    }
}

class PluginLoader {
    private final Map<String, ImageFilter> prototypes = new HashMap<>();

    public void registerPlugin(String name, ImageFilter prototype) {
        prototypes.put(name, prototype);
    }

    public ImageFilter getFilter(String name) {
        if (prototypes.containsKey(name)) {
            return prototypes.get(name).copy();
        }
        System.out.println("No plugin named " + name + " found");
        return new NoFilter();
    }
}
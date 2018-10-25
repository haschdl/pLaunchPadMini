import static java.lang.Math.pow;

public class ImageUtils {

    // sRGB luminance(Y) values
    private static double rY = 0.212655;
    private static double gY = 0.715158;
    private static double bY = 0.072187;

    // Inverse of sRGB "gamma" function. (approx 2.2)
    private static double inv_gam_sRGB(float ic) {
        double c = ic / 255.0;
        if (c <= 0.04045)
            return c / 12.92;
        else
            return pow(((c + 0.055) / (1.055)), 2.4);
    }

    // sRGB "gamma" function (approx 2.2)
    private static double gam_sRGB(double v) {
        if (v <= 0.0031308)
            v *= 12.92;
        else
            v = 1.055 * pow(v, 1.0 / 2.4) - 0.055;
        return v * 255 + 0.5; // This is correct in C++. Other languages may not
        // require +0.5
    }

    // GRAY VALUE ("brightness")
    public static double gray(float r, float g, float b) {
        return gam_sRGB(
                rY * inv_gam_sRGB(r) +
                        gY * inv_gam_sRGB(g) +
                        bY * inv_gam_sRGB(b)
        );
    }

    public static double perceivedL(float r, float g, float b) {
        return Math.sqrt( pow(0.299*r,2) + pow(0.587*g,2) + pow(0.114*b,2) );
    }
}

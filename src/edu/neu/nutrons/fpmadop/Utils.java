package edu.neu.nutrons.fpmadop;

import com.sun.squawk.util.MathUtils;

/**
 * Various useful functions.
 *
 * @author Ziv
 */
public class Utils {

    /**
     * The residue of a number modulo a given base.
     * @param x A number
     * @param base The divisor for which we calculate residues.
     * @return {@code x} mod {@code base}.
     */
    public static double modulo(double base, double x) {
        // Negate if and only if base is negative.
        // (Java's % isn't mathematically pretty in this way.)
        double sign = (base < 0) ? -1 : 1;
        return sign * (Math.abs(x) % Math.abs(base));
    }

    /**
     * Raises the absolute value of a number to a given power, then multiplies
     * by the sign of the original number.
     * @param x A number.
     * @param pow The power to raise {@code x} to.
     * @return {@code sign(x) * abs(x)^pow}.
     */
    public static double absPow(double x, double pow) {
        int sign = (x >= 0) ? 1 : -1;
        return sign * MathUtils.pow(Math.abs(x), Math.abs(pow));
    }

    /**
     * Limits a given number to a specific range.
     * @param min The minimum value the output takes.
     * @param max The maximum value the output takes.
     * @param x A number.
     * @return If {@code x} is outside the return range, returns the appropriate
     * extremum. Otherwise, returns {@code x}.
     */
    public static double limit(double min, double max, double x) {
        return Math.max(Math.min(x, max), min);
    }

    /**
     * Makes a number snap to a certain value if it is sufficiently close.
     * @param center The value snapped to if {@code x} is sufficiently close.
     * @param range The maximum distance {@code x} can be from {@code center}
     * and still snap to {@code center}.
     * @param x A number.
     * @return If {@code x} is within {@code deadband} of {@code center}, return
     * {@code center}. Otherwise, return {@code x}.
     */
    public static double deadband(double center, double range, double x) {
        return (x < (center + range) && x > (center - range))
                ? center : x;
    }
}

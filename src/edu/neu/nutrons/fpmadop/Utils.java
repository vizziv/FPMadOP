package edu.neu.nutrons.fpmadop;

import com.sun.squawk.util.MathUtils;

/**
 * Various useful functions.
 *
 * @author Ziv
 */
public class Utils {

    /**
     * Limits a number to a given range.
     * @param x A number.
     * @param min The minimum of the return range.
     * @param max The maximum of the return range.
     * @return If {@code x} is outside the return range, returns the appropriate
     * extremum. Otherwise, returns {@code x}.
     */
    public static double limit(double x, double min, double max) {
        return Math.max(Math.min(x, max), min);
    }

    /**
     * The residue of a number modulo a given base.
     * @param x A number
     * @param base The divisor for which we calculate residues.
     * @return {@code x} mod {@code base}.
     */
    public static double modulo(double x, double base) {
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
     * Makes a number snap to a certain value if it is sufficiently close.
     * @param x A number.
     * @param deadband The maximum distance {@code x} can be from {@code center}
     * and still snap to {@code center}.
     * @param center The value snapped to if {@code x} is sufficiently close.
     * @return If {@code x} is within {@code deadband} of {@code center}, return
     * center. Otherwise, return {@code x}.
     */
    public static double deadband(double x, double deadband, double center) {
        return (x < (center + deadband) && x > (center - deadband))
                ? center : x;
    }
}

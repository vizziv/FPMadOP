package edu.neu.nutrons.fpmadop;

/**
 * Uses one number to select from a list of others.
 *
 * @author Ziv
 */
public class Multiplexer {

    private Num s;
    private Object[] os;

    /**
     * Creates a multiplexer, using a given {@link Num} to select from a list of
     * objects.
     * @param selector The number that determines which option is chosen.
     * @param objs The list of options.
     */
    public Multiplexer(Num selector, Object[] objs) {
        s = selector;
        os = objs;
    }

    /**
     * The object at index {@code selector.get()} (rounded to nearest integer)
     * of {@code objs}.
     * @return The chosen object.
     */
    public Object getChoice() {
        // Limits selector to valid range, rounds to nearest integer.
        return os[(int)(0.5 + Utils.limit(0, os.length - 1, s.getN()))];
    }
}

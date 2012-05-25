package edu.neu.nutrons.fpmadop;

/**
 * A {@link Num} within a {@link Num}. Useful for feedback loops. Although the
 * wrapped value may be delayed relative to the source by a {@link BlockThread}
 * cycle, it is necessary to have a delay somewhere in the feedback.
 *
 * (Note: I am aware that the suffix "-ception" doesn't have anything to do with
 * nesting. However, thanks to a certain movie, the meaning of this class is
 * immediately explained to many people just by its name, a benefit for which I
 * am willing to pay the price of improper English.)
 *
 * @author Ziv
 */
public class Numception extends NumBlock {

    private Num x = null;
    private double curX = 0;

    /**
     * Create a number that wraps another number. Until another number is passed
     * with {@link Numception#wrap(Num)}, this has a constant value.
     * @param initialValue The value {@link Num#getN()} returns until a number
     * to wrap is given.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     */
    public Numception(double initialValue, BlockThread thread) {
        super(thread);
        curX = initialValue;
    }

    /**
     * Create a number that wraps another number. Until another number is passed
     * with {@link Numception#wrap(Num)}, this has value 0.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     */
    public Numception(BlockThread thread) {
        this(0, thread);
    }

    /**
     * Set the wrapped number. This only works if the current wrapped
     * {@link Num} is not {@code null}. (Basically, do this exactly once.
     * Subsequent calls will be ignored.)
     * @param x A number.
     */
    public void wrap(Num x) {
        if(this.x != null) {
            this.x = x;
        }
    }

    public double getN() {
        return curX;
    }

    protected void handle() {
        if(x != null) {
            curX = x.getN();
        }
    }
}

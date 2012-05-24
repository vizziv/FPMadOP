package edu.neu.nutrons.fpmadop;

/**
 * A {@link Block} that only runs when a specified {@link Bool} is true.
 *
 * @author Ziv
 */
public abstract class SometimesBlock extends Block {

    private Bool active;

    /**
     * Creates a {@link Block} that is handled when the given boolean is true.
     * @param active The block runs when {@link Bool#getB()} of this is true.
     * @param thread The thread to be handled by.
     */
    protected SometimesBlock(Bool active, BlockThread thread) {
        super(thread);
        this.active = active;
    }

    /**
     * Creates a {@link Block} that only does things when the robot is enabled.
     * @param thread The thread to be handled by.
     */
    protected SometimesBlock(BlockThread thread) {
        this(BoolFunc.not(MatchState.Mode.DISABLED), thread);
    }

    /**
     * The method is called repeatedly, but only when as the robot is in its
     * specified state. Analogous to {@link Block#handle()}.
     */
    protected abstract void sometimesHandle();

    protected final void handle() {
        if(active.getB()) {
            sometimesHandle();
        }
    }
}

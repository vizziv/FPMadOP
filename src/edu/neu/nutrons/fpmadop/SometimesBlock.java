package edu.neu.nutrons.fpmadop;

/**
 * Represents a physical subsystem. A {@link Block} that doesn't do work when
 * the robot is disabled.
 *
 * @author Ziv
 */
public abstract class SometimesBlock extends Block {

    private MatchState.Mode mode = MatchState.Mode.DISABLED;
    private boolean reverse = false;

    /**
     * Creates a {@link Block} that only does things during the specified game
     * mode.
     * @param mode When this block does things.
     * @param reverse If true, instead do things when not in the specified mode.
     * @param thread The thread to be handled by.
     */
    protected SometimesBlock(MatchState.Mode mode, boolean reverse,
                             BlockThread thread) {
        super(thread);
        this.mode = mode;
        this.reverse = reverse;
    }

    /**
     * Creates a {@link Block} that only does things during the specified game
     * mode.
     * @param mode When this block does things.
     * @param thread The thread to be handled by.
     */
    protected SometimesBlock(MatchState.Mode mode, BlockThread thread) {
        this(mode, false, thread);
    }

    /**
     * Creates a {@link Block} that only does things when the robot is enabled.
     * @param thread The thread to be handled by.
     */
    protected SometimesBlock(BlockThread thread) {
        this(MatchState.Mode.DISABLED, true, thread);
    }

    /**
     * The method is called repeatedly, but only when as the robot is in its
     * specified state.
     * Physical actuator movement should probably go here. Analogous to
     * {@link Block#handle()}.
     */
    protected abstract void sometimesHandle();

    protected final void handle() {
        if((MatchState.getMode() == mode) != reverse) {
            sometimesHandle();
        }
    }
}

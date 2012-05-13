package edu.neu.nutrons.fpmadop;

/**
 * Both a {@link Bool} and a {@link Block}.
 *
 * @author Ziv
 */
public abstract class BoolBlock extends Block implements Bool {

    /**
    * Creates a boolean block handled by the specified {@link BlockThread}.
    * @param thread The thread to be handled by.
    */
    protected BoolBlock(BlockThread thread) {
        super(thread);
    }
}

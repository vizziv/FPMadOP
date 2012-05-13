package edu.neu.nutrons.fpmadop;

/**
 * Both a {@link Num} and a {@link Block}.
 *
 * @author Ziv
 */
public abstract class NumBlock extends Block implements Num {

    /**
    * Creates a number block handled by the specified {@link BlockThread}.
    * @param thread The thread to be handled by.
    */
    protected NumBlock(BlockThread thread) {
        super(thread);
    }
}

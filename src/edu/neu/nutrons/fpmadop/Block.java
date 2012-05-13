package edu.neu.nutrons.fpmadop;

/**
 * An object with a method that must be called periodically. Each block is
 * assigned to a {@link BlockThread} that calls its {@link Block#handle()}
 * method.
 *
 * @author Ziv
 */
public abstract class Block {

    private BlockThread bt = null;

    /**
     * Creates a block handled by the specified {@link BlockThread}.
     * @param thread The thread to be handled by.
     */
    public Block(BlockThread thread) {
        // TODO: Test leaking.
        setThread(bt);
    }

    /**
     * The {@link BlockThread} this block is being handled by.
     * @return The handling thread.
     */
    public final BlockThread getThread() {
        return bt;
    }

    /**
     * Changes the {@link BlockThread} this block is being handled by. It is
     * safe for either the new thread to be null, which stops the block from
     * being handled.
     * @param thread The new thread.
     */
    protected final void setThread(BlockThread thread) {
        if(bt != null) {
            bt.removeBlock(this);
        }
        bt = thread;
        if(bt != null) {
            bt.addBlock(this);
        }
    }

    /**
     * The method called repeatedly by this block's {@link BlockThread}.
     */
    protected abstract void handle();

    /**
     * If a block has anything to reset, do so.
     */
    public void reset() {}
}

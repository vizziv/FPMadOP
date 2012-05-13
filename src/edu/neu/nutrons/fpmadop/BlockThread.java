package edu.neu.nutrons.fpmadop;

import edu.wpi.first.wpilibj.IterativeRobot;
import java.util.Enumeration;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Handles blocks, objects that have a method that must be called repeatedly.
 * Whenever {@link BlockThread#run()} is called, calls {@link Block#handle()}
 * on every {@link Block} that has been added to it. Generally, blocks add
 * themselves to threads behind the scenes and the programmer never does it
 * manually.
 *
 * @author Ziv
 */
public class BlockThread {

    private static final double MAIN_PERIOD = -1.0;
    private Vector blocks = new Vector();
    private static BlockThread main = null;
    private java.util.Timer loop = null;
    private Num t;
    private NumBlock dt;

    // This class is necessary because run is public in the TimerTask interface,
    // but we want the method that handles all blocks without checking whether
    // the thread is timed or manual to be private.
    private static class BlockThreadTask extends TimerTask {
        private BlockThread bt;
        public BlockThreadTask(BlockThread thread) {
            bt = thread;
        }
        public void run() {
            bt.handleBlocks();
        }
    }

    /**
     * Creates a thread that must be run manually. For example, you might call
     * {@link BlockThread#run()} in your project's overrides of
     * {@link IterativeRobot#autonomousPeriodic()} and
     * {@link IterativeRobot#teleopPeriodic()}.
     */
    public BlockThread() {
        t = new Timer(); // Timer starts in constructor.
        // It's generally a bad idea to pass a null thread, but we're handling
        // this block manually. Kids, don't try this at home!
        dt = NumFunc.delta(t, null);
    }

    /**
     * Creates a thread that is automatically run periodically. It cannot be run
     * manually. (That is, calling {@link BlockThread#run()} will do nothing.)
     * @param period Time in seconds to wait between runs.
     */
    private BlockThread(double period) {
        this();
        // Thread will be run automatically.
        loop = new java.util.Timer();
        // TODO: test leaking.
        loop.schedule(new BlockThreadTask(this), 0, (long)(1000*period));
    }

    /**
     * The main thread, which is the default one that new blocks are added to.
     * @return The main thread.
     */
    public static BlockThread main() {
        if(main == null) {
            // If we want a manual main thread, we can set MAIN_PERIOD negative.
            if(MAIN_PERIOD < 0.0) {
                main = new BlockThread();
            }
            else {
                main = new BlockThread(MAIN_PERIOD);
            }
        }
        return main;
    }

    /**
     * Timer that starts when the thread is constructed. Only useful for
     * relative measurements.
     * @return A {@link Num} whose {@link Num#get()} method returns the amount
     * of time in seconds that has passed since the thread was initialized.
     */
    public Num t() {
        return t;
    }

    /**
     * The amount of time elapsed in the most recent time step.
     * @return A {@link Num} whose {@link Num#get()} method returns the amount
     * of time in seconds that has passed between calls to this thread's
     * {@link BlockThread#run()} method.
     */
    public Num dt() {
        return dt;
    }

    /**
     * Adds a {@link Block} to be handled.
     * @param b The block being added.
     */
    protected void addBlock(Block b) {
        blocks.addElement(b);
    }

    /**
     * Removes a {@link Block} to be handled.
     * @param b The block being removed.
     */
    protected void removeBlock(Block b) {
        blocks.removeElement(b);
    }

    private void handleBlocks() {
        // Handle dt before other blocks that may rely on it.
        dt.handle();
        for(Enumeration e = blocks.elements(); e.hasMoreElements();) {
            Block b = (Block)e.nextElement();
            b.handle();
        }
    }

    /**
     * Calls {@link Block#handle()} of each {@link Block}. Does nothing if this
     * thread is periodic.
     */
    public void run() {
        // If the thread is not periodic, loop is null.
        if(loop == null) {
            handleBlocks();
        }
    }
}

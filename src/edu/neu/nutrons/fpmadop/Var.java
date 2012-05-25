package edu.neu.nutrons.fpmadop;

import edu.neu.nutrons.fpmadop.NumFunc.N;
import java.util.Enumeration;
import java.util.Vector;

/**
 * A number that changes value when it receives signals. Different signals can
 * set the value directly, add a number to it or multiply it by a number. At
 * most one signal is responded to each time step, and the earlier a signal is
 * added, the higher its priority.
 *
 * @author Ziv
 */
public class Var extends NumBlock {

    private double curX = 0;
    private Vector signals = new Vector();

    /**
     * Creates a new settable variable. Responses to signals must be added after
     * construction using {@link Var#addMult(Bool, Num)} and similar methods.
     * Earlier signals take priority over later ones.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     */
    public Var(BlockThread thread) {
        super(thread);
    }

    private static class SetSignal {
        protected Bool signal;
        protected Num x;
        private SetSignal(Bool signal, Num x) {
            this.signal = signal;
            this.x = x;
        }
        private double apply(double x) {
            return this.x.getN();
        }
    }

    private static class AddSignal extends SetSignal {
        private AddSignal(Bool signal, Num x) {
            super(signal, x);
        }
        private double apply(double x) {
            return this.x.getN() + x;
        }
    }

    private static class MultSignal extends SetSignal {
        private MultSignal(Bool signal, Num x) {
            super(signal, x);
        }
        private double apply(double x) {
            return this.x.getN() * x;
        }
    }

    /**
     * When the given signal is true, will set this number to the given number.
     * @param signal A boolean.
     * @param x A number.
     */
    public void addSet(Bool signal, Num x) {
        signals.addElement(new SetSignal(signal, x));
    }

    /**
     * When the given signal is true, will set this number to the given number.
     * @param signal A boolean.
     * @param x A primitive number.
     */
    public void addSet(Bool signal, double x) {
        addSet(signal, N.id(x));
    }

    /**
     * When the given signal is true, will add the given number to this number.
     * @param signal A boolean.
     * @param x A number.
     */
    public void addPlus(Bool signal, Num x) {
        signals.addElement(new AddSignal(signal, x));
    }

    /**
     * When the given signal is true, will add the given number to this number.
     * @param signal A boolean.
     * @param x A primitive number.
     */
    public void addPlus(Bool signal, double x) {
        addPlus(signal, N.id(x));
    }

    /**
     * When the given signal is true, will multiply this number by the given
     * number.
     * @param signal A boolean.
     * @param x A number.
     */
    public void addMult(Bool signal, Num x) {
        signals.addElement(new MultSignal(signal, x));
    }

    /**
     * When the given signal is true, will multiply this number by the given
     * number.
     * @param signal A boolean.
     * @param x A primitive number.
     */
    public void addMult(Bool signal, double x) {
        addMult(signal, N.id(x));
    }

    protected void handle() {
        for(Enumeration e = signals.elements(); e.hasMoreElements();) {
            SetSignal s = (SetSignal)e.nextElement();
            if(s.signal.getB()) {
                curX = s.apply(curX);
                break;
            }
        }
    }

    public double getN() {
        return curX;
    }
}

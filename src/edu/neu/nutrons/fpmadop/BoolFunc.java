package edu.neu.nutrons.fpmadop;

import edu.wpi.first.wpilibj.buttons.Button;

/**
 * Many operators, each of which returns a {@link Bool}.
 *
 * @author Ziv
 */
public class BoolFunc {

    private BoolFunc() {}

    /**
     * Short alias for {@link BoolFunc}.
     */
    public static class BF extends BoolFunc {
        private BF() {}
    }

    // Classes.

    private static class Constant implements Bool {
        private boolean p = false;
        private Constant(boolean p) {
            this.p = p;
        }
        public boolean getB() {
            return p;
        }
    }

    private static class FromButton implements Bool {
        private Button p;
        private FromButton(Button p) {
            this.p = p;
        }
        public boolean getB() {
            return p.get();
        }
    }

    private static class Not implements Bool {
        private Bool p;
        private Not(Bool p) {
            this.p = p;
        }
        public boolean getB() {
            return !p.getB();
        }
    }

    private static class Or implements Bool {
        private Bool[] ps;
        private Or(Bool[] ps) {
            this.ps = ps;
        }
        public boolean getB() {
            boolean ret = false;
            for(int i=0; i<ps.length; i++) {
                ret = ret || ps[i].getB();
            }
            return ret;
        }
    }

    private static class And implements Bool {
        private Bool[] ps;
        private And(Bool[] ps) {
            this.ps = ps;
        }
        public boolean getB() {
            boolean ret = true;
            for(int i=0; i<ps.length; i++) {
                ret = ret && ps[i].getB();
            }
            return ret;
        }
    }

    private static class Xor implements Bool {
        private Bool p, q;
        private Xor(Bool p, Bool q) {
            this.p = p;
            this.q = q;
        }
        public boolean getB() {
            return p.getB() != q.getB();
        }
    }

    private static class Compare implements Bool {
        private boolean less;
        private Num x, y;
        private Compare(boolean less, Num x, Num y) {
            this.less = less;
            this.x = x;
            this.y = y;
        }
        public boolean getB() {
            return (x.getN() <= y.getN()) == less;
        }
    }

    private static class Delay extends BoolBlock {
        private Bool p;
        private boolean[] pastP;
        private int i = 0;
        private Delay(int delay, Bool p, BlockThread thread) {
            super(thread);
            this.p = p;
            this.pastP = new boolean[delay];
        }
        protected void handle() {
            pastP[i] = p.getB();
            i = i+1 % pastP.length;
        }
        public boolean getB() {
            return pastP[i];
        }
    }

    private static class Delta extends BoolBlock {
        private boolean lastP = false;
        private boolean curP = false;
        private Bool p;
        private Delta(Bool p, BlockThread thread) {
            super(thread);
            this.p = p;
        }
        protected void handle() {
            lastP = curP;
            curP = p.getB();
        }
        public boolean getB() {
            return curP != lastP;
        }
    }

    private static class DebounceInt extends BoolBlock {
        private Bool p;
        private boolean state = false;
        private int i = 0;
        private int length = 0;
        private DebounceInt(int length, Bool p, BlockThread thread) {
            super(thread);
            this.p = p;
            this.length = length;
        }
        protected void handle() {
            if(p.getB() != state) {
                i++;
                if(i > length) {
                    state = !state;
                    i = 0;
                }
            }
            else {
                i = 0;
            }
        }
        public boolean getB() {
            return state;
        }
    }

    private static class DebounceDouble extends BoolBlock {
        private Bool p;
        private Num inc;
        private double length = 0.0;
        private double i = 0.0;
        private boolean state = false;
        private DebounceDouble(double length, Num inc, Bool p,
                               BlockThread thread) {
            super(thread);
            this.p = p;
            this.inc = inc;
            this.length = length;
        }
        protected void handle() {
            if(p.getB() != state) {
                i += inc.getN();
                if(i > length) {
                    state = !state;
                    i = 0.0;
                }
            }
            else {
                i = 0.0;
            }
        }
        public boolean getB() {
            return state;
        }
    }

    /**
     * Does nothing. This exists for completeness.
     * @param p A boolean.
     * @return The very same boolean!
     */
    public static Bool id(Bool p) {
        return p;
    }

    /**
     * Turns a {@code boolean} into a {@link Bool}.
     * @param p A primitive boolean.
     * @return A {@link Bool} whose {@link Bool#getB()} method returns {@code p}.
     */
    public static Bool id(boolean p) {
        return new Constant(p);
    }

    /**
     * Turns a {@link Button} into a {@link Bool}.
     * @param p An operator interface or other button.
     * @return A {@link Num} whose {@link Bool#getB()} method returns
     * {@code p.get()}.
     */
    public static Bool id(Button p) {
        return new FromButton(p);
    }

    /**
     * Negates the given boolean.
     * @param p A boolean.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code !p.getB()}.
     */
    public static Bool not(Bool p) {
        return new Not(p);
    }

    /**
     * Is true if any boolean from the given list is true.
     * @param ps A list of booleans.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns true if
     * any {@code Bool#getB()} for {@link Bool} in {@code ps} returns true.
     */
    public static Bool or(Bool[] ps) {
        return new Or(ps);
    }

    /**
     * Is true if any boolean given is true.
     * @param p A boolean.
     * @param q Another boolean.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code p.getB() || q.getB()}.
     */
    public static Bool or(Bool p, Bool q) {
        Bool[] ps = {p, q};
        return or(ps);
    }

    /**
     * Is true if any boolean given is true.
     * @param p A primitive boolean.
     * @param q A boolean.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code p || q.getB()}.
     */
    public static Bool or(boolean p, Bool q) {
        Bool[] ps = {id(p), q};
        return or(ps);
    }

    /**
     * Is true if any boolean given is true.
     * @param p A boolean.
     * @param q Another boolean.
     * @param r Yet another boolean.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code p.getB() || q.getB() || r.getB()}.
     */
    public static Bool or(Bool p, Bool q, Bool r) {
        Bool[] ps = {p, q, r};
        return or(ps);
    }

    /**
     * Is true if all booleans from the given list are true.
     * @param ps A list of booleans.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns true if
     * all {@code Bool#getB()} for {@link Bool} in {@code ps} return true.
     */
    public static Bool and(Bool[] ps) {
        return new And(ps);
    }

    /**
     * Is true if all booleans given are true.
     * @param p A boolean.
     * @param q Another boolean.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code p.getB() && q.getB()}.
     */
    public static Bool and(Bool p, Bool q) {
        Bool[] ps = {p, q};
        return and(ps);
    }

    /**
     * Is true if all booleans given are true.
     * @param p A primitive boolean.
     * @param q A boolean.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code p && q.getB()}.
     */
    public static Bool and(boolean p, Bool q) {
        Bool[] ps = {id(p), q};
        return and(ps);
    }

    /**
     * Is true if all booleans given are true.
     * @param p A boolean.
     * @param q Another boolean.
     * @param r Yet another boolean.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code p.getB() && q.getB() && r.getB()}.
     */
    public static Bool and(Bool p, Bool q, Bool r) {
        Bool[] ps = {p, q, r};
        return and(ps);
    }
    /**
     * Is true if exactly one of the booleans given is true.
     * @param p A boolean.
     * @param q Another boolean.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code p.getB() != q.getB()}.
     */
    public static Bool xor(Bool p, Bool q) {
        return new Xor(p, q);
    }

    /**
     * Is true if exactly one of the booleans given is true.
     * @param p A primitive boolean.
     * @param q A boolean.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code p != q.getB()}.
     */
    public static Bool xor(boolean p, Bool q) {
        return xor(id(p), q);
    }

    /**
     * Is true if the first number is less than the second.
     * @param x A number.
     * @param y Another number.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code x.getN() &lt= y.getN()}.
     */
    public static Bool lessThan(Num x, Num y) {
        return new Compare(true, x, y);
    }

    /**
     * Is true if the first number is less than the second.
     * @param x A primitive number.
     * @param y A number.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code x &lt= y.getN()}.
     */
    public static Bool lessThan(double x, Num y) {
        return lessThan(NumFunc.id(x), y);
    }

    /**
     * Is true if the first number is greater than the second.
     * @param x A number.
     * @param y Another number.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code x.getN() &gt= y.getN()}.
     */
    public static Bool greaterThan(Num x, Num y) {
        return new Compare(false, x, y);
    }

    /**
     * Is true if the first number is greater than the second.
     * @param x A primitive number.
     * @param y A number.
     * @return A {@link Bool} whose {@code Bool#getB()} method returns
     * {@code x &lg= y.getN()}.
     */
    public static Bool greaterThan(double x, Num y) {
        return greaterThan(NumFunc.id(x), y);
    }

    /**
     * A {@link Bool} as it appeared a number of time steps ago. All values
     * before initialization are assumed to be false.
     * @param delay The number of steps to delay {@code p}.
     * @param p A boolean.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Bool} whose {@link Bool#getB()} method returns the value
     * of {@code p.getB()} from {@code delay} samples ago. It is a {@link Block}
     * in the given thread.
     */
    public static BoolBlock delay(int delay, Bool p, BlockThread thread) {
        return new Delay(delay, p, thread);
    }

    /**
     * A {@link Bool} as it appeared a number of time steps ago. All values
     * before initialization are assumed to be false.
     * @param delay The number of steps to delay {@code p}.
     * @param p A boolean.
     * @return A {@link Bool} whose {@link Bool#getB()} method returns the value
     * of {@code p.getB()} from {@code delay} samples ago. It is a {@link Block}
     * in {@link BlockThread#main()}.
     */
    public static BoolBlock delay(int delay, Bool p) {
        return delay(delay, p, BlockThread.main());
    }

    /**
     * Is true if the given {@link Bool} just changed value. The value before
     * initialization is assumed to be false.
     * @param p A boolean.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Bool} whose {@link Bool#getB()} method is true if the two
     * most recent values of {@code p.getB()} are unequal. It is a {@link Block}
     * in the given thread.
     */
    public static BoolBlock delta(Bool p, BlockThread thread) {
        return new Delta(p, thread);
    }

    /**
     * Is true if the given {@link Bool} just changed value. The value before
     * initialization is assumed to be false.
     * @param p A boolean.
     * @return A {@link Bool} whose {@link Bool#getB()} method is true if the two
     * most recent values of {@code p.getB()} are unequal. It is a {@link Block}
     * in {@link BlockThread#main()}.
     */
    public static BoolBlock delta(Bool p) {
        return delta(p, BlockThread.main());
    }

    /**
     * Only changes state if the new state persists for sufficiently long. More
     * specifically, this block has a current state (initially false). Each time
     * step, if {@code p.getB()} is not equal to the state, add
     * {@code inc.getN()} to an accumulator. If the accumulated sum exceeds
     * {@code length}, switch states. If {@code p.getB()} is equal to the state,
     * reset the accumulator.
     * @param length The resistance to state change.
     * @param inc Feeds into the accumulator that triggers state change.
     * @param p A boolean.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Bool} whose {@link Bool#getB()} method switches value
     * only when {@code p.getB()} remains unchanged for sufficiently long. It is
     * a {@link Block} in the given thread.
     */
    public static BoolBlock debounce(double length, Num inc, Bool p,
                                     BlockThread thread) {
        return new DebounceDouble(length, inc, p, thread);
    }

    /**
     * Only changes state if the new state persists for sufficiently long. More
     * specifically, this block has a current state (initially false). Each time
     * step, if {@code p.getB()} is not equal to the state, add
     * {@code inc.getN()} to an accumulator. If the accumulated sum exceeds
     * {@code length}, switch states. If {@code p.getB()} is equal to the state,
     * reset the accumulator.
     * @param length The resistance to state change.
     * @param inc Feeds into the accumulator that triggers state change.
     * @param p A boolean.
     * @return A {@link Bool} whose {@link Bool#getB()} method switches value
     * only when {@code p.getB()} remains unchanged for sufficiently long. It is
     * a {@link Block} in {@link BlockThread#main()}.
     */
    public static BoolBlock debounce(double length, Num inc, Bool p) {
        return debounce(length, inc, p, BlockThread.main());
    }

    /**
     * Only changes state if the new state persists for sufficiently many time
     * steps. More specifically, this block has a current state (initially
     * false). Each time step, if {@code p.getB()} is not equal to the state,
     * add 1 to an accumulator. If the accumulated sum exceeds {@code length},
     * switch states. If {@code p.getB()} is equal to the state, reset the
     * accumulator.
     * @param length The resistance to state change.
     * @param p A boolean.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Bool} whose {@link Bool#getB()} method switches value
     * only when {@code p.getB()} remains unchanged for sufficiently long. It is
     * a {@link Block} in the given thread.
     */
    public static BoolBlock debounceStep(int length, Bool p,
                                         BlockThread thread) {
        return new DebounceInt(length, p, thread);
    }

    /**
     * Only changes state if the new state persists for sufficiently many time
     * steps. More specifically, this block has a current state (initially
     * false). Each time step, if {@code p.getB()} is not equal to the state,
     * add 1 to an accumulator. If the accumulated sum exceeds {@code length},
     * switch states. If {@code p.getB()} is equal to the state, reset the
     * accumulator.
     * @param length The resistance to state change.
     * @param p A boolean.
     * @return A {@link Bool} whose {@link Bool#getB()} method switches value
     * only when {@code p.getB()} remains unchanged for sufficiently long. It is
     * a {@link Block} in {@link BlockThread#main()}.
     */
    public static BoolBlock debounceStep(int length, Bool p) {
        return debounceStep(length, p, BlockThread.main());
    }

    /**
     * Only changes state if the new state persists for a sufficiently long
     * time.
     * @param length The amount of time it takes for the state to change.
     * @param p A boolean.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Bool} whose {@link Bool#getB()} method switches value
     * only when {@code p.getB()} remains unchanged for sufficiently long. It is
     * a {@link Block} in the given thread.
     */
    public static Bool debounceTime(double length, Bool p, BlockThread thread) {
        return debounce(length, thread.dt(), p, thread);
    }

    /**
     * Only changes state if the new state persists for a sufficiently long
     * time.
     * @param length The amount of time it takes for the state to change.
     * @param p A boolean.
     * @return A {@link Bool} whose {@link Bool#getB()} method switches value
     * only when {@code p.getB()} remains unchanged for sufficiently long. It is
     * a {@link Block} in {@link BlockThread#main()}.
     */
    public static Bool debounceTime(double length, Bool p) {
        return debounceTime(length, p, BlockThread.main());
    }

    /**
     * Is true if the given {@link Bool} just changed away from its default
     * state.
     * @param defaultState The default state we expect from {@code p}.
     * @param p A boolean.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Bool} whose {@link Bool#getB()} method is true right
     * after {@code p.getB()} changes away from {@code defaultState}. It is a
     * {@link Block} in the given thread.
     */
    public static Bool pulseTrigger(boolean defaultState, Bool p,
                                    BlockThread thread) {
        return and(delta(p, thread), xor(defaultState, p));
    }

    /**
     * Is true if the given {@link Bool} just changed away from its default
     * state.
     * @param defaultState The default state we expect from {@code p}.
     * @param p A boolean.
     * @return A {@link Bool} whose {@link Bool#getB()} method is true right
     * after {@code p.getB()} changes away from {@code defaultState}. It is a
     * {@link Block} in {@link BlockThread#main()}.
     */
    public static Bool pulseTrigger(boolean defaultState, Bool p) {
        return pulseTrigger(defaultState, p, BlockThread.main());
    }
}

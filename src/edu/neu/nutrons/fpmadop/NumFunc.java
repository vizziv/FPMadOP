package edu.neu.nutrons.fpmadop;

/**
 * Many operators, each of which returns a {@link Num}.
 *
 * @author Ziv
 */
public class NumFunc {

    private NumFunc() {}

    /**
     * Short alias for {@link NumFunc}.
     */
    public static class NF extends NumFunc {
        private NF() {}
    }

    private static class Id implements Num {
        private double x = 0;
        private Id(double x) {
            this.x = x;
        }
        public double get() {
            return x;
        }
    }

    private static class Sum implements Num {
        private Num[] xs;
        private Sum(Num[] xs) {
            this.xs = xs;
        }
        public double get() {
            double ret = 0;
            for(int i=0; i<xs.length; i++) {
                ret += xs[i].get();
            }
            return ret;
        }
    }

    private static class Prod implements Num {
        private Num[] xs;
        private Prod(Num[] xs) {
            this.xs = xs;
        }
        public double get() {
            double ret = 1;
            for(int i=0; i<xs.length; i++) {
                ret *= xs[i].get();
            }
            return ret;
        }
    }

    private static class Diff implements Num {
        private Num x, y;
        private Diff(Num x, Num y) {
            this.x = x;
            this.y = y;
        }
        public double get() {
            return x.get() - y.get();
        }
    }

    private static class Quot implements Num {
        private Num x, y;
        private Quot(Num x, Num y) {
            this.x = x;
            this.y = y;
        }
        public double get() {
            double xVal = x.get();
            double yVal = y.get();
            // 0/0 often occurs upon initialization. Returning 1 in this case
            // is mostly harmless, even though it's a hack.
            if(yVal == 0.0 && Math.abs(xVal) < 0.0001) {
                return 1.0;
            }
            return xVal / yVal;
        }
    }

    private static class BoolToNum implements Num {
        private Bool b;
        private BoolToNum(Bool b) {
            this.b = b;
        }
        public double get() {
            return b.get() ? 1.0 : 0.0;
        }
    }

    private static class NumMux extends Multiplexer implements Num {
        private NumMux(Num s, Num[] xs) {
            super(s, xs);
        }
        public double get() {
            return ((Num)super.choice()).get();
        }
    }

    private static class Delay extends NumBlock {
        private Num x;
        private double[] pastX;
        private int i = 0;
        private Delay(int delay, Num x, BlockThread thread) {
            super(thread);
            this.x = x;
            this.pastX = new double[delay];
        }
        protected void handle() {
            pastX[i] = x.get();
            i = i+1 % pastX.length;
        }
        public double get() {
            return pastX[i];
        }
    }

    private static class Delta extends NumBlock {
        private Num x;
        private double lastX = 0.0;
        private double curX = 0.0;
        private Delta(Num x, BlockThread thread) {
            super(thread);
            this.x = x;
        }
        protected void handle() {
            lastX = curX;
            curX = x.get();
        }
        public double get() {
            return curX - lastX;
        }
    }

    private static class Accumulator extends NumBlock {
        private Num x;
        private double sumX = 0;
        private Accumulator(Num x, BlockThread thread) {
            super(thread);
           this.x = x;
        }
        protected void handle() {
            sumX += x.get();
        }
        public double get() {
            return sumX;
        }
        public void reset() {
            sumX = 0;
        }
    }

    /**
     * Does nothing. This exists for completeness.
     * @param x A number.
     * @return The very same number!
     */
    public static Num id(Num x) {
        return x;
    }

    /**
     * Turns a {@code double} into a {@link Num}.
     * @param x A primitive number.
     * @return A {@link Num} whose {@link Num#get()} method returns {@code x}.
     */
    public static Num id(double x) {
        return new Id(x);
    }

    /**
     * Adds a list of numbers.
     * @param xs A list of numbers.
     * @return A {@link Num} whose {@code Num#get()} method returns the sum of
     * {@code Num#get()} for each {@link Num} in {@code xs}.
     */
    public static Num sum(Num[] xs) {
        return new Sum(xs);
    }

    /**
     * Adds two numbers.
     * @param x A number.
     * @param y Another number.
     * @return A {@link Num} whose {@code Num#get()} method returns
     * {@code x.get() + y.get()}.
     */
    public static Num sum(Num x, Num y) {
        Num[] xs = {x, y};
        return sum(xs);
    }

    /**
     * Adds two numbers, one of them primitive.
     * @param x A primitive number.
     * @param y A number.
     * @return A {@link Num} whose {@code Num#get()} method returns
     * {@code x + y.get()}.
     */
    public static Num sum(double x, Num y) {
        Num[] xs = {id(x), y};
        return sum(xs);
    }

    /**
     * Adds three numbers.
     * @param x A number.
     * @param y Another number.
     * @param z Yet another number.
     * @return A {@link Num} whose {@code Num#get()} method returns
     * {@code x.get() + y.get() + z.get()}.
     */
    public static Num sum(Num x, Num y, Num z) {
        Num[] xs = {x, y, z};
        return sum(xs);
    }

    /**
     * Multiplies a list of numbers.
     * @param xs A list of numbers.
     * @return A {@link Num} whose {@code Num#get()} method returns the product
     * of {@code Num#get()} for each {@link Num} in {@code xs}.
     */
    public static Num prod(Num[] xs) {
        return new Prod(xs);
    }

    /**
     * Multiplies two numbers.
     * @param x A number.
     * @param y Another number.
     * @return A {@link Num} whose {@code Num#get()} method returns
     * {@code x.get() * y.get()}.
     */
    public static Num prod(Num x, Num y) {
        Num[] xs = {x, y};
        return prod(xs);
    }

    /**
     * Multiplies two numbers, one of them primitive.
     * @param x A primitive number.
     * @param y A number.
     * @return A {@link Num} whose {@code Num#get()} method returns
     * {@code x * y.get()}.
     */
    public static Num prod(double x, Num y) {
        Num[] xs = {id(x), y};
        return prod(xs);
    }

    /**
     * Multiplies three numbers.
     * @param x A number.
     * @param y Another number.
     * @param z Yet another number.
     * @return A {@link Num} whose {@code Num#get()} method returns
     * {@code x.get() * y.get() * z.get()}.
     */
    public static Num prod(Num x, Num y, Num z) {
        Num[] xs = {x, y, z};
        return prod(xs);
    }

    /**
     * Subtracts the second number from the first.
     * @param x A number, the minuend.
     * @param y Another number, the subtrahend.
     * @return A {@link Num} whose {@code Num#get()} method returns
     * {@code x.get() - y.get()}.
     */
    public static Num diff(Num x, Num y) {
        return new Diff(x, y);
    }

    /**
     * Subtracts the second number from the first, which is primitive.
     * @param x A primitive number, the minuend.
     * @param y Another number, the subtrahend.
     * @return A {@link Num} whose {@code Num#get()} method returns
     * {@code x - y.get()}.
     */
    public static Num diff(double x, Num y) {
        return new Diff(id(x), y);
    }

    /**
     * The quotient of the first number over the second.
     * @param x A number, the dividend.
     * @param y Another number, the divisor.
     * @return A {@link Num} whose {@code Num#get()} method returns
     * {@code x.get() / y.get()}.
     */
    public static Num quot(Num x, Num y) {
        return new Quot(x, y);
    }

    /**
     * The quotient of the first number, a primitive, over the second.
     * @param x A primitive number, the dividend.
     * @param y Another number, the divisor.
     * @return A {@link Num} whose {@code Num#get()} method returns
     * {@code x / y.get()}.
     */
    public static Num quot(double x, Num y) {
        return new Quot(id(x), y);
    }

    /**
     * Converts a {@link Bool} to a {@link Num}.
     * @param b A boolean.
     * @return A {@link Num} whose {@link Num#get()} method returns one if
     * {@code b.get()} is true and zero otherwise.
     */
    public static Num boolToNum(Bool b) {
        return new BoolToNum(b);
    }

    /**
     * A {@link Num} as it appeared a number of time steps ago. All values
     * before initialization are assumed to be zero.
     * @param delay The number of steps to delay {@code x}.
     * @param x A number.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Num} whose {@link Num#get()} method returns the value of
     * {@code x.get()} from {@code delay} samples ago. It is a {@link Block} in
     * the given thread.
     */
    public static NumBlock delay(int delay, Num x, BlockThread thread) {
        return new Delay(delay, x, thread);
    }

    /**
     * A {@link Num} as it appeared a number of time steps ago. All values
     * before initialization are assumed to be zero.
     * @param delay The number of steps to delay {@code x}.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#get()} method returns the value of
     * {@code x.get()} from {@code delay} samples ago. It uses {@link Block} in
     * {@link BlockThread#main()}.
     */
    public static NumBlock delay(int delay, Num x) {
        return new Delay(delay, x, BlockThread.main());
    }

    /**
     * The change between the two most recent values of the given {@link Num}.
     * @param x A number.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * difference of the two most recent values of {@code x.get()}. It is a
     * {@link Block} in the given thread.
     */
    public static NumBlock delta(Num x, BlockThread thread) {
        return new Delta(x, thread);
    }

    /**
     * The change between the two most recent values of the given {@link Num}.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * difference of the two most recent values of {@code x.get()}. It is a
     * {@link Block} in {@link BlockThread#main()}.
     */
    public static NumBlock delta(Num x) {
        return delta(x, BlockThread.main());
    }

    /**
     * The sum of all previous values of the given {@link Num}.
     * @param x A number.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * sum of all previous values of {@code x.get()}. It is a {@link Block} in
     * the given thread.
     */
    public static NumBlock accumulator(Num x, BlockThread thread) {
        return new Accumulator(x, thread);
    }

    /**
     * The sum of all previous values of the given {@link Num}.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * difference of the two most recent values of {@code x.get()}. It is a
     * {@link Block} in {@link BlockThread#main()}.
     */
    public static NumBlock accumulator(Num x) {
        return accumulator(x, BlockThread.main());
    }

    /**
     * Uses a given {@link Bool} to select one of two numbers to return.
     * @param b A boolean.
     * @param x A number.
     * @param y Another number.
     * @return A {@link Num} whose {@link Num#get()} method returns
     * {@code x.get()} if {@code b.get()} is true and {@code y.get()} otherwise.
     */
    public static Num ifThenElse(Bool b, Num x, Num y) {
        Num[] xs = {x, y};
        return new NumMux(boolToNum(b), xs);
    }

    /**
     * The derivative of the given {@link Num} with respect to time.
     * @param x A number.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * derivative of {@code x.get()} with respect to time. It uses blocks in the
     * given thread.
     */
    public static Num derivative(Num x, BlockThread thread) {
        return quot(delta(x, thread), thread.dt());
    }

    /**
     * The derivative of the given {@link Num} with respect to time.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * derivative of {@code x.get()} with respect to time. It uses blocks in
     * {@link BlockThread#main()}.
     */
    public static Num derivative(Num x) {
        return derivative(x, BlockThread.main());
    }

    /**
     * The integral of the given {@link Num} with respect to time.
     * @param x A number.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * integral of {@code x.get()} with respect to time. It uses blocks in the
     * given thread.
     */
    public static NumBlock integral(Num x, BlockThread thread) {
        return accumulator(prod(x, thread.dt()), thread);
    }

    /**
     * The integral of the given {@link Num} with respect to time.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * integral of {@code x.get()} with respect to time. It uses blocks in
     * {@link BlockThread#main()}.
     */
    public static NumBlock integral(Num x) {
        return integral(x, BlockThread.main());
    }

    /**
     * The sum of the most recent values of the given {@link Num}.
     * @param length The number of recent steps to use.
     * @param x A number.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * sum of the {@code length} most recent values of {@code x.get()}. It uses
     * blocks in the given thread.
     */
    public static Num recentAccumulator(int length, Num x, BlockThread thread) {
        // We could implement this using integral, but by the end of a match
        // we might be subtracting two very large doubles from eachother, which
        // would be imprecise. Hooray for numerical stability!
        return accumulator(diff(x, delay(length, x)), thread);
    }

    /**
     * The sum of the most recent values of the given {@link Num}.
     * @param length The number of recent steps to use.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * sum of the {@code length} most recent values of {@code x.get()}. It uses
     * blocks in {@link BlockThread#main()}.
     */
    public static Num recentAccumulator(int length, Num x) {
        return accumulator(diff(x, delay(length, x)), BlockThread.main());
    }

    /**
     * The average of the most recent values of the given {@link Num}. The
     * average is weighted by the time elapsed during each step. (That is, this
     * is a definite integral with respect to time divided by the length of the
     * interval integrated over.)
     * @param length The number of recent steps to use.
     * @param x A number.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * time-weighted average of the {@code length} most recent values of
     * {@code x.get()}. It uses blocks in the given thread.
     */
    public static Num movingAverage(int length, Num x, BlockThread thread) {
        // Definite integral with respect to time over past few steps divided
        // by total elapsed time over those steps.
        return quot(recentAccumulator(length, prod(x, thread.dt()), thread),
                    diff(thread.t(), delay(length, thread.t())));
    }

    /**
     * The average of the most recent values of the given {@link Num}. The
     * average is weighted by the time elapsed during each step. (That is, this
     * is a definite integral with respect to time divided by the length of the
     * interval integrated over.)
     * @param length The number of recent steps to use.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#get()} method returns the
     * time-weighted average of the {@code length} most recent values of
     * {@code x.get()}. It uses blocks in {@link BlockThread#main()}.
     */
    public static Num movingAverage(int length, Num x) {
        return movingAverage(length, x, BlockThread.main());
    }
}

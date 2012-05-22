package edu.neu.nutrons.fpmadop;

import edu.wpi.first.wpilibj.PIDSource;

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

    private static class Constant implements Num {
        private double x = 0;
        private Constant(double x) {
            this.x = x;
        }
        public double getN() {
            return x;
        }
    }

    private static class FromPIDSource implements Num {
        private PIDSource x;
        private FromPIDSource(PIDSource x) {
            this.x = x;
        }
        public double getN() {
            return x.pidGet();
        }
    }

    private static class Sum implements Num {
        private Num[] xs;
        private Sum(Num[] xs) {
            this.xs = xs;
        }
        public double getN() {
            double ret = 0;
            for(int i=0; i<xs.length; i++) {
                ret += xs[i].getN();
            }
            return ret;
        }
    }

    private static class Prod implements Num {
        private Num[] xs;
        private Prod(Num[] xs) {
            this.xs = xs;
        }
        public double getN() {
            double ret = 1;
            for(int i=0; i<xs.length; i++) {
                ret *= xs[i].getN();
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
        public double getN() {
            return x.getN() - y.getN();
        }
    }

    private static class Quot implements Num {
        private Num x, y;
        private Quot(Num x, Num y) {
            this.x = x;
            this.y = y;
        }
        public double getN() {
            double xVal = x.getN();
            double yVal = y.getN();
            // 0/0 often occurs upon initialization. Returning 1 in this case
            // is mostly harmless, even though it's a hack.
            if(yVal == 0.0 && Math.abs(xVal) < 0.0001) {
                return 1.0;
            }
            return xVal / yVal;
        }
    }

    private static class BoolToNum implements Num {
        private Bool p;
        private BoolToNum(Bool b) {
            this.p = b;
        }
        public double getN() {
            return p.getB() ? 1.0 : 0.0;
        }
    }

    private static class Limit implements Num {
        private Num min, max, x;
        private Limit(Num min, Num max, Num x) {
            this.min = min;
            this.max = max;
            this.x = x;
        }
        public double getN() {
            return Utils.limit(min.getN(), max.getN(), x.getN());
        }
    }

    private static class Deadband implements Num {
        private Num center, range, x;
        private Deadband(Num center, Num range, Num x) {
            this.center = center;
            this.range = range;
            this.x = x;
        }
        public double getN() {
            return Utils.deadband(center.getN(), range.getN(), x.getN());
        }
    }

    private static class NumMux extends Multiplexer implements Num {
        private NumMux(Num s, Num[] xs) {
            super(s, xs);
        }
        public double getN() {
            return ((Num)super.getChoice()).getN();
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
            pastX[i] = x.getN();
            i = i+1 % pastX.length;
        }
        public double getN() {
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
            curX = x.getN();
        }
        public double getN() {
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
            sumX += x.getN();
        }
        public double getN() {
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
     * @return A {@link Num} whose {@link Num#getN()} method returns {@code x}.
     */
    public static Num id(double x) {
        return new Constant(x);
    }

    /**
     * Turns a {@link PIDSource} into a {@link Num}.
     * @param x A sensor or other PID source.
     * @return A {@link Num} whose {@link Num#getN()} method returns
     * {@code x.pidGet()}.
     */
    public static Num id(PIDSource x) {
        return new FromPIDSource(x);
    }

    /**
     * Adds a list of numbers.
     * @param xs A list of numbers.
     * @return A {@link Num} whose {@code Num#getN()} method returns the sum of
     * {@code Num#getN()} for each {@link Num} in {@code xs}.
     */
    public static Num sum(Num[] xs) {
        return new Sum(xs);
    }

    /**
     * Adds two numbers.
     * @param x A number.
     * @param y Another number.
     * @return A {@link Num} whose {@code Num#getN()} method returns
     * {@code x.getN() + y.getN()}.
     */
    public static Num sum(Num x, Num y) {
        Num[] xs = {x, y};
        return sum(xs);
    }

    /**
     * Adds two numbers, one of them primitive.
     * @param x A primitive number.
     * @param y A number.
     * @return A {@link Num} whose {@code Num#getN()} method returns
     * {@code x + y.getN()}.
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
     * @return A {@link Num} whose {@code Num#getN()} method returns
     * {@code x.getN() + y.getN() + z.getN()}.
     */
    public static Num sum(Num x, Num y, Num z) {
        Num[] xs = {x, y, z};
        return sum(xs);
    }

    /**
     * Multiplies a list of numbers.
     * @param xs A list of numbers.
     * @return A {@link Num} whose {@code Num#getN()} method returns the product
     * of {@code Num#getN()} for each {@link Num} in {@code xs}.
     */
    public static Num prod(Num[] xs) {
        return new Prod(xs);
    }

    /**
     * Multiplies two numbers.
     * @param x A number.
     * @param y Another number.
     * @return A {@link Num} whose {@code Num#getN()} method returns
     * {@code x.getN() * y.getN()}.
     */
    public static Num prod(Num x, Num y) {
        Num[] xs = {x, y};
        return prod(xs);
    }

    /**
     * Multiplies two numbers, one of them primitive.
     * @param x A primitive number.
     * @param y A number.
     * @return A {@link Num} whose {@code Num#getN()} method returns
     * {@code x * y.getN()}.
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
     * @return A {@link Num} whose {@code Num#getN()} method returns
     * {@code x.getN() * y.getN() * z.getN()}.
     */
    public static Num prod(Num x, Num y, Num z) {
        Num[] xs = {x, y, z};
        return prod(xs);
    }

    /**
     * Subtracts the second number from the first.
     * @param x A number, the minuend.
     * @param y Another number, the subtrahend.
     * @return A {@link Num} whose {@code Num#getN()} method returns
     * {@code x.getN() - y.getN()}.
     */
    public static Num diff(Num x, Num y) {
        return new Diff(x, y);
    }

    /**
     * Subtracts the second number from the first, which is primitive.
     * @param x A primitive number, the minuend.
     * @param y Another number, the subtrahend.
     * @return A {@link Num} whose {@code Num#getN()} method returns
     * {@code x - y.getN()}.
     */
    public static Num diff(double x, Num y) {
        return new Diff(id(x), y);
    }

    /**
     * The quotient of the first number over the second.
     * @param x A number, the dividend.
     * @param y Another number, the divisor.
     * @return A {@link Num} whose {@code Num#getN()} method returns
     * {@code x.getN() / y.getN()}.
     */
    public static Num quot(Num x, Num y) {
        return new Quot(x, y);
    }

    /**
     * The quotient of the first number, a primitive, over the second.
     * @param x A primitive number, the dividend.
     * @param y Another number, the divisor.
     * @return A {@link Num} whose {@code Num#getN()} method returns
     * {@code x / y.getN()}.
     */
    public static Num quot(double x, Num y) {
        return new Quot(id(x), y);
    }

    /**
     * Converts a {@link Bool} to a {@link Num}.
     * @param p A boolean.
     * @return A {@link Num} whose {@link Num#getN()} method returns one if
     * {@code p.getB()} is true and zero otherwise.
     */
    public static Num boolToNum(Bool p) {
        return new BoolToNum(p);
    }

    /**
     * Limits a given number to a specific range.
     * @param min The minimum value the output takes.
     * @param max The maximum value the output takes.
     * @param x number.
     * @return A {@link Num} whose {@link Num#getN()} method returns {@x.getN()}
     * if it is between {@code min.getN()} and {@code max.getN()} and returns
     * the closer extremum otherwise.
     */
    public static Num limit(Num min, Num max, Num x) {
        return new Limit(min, max, x);
    }

    /**
     * Limits a given number to a specific range.
     * @param min The minimum value the output takes.
     * @param max The maximum value the output takes.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#getN()} method returns {@x.getN()}
     * if it is between {@code min.getN()} and {@code max.getN()} and returns
     * the closer extremum otherwise.
     */
    public static Num limit(double min, double max, Num x) {
        return limit(id(min), id(max), x);
    }

    /**
     * Makes a number snap to a certain value if it is sufficiently close.
     * @param center The value snapped to if {@code x.getN()} is sufficiently
     * close.
     * @param range The maximum distance {@code x.getN()} can be from
     * {@code center.getN()} and still snap to {@code center.getN()}.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#getN()} method returns
     * {@code x.getN()} if it is not within {@code deadband.getN()} of
     * {@code center.getN()} and returns {@code center.getN()} otherwise.
     */
    public static Num deadband(Num center, Num range, Num x) {
        return new Deadband(center, range, x);
    }

    /**
     * Makes a number snap to a certain value if it is sufficiently close.
     * @param center The value snapped to if {@code x.getN()} is sufficiently
     * close.
     * @param range The maximum distance {@code x.getN()} can be from
     * {@code center} and still snap to {@code center}.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#getN()} method returns
     * {@code x.getN()} if it is not within {@code deadband} of {@code center}
     * and returns {@code center} otherwise.
     */
    public static Num deadband(double center, double range, Num x) {
        return deadband(id(center), id(range), x);
    }

    /**
     * A {@link Num} as it appeared a number of time steps ago. All values
     * before initialization are assumed to be zero.
     * @param delay The number of steps to delay {@code x}.
     * @param x A number.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Num} whose {@link Num#getN()} method returns the value of
     * {@code x.getN()} from {@code delay} samples ago. It is a {@link Block} in
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
     * @return A {@link Num} whose {@link Num#getN()} method returns the value of
     * {@code x.getN()} from {@code delay} samples ago. It uses {@link Block} in
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
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * difference of the two most recent values of {@code x.getN()}. It is a
     * {@link Block} in the given thread.
     */
    public static NumBlock delta(Num x, BlockThread thread) {
        return new Delta(x, thread);
    }

    /**
     * The change between the two most recent values of the given {@link Num}.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * difference of the two most recent values of {@code x.getN()}. It is a
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
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * sum of all previous values of {@code x.getN()}. It is a {@link Block} in
     * the given thread.
     */
    public static NumBlock accumulator(Num x, BlockThread thread) {
        return new Accumulator(x, thread);
    }

    /**
     * The sum of all previous values of the given {@link Num}.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * difference of the two most recent values of {@code x.getN()}. It is a
     * {@link Block} in {@link BlockThread#main()}.
     */
    public static NumBlock accumulator(Num x) {
        return accumulator(x, BlockThread.main());
    }

    /**
     * Uses a given {@link Bool} to select one of two numbers to return.
     * @param p A boolean.
     * @param x A number.
     * @param y Another number.
     * @return A {@link Num} whose {@link Num#getN()} method returns
     * {@code x.getN()} if {@code p.getB()} is true and {@code y.getN()} otherwise.
     */
    public static Num ifThenElse(Bool p, Num x, Num y) {
        Num[] xs = {x, y};
        return new NumMux(boolToNum(p), xs);
    }

    /**
     * The derivative of the given {@link Num} with respect to time.
     * @param x A number.
     * @param thread The {@link BlockThread} which determines how frequently
     * samples are taken.
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * derivative of {@code x.getN()} with respect to time. It uses blocks in the
     * given thread.
     */
    public static Num derivative(Num x, BlockThread thread) {
        return quot(delta(x, thread), thread.dt());
    }

    /**
     * The derivative of the given {@link Num} with respect to time.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * derivative of {@code x.getN()} with respect to time. It uses blocks in
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
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * integral of {@code x.getN()} with respect to time. It uses blocks in the
     * given thread.
     */
    public static NumBlock integral(Num x, BlockThread thread) {
        return accumulator(prod(x, thread.dt()), thread);
    }

    /**
     * The integral of the given {@link Num} with respect to time.
     * @param x A number.
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * integral of {@code x.getN()} with respect to time. It uses blocks in
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
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * sum of the {@code length} most recent values of {@code x.getN()}. It uses
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
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * sum of the {@code length} most recent values of {@code x.getN()}. It uses
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
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * time-weighted average of the {@code length} most recent values of
     * {@code x.getN()}. It uses blocks in the given thread.
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
     * @return A {@link Num} whose {@link Num#getN()} method returns the
     * time-weighted average of the {@code length} most recent values of
     * {@code x.getN()}. It uses blocks in {@link BlockThread#main()}.
     */
    public static Num movingAverage(int length, Num x) {
        return movingAverage(length, x, BlockThread.main());
    }
}

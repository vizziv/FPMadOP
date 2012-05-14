package edu.neu.nutrons.fpmadop;

/**
 * A number. Could be a constant, variable, real-time sensor value, etc.
 *
 * @author Ziv
 */
public interface Num {

    /**
     * Get the encapsulated number.
     * @return The number.
     */
    public double getN();
}

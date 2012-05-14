package edu.neu.nutrons.fpmadop.wpiclone;

import edu.neu.nutrons.fpmadop.Num;

/**
 * WPILib's timer with the {@link Num} interface. The only change is that this
 * class doesn't require an initial {@link edu.wpi.first.wpilibj.Timer#start()}.
 *
 * @author Ziv
 */
public class Timer extends edu.wpi.first.wpilibj.Timer implements Num {

    /**
     * Creates and starts a timer identical to
     * {@link edu.wpi.first.wpilibj.Timer}.
     */
    public Timer() {
        start();
    }

    public double getN() {
        return get();
    }
}

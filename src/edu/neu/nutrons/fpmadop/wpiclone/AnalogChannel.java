package edu.neu.nutrons.fpmadop.wpiclone;

import edu.neu.nutrons.fpmadop.Num;

/**
 * WPILib's analog channel with the {@link Num} interface. {@link Num#getN()} is
 * {@link edu.wpi.first.wpilibj.AnalogChannel#getVoltage()}.
 *
 * @author Ziv
 */
public class AnalogChannel extends edu.wpi.first.wpilibj.AnalogChannel
                           implements Num {

    /**
     * Creates an analog channel identical to
     * {@link edu.wpi.first.wpilibj.AnalogChannel}.
     * @param channel The channel number.
     */
    public AnalogChannel(int channel) {
        super(channel);
    }

    public double getN() {
        return getVoltage();
    }
}

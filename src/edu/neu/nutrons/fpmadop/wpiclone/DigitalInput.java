package edu.neu.nutrons.fpmadop.wpiclone;

import edu.neu.nutrons.fpmadop.Bool;

/**
 * WPILib's digital input with the {@link Bool} interface. {@link Bool#getB()}
 * is {@link edu.wpi.first.wpilibj.DigitalInput#get()}.
 *
 * @author Ziv
 */
public class DigitalInput extends edu.wpi.first.wpilibj.DigitalInput
                          implements Bool {

    /**
     * Creates a digital input identical to
     * {@link edu.wpi.first.wpilibj.DigitalInput}.
     * @param channel The channel number.
     */
    public DigitalInput(int channel) {
        super(channel);
    }

    public boolean getB() {
        return get();
    }
}

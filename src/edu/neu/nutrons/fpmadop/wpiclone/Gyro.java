package edu.neu.nutrons.fpmadop.wpiclone;

import edu.neu.nutrons.fpmadop.Num;

/**
 * WPILib's gyro with the {@link Num} interface.  {@link Num#getN()} is
 * {@link edu.wpi.first.wpilibj.Gyro#getAngle()}.
 *
 * @author Ziv
 */
public class Gyro extends edu.wpi.first.wpilibj.Gyro implements Num {

    /**
     * Creates a gyro identical to {@link edu.wpi.first.wpilibj.Gyro}.
     * @param channel The analog channel the gyro is connected to.
     */
    public Gyro(int channel) {
        super(channel);
    }

    public double getN() {
        return getAngle();
    }
}

package edu.neu.nutrons.fpmadop.wpiclone;

import edu.neu.nutrons.fpmadop.Num;

/**
 * WPILib's encoder with the {@link Num} interface. The only change is that this
 * class doesn't require an initial
 * {@link edu.wpi.first.wpilibj.Encoder#start()}.  {@link Num#getN()} is
 * {@link edu.wpi.first.wpilibj.Encoder#get()}.
 *
 * @author Ziv
 */
public class Encoder extends edu.wpi.first.wpilibj.Encoder implements Num {

    /**
     * Creates and starts an encoder identical to
     * {@link edu.wpi.first.wpilibj.Encoder}.
     * @param aChannel The A digital input channel.
     * @param bChannel The B digital input channel.
     * @param reverseDirection If true, flip the sign of the encoder output.
     * @param encodingType Choose between 1X, 2X or 4X decoding.
     */
    public Encoder(int aChannel, int bChannel, boolean reverseDirection,
                   EncodingType encodingType) {
        super(aChannel, bChannel, reverseDirection, encodingType);
        start();
    }

    public double getN() {
        return get();
    }
}

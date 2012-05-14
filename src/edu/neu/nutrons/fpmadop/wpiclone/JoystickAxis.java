package edu.neu.nutrons.fpmadop.wpiclone;

import edu.neu.nutrons.fpmadop.Num;
import edu.wpi.first.wpilibj.GenericHID;

/**
 * The value of one of a joystick's axes.
 *
 * @author Ziv
 */
public class JoystickAxis implements Num {

    private GenericHID js;
    private int axis = 1;

    /**
     * Makes a joystick axis.
     * @param joystick The joystick the axis is on.
     * @param axisNumber The number of the axis.
     */
    public JoystickAxis(GenericHID joystick, int axisNumber) {
        js = joystick;
        axis = axisNumber;
    }

    public double getN() {
        return js.getRawAxis(axis);
    }
}

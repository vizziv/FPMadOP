package edu.neu.nutrons.fpmadop.wpiclone;

import edu.neu.nutrons.fpmadop.Bool;
import edu.wpi.first.wpilibj.GenericHID;

/**
 * WPILib's joystick button with the {@link Bool} interface. {@link Bool#getB()}
 * is {@link edu.wpi.first.wpilibj.JoystickButton#get()}.
 *
 * @author Ziv
 */
public class JoystickButton extends edu.wpi.first.wpilibj.buttons.JoystickButton
                            implements Bool {

    /**
     * Creates a joystick button identical to
     * {@link edu.wpi.first.wpilibj.JoystickButton}.
     * @param joystick The joystick the button is on.
     * @param buttonNumber The number of the button.
     */
    public JoystickButton(GenericHID joystick, int buttonNumber) {
        super(joystick, buttonNumber);
    }

    public boolean getB() {
        return get();
    }
}

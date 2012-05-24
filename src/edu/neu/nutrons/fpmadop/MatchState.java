package edu.neu.nutrons.fpmadop;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;


/**
 * Keeps track of whether the robot is in disabled, autonomous or teleop.
 */
public class MatchState {

    private Mode mode = Mode.DISABLED;
    private static RobotBase bot = null;

    /**
     * An enumeration of the modes the robot can be in: disabled, autonomous,
     * and teleoperated. Each mode object is a {@link Bool} that returns whether
     * or not the robot is in that mode.
     */
    public static abstract class Mode implements Bool {
        private Mode() {}
        /**
         * Represents disabled mode.
         */
        public static Mode DISABLED = new DisabledMode();
        /**
         * Represents autonomous mode.
         */
        public static Mode AUTO = new AutoMode();
        /**
         * Represents teleoperated mode.
         */
        public static Mode TELEOP = new TeleopMode();
    }

    private static class DisabledMode extends Mode {
        private DisabledMode() {}
        public boolean getB() {
            return isDisabled();
        }
    }

    private static class AutoMode extends Mode {
        private AutoMode() {}
        public boolean getB() {
            return isAuto();
        }
    }

    private static class TeleopMode extends Mode {
        private TeleopMode() {}
        public boolean getB() {
            return isTeleop();
        }
    }

    private MatchState() {}

    /**
     * Sets the robot that has it's state tracked.
     * @param robot An instance of the main robot object.
     */
    public static void setRobot(RobotBase robot) {
        bot = robot;
    }

    /**
     * True when the robot is in disabled.
     * @return Whether or not the robot is in disabled.
     */
    public static boolean isDisabled() {
        if(bot == null) {
            return true;
        }
        else {
            return bot.isDisabled();
        }
    }

    /**
     * True when the robot is in autonomous.
     * @return Whether or not the robot is in autonomous.
     */
    public static boolean isAuto() {
        if(bot == null) {
            return false;
        }
        else {
            return bot.isAutonomous();
        }
    }

    /**
     * True when the robot is in teleop.
     * @return Whether or not the robot is in teleop.
     */
    public static boolean isTeleop() {
        if(bot == null) {
            return false;
        }
        else {
            return bot.isOperatorControl();
        }
    }

    /**
     * The mode the robot is in.
     * @return Whether the robot is in disabled, autonomous or teleoperated
     * mode.
     */
    public static Mode getMode() {
        if(isAuto()) {
            return Mode.AUTO;
        }
        else if(isTeleop()) {
            return Mode.TELEOP;
        }
        else {
            return Mode.DISABLED;
        }
    }

    /**
     * The amount of time since the beginning of the match. Unofficial; see
     * {@link DriverStation#getInstance()#getMatchTime()} for details.
     * @return The amount of time passed since the beginning of the match.
     */
    public static double getMatchTime() {
        return DriverStation.getInstance().getMatchTime();
    }
}

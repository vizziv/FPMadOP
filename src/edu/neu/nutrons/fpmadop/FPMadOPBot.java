/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.neu.nutrons.fpmadop;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * Central program logic. Runs {@link BlockThread#main()}.
 *
 * @author Ziv (modified from WPILib template).
 */
public class FPMadOPBot extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        MatchState.setRobot(this);
        System.out.println("Robot awake!");
    }

    /**
     * This function is called periodically during disabled.
     */
    public void disabledPeriodic() {
        periodic();
    }

    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {
        periodic();
    }

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {
        periodic();
    }

    /**
     * This function is called periodically during disabled, auto and teleop.
     */
    private void periodic() {
        BlockThread.main().run();
    }
}

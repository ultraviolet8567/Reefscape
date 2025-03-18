package frc.robot.commands.auto;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Odometry;
import frc.robot.subsystems.Swerve;

public class AutoAlignWithReef extends Command {
	private Swerve swerve;
	private Odometry odometry;

	public AutoAlignWithReef(Swerve swerve, Odometry odometry) {
		this.swerve = swerve;
		this.odometry = odometry;

		addRequirements(swerve);
	}

	@Override
	public void execute() {
		// Makes chassis speeds to turn into swerve module states
		// TODO: get setpoints from odometry of reef april tags and do some constant
		// stuff
		ChassisSpeeds chassisSpeeds = swerve.calculateChassisSpeed(odometry.getPose().getX(), 0,
				odometry.getPose().getY(), 0, odometry.getHeading(), null);

		swerve.setModuleStates(chassisSpeeds);
	}

	@Override
	public void end(boolean interrupted) {
		swerve.stopModules();
	}
}

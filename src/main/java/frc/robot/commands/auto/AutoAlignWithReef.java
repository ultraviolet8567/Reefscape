package frc.robot.commands.auto;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Odometry;
import frc.robot.subsystems.Swerve;

public class AutoAlignWithReef extends Command {
	private Swerve swerve;
	private Odometry odometry;
	private boolean rightReef;
	private int reefSide;
	private ChassisSpeeds chassisSpeeds;
	private Pose2d setpoint;

	public AutoAlignWithReef(Swerve swerve, Odometry odometry, boolean rightReef) {
		this.swerve = swerve;
		this.odometry = odometry;
		this.rightReef = rightReef;

		addRequirements(swerve);
	}

	@Override
	public void execute() {
		// Makes chassis speeds to turn into swerve module states
		// TODO: get setpoints from odometry of reef april tags and do some constant
		// how do i find which april tag we are trying to align to

		chassisSpeeds = swerve.calculateChassisSpeed(odometry.getPose().getX(), 0, odometry.getPose().getY(), 0,
				odometry.getHeading(), null);

		chassisSpeeds = swerve.calculateChassisSpeed(odometry.getPose(), odometry.closestReefEdge().setpointLeft());

		swerve.setModuleStates(chassisSpeeds);
	}

	@Override
	public void end(boolean interrupted) {
		swerve.stopModules();
	}
}

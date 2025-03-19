package frc.robot.commands.auto;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.AutoConstants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Odometry;
import frc.robot.subsystems.Odometry.ReefEdge;
import frc.robot.subsystems.Swerve;
import java.util.function.Function;

public class AutoAlignWithReef extends Command {
	private Swerve swerve;
	private Odometry odometry;

	private Function<ReefEdge, Pose2d> setpointFunction;
	private Pose2d current, setpoint;

	private ChassisSpeeds chassisSpeeds;

	public AutoAlignWithReef(Swerve swerve, Odometry odometry, boolean onTheRight) {
		this.swerve = swerve;
		this.odometry = odometry;

		// Determine whether the pose of the left stalk or right stalk should be the
		// setpoint
		if (onTheRight) {
			setpointFunction = (ReefEdge edge) -> edge.setpointRight();
		} else {
			setpointFunction = (ReefEdge edge) -> edge.setpointLeft();
		}

		addRequirements(swerve);
	}

	@Override
	public void execute() {
		current = odometry.getPose();
		setpoint = setpointFunction.apply(odometry.closestReefEdge());

		chassisSpeeds = swerve.calculateChassisSpeed(current, setpoint);
		swerve.setModuleStates(chassisSpeeds);
	}

	@Override
	public void end(boolean interrupted) {
		swerve.stopModules();

		if (interrupted) {
			RobotContainer.getDriverJoystick().setRumble(RumbleType.kRightRumble, 0.25);
			RobotContainer.getOperatorJoystick().setRumble(RumbleType.kRightRumble, 0.25);
		} else {
			RobotContainer.getDriverJoystick().setRumble(RumbleType.kBothRumble, 0.25);
			RobotContainer.getOperatorJoystick().setRumble(RumbleType.kBothRumble, 0.25);
		}
	}

	@Override
	public boolean isFinished() {
		return current.minus(setpoint).getTranslation().getNorm() < AutoConstants.kAutoAlignTolerance;
	}
}

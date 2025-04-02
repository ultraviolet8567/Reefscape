package frc.robot.commands.auto;

import com.pathplanner.lib.trajectory.PathPlannerTrajectoryState;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.AutoConstants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Odometry;
import frc.robot.subsystems.Odometry.StationEdge;
import frc.robot.subsystems.Swerve;
import java.util.function.Function;
import org.littletonrobotics.junction.Logger;

public class AutoAlignWithCoralStation extends Command {
	private Swerve swerve;
	private Odometry odometry;

	private Function<Pose2d, Pose2d> setpointFunction;
	private Pose2d current;
	private PathPlannerTrajectoryState setpoint;

	private ChassisSpeeds chassisSpeeds;

	private Timer timer;

	public AutoAlignWithCoralStation(Swerve swerve, Odometry odometry) {
		this.swerve = swerve;
		this.odometry = odometry;

		// Determine whether the pose of the left stalk or right stalk should be the
		// setpoint
		setpointFunction = (Pose2d robotPose) -> StationEdge.stationSetpoint(robotPose);

		addRequirements(swerve);

		timer = new Timer();
	}

	@Override
	public void initialize() {
		timer.restart();
	}

	@Override
	public void execute() {
		current = odometry.getPose();
		setpoint = new PathPlannerTrajectoryState();
		setpoint.pose = setpointFunction.apply(current);

		Logger.recordOutput("Odometry/StationSetpoint", setpoint.pose);

		chassisSpeeds = swerve.calculateChassisSpeed(current, setpoint);
		swerve.setModuleStates(chassisSpeeds);
	}

	@Override
	public void end(boolean interrupted) {
		swerve.stopModules();

		if (!interrupted) {
			RobotContainer.getOperatorJoystick().setRumble(RumbleType.kBothRumble, 0.25);
			RobotContainer.getDriverJoystick().setRumble(RumbleType.kBothRumble, 0.25);
		}
	}

	@Override
	public boolean isFinished() {
		return current.minus(setpoint.pose).getTranslation().getNorm() < AutoConstants.kAutoAlignTolerance;
	}
}

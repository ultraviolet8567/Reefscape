// package frc.robot.commands.auto;

// import com.pathplanner.lib.trajectory.PathPlannerTrajectoryState;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.kinematics.ChassisSpeeds;
// import edu.wpi.first.wpilibj.GenericHID.RumbleType;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.Constants.AutoConstants;
// import frc.robot.RobotContainer;
// import frc.robot.subsystems.Odometry;
// import frc.robot.subsystems.Swerve;
// import java.util.function.Supplier;
// import org.littletonrobotics.junction.Logger;

// public class AutoAlignWithSetpoint extends Command {
// private Swerve swerve;
// private Odometry odometry;

// private Supplier<Pose2d> setpointFunction;
// private Pose2d current;
// private PathPlannerTrajectoryState setpoint;

// private ChassisSpeeds chassisSpeeds;

// public AutoAlignWithSetpoint(Swerve swerve, Odometry odometry,
// Supplier<Pose2d> setpointFunction) {
// this.swerve = swerve;
// this.odometry = odometry;
// this.setpointFunction = setpointFunction;

// addRequirements(swerve);
// }

// @Override
// public void execute() {
// current = odometry.getPose();
// setpoint = new PathPlannerTrajectoryState();
// setpoint.pose = setpointFunction.get();

// Logger.recordOutput("Odometry/AutoAlign/Setpoint", setpoint.pose);

// chassisSpeeds = swerve.calculateChassisSpeed(current, setpoint);
// swerve.setModuleStates(chassisSpeeds);
// }

// @Override
// public void end(boolean interrupted) {
// swerve.stopModules();

// if (!interrupted) {
// RobotContainer.getOperatorJoystick().setRumble(RumbleType.kBothRumble, 0.25);
// RobotContainer.getDriverJoystick().setRumble(RumbleType.kBothRumble, 0.25);
// }
// }

// @Override
// public boolean isFinished() {
// double alignError = current.minus(setpoint.pose).getTranslation().getNorm();
// Logger.recordOutput("Odometry/AutoAlign/AlignError", alignError);

// return alignError < AutoConstants.kAutoAlignTolerance;
// }
// }

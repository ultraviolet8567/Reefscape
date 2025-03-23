package frc.robot.subsystems;

import com.pathplanner.lib.trajectory.PathPlannerTrajectoryState;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CAN;
import frc.robot.Constants.DriveConstants;
import java.util.Arrays;
import org.littletonrobotics.junction.Logger;

public class Swerve extends SubsystemBase {
	private final SwerveModule frontLeft, frontRight, backLeft, backRight;
	private SendableChooser<Boolean> optimizerOverride;

	public Swerve() {
		System.out.println("[Init] Creating Swerve");

		optimizerOverride = new SendableChooser<>();
		optimizerOverride.setDefaultOption("Optimize", false);
		optimizerOverride.addOption("Don't optimize", true);

		Shuffleboard.getTab("Main").add("Swerve Optimizer Override", optimizerOverride)
				.withWidget(BuiltInWidgets.kComboBoxChooser).withSize(2, 1).withPosition(5, 3);

		frontLeft = new SwerveModule(CAN.kFrontLeftDriveMotorPort, CAN.kFrontLeftTurningMotorPort,
				DriveConstants.kFrontLeftDriveEncoderReversed, DriveConstants.kFrontLeftTurningEncoderReversed,
				DriveConstants.kFrontLeftDriveAbsoluteEncoderPort,
				DriveConstants.kFrontLeftDriveAbsoluteEncoderOffsetRad,
				DriveConstants.kFrontLeftDriveAbsoluteEncoderReversed, optimizerOverride);

		frontRight = new SwerveModule(CAN.kFrontRightDriveMotorPort, CAN.kFrontRightTurningMotorPort,
				DriveConstants.kFrontRightDriveEncoderReversed, DriveConstants.kFrontRightTurningEncoderReversed,
				DriveConstants.kFrontRightDriveAbsoluteEncoderPort,
				DriveConstants.kFrontRightDriveAbsoluteEncoderOffsetRad,
				DriveConstants.kFrontRightDriveAbsoluteEncoderReversed, optimizerOverride);

		backLeft = new SwerveModule(CAN.kBackLeftDriveMotorPort, CAN.kBackLeftTurningMotorPort,
				DriveConstants.kBackLeftDriveEncoderReversed, DriveConstants.kBackLeftTurningEncoderReversed,
				DriveConstants.kBackLeftDriveAbsoluteEncoderPort, DriveConstants.kBackLeftDriveAbsoluteEncoderOffsetRad,
				DriveConstants.kBackLeftDriveAbsoluteEncoderReversed, optimizerOverride);

		backRight = new SwerveModule(CAN.kBackRightDriveMotorPort, CAN.kBackRightTurningMotorPort,
				DriveConstants.kBackRightDriveEncoderReversed, DriveConstants.kBackRightTurningEncoderReversed,
				DriveConstants.kBackRightDriveAbsoluteEncoderPort,
				DriveConstants.kBackRightDriveAbsoluteEncoderOffsetRad,
				DriveConstants.kBackRightDriveAbsoluteEncoderReversed, optimizerOverride);
	}

	@Override
	public void periodic() {
		Logger.recordOutput("Swerve/Measured", getModuleStates());
		Logger.recordOutput("Swerve/Absolute Encoders",
				new double[]{frontLeft.getAbsoluteEncoderAngle(), frontRight.getAbsoluteEncoderAngle(),
						backLeft.getAbsoluteEncoderAngle(), backRight.getAbsoluteEncoderAngle()});

	}

	public SwerveModulePosition[] getModulePositions() {
		return new SwerveModulePosition[]{frontLeft.getModulePosition(), frontRight.getModulePosition(),
				backLeft.getModulePosition(), backRight.getModulePosition()};
	}

	public SwerveModuleState[] getModuleStates() {
		return new SwerveModuleState[]{frontLeft.getState(), frontRight.getState(), backLeft.getState(),
				backRight.getState()};
	}

	public ChassisSpeeds getRobotRelativeSpeeds() {
		return DriveConstants.kDriveKinematics.toChassisSpeeds(getModuleStates());
	}

	public void setModuleStates(ChassisSpeeds chassisSpeeds) {
		SwerveModuleState[] moduleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(chassisSpeeds);
		setModuleStates(moduleStates);
	}

	public void setModuleStates(SwerveModuleState[] desiredStates) {
		// double maxSpeed = Lights.getInstance().isDemo
		// ? DriveConstants.kDemoTeleDriveMaxSpeedMetersPerSecond
		// : DriveConstants.kTeleDriveMaxSpeedMetersPerSecond;

		double maxSpeed = DriveConstants.kTeleDriveMaxSpeedMetersPerSecond;

		SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, maxSpeed);
		frontLeft.setDesiredState(desiredStates[0]);
		frontRight.setDesiredState(desiredStates[1]);
		backLeft.setDesiredState(desiredStates[2]);
		backRight.setDesiredState(desiredStates[3]);

		Logger.recordOutput("Swerve/Setpoints", desiredStates);
	}

	// Calculate chassis speeds using Pathplanner
	public ChassisSpeeds calculateChassisSpeed(Pose2d currentPose, PathPlannerTrajectoryState setpointPose) {
		return DriveConstants.kAutoAlignController.calculateRobotRelativeSpeeds(currentPose, setpointPose);
	}

	// Sets the wheels to 45 degree angles so it doesn't move
	public void lockWheels() {
		SwerveModuleState[] locked = new SwerveModuleState[]{new SwerveModuleState(0, Rotation2d.fromDegrees(135)),
				new SwerveModuleState(0, Rotation2d.fromDegrees(45)),
				new SwerveModuleState(0, Rotation2d.fromDegrees(-135)),
				new SwerveModuleState(0, Rotation2d.fromDegrees(45))};

		setModuleStates(locked);
	}

	public double[] getWheelRadiusCharacterizationPosition() {
		return Arrays.stream(new SwerveModule[]{frontLeft, frontRight, backLeft, backRight})
				.mapToDouble(SwerveModule::getTurningPosition).toArray();
	}

	public double solveBodyRot(Pose2d robotPose, Translation3d targetPose) {
		return Math.atan2(targetPose.getY() - robotPose.getY(), targetPose.getX() - robotPose.getX());
	}

	public void resetEncoders() {
		frontLeft.resetEncoders();
		frontRight.resetEncoders();
		backLeft.resetEncoders();
		backRight.resetEncoders();
	}

	public void stopModules() {
		frontLeft.stop();
		frontRight.stop();
		backLeft.stop();
		backRight.stop();
	}

	public static enum SwerveMode {
		MANUAL, AUTOALIGN, EDGE_A, EDGE_B, EDGE_C, EDGE_D, EDGE_E, EDGE_F
	}
}

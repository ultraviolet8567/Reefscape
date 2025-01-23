// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
	public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : Mode.SIM;
	public static final RobotType currentRobot = (currentMode == Mode.REAL) ? RobotType.REALBOT : RobotType.SIMBOT;

	public static final boolean fieldOriented = true;
	public static class ElevatorConstants {
		// CHANGE LATER:
	}

	public static class OperatorConstants {
		public static final int kDriverControllerPort = 0;
		public static final int kOperatorControllerPort = 0;
	}

	public static class ModuleConstants {
		public static final double kDriveEncoderRot2Meter = 0.0;
		public static final double kDriveEncoderRPM2MeterPerSec = 0.0;
		public static final double kTurningEncoderRot2Rad = 0.0;
		public static final double kTurningEncoderRPM2RadPerSec = 0.0;
		public static final double kPTurning = 0.0;
	}
	// CHANGE LATER:
	public static class DriveConstants {

		public static final double kTrackWidth = Units.inchesToMeters(20.75);
		// Distance between front and back wheels:
		public static final double kWheelBase = Units.inchesToMeters(20.75);

		public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
				new Translation2d(kWheelBase / 2, kTrackWidth / 2), // Front left (+/+)
				new Translation2d(kWheelBase / 2, -kTrackWidth / 2), // Front right (+/-)
				new Translation2d(-kWheelBase / 2, kTrackWidth / 2), // Back left (-/+)
				new Translation2d(-kWheelBase / 2, -kTrackWidth / 2)); // Back right (-/-)

		public static final boolean kFrontLeftDriveEncoderReversed = false;
		public static final boolean kFrontLeftTurningEncoderReversed = false;
		public static final double kFrontLeftDriveAbsoluteEncoderOffsetRad = 0.01;
		public static final boolean kFrontLeftDriveAbsoluteEncoderReversed = false;
		public static final int kFrontLeftDriveAbsoluteEncoderPort = 0;

		public static final boolean kFrontRightDriveEncoderReversed = false;
		public static final boolean kFrontRightTurningEncoderReversed = false;
		public static final double kFrontRightDriveAbsoluteEncoderOffsetRad = 0.01;
		public static final boolean kFrontRightDriveAbsoluteEncoderReversed = false;
		public static final int kFrontRightDriveAbsoluteEncoderPort = 0;

		public static final boolean kBackLeftDriveEncoderReversed = false;
		public static final boolean kBackLeftTurningEncoderReversed = false;
		public static final double kBackLeftDriveAbsoluteEncoderOffsetRad = 0.01;
		public static final boolean kBackLeftDriveAbsoluteEncoderReversed = false;
		public static final int kBackLeftDriveAbsoluteEncoderPort = 0;

		public static final boolean kBackRightDriveEncoderReversed = false;
		public static final boolean kBackRightTurningEncoderReversed = false;
		public static final double kBackRightDriveAbsoluteEncoderOffsetRad = 0.01;
		public static final boolean kBackRightDriveAbsoluteEncoderReversed = false;
		public static final int kBackRightDriveAbsoluteEncoderPort = 0;

		public static final double kPhysicalMaxSpeedMetersPerSecond = 0.01;
		public static final double kTeleDriveMaxSpeedMetersPerSecond = 0.01;

		public static final double kTeleDriveMaxAccelerationUnitsPerSecond = 0.01;
		public static final double kTeleDriveMaxAngularAccelerationUnitsPerSecond = 0.01;
	}
	// CAN = computer area network
	public static class CAN {
		public static final int kFrontLeftDriveMotorPort = 0;
		public static final int kFrontLeftTurningMotorPort = 0;

		public static final int kFrontRightDriveMotorPort = 0;
		public static final int kFrontRightTurningMotorPort = 0;

		public static final int kBackLeftDriveMotorPort = 0;
		public static final int kBackLeftTurningMotorPort = 0;

		public static final int kBackRightDriveMotorPort = 0;
		public static final int kBackRightTurningMotorPort = 0;

	}

	public static class OIConstants {
		public static final ControllerType controllerTypeDriver = ControllerType.XBOX;
		public static final ControllerType controllerTypeOperator = ControllerType.XBOX;

		public static final int kDriverControllerPort = 0;
		public static final int kOperatorControllerPort = 1;

		public static final double kDeadband = 0.1;
	}

	public static enum RobotType {
		/** Physical robot */
		REALBOT,
		/** Simulated robot */
		SIMBOT
	}

	public static enum Mode {
		/** Running on a real robot */
		REAL,
		/** Running a simulator */
		SIM,
		/** Replaying from a log file */
		REPLAY
	}

	public static enum ControllerType {
		XBOX, LOGITECH, JOYSTICK
	}
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;

public final class Constants {
	public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : Mode.SIM;
	public static final RobotType currentRobot = (currentMode == Mode.REAL) ? RobotType.REALBOT : RobotType.SIMBOT;
	public static final boolean lightsExist = true;

	public static final boolean fieldOriented = true;

	public static class ElevatorConstants {
		public static final double kElevatorVoltage = 7;
		public static final double kP = 15;
		public static final double kI = 0.0;
		public static final double kD = 0.0;
		public static final double kS = 0.1;
		public static final double kG = 1.0;
		public static final double kV = 0.0;

		public static final int kManualVoltage = 5;

		// Elevator attributes
		public static final double kElevatorStartHeight = 0;
		public static final double kElevatorCarriageWeight = 1.0;
		public static final double kElevatorDrumRadius = 0.0254;
		public static final double kElevatorGearing = 1.0 / 6;
		public static final double kElevatorMetersPerRad = 0.413 / (2 * Math.PI);

		// Encoder
		public static final int kElevatorAbsoluteEncoderPort = 9;
		public static final double kAbsoluteEncoderOffset = -0.417;
		public static final boolean kAbsoluteEncoderReversed = true;

		// Limits
		public static final double kElevatorMaxRadians = 4.25;
		public static final double kElevatorMinRadians = 0.1;

		public static final double kElevatorMaxHeight = 2; // in meters
		public static final double kElevatorMinHeight = 0.05; // in meters

		// Setpoints
		public static final double kHeightDefault = 0.1; // Dummy value
		public static final double kHeightGround = 0.1; // Dummy value
		public static final double kHeightStation = 0.2861;
		public static final double kHeightProcessor = 1.0381;
		public static final double kHeightL1 = 0.1; // Dummy value
		public static final double kHeightL2 = 0.1525;
		public static final double kHeightL3 = 0.76;
		public static final double kHeightL4 = 1.69;
		public static final double kHeightMax = 1.8;
		public static final double kHeightAlgaeLower = 1.0; // Dummy value
		public static final double kHeightAlgaeHigher = 1.55; // Dummy value
		public static final double kHeightAlgaeCandlestick = 0.5; // Dummy value
	}

	public static class IntakeConstants {
		public static final int kAlgaeIntakeVoltage = 10;

		public static final double kCoralVoltageDefault = 1.5; // Dummy value
		public static final double kCoralVoltageL1 = 1.5; // Dummy value
		public static final double kCoralVoltageL2 = 1.5;
		public static final double kCoralVoltageL3 = 1.5;
		public static final double kCoralVoltageL4 = 1.2;

		public static final int kAlgaeIntakeExtensionAbsoluteEncoderPort = 8;
		public static final boolean kAlgaeIntakeExtensionAbsoluteEncoderReversed = false;

		public static final double kP = 20.0; // Need to tune
		public static final double kI = 0.0;
		public static final double kD = 0.0;

		public static final double kAlgaeIntakeRetractedPosition = 5.785;
		public static final double kAlgaeIntakeExtendedPosition = 3.425;
	}

	public static class OperatorConstants {
		public static final int kDriverControllerPort = 0;
		public static final int kOperatorControllerPort = 1;
	}

	public static class ModuleConstants {
		public static final double kWheelDiameterMeters = Units.inchesToMeters(3.75);
		public static final double kDriveMotorGearRatio = 1 / 5.9;
		public static final double kTurningMotorGearRatio = 1 / (150 / 7.0);

		public static final double kDriveEncoderRot2Meter = kDriveMotorGearRatio * Math.PI * kWheelDiameterMeters;
		public static final double kTurningEncoderRot2Rad = kTurningMotorGearRatio * 2 * Math.PI;
		public static final double kDriveEncoderRPM2MeterPerSec = kDriveEncoderRot2Meter / 60;
		public static final double kTurningEncoderRPM2RadPerSec = kTurningEncoderRot2Rad / 60;

		public static final double kPTurning = 0.25;
	}

	public static class DriveConstants {
		public static final double kTrackWidth = Units.inchesToMeters(20.5);
		// Distance between front and back wheels:
		public static final double kWheelBase = Units.inchesToMeters(20.5);

		public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
				new Translation2d(kWheelBase / 2, kTrackWidth / 2), // Front left (+/+)
				new Translation2d(kWheelBase / 2, -kTrackWidth / 2), // Front right (+/-)
				new Translation2d(-kWheelBase / 2, kTrackWidth / 2), // Back left (-/+)
				new Translation2d(-kWheelBase / 2, -kTrackWidth / 2)); // Back right (-/-)

		public static final boolean kFrontLeftDriveEncoderReversed = true;
		public static final boolean kFrontLeftTurningEncoderReversed = true;
		public static final boolean kFrontLeftDriveAbsoluteEncoderReversed = false;
		public static final int kFrontLeftDriveAbsoluteEncoderPort = 0;

		public static final boolean kFrontRightDriveEncoderReversed = true;
		public static final boolean kFrontRightTurningEncoderReversed = true;
		public static final boolean kFrontRightDriveAbsoluteEncoderReversed = false;
		public static final int kFrontRightDriveAbsoluteEncoderPort = 1;

		public static final boolean kBackLeftDriveEncoderReversed = true;
		public static final boolean kBackLeftTurningEncoderReversed = true;
		public static final boolean kBackLeftDriveAbsoluteEncoderReversed = false;
		public static final int kBackLeftDriveAbsoluteEncoderPort = 2;

		public static final boolean kBackRightDriveEncoderReversed = true;
		public static final boolean kBackRightTurningEncoderReversed = true;
		public static final boolean kBackRightDriveAbsoluteEncoderReversed = false;
		public static final int kBackRightDriveAbsoluteEncoderPort = 3;

		public static final double kFrontLeftDriveAbsoluteEncoderOffsetRad = -2.854 - 0.494 - 0.548 - 2.987;
		public static final double kFrontRightDriveAbsoluteEncoderOffsetRad = -2.231 + 0.060 - 0.05;
		public static final double kBackLeftDriveAbsoluteEncoderOffsetRad = 3.022 + 0.115 - 0.092;
		public static final double kBackRightDriveAbsoluteEncoderOffsetRad = 0.349 + 0.324 + 1.029 + 0.672 - 1.961
				- 0.082 + 0.125;

		public static final double kPhysicalMaxSpeedMetersPerSecond = 4.5;
		public static final double kPhysicalMaxAngularSpeedRadiansPerSecond = 3 * Math.PI;

		public static final double kTeleDriveMaxSpeedMetersPerSecond = kPhysicalMaxSpeedMetersPerSecond;
		public static final double kTeleDriveMaxAngularSpeedRadiansPerSecond = kPhysicalMaxSpeedMetersPerSecond;

		public static final double kTeleDriveMaxAccelerationUnitsPerSecond = 3;
		public static final double kTeleDriveMaxAngularAccelerationUnitsPerSecond = 3;

		public static final double kRobotMass = 51.25;
		public static final double kRobotMOI = 7.0;

		public static final ModuleConfig kRobotModuleConfig = new ModuleConfig(ModuleConstants.kWheelDiameterMeters / 2,
				kPhysicalMaxSpeedMetersPerSecond, 1, // friction coefficient between wheel and carpet, (unsure so 1.0)
				DCMotor.getNEO(1), 1 / ModuleConstants.kDriveMotorGearRatio, 80, 1);
		public static final RobotConfig kRobotConfig = new RobotConfig(kRobotMass, // mass, kg
				kRobotMOI, // moment of inertia (why), kgm^2
				kRobotModuleConfig, // module config
				kDriveKinematics.getModules()); // locations of modules relative of robot center

		// PID constants for auto alignment
		public static final double kTranslationP = 0.5;
		public static final double kTranslationI = 0.0;
		public static final double kTranslationD = 0.0;

		public static final double kAngleP = 0.5;
		public static final double kAngleI = 0.0;
		public static final double kAngleD = 0.0;
	}

	public static class AutoConstants {
		// Speeds from -1 to 1
		public static final double kAutoXDriveSpeed = 0.0;
		public static final double kAutoYDriveSpeed = 0.5;
		public static final double kAutoTurningSpeed = 0.0;
	}

	public static class VisionConstants {
		public static final AprilTagFieldLayout kAprilTagFieldLayout = AprilTagFieldLayout
				.loadField(AprilTagFields.k2025ReefscapeAndyMark);

		// TODO: get these values once they are in the CAD

		// Distances from cameras to robot
		public static final double kFrontX = 0.0;
		public static final double kFrontY = 0.0;
		public static final double kFrontZ = 0.0;
		// Rotation about x axis (left to right rotation)
		public static final double kFrontRoll = 0.0;
		// Rotation about y axis (forward to backward rotation)
		public static final double kFrontPitch = 0.0;
		// Rotation about z axis (spinning on the ground)
		public static final double kFrontYaw = 0.0;
		public static final Transform3d kFrontCamToRobot = new Transform3d(kFrontX, kFrontY, kFrontZ,
				new Rotation3d(kFrontRoll, kFrontPitch, kFrontYaw));

		public static final double kBackX = 0.0;
		public static final double kBackY = 0.0;
		public static final double kBackZ = 0.0;
		// Rotation about x axis (left to right rotation)
		public static final double kBackRoll = 0.0;
		// Rotation about y axis (forward to backward rotation)
		public static final double kBackPitch = 0.0;
		// Rotation about z axis (spinning on the ground)
		public static final double kBackYaw = 0.0;
		public static final Transform3d kBackCamToRobot = new Transform3d(kBackX, kBackY, kBackZ,
				new Rotation3d(kBackRoll, kBackPitch, kBackYaw));
	}

	// CAN = computer area network
	public static class CAN {
		public static final int kFrontLeftDriveMotorPort = 10;
		public static final int kFrontLeftTurningMotorPort = 20;

		public static final int kFrontRightDriveMotorPort = 11;
		public static final int kFrontRightTurningMotorPort = 21;

		public static final int kBackLeftDriveMotorPort = 12;
		public static final int kBackLeftTurningMotorPort = 22;

		public static final int kBackRightDriveMotorPort = 13;
		public static final int kBackRightTurningMotorPort = 23;

		public static final int kElevatorLeadMotorPort = 1;
		public static final int kElevatorFollowerMotorPort = 3;

		public static final int kAlgaeLeftMotorPort = 2;
		public static final int kAlgaeRightMotorPort = 4;

		public static final int kAlgaeIntakeExtensionMotorPort = 5;
		public static final int kCoralIntakePort = 6;
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

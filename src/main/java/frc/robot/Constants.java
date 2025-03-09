// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
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
		// TODO: FINALIZE
		public static final double kElevatorVoltage = 12;
		public static final double kP = 0.027;
		public static final double kI = 0.0;
		public static final double kD = 0.0;

		public static final int kManualVoltage = 3;

		// Elevator attributes
		public static final double kElevatorStartHeight = 0;
		public static final double kElevatorCarriageWeight = 1.0;
		public static final double kElevatorDrumRadius = 0.0254;
		public static final double kElevatorGearing = 1.0 / 6;
		public static final double kElevatorMetersPerRad = 0.413;

		// Encoder
		public static final int kElevatorAbsoluteEncoderPort = 9;
		public static final double kAbsoluteEncoderOffset = -0.417;
		public static final boolean kAbsoluteEncoderReversed = false;

		// Limits
		public static final double kElevatorMaxRadians = 4.25;
		public static final double kElevatorMinRadians = 0.1;

		public static final double kElevatorMaxHeight = 1.75; // in meters
		public static final double kElevatorMinHeight = 0.05; // in meters

		// Setpoints
		public static final double kHeightDefault = 1.0; // Dummy value
		public static final double kHeightGround = 1.0; // Dummy value
		public static final double kHeightStation = 1.0; // Dummy value
		public static final double kHeightProcessor = 1.0; // Dummy value
		public static final double kHeightL1 = 1.0; // Dummy value
		public static final double kHeightL2 = 1.0; // Dummy value
		public static final double kHeightL3 = 1.0; // Dummy value
		public static final double kHeightL4 = 1.0; // Dummy value

	}

	public static class IntakeConstants {
		// TODO: FINALIZE
		public static final int kAlgaeIntakeVoltage = 5;
		public static final int kCoralIntakeVoltage = 3;

		public static final int kAlgaeIntakeExtensionAbsoluteEncoderPort = 8;
		public static final boolean kAlgaeIntakeExtensionAbsoluteEncoderReversed = false;

		public static final double kP = 20.0; // need to tune
		public static final double kI = 0.0;
		public static final double kD = 0.0;

		public static final double kAlgaeIntakeRetractedPosition = -2.55;
		public static final double kAlgaeIntakeExtendedPosition = 2.00;
	}

	public static class OperatorConstants {
		public static final int kDriverControllerPort = 0;
		public static final int kOperatorControllerPort = 1;
	}

	public static class ModuleConstants {
		public static final double kWheelDiameterMeters = Units.inchesToMeters(3.75);
		public static final double kDriveMotorGearRatio = 1 / 6.75;
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

		public static final double kFrontLeftDriveAbsoluteEncoderOffsetRad = -2.854;
		public static final double kFrontRightDriveAbsoluteEncoderOffsetRad = -2.231;
		public static final double kBackLeftDriveAbsoluteEncoderOffsetRad = 3.022;
		public static final double kBackRightDriveAbsoluteEncoderOffsetRad = 0.349 + 0.324 + 1.029 + 0.672 - 1.961;

		public static final double kPhysicalMaxSpeedMetersPerSecond = 4.5;
		public static final double kPhysicalMaxAngularSpeedRadiansPerSecond = 3 * Math.PI;

		public static final double kTeleDriveMaxSpeedMetersPerSecond = kPhysicalMaxSpeedMetersPerSecond;
		public static final double kTeleDriveMaxAngularSpeedRadiansPerSecond = kPhysicalMaxSpeedMetersPerSecond * 0.7;

		public static final double kTeleDriveMaxAccelerationUnitsPerSecond = 3;
		public static final double kTeleDriveMaxAngularAccelerationUnitsPerSecond = 3;

		// TODO: these are also placeholders
		public static final double kRobotMass = 100.0;
		public static final double kRobotMOI = 100.0;

		// TODO: THESE ARE ALL PLACEHOLDERS
		public static final ModuleConfig kRobotModuleConfig = new ModuleConfig(1.0, // TODO: radius of drive wheels, m
				kPhysicalMaxSpeedMetersPerSecond, // max spd while driving full output, m/s
				1, // friction coefficient between wheel and carpet, (unsure so 1.0)
				new DCMotor(1.0, 1.0, 1.0, 1.0, 1.0, 1), // TODO: drive motor gearbox, including gear reduction
				1, // TODO: current limit of drive motor, Amps
				1); // number of motors per module (1 for swerve)
		public static final RobotConfig kRobotConfig = new RobotConfig(kRobotMass, // mass, kg
				kRobotMOI, // moment of inertia (why), kgm^2
				kRobotModuleConfig, // module config
				kDriveKinematics.getModules()); // locations of modules relative of robot center

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

		public static final double kDeadband = 0.05;
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

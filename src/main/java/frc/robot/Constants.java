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
	public static final boolean lightsExist = true;

	public static final boolean fieldOriented = true;

	public static class ElevatorConstants {
		// CHANGE LATER:
		public static final double kElevatorVoltage = 12;
		public static final double kP = 0.027;
		public static final double kI = 0.0;
		public static final double kD = 0.0;
		public static final double kElevatorMax = 10.0;
		public static final double kElevatorMin = 0.0;
	}

	public static class IntakeConstants {
		// TODO: CHANGE LATER:
		public static final int kAlgaeIntakeVoltage = 9;
		public static final int kCoralIntakeVoltage = 9;

	}

	public static class WristConstants {
		// TODO: CHANGE LATER:
		public static final double kWristVoltage = 12;

		public static final double kFFS = 0.0;
		public static final double kFFG = 0.0;
		public static final double kFFV = 0.0;
		public static final double kP = 0.0;
		public static final double kI = 0.0;
		public static final double kD = 0.0;
	}

	public static class OperatorConstants {
		public static final int kDriverControllerPort = 0;
		public static final int kOperatorControllerPort = 1;
	}

	public static class ModuleConstants {
		public static final double kDriveEncoderRot2Meter = 0.0;
		public static final double kDriveEncoderRPM2MeterPerSec = 0.0;
		public static final double kTurningEncoderRot2Rad = 0.0;
		public static final double kTurningEncoderRPM2RadPerSec = 0.0;
		public static final double kPTurning = 0.0;
	}

	// TODO: CHANGE LATER:
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
		public static final int kFrontRightDriveAbsoluteEncoderPort = 1;

		public static final boolean kBackLeftDriveEncoderReversed = false;
		public static final boolean kBackLeftTurningEncoderReversed = false;
		public static final double kBackLeftDriveAbsoluteEncoderOffsetRad = 0.01;
		public static final boolean kBackLeftDriveAbsoluteEncoderReversed = false;
		public static final int kBackLeftDriveAbsoluteEncoderPort = 2;

		public static final boolean kBackRightDriveEncoderReversed = false;
		public static final boolean kBackRightTurningEncoderReversed = false;
		public static final double kBackRightDriveAbsoluteEncoderOffsetRad = 0.01;
		public static final boolean kBackRightDriveAbsoluteEncoderReversed = false;
		public static final int kBackRightDriveAbsoluteEncoderPort = 3;

		public static final double kPhysicalMaxSpeedMetersPerSecond = 0.01;
		public static final double kTeleDriveMaxSpeedMetersPerSecond = 0.01;

		public static final double kTeleDriveMaxAccelerationUnitsPerSecond = 0.01;
		public static final double kTeleDriveMaxAngularAccelerationUnitsPerSecond = 0.01;

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
		public static final int kElevatorFollowerMotorPort = 2;

		public static final int kElevatorAbsoluteEncoderPort = 5;

		public static final int kAlgaeLeftMotorPort = 3;
		public static final int kAlgaeRightMotorPort = 4;

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

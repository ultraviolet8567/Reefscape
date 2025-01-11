// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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
	public static class OperatorConstants {
		public static final int kDriverControllerPort = 0;
	}

	public static class ModuleConstants
	{
		//CHANGE LATER:
		public static final double kDriveEncoderRot2Meter = 0.0;
		public static final double kDriveEncoderRPM2MeterPerSec = 0.0;
		public static final double kTurningEncoderRot2Rad = 0.0;
		public static final double kTurningEncoderRPM2RadPerSec = 0.0;
		public static final double kPTurning = 0.0;
	}
	//CHANGE LATER:
	public static class DriveConstants {
	public static final double kFrontLeftDriveAbsoluteEncoderPort = 0.01;
	public static final double kBackRightDriveAbsoluteEncoderPort = 0.01;
	public static final boolean kFrontRightDriveEncoderReversed = false;
	public static final boolean kFrontRightTurningEncoderReversed = false;
	public static final double kPhysicalMaxSpeedMetersPerSecond = 0.01;
	public static final double kFrontRightDriveAbsoluteEncoderOffsetRad = 0.01;
	public static final boolean kFrontRightDriveAbsoluteEncoderReversed = false;
	public static final boolean kBackLeftDriveEncoderReversed = false;
	public static final boolean kBackLeftTurningEncoderReversed = false;
	public static final boolean kBackRightDriveEncoderReversed = false;
	public static final boolean kBackRightTurningEncoderReversed = false;
	public static final double kBackRightDriveAbsoluteEncoderOffsetRad = 0.01;
	public static final boolean kBackRightDriveAbsoluteEncoderReversed = false;
	}
//CAN = computer area network
	public static class CAN
	{
		public static final double kFrontLeftDriveMotorPort = 0.01;
		public static final double kFrontLeftTurningMotorPort = 0.01;
		public static final double kFrontRightDriveMotorPort = 0.01;
		public static final double kBackLeftDriveMotorPort = 0.01;
		public static final double kBackLeftTurningMotorPort = 0.01;
		public static final double kBackRightDriveMotorPort = 0.01;
		public static final double kFrontRightTurningMotorPort = 0.01;
		public static final double kBackRightTurningMotorPort = 0.01;
		
	}
}


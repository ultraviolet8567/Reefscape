package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CAN;
import frc.robot.Constants.DriveConstants;
import java.util.Arrays;
//import org.littletonrobotics.junction.Logger;

public class Swerve extends SubsystemBase {
        private final SwerveModule frontLeft, frontRight, backLeft, backRight;


        public Swerve() 
        {
		    System.out.println("[Init] Creating Swerve");

		    frontLeft = new SwerveModule(CAN.kFrontLeftDriveMotorPort, CAN.kFrontLeftTurningMotorPort,
				DriveConstants.kFrontLeftDriveEncoderReversed, DriveConstants.kFrontLeftTurningEncoderReversed,
				DriveConstants.kFrontLeftDriveAbsoluteEncoderPort,
				DriveConstants.kFrontLeftDriveAbsoluteEncoderOffsetRad,
				DriveConstants.kFrontLeftDriveAbsoluteEncoderReversed);

            frontRight = new SwerveModule(CAN.kFrontRightDriveMotorPort, CAN.kFrontRightTurningMotorPort,
                DriveConstants.kFrontRightDriveEncoderReversed, DriveConstants.kFrontRightTurningEncoderReversed,
                DriveConstants.kFrontRightDriveAbsoluteEncoderPort,
                DriveConstants.kFrontRightDriveAbsoluteEncoderOffsetRad,
                DriveConstants.kFrontRightDriveAbsoluteEncoderReversed);
        
            backLeft = new SwerveModule(CAN.kBackLeftDriveMotorPort, CAN.kBackLeftTurningMotorPort,
                DriveConstants.kBackLeftDriveEncoderReversed, DriveConstants.kBackLeftTurningEncoderReversed,
                DriveConstants.kBackLeftDriveAbsoluteEncoderPort, DriveConstants.kBackLeftDriveAbsoluteEncoderOffsetRad,
                DriveConstants.kBackLeftDriveAbsoluteEncoderReversed);
                
            backRight = new SwerveModule(CAN.kBackRightDriveMotorPort, CAN.kBackRightTurningMotorPort,
				DriveConstants.kBackRightDriveEncoderReversed, DriveConstants.kBackRightTurningEncoderReversed,
				DriveConstants.kBackRightDriveAbsoluteEncoderPort,
				DriveConstants.kBackRightDriveAbsoluteEncoderOffsetRad,
				DriveConstants.kBackRightDriveAbsoluteEncoderReversed);
	    }

}
    public SwerveModuleState[] getModuleStates() {
        return new SwerveModuleState[]{frontLeft.getState(), frontRight.getstate(), backLeft.getState(),
                        backRight.getState()};
    }
    public void setModuleStates(ChassisSpeeds chassisSpeeds) {
        SwerveModuleState[] moduleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(chassisSpeeds);
        setModuleStates(moduleStates);    
    }

    public void setModuleStates(SwerveModuleState[] desiredStates) {
        double maxSpeed = Lights.getInstance().isDemo
                        ? Drive.Constants.kDemoTeleDriveMaxSpeedMetersPerSecond
                        : Drive.Constants.kTeleDriveMaxSpeedMetersPerSecond;

        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, maxSpeed);
        frontLeft.setDesiredState(desiredStates[0]);
        frontRight.setDesiredState(desiredStates[1]);
        backLeft.setDesiredState(desiredStatees[2]);
        backRight.setDesiredState(desiredStates[3]);

        t.recordOutput("Swerve/ModuleStateSetpoints", desiredStates);    
    }

    //Sets the wheels to 45 degree angles so it doesn't move
    public void lockWheels() {
        SwerveModuleState[] locked = new SwerveModuleState[]{new SwerveModuleState(0, Rotation2d,fromDegrees(135)),
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
        
    }
    

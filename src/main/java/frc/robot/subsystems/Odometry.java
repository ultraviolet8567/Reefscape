package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.AllianceFlipUtil;
import frc.robot.Constants.DriveConstants;

public class Odometry extends SubsystemBase {
	private Swerve swerve;
	private SwerveDrivePoseEstimator poseEstimator;

	private Pigeon2 gyro;
	private SwerveDriveOdometry odometer;
	
	public Odometry(Swerve swerve) {
		System.out.println("[Init] Creating Odometry");
		
		this.swerve = swerve;

		/* Gyro */
		gyro = new Pigeon2(31);
		gyro.reset();

		/* Odometer */
		odometer = new SwerveDriveOdometry(DriveConstants.kDriveKinematics, getGyrometerHeading(), 
				swerve.getModulePositions(), AllianceFlipUtil.apply(new Pose2d(1.5, 5.5, new Rotation2d())));

		/* Odometry */
		poseEstimator = new SwerveDrivePoseEstimator(DriveConstants.kDriveKinematics, gyro.getRotation2d(),
				swerve.getModulePositions(), new Pose2d());
	}

	/* Runs periodically (about once every 20 ms) */
	@Override
	public void periodic() {
		Logger.recordOutput("Odometry/Pose", getPose());
		Logger.recordOutput("Odometry/Heading", getHeading());

		Logger.recordOutput("Gyrometer/Pose", odometer.getPoseMeters());
		Logger.recordOutput("Gyrometer/Heading", gyro.getRotation2d());

		/* Gyro */
		odometer.update(getGyrometerHeading(), swerve.getModulePositions());

		/* Odometry */
		poseEstimator.update(gyro.getRotation2d(), swerve.getModulePositions());
	}

	public Pose2d getPose() {
		return poseEstimator.getEstimatedPosition();
	}

	public Pose2d getOdometerPose() {
		return odometer.getPoseMeters();
	}

	public Rotation2d getHeading() {
		return poseEstimator.getEstimatedPosition().getRotation();
	}

	public Rotation2d getGyrometerHeading() {
		return gyro.getRotation2d();
	}

	public void setGyroYaw(Rotation2d yaw) {
		gyro.setYaw(yaw.getDegrees());
	}

	public void resetPose(Pose2d pose) {
		poseEstimator.resetPosition(gyro.getRotation2d(), swerve.getModulePositions(), pose);
	}

	public void resetOdometerPose(Pose2d pose) {
		odometer.resetPosition(getGyrometerHeading(), swerve.getModulePositions(), pose);
	}

	public void resetGyrometerHeading() {
		gyro.reset();
	}
}

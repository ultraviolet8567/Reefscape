package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.util.AllianceFlipUtil;
import java.util.Optional;
import org.littletonrobotics.junction.Logger;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;

public class Odometry extends SubsystemBase {
	private Swerve swerve;
	private SwerveDrivePoseEstimator poseEstimator;

	private Pigeon2 gyro;
	private SwerveDriveOdometry odometer;

	private PhotonCamera frontCamera, backCamera;
	private PhotonPoseEstimator frontPoseEstimator, backPoseEstimator;

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

		/* PhotonVision */
		System.out.println("[Init] Starting PhotonVision");

		// Add to constants file: PhotonVisionConstants.hostname = "photonvision.local"
		// or whatever the hostname is renamed to in the PhotonVision web interface
		PortForwarder.add(5800, "Photon-OrangePi-Front", 5800);
		frontCamera = new PhotonCamera("OV9281_Front ");
		// backCamera = new PhotonCamera("OV9281_Back");

		// TODO: Figure out whether to use closest to reference pose or lowest ambiguity
		frontPoseEstimator = new PhotonPoseEstimator(VisionConstants.kAprilTagFieldLayout,
				PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, VisionConstants.kFrontCamToRobot);
		frontPoseEstimator.setMultiTagFallbackStrategy(PoseStrategy.CLOSEST_TO_REFERENCE_POSE);
		// frontPoseEstimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
		// backPoseEstimator = new
		// PhotonPoseEstimator(VisionConstants.kAprilTagFieldLayout,
		// PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, VisionConstants.kBackCamToRobot);
		// backPoseEstimator.setMultiTagFallbackStrategy(PoseStrategy.CLOSEST_TO_REFERENCE_POSE);
		// backPoseEstimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
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

		/* Vision */
		updateVision();

		/* Odometry */
		poseEstimator.update(gyro.getRotation2d(), swerve.getModulePositions());
	}

	public void updateVision() {
		frontPoseEstimator.setReferencePose(getPose());
		// backPoseEstimator.setReferencePose(getPose());

		// Could get all unread results rather than just the latest
		var frontResult = frontCamera.getLatestResult();
		Optional<EstimatedRobotPose> frontEstPose = frontPoseEstimator.update(frontResult);
		if (frontEstPose.isPresent()) {
			// Could add a standard deviation calculation for more accuracy
			poseEstimator.addVisionMeasurement(frontEstPose.get().estimatedPose.toPose2d(),
					frontResult.getTimestampSeconds());
		}

		// Could get all unread results rather than just the latest
		// var backResult = backCamera.getLatestResult();
		// Optional<EstimatedRobotPose> backEstPose =
		// backPoseEstimator.update(backResult);
		// if (backEstPose.isPresent()) {
		// // Could add a standard deviation calculation for more accuracy
		// poseEstimator.addVisionMeasurement(backEstPose.get().estimatedPose.toPose2d(),
		// backResult.getTimestampSeconds());
		// }
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

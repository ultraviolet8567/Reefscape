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

	private Optional<EstimatedRobotPose> frontEstPose, backEstPose;

	private Pose2d detectedTagPoseFront;
	private Pose2d detectedTagPoseBack;

	public Odometry(Swerve swerve) {
		System.out.println("[Init] Creating Odometry");

		this.swerve = swerve;

		/* Gyro */
		gyro = new Pigeon2(31);
		gyro.reset();

		/* Odometer */
		odometer = new SwerveDriveOdometry(DriveConstants.kDriveKinematics, getGyrometerHeading(),
				swerve.getModulePositions(), new Pose2d());

		/* Odometry */
		poseEstimator = new SwerveDrivePoseEstimator(DriveConstants.kDriveKinematics, gyro.getRotation2d(),
				swerve.getModulePositions(), new Pose2d());

		/* PhotonVision */
		System.out.println("[Init] Starting PhotonVision");

		// Add to constants file: PhotonVisionConstants.hostname = "photonvision.local"
		// or whatever the hostname is renamed to in the PhotonVision web interface
		PortForwarder.add(5800, "Photon-OrangePi-Front", 5800);
		frontCamera = new PhotonCamera("OV9281_Front");
		PortForwarder.add(5800, "Photon-OrangePi-Bront", 5800);
		backCamera = new PhotonCamera("OV9281_Back");

		frontPoseEstimator = new PhotonPoseEstimator(VisionConstants.kAprilTagFieldLayout,
				PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, VisionConstants.kFrontCamToRobot);
		frontPoseEstimator.setMultiTagFallbackStrategy(PoseStrategy.CLOSEST_TO_REFERENCE_POSE);

		backPoseEstimator = new PhotonPoseEstimator(VisionConstants.kAprilTagFieldLayout,
				PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, VisionConstants.kBackCamToRobot);
		backPoseEstimator.setMultiTagFallbackStrategy(PoseStrategy.CLOSEST_TO_REFERENCE_POSE);
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
		backPoseEstimator.setReferencePose(getPose());

		// Could get all unread results rather than just the latest
		var frontResult = frontCamera.getLatestResult();
		frontEstPose = frontPoseEstimator.update(frontResult);
		if (frontEstPose.isPresent()) {
			// Could add a standard deviation calculation for more accuracy
			poseEstimator.addVisionMeasurement(frontEstPose.get().estimatedPose.toPose2d(),
					frontResult.getTimestampSeconds());

			Logger.recordOutput("Vision/Front Estimated Pose", frontEstPose.get().estimatedPose.toPose2d());
			Logger.recordOutput("Vision/Front April Tag ID", frontResult.getBestTarget().getFiducialId());

			// get the position of the best detected april tag
			detectedTagPoseFront = new Pose2d();
		}

		// Could get all unread results rather than just the latest
		var backResult = backCamera.getLatestResult();
		backEstPose = backPoseEstimator.update(backResult);
		if (backEstPose.isPresent()) {
			// Could add a standard deviation calculation for more accuracy
			poseEstimator.addVisionMeasurement(backEstPose.get().estimatedPose.toPose2d(),
					backResult.getTimestampSeconds());

			Logger.recordOutput("Vision/Back Estimated Pose", backEstPose.get().estimatedPose.toPose2d());
			Logger.recordOutput("Vision/Back April Tag ID", backResult.getBestTarget().getFiducialId());

			// get the position of the best detected april tag
			detectedTagPoseBack = new Pose2d();
		}
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

enum ReefEdge {
	A,
	B,
	C,
	D,
	E,
	F;

	Pose2d edgePosition() {
		return new Pose2d();
	}

	Pose2d setpointLeft() {
		return new Pose2d();
	}

	Pose2d setpointRight() {
		switch (this) {
			case A: 
			case B:
		}
	}
}
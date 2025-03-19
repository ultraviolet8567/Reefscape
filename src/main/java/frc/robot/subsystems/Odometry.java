package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.FieldConstants;
import frc.robot.Constants.VisionConstants;
import java.util.List;
import java.util.Optional;
import org.littletonrobotics.junction.Logger;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonTrackedTarget;

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
		Optional<EstimatedRobotPose> frontEstPose = frontPoseEstimator.update(frontResult);
		if (frontEstPose.isPresent()) {
			// Could add a standard deviation calculation for more accuracy
			poseEstimator.addVisionMeasurement(frontEstPose.get().estimatedPose.toPose2d(),
					frontResult.getTimestampSeconds());

			Logger.recordOutput("Vision/Front Estimated Pose", frontEstPose.get().estimatedPose.toPose2d());
			Logger.recordOutput("Vision/Front Tags", getTagPositions(frontResult.getTargets()));
		}

		// Could get all unread results rather than just the latest
		var backResult = backCamera.getLatestResult();
		Optional<EstimatedRobotPose> backEstPose = backPoseEstimator.update(backResult);
		if (backEstPose.isPresent()) {
			// Could add a standard deviation calculation for more accuracy
			poseEstimator.addVisionMeasurement(backEstPose.get().estimatedPose.toPose2d(),
					backResult.getTimestampSeconds());

			Logger.recordOutput("Vision/Back Estimated Pose", backEstPose.get().estimatedPose.toPose2d());
			Logger.recordOutput("Vision/Back Tags", getTagPositions(backResult.getTargets()));
			Logger.recordOutput("Vision/Back Best Tag", backResult.getBestTarget());
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

	public Pose3d[] getTagPositions(List<PhotonTrackedTarget> targets) {
		List<Pose3d> tagPoses = List.of();

		for (PhotonTrackedTarget target : targets) {
			Optional<Pose3d> tagPose = VisionConstants.kAprilTagFieldLayout.getTagPose(target.getFiducialId());
			if (tagPose.isPresent()) {
				tagPoses.add(tagPose.get());
			}
		}

		return tagPoses.toArray(new Pose3d[0]);
	}

	public ReefEdge closestReefEdge() {
		return ReefEdge.nearestReefEdge(getPose());
	}

	public static enum ReefEdge {
		A, B, C, D, E, F;

		Pose2d edgePosition() {
			switch (this) {
				case A :
					return FieldConstants.kReefEdgeA;
				case B :
					return FieldConstants.kReefEdgeB;
				case C :
					return FieldConstants.kReefEdgeC;
				case D :
					return FieldConstants.kReefEdgeD;
				case E :
					return FieldConstants.kReefEdgeE;
				case F :
					return FieldConstants.kReefEdgeF;
				default :
					return FieldConstants.kReefEdgeA;
			}
		}

		public static ReefEdge nearestReefEdge(Pose2d pose) {
			Pose2d closestReefPose = pose.nearest(List.of(A.edgePosition(), B.edgePosition(), C.edgePosition(),
					D.edgePosition(), E.edgePosition(), F.edgePosition()));

			if (closestReefPose.equals(A.edgePosition())) {
				return A;
			} else if (closestReefPose.equals(B.edgePosition())) {
				return B;
			} else if (closestReefPose.equals(C.edgePosition())) {
				return C;
			} else if (closestReefPose.equals(D.edgePosition())) {
				return D;
			} else if (closestReefPose.equals(E.edgePosition())) {
				return E;
			} else if (closestReefPose.equals(F.edgePosition())) {
				return F;
			} else {
				return A;
			}
		}

		public Pose2d setpointLeft() {
			// Get the position of this edge
			Pose2d edgePose = this.edgePosition();

			// Rotate translation to be along face of this edge
			Translation2d edgeToLeftStalk = FieldConstants.kLeftStalkOffset.rotateBy(edgePose.getRotation());

			// Return the pose of the left stalk
			Pose2d stalkPose = edgePose.transformBy(new Transform2d(edgeToLeftStalk, new Rotation2d()));

			// To get the coral deploy point to stalkPose, need to transform the setpoint to be for the center of the robot
			return stalkPose.transformBy(VisionConstants.kRobotCenterToCoralDeploy.inverse());
		}

		public Pose2d setpointRight() {
			// Get the position of this edge
			Pose2d edgePose = this.edgePosition();

			// Rotate translation to be along face of this edge
			Translation2d edgeToRightStalk = FieldConstants.kRightStalkOffset.rotateBy(edgePose.getRotation());

			// Return the pose of the right stalk
			Pose2d stalkPose = edgePose.transformBy(new Transform2d(edgeToRightStalk, new Rotation2d()));
			
			// To get the coral deploy point to stalkPose, need to transform the setpoint to be for the center of the robot
			return stalkPose.transformBy(VisionConstants.kRobotCenterToCoralDeploy.inverse());
		}
	}
}

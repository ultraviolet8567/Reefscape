package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Odometry;
import frc.robot.subsystems.Swerve;
import java.util.function.Supplier;

public class SwerveTeleOp extends Command {
	private final Swerve swerve;
	private final Odometry odometry;
	private final Supplier<Double> xSpdFunction, ySpdFunction, turningSpdFunction;
	private final Supplier<Boolean> rightBumper;
	private final SlewRateLimiter xLimiter, yLimiter, turningLimiter;

	public SwerveTeleOp(Swerve swerve, Odometry odometry, Supplier<Double> xSpdFunction, Supplier<Double> ySpdFunction,
			Supplier<Double> turningSpdFunction, Supplier<Boolean> rightBumper) {
		this.swerve = swerve;
		this.odometry = odometry;
		this.xSpdFunction = xSpdFunction;
		this.ySpdFunction = ySpdFunction;
		this.turningSpdFunction = turningSpdFunction;
		this.rightBumper = rightBumper;
		this.xLimiter = new SlewRateLimiter(DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);
		this.yLimiter = new SlewRateLimiter(DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);
		this.turningLimiter = new SlewRateLimiter(DriveConstants.kTeleDriveMaxAngularAccelerationUnitsPerSecond);

		addRequirements(swerve);
	}

	@Override
	public void initialize() {
	}

	@Override
	public void execute() {
		double xSpeed = xSpdFunction.get();
		double ySpeed = ySpdFunction.get();
		double turningSpeed = turningSpdFunction.get();

		xSpeed *= (xSpeed > 0) ? (1.0 / 0.8) : (1.0 / 0.9);
		ySpeed *= (1.0 / 0.9);

		if (Math.sqrt(Math.pow(xSpeed, 2)) < OIConstants.kDeadband) {
			xSpeed = 0;
			ySpeed = 0;
		}
		turningSpeed = Math.abs(turningSpeed) > OIConstants.kDeadband ? turningSpeed : 0;

		if (rightBumper.get()) {
			RobotContainer.getDriverJoystick().setRumble(RumbleType.kRightRumble, 0.025);
			xSpeed *= 0.33;
			ySpeed *= 0.33;
			turningSpeed *= 0.33;
		} else {
			RobotContainer.getDriverJoystick().setRumble(RumbleType.kBothRumble, 0);

		}

		xSpeed = MathUtil.clamp(xSpeed, -1, 1);
		ySpeed = MathUtil.clamp(ySpeed, -1, 1);
		turningSpeed = MathUtil.clamp(turningSpeed, -1, 1);

		// double teleMaxSpeed = Lights.getInstance().isDemo
		// ? DriveConstants.kDemoTeleDriveMaxSpeedMetersPerSecond
		// : DriveConstants.kTeleDriveMaxSpeedMetersPersecond;
		// double teleMaxAngularSpeed = Lights.getInstance().isDemo?
		// DriveConstants.kDemoTeleDriveMaxAngularSpeedRadiansPerSecond
		// : DriveConstants.kTeleDriveMaxAngularSpeedRadiansPerSecond;
		double teleMaxSpeed = 0.01;
		double teleMaxAngularSpeed = 0.01;
		xSpeed = xLimiter.calculate(xSpeed) * teleMaxSpeed;
		ySpeed = yLimiter.calculate(ySpeed) * teleMaxSpeed;
		turningSpeed = turningLimiter.calculate(turningSpeed) * teleMaxAngularSpeed;

		ChassisSpeeds chassisSpeeds;
		if (Constants.fieldOriented) {
			chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, turningSpeed,
					odometry.getGyrometerHeading());
		} else {
			chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, turningSpeed);
		}

		swerve.setModuleStates(chassisSpeeds);
	}

	@Override
	public void end(boolean interrupted) {
		swerve.stopModules();
	}

}

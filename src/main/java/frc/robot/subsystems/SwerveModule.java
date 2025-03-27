package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;

//Encoder: Thing that is above wheel and records how much it moves.
public class SwerveModule {
	private final SparkMax driveMotor;
	private final SparkMax turningMotor;
	private final SparkMaxConfig driveConfig;
	private final SparkMaxConfig turningConfig;
	private final PIDController turningPidController;
	public SwerveModulePosition modulePosition;
	private final AnalogInput absoluteEncoder;
	private double ConfigOffset;
	private final boolean ConfigReversed;

	private SendableChooser<Boolean> optimizerOverride;

	public SwerveModule(int driveMotorID, int turningMotorID, boolean driveMotorReversed, boolean turningMotorReversed,
			int absoluteEncoderID, double ConfigOffset, boolean ConfigReversed,
			SendableChooser<Boolean> optimizerOverride) {
		System.out.println("[Init] Creating SwerveModule with absoluteEncoderID: " + absoluteEncoderID);

		this.ConfigOffset = ConfigOffset;
		this.ConfigReversed = ConfigReversed;
		absoluteEncoder = new AnalogInput(absoluteEncoderID);
		driveMotor = new SparkMax(driveMotorID, MotorType.kBrushless);
		turningMotor = new SparkMax(turningMotorID, MotorType.kBrushless);

		driveConfig = new SparkMaxConfig();
		turningConfig = new SparkMaxConfig();

		driveConfig.voltageCompensation(12.0);
		driveConfig.smartCurrentLimit(40);
		driveConfig.inverted(driveMotorReversed);
		driveConfig.idleMode(IdleMode.kBrake);

		turningConfig.voltageCompensation(12.0);
		turningConfig.smartCurrentLimit(40);
		turningConfig.idleMode(IdleMode.kBrake);
		turningConfig.inverted(turningMotorReversed);

		driveConfig.encoder.positionConversionFactor(ModuleConstants.kDriveEncoderRot2Meter);
		driveConfig.encoder.velocityConversionFactor(ModuleConstants.kDriveEncoderRPM2MeterPerSec);
		turningConfig.encoder.positionConversionFactor(ModuleConstants.kTurningEncoderRot2Rad);
		turningConfig.encoder.velocityConversionFactor(ModuleConstants.kTurningEncoderRPM2RadPerSec);

		driveMotor.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
		turningMotor.configure(turningConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		turningPidController = new PIDController(ModuleConstants.kPTurning, 0, 0);
		turningPidController.enableContinuousInput(-Math.PI, Math.PI);

		resetEncoders();

		this.optimizerOverride = optimizerOverride;
	}

	public double getDrivePosition() {
		return driveMotor.getEncoder().getPosition();
	}

	public double getTurningPosition() {
		return turningMotor.getEncoder().getPosition();
	}
	public double getDriveVelocity() {
		return driveMotor.getEncoder().getVelocity();
	}

	public double getTurningVelocity() {
		return turningMotor.getEncoder().getVelocity();
	}

	public double getAbsoluteEncoderAngle() {
		double angle = absoluteEncoder.getAverageVoltage() / RobotController.getVoltage5V();
		angle *= 2 * Math.PI;
		angle += ConfigOffset;
		angle = MathUtil.inputModulus(angle, -Math.PI, Math.PI);

		return angle * (ConfigReversed ? -1 : 1);
	}

	public void resetEncoders() {
		driveMotor.getEncoder().setPosition(0);
		turningMotor.getEncoder().setPosition(getAbsoluteEncoderAngle());
	}

	public SwerveModuleState getState() {
		return new SwerveModuleState(getDriveVelocity(), new Rotation2d(getTurningPosition()));
	}

	public void setDesiredState(SwerveModuleState state) {
		if (Math.abs(state.speedMetersPerSecond) < 0.001) {
			stop();
		} else {
			if (!optimizerOverride.getSelected()) {
				state.optimize(getState().angle);
			}

			driveMotor.set(state.speedMetersPerSecond / DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
			turningMotor.set(turningPidController.calculate(getTurningPosition(), state.angle.getRadians()));
		}
	}

	public SwerveModulePosition getModulePosition() {
		return new SwerveModulePosition(getDrivePosition(), getState().angle);
	}

	public void stop() {
		driveMotor.stopMotor();
		turningMotor.stopMotor();
	}
}

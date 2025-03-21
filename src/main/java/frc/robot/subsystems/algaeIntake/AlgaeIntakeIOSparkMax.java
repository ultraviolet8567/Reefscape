package frc.robot.subsystems.algaeIntake;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import frc.robot.Constants.CAN;
import frc.robot.Constants.IntakeConstants;
import org.littletonrobotics.junction.Logger;

public class AlgaeIntakeIOSparkMax implements AlgaeIntakeIO {
	private final SparkMax rightMotor, leftMotor;
	// private final SparkFlex extensionMotor;
	private final SparkMaxConfig rightConfig, leftConfig;
	// private final SparkFlexConfig extensionConfig;
	private final DutyCycleEncoder absoluteEncoder;
	private final PIDController extensionPidController;

	// Constructor
	public AlgaeIntakeIOSparkMax() {
		System.out.println("[Init] Creating AlgaeIntakeIOSparkMax");

		// TODO: configure the position/velocity conversion factors

		// Initialize the CANSparkMax motors for right and left
		rightMotor = new SparkMax(CAN.kAlgaeLeftMotorPort, MotorType.kBrushless);
		leftMotor = new SparkMax(CAN.kAlgaeRightMotorPort, MotorType.kBrushless);
		rightConfig = new SparkMaxConfig();
		leftConfig = new SparkMaxConfig();

		leftConfig.smartCurrentLimit(50);
		rightConfig.smartCurrentLimit(50);

		rightMotor.configure(rightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
		leftMotor.configure(leftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		// Initialize the CANSparkMax motors & absolute encoder for the extender
		// extensionMotor = new SparkFlex(CAN.kAlgaeIntakeExtensionMotorPort,
		// MotorType.kBrushless);
		// extensionConfig = new SparkFlexConfig();

		// extensionMotor.configure(extensionConfig, ResetMode.kResetSafeParameters,
		// PersistMode.kPersistParameters);

		absoluteEncoder = new DutyCycleEncoder(IntakeConstants.kAlgaeIntakeExtensionAbsoluteEncoderPort);

		extensionPidController = new PIDController(IntakeConstants.kP, IntakeConstants.kI, IntakeConstants.kD);
	}

	@Override
	public void updateInputs(AlgaeIntakeIOInputs inputs) {
		// this is in rpm, convert
		inputs.currentAmps = new double[]{rightMotor.getOutputCurrent(), leftMotor.getOutputCurrent()};
		// extensionMotor.getOutputCurrent()};
		inputs.appliedVoltage = new double[]{rightMotor.getAppliedOutput() * rightMotor.getBusVoltage(),
				leftMotor.getAppliedOutput() * leftMotor.getBusVoltage()};
		// extensionMotor.getAppliedOutput() * extensionMotor.getBusVoltage()};
		inputs.velocityRadsPerSecond = new double[]{rightMotor.getEncoder().getVelocity(),
				leftMotor.getEncoder().getVelocity()}; // extensionMotor.getEncoder().getVelocity()};
		inputs.tempCelsius = new double[]{rightMotor.getMotorTemperature(), leftMotor.getMotorTemperature()};
		// extensionMotor.getMotorTemperature()};
		inputs.absoluteEncoderValue = getAbsoluteEncoderAngle();
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		rightMotor.setVoltage(voltage * 0.3);
		leftMotor.setVoltage(-voltage * 0.3);
	}

	public double getTurningPosition() {
		return getAbsoluteEncoderAngle();
	}

	public double getAbsoluteEncoderAngle() {
		double angle = absoluteEncoder.get();
		angle *= 2 * Math.PI;
		angle += IntakeConstants.kAlgaeIntakeExtensionAbsoluteEncoderOffset;
		angle = MathUtil.inputModulus(angle, 0, 2 * Math.PI);

		return angle * (IntakeConstants.kAlgaeIntakeExtensionAbsoluteEncoderReversed ? -1 : 1);
	}

	public void setExtension(boolean algaeExtended) {
		double setpoint = algaeExtended
				? IntakeConstants.kAlgaeIntakeExtendedPosition
				: IntakeConstants.kAlgaeIntakeRetractedPosition;

		double pidOutput = extensionPidController.calculate(getAbsoluteEncoderAngle(), setpoint);
		// extensionMotor.set(pidOutput);
		Logger.recordOutput("AlgaeIntake", pidOutput);
	}

	@Override
	public void stop() {
		rightMotor.setVoltage(0);
		leftMotor.setVoltage(0);
	}

	@Override
	public void stopExtension() {
		// extensionMotor.setVoltage(0);
	}
}

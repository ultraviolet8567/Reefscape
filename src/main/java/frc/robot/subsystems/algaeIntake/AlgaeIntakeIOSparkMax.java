package frc.robot.subsystems.algaeIntake;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import frc.robot.Constants.CAN;
import frc.robot.Constants.IntakeConstants;

public class AlgaeIntakeIOSparkMax implements AlgaeIntakeIO {
	private final SparkMax rightMotor, leftMotor;
	private final SparkFlex extensionMotor;
	private final SparkMaxConfig rightConfig, leftConfig;
	private final SparkFlexConfig extensionConfig;
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

		rightMotor.configure(rightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
		leftMotor.configure(leftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		// Initialize the CANSparkMax motors & absolute encoder for the extender
		extensionMotor = new SparkFlex(CAN.kAlgaeIntakeExtensionMotorPort, MotorType.kBrushless);
		extensionConfig = new SparkFlexConfig();

		extensionMotor.configure(extensionConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		absoluteEncoder = new DutyCycleEncoder(IntakeConstants.kAlgaeIntakeExtensionAbsoluteEncoderPort);

		extensionPidController = new PIDController(IntakeConstants.kP, IntakeConstants.kI, IntakeConstants.kD);
		extensionPidController.enableContinuousInput(-Math.PI, Math.PI);
	}

	@Override
	public void updateInputs(AlgaeIntakeIOInputs inputs) {
		// this is in rpm, convert
		inputs.currentVoltage = new double[]{rightMotor.getOutputCurrent(), leftMotor.getOutputCurrent()};
		inputs.appliedVoltage = new double[]{rightMotor.getAppliedOutput() * rightMotor.getBusVoltage(),
				leftMotor.getAppliedOutput() * leftMotor.getBusVoltage()};
		inputs.velocityRadsPerSecond = new double[]{rightMotor.getEncoder().getVelocity(),
				leftMotor.getEncoder().getVelocity()};
		inputs.tempCelsius = new double[]{rightMotor.getMotorTemperature(), leftMotor.getMotorTemperature()};
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		rightMotor.setVoltage(voltage * 0.3);
		leftMotor.setVoltage(-voltage * 0.3);
	}

	public double getTurningPosition() {
		return getAbsoluteEncoderAngle();
		// return turningMotor.getEncoder().getPosition();
	}

	public double getAbsoluteEncoderAngle() {
		double angle = absoluteEncoder.get();
		angle *= 2 * Math.PI;
		angle += IntakeConstants.kAlgaeIntakeAbosluteEncoderOffset;
		angle = MathUtil.inputModulus(angle, -Math.PI, Math.PI);

		return angle * (IntakeConstants.kAlgaeIntakeExtensionAbsoluteEncoderReversed ? -1 : 1);
	}

	public void setExtension(double setpoint) {
		extensionMotor.set(extensionPidController.calculate(getTurningPosition(), setpoint));
	}

	@Override
	public void stop() {
		rightMotor.setVoltage(0);
		leftMotor.setVoltage(0);
		extensionMotor.setVoltage(0);
	}
}

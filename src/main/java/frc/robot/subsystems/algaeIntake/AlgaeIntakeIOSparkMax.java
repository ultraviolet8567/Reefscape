package frc.robot.subsystems.algaeIntake;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.Constants.CAN;

public class AlgaeIntakeIOSparkMax implements AlgaeIntakeIO {
	private final SparkMax rightMotor, leftMotor;
	private final SparkMaxConfig rightConfig, leftConfig;
	// private final RelativeEncoder encoder;

	// Constructor
	public AlgaeIntakeIOSparkMax() {
		System.out.println("[Init] Creating AlgaeIntakeIOSparkMax");

		// Initialize the CANSparkMax motors for right and left
		rightMotor = new SparkMax(CAN.kAlgaeLeftMotorPort, MotorType.kBrushless);
		leftMotor = new SparkMax(CAN.kAlgaeRightMotorPort, MotorType.kBrushless);
		rightConfig = new SparkMaxConfig();
		leftConfig = new SparkMaxConfig();

		rightMotor.configure(rightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
		leftMotor.configure(leftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		rightMotor.setVoltage(voltage * 0.3);
		leftMotor.setVoltage(-voltage * 0.3);
	}

	// Will be called periodically
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
	public void stop() {
		rightMotor.setVoltage(0);
		leftMotor.setVoltage(0);
	}
}

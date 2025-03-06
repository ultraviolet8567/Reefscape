package frc.robot.subsystems.coralIntake;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

public class CoralIntakeIOSparkMax implements CoralIntakeIO {
	private final SparkMax motor;
	private final SparkMaxConfig config;
	public CoralIntakeIOSparkMax() {
		System.out.println("[Init] Creating CoralIntakeIOSparkMax");

		// TODO: configure the position/velocity conversion factors

		// Initialize the CANSparkMax motors for main and follower
		motor = new SparkMax(5, MotorType.kBrushless);
		config = new SparkMaxConfig();

		motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
	}

	// Will be called periodically
	@Override
	public void updateInputs(CoralIntakeIOInputs inputs) {
		// this is in rpm, convert
		inputs.currentVoltage = motor.getOutputCurrent();
		inputs.appliedVoltage = motor.getAppliedOutput() * motor.getBusVoltage();
		inputs.velocityRadsPerSecond = motor.getEncoder().getVelocity();
		inputs.tempCelsius = motor.getMotorTemperature();
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		motor.setVoltage(voltage);
	}

	@Override
	public void stop() {
		motor.setVoltage(0);
	}
}

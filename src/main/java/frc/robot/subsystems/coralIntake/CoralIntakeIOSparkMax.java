package frc.robot.subsystems.coralIntake;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import frc.robot.Constants.CAN;

public class CoralIntakeIOSparkMax implements CoralIntakeIO {
	private final SparkFlex motor;
	private final SparkFlexConfig config;
	public CoralIntakeIOSparkMax() {
		System.out.println("[Init] Creating CoralIntakeIOSparkMax");

		// TODO: configure the position/velocity conversion factors

		// Initialize the CANSparkMax motors for main and follower
		motor = new SparkFlex(CAN.kCoralIntakePort, MotorType.kBrushless);
		config = new SparkFlexConfig();

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

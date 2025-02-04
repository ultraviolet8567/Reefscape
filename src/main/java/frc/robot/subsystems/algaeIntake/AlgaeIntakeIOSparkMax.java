package frc.robot.subsystems.algaeIntake;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

public class AlgaeIntakeIOSparkMax implements AlgaeIntakeIO {
	private final SparkMax leadMotor, followerMotor;
	private final SparkMaxConfig leadConfig, followerConfig;
	// private final RelativeEncoder encoder;

	// Constructor
	public AlgaeIntakeIOSparkMax() {
		// Initialize the CANSparkMax motors for main and follower
		leadMotor = new SparkMax(3, MotorType.kBrushless);
		followerMotor = new SparkMax(4, MotorType.kBrushless);
		leadConfig = new SparkMaxConfig();
		followerConfig = new SparkMaxConfig();

		followerConfig.inverted(true);
		followerConfig.follow(leadMotor);

		leadMotor.configure(leadConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
		followerMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		leadMotor.setVoltage(voltage);
	}

	// Will be called periodically
	@Override
	public void updateInputs(AlgaeIntakeIOInputs inputs) {
		// this is in rpm, convert
		inputs.currentVoltage = new double[]{leadMotor.getOutputCurrent(), followerMotor.getOutputCurrent()};
		inputs.appliedVoltage = new double[]{leadMotor.getAppliedOutput() * leadMotor.getBusVoltage(),
				followerMotor.getAppliedOutput() * followerMotor.getBusVoltage()};
		inputs.velocityRadsPerSecond = new double[]{leadMotor.getEncoder().getVelocity(),
				followerMotor.getEncoder().getVelocity()};
		inputs.tempCelsius = new double[]{leadMotor.getMotorTemperature(), followerMotor.getMotorTemperature()};
	}

	@Override
	public void stop() {
		leadMotor.setVoltage(0);
	}
}

package frc.robot.subsystems.algaeIntake;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.Constants.CAN;

//
public class AlgaeIntakeIOSparkMax implements AlgaeIntakeIO {

	// Attributes go here
	// visibility final? type name = value
	// final is a constant but also not really
	private final SparkMax rightMotor, leftMotor;
	private final SparkMaxConfig config;

	// Constructor
	public AlgaeIntakeIOSparkMax() {
		rightMotor = new SparkMax(CAN.kAlgaeRightMotorPort, MotorType.kBrushless);
		leftMotor = new SparkMax(CAN.kAlgaeLeftMotorPort, MotorType.kBrushless);

		config = new SparkMaxConfig();
		config.idleMode(IdleMode.kBrake);
		config.smartCurrentLimit(40);

		rightMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
		leftMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		rightMotor.setVoltage(-voltage);
		leftMotor.setVoltage(voltage);
	}

	@Override
	public void stop() {
		rightMotor.setVoltage(0);
		leftMotor.setVoltage(0);
	}
}

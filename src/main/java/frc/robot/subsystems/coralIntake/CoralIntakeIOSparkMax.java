package frc.robot.subsystems.algaeIntake;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.Constants.CAN;

public class AlgaeIntakeIOSparkMax implements AlgaeIntakeIO {
	private final SparkMax leftIntakeMotor, rightIntakeMotor;
	private final RelativeEncoder leftIntakeEncoder, rightIntakeEncoder;

    // TODO: still need to code possible sensor

	// Constructor
	public AlgaeIntakeIOSparkMax() {
		// Initialize the CANSparkMax motors for left and right
		leftIntakeMotor = new CANSparkMax(CAN.kLeftAlgaeIntakePort, MotorType.kBrushless);
		SparkConfig.config(leftIntakeEncoder, SparkType.kSparkMax);
		rightIntakeMotor = new CANSparkMax(CAN.kRightAlgaeIntakePort, MotorType.kBrushless);
		SparkConfig.config(rightIntakeEncoder, SparkType.kSparkMax);

		leftIntakeEncoder = leftIntakeMotor.getEncoder();
		rightIntakeEncoder = rightIntakeMotor.getEncoder();

        leftIntakeMotor.setIdleMode(IdleMode.kBrake);
    	rightIntakeMotor.setIdleMode(IdleMode.kBrake);
	}

	@Override
	public void updateInputs(IntakeIOInputs inputs) {
		inputs.velocityRadPerSec = leftIntakeEncoder.getVelocity();
		inputs.appliedVoltage = leftIntakeMotor.getAppliedOutput() * intakeMotor.getBusVoltage();
		inputs.currentAmps = new double[]{leftIntakeMotor.getOutputCurrent()};
		inputs.tempCelsius = new double[]{leftIntakeMotor.getMotorTemperature()};
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		leftIntakeMotor.setVoltage(voltage);
		rightIntakeMotor.setVoltage(voltage);
	}

	@Override
	public void stop() {
		leftIntakeMotor.setVoltage(0);
		rightIntakeMotor.setVoltage(0);
	}
}

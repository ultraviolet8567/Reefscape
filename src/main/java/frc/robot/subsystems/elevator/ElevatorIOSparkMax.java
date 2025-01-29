package frc.robot.subsystems.elevator;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;

public class ElevatorIOSparkMax implements ElevatorIO {
	private final SparkMax leadMotor, followerMotor;
	private final SparkMaxConfig leadConfig, followerConfig;
	private final PIDController leadPidController/* , followerPidController */;
	// private final RelativeEncoder encoder;

	// Constructor
	public ElevatorIOSparkMax() {
		leadPidController = new PIDController(0.027, 0, 0);
		// followerPidController = new PIDController(0.027, 0, 0);

		// Initialize the CANSparkMax motors for main and follower
		leadMotor = new SparkMax(3, MotorType.kBrushless);
		followerMotor = new SparkMax(4, MotorType.kBrushless);
		leadConfig = new SparkMaxConfig();
		followerConfig = new SparkMaxConfig();

		followerConfig.inverted(true);
		followerConfig.follow(leadMotor);

		leadMotor.configure(leadConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
		followerMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		leadMotor.setIdleMode(IdleMode.kBrake);
    	followerMotor.setIdleMode(IdleMode.kBrake);
	}

	@Override
	public default void updateInputs(ElevatorIOInputs inputs) {
		inputs.velocityRadPerSec = leadMotor.getEncoder().getVelocity();
		inputs.appliedVoltage = leadMotor.getAppliedOutput() * leadMotor.getBusVoltage();
		inputs.positionRads = getPosition();
		inputs.currentAmps = new double[]{arm1Motor.getOutputCurrent()};
		inputs.tempCelsius = new double[]{arm1Motor.getMotorTemperature()};
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		leadMotor.setVoltage(voltage);
	}

	@Override
	public double getPosition() {
		// Get the position from the encoder
		return leadMotor.getEncoder().getPosition();
	}

	@Override
	public double getVelocity() {
		// Get the velocity from the encoder
		return leadMotor.getEncoder().getVelocity();
	}

	@Override
	public void resetPosition() {
		// Reset the encoder to the specified position
		leadMotor.getEncoder().setPosition(0);
	}

	@Override
	public void setPosition(double position) {
		// TODO: check that this works & should probably clamp by less than the max speed, possibly make a ProfiledPIDController
		set(MathUtil.clamp(leadPidController.calculate(getPosition(), position), -12.0, 12.0));
	}

	@Override
	public void stop() {
		leadMotor.setVoltage(0);
	}
}

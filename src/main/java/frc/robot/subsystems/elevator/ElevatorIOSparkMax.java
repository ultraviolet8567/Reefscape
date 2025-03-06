package frc.robot.subsystems.elevator;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import frc.robot.Constants.CAN;
import frc.robot.Constants.ElevatorConstants;

public class ElevatorIOSparkMax implements ElevatorIO {
	private final SparkMax leadMotor, followerMotor;
	private final SparkMaxConfig leadConfig, followerConfig;
	private final PIDController leadPidController/* , followerPidController */;
	private final RelativeEncoder leadEncoder;
	private final DutyCycleEncoder absoluteEncoder;

	// Constructor
	public ElevatorIOSparkMax() {
		System.out.println("[Init] Creating ElevatorIOSparkMax");

		//TODO: configure the position/velocity conversion factors

		// Defaults from Penn State
		leadPidController = new PIDController(ElevatorConstants.kP, ElevatorConstants.kI, ElevatorConstants.kD);

		// Initialize the CANSparkMax motors for main and follower
		leadMotor = new SparkMax(CAN.kElevatorLeadMotorPort, MotorType.kBrushless);
		followerMotor = new SparkMax(CAN.kElevatorFollowerMotorPort, MotorType.kBrushless);
		leadConfig = new SparkMaxConfig();
		followerConfig = new SparkMaxConfig();

		leadConfig.idleMode(IdleMode.kBrake);
		followerConfig.idleMode(IdleMode.kBrake);

		followerConfig.inverted(true);
		followerConfig.follow(leadMotor);

		leadMotor.configure(leadConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
		followerMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		absoluteEncoder = new DutyCycleEncoder(CAN.kElevatorAbsoluteEncoderPort);
		leadEncoder = leadMotor.getEncoder();
	}

	@Override
	public void updateInputs(ElevatorIOInputs inputs) {
		// this is in rpm, convert
		inputs.currentVoltage = new double[]{leadMotor.getOutputCurrent(), followerMotor.getOutputCurrent()};
		inputs.appliedVoltage = new double[]{leadMotor.getAppliedOutput() * leadMotor.getBusVoltage(),
				followerMotor.getAppliedOutput() * followerMotor.getBusVoltage()};
		inputs.angleRadians = new double[]{getPositionRads()};
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		leadMotor.setVoltage(voltage);
	}

	// this should be from the absolute encoder
	@Override
	public double getPositionRads() {
		// Get the position from the encoder
		return absoluteEncoder.get() * 2 * Math.PI;
	}

	@Override
	public double getVelocity() {
		// Get the velocity from the encoder
		return leadEncoder.getVelocity();
	}

	// What is this
	@Override
	public void resetPosition() {
		// Reset the encoder to the specified position
		setPosition(0);
	}

	@Override
	public void setPosition(double position) {
		// Check if this method returns voltage as a parameter of set()
		set(MathUtil.clamp(leadPidController.calculate(getPositionRads(), position),
				-ElevatorConstants.kElevatorVoltage, ElevatorConstants.kElevatorVoltage));
	}

	public void turn(double factor) {
		double voltage = factor * 0;

		if (getPositionRads() < ElevatorConstants.kElevatorMax && getPositionRads() > ElevatorConstants.kElevatorMin
				|| (getPositionRads() >= ElevatorConstants.kElevatorMax && voltage <= 0)
				|| (getPositionRads() <= ElevatorConstants.kElevatorMin && voltage >= 0)) {
			set(voltage);
		} else {
			stop();
		}
	}

	@Override
	public void stop() {
		leadMotor.setVoltage(0);
	}
}

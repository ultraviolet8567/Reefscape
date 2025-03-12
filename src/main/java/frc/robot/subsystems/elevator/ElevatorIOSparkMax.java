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
import org.littletonrobotics.junction.Logger;

public class ElevatorIOSparkMax implements ElevatorIO {
	private final SparkMax leadMotor, followerMotor;
	private final SparkMaxConfig leadConfig, followerConfig;
	private final PIDController leadPidController;
	private final RelativeEncoder leadEncoder;
	private final DutyCycleEncoder absoluteEncoder;

	// Constructor
	public ElevatorIOSparkMax() {
		System.out.println("[Init] Creating ElevatorIOSparkMax");

		// TODO: configure the position/velocity conversion factors

		// Defaults from Penn State
		leadPidController = new PIDController(ElevatorConstants.kP, ElevatorConstants.kI, ElevatorConstants.kD);

		// Initialize the CANSparkMax motors for main and follower
		leadMotor = new SparkMax(CAN.kElevatorLeadMotorPort, MotorType.kBrushless);
		followerMotor = new SparkMax(CAN.kElevatorFollowerMotorPort, MotorType.kBrushless);
		leadConfig = new SparkMaxConfig();
		followerConfig = new SparkMaxConfig();

		leadConfig.encoder.positionConversionFactor(ElevatorConstants.kElevatorGearing);
		followerConfig.encoder.positionConversionFactor(ElevatorConstants.kElevatorGearing);

		leadConfig.idleMode(IdleMode.kBrake);
		followerConfig.idleMode(IdleMode.kBrake);

		followerConfig.inverted(true);
		followerConfig.follow(leadMotor);

		leadMotor.configure(leadConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
		followerMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		absoluteEncoder = new DutyCycleEncoder(ElevatorConstants.kElevatorAbsoluteEncoderPort);
		leadEncoder = leadMotor.getEncoder();

		resetEncoder();
	}

	@Override
	public void updateInputs(ElevatorIOInputs inputs) {
		// this is in rpm, convert
		inputs.currentAmps = new double[]{leadMotor.getOutputCurrent(), followerMotor.getOutputCurrent()};
		inputs.appliedVoltage = new double[]{leadMotor.getAppliedOutput() * leadMotor.getBusVoltage(),
				followerMotor.getAppliedOutput() * followerMotor.getBusVoltage()};
		inputs.angleRadians = getRotationRads();
		inputs.heightMeters = getHeight();
		inputs.velocity = getVelocity();
		inputs.absoluteEncoderValue = getAbsoluteRotationRads();
	}

	// Gets the current velocity of the elevator
	@Override
	public double getVelocity() {
		return leadEncoder.getVelocity();
	}

	// Gets the absoltue rotation of the elevator shaft
	public double getAbsoluteRotationRads() {
		double angle = absoluteEncoder.get();
		angle *= 2 * Math.PI;
		angle += ElevatorConstants.kAbsoluteEncoderOffset;
		angle = MathUtil.inputModulus(angle, -Math.PI, Math.PI);

		return angle * (ElevatorConstants.kAbsoluteEncoderReversed ? -1 : 1);
	}

	// Gets the current rotation of the elevator shaft\
	@Override
	public double getRotationRads() {
		return leadEncoder.getPosition();
	}

	// Gets the current height of the elevator
	@Override
	public double getHeight() {
		return getRotationRads() * ElevatorConstants.kElevatorMetersPerRad;
	}

	@Override
	public void setVoltage(double voltage) {
		// Set the power to the main motor
		leadMotor.setVoltage(voltage);
	}

	// Moves the elevator to the given height
	@Override
	public void setHeight(double height) {
		// PID computed voltage to move to the given height
		double voltage = MathUtil.clamp(leadPidController.calculate(getHeight(), height),
				-ElevatorConstants.kElevatorVoltage, ElevatorConstants.kElevatorVoltage);
		Logger.recordOutput("Elevator/applied voltage", voltage);

		setVoltage(voltage);
	}

	// Resets the encoder rotation to a specific value
	@Override
	public void resetEncoder() {
		leadEncoder.setPosition(getAbsoluteRotationRads());
	}

	@Override
	public boolean withinRange() {
		return !tooLow() && !tooHigh();
	}

	@Override
	public boolean tooLow() {
		return getHeight() < ElevatorConstants.kElevatorMinHeight;
	}

	@Override
	public boolean tooHigh() {
		return getHeight() > ElevatorConstants.kElevatorMaxHeight;
	}

	@Override
	public void stop() {
		leadMotor.setVoltage(0);
	}
}

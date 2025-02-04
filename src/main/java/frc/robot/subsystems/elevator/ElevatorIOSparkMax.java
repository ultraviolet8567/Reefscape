package frc.robot.subsystems.elevator;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.ElevatorConstants;

public class ElevatorIOSparkMax implements ElevatorIO {
	private final SparkMax leadMotor, followerMotor;
	private final SparkMaxConfig leadConfig, followerConfig;
	private final PIDController leadPidController/* , followerPidController */;
	// private final RelativeEncoder encoder;
	private final AbsoluteEncoder leadEncoder;
	private final AbsoluteEncoder followerEncoder;

	// Constructor
	public ElevatorIOSparkMax() {
		// Defaults from Penn State
		leadPidController = new PIDController(ElevatorConstants.kP, ElevatorConstants.kI, ElevatorConstants.kD);

		// Initialize the CANSparkMax motors for main and follower
		leadMotor = new SparkMax(3, MotorType.kBrushless);
		followerMotor = new SparkMax(4, MotorType.kBrushless);
		leadConfig = new SparkMaxConfig();
		followerConfig = new SparkMaxConfig();

		leadConfig.idleMode(IdleMode.kBrake);
		followerConfig.idleMode(IdleMode.kBrake);

		followerConfig.inverted(true);
		followerConfig.follow(leadMotor);

		leadMotor.configure(leadConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
		followerMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		leadEncoder = leadMotor.getAbsoluteEncoder();
		followerEncoder = followerMotor.getAbsoluteEncoder();
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		leadMotor.setVoltage(voltage);
	}

	// this should be from the absolute encoder
	@Override
	public double getPosition() {
		// Get the position from the encoder
		return leadEncoder.getPosition();
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
		set(MathUtil.clamp(leadPidController.calculate(getPosition(), position), -ElevatorConstants.kElevatorVoltage,
				ElevatorConstants.kElevatorVoltage));
	}

	@Override
	public void updateInputs(ElevatorIOInputs inputs) {
		// this is in rpm, convert
		inputs.currentVoltage = new double[]{leadMotor.getOutputCurrent(), followerMotor.getOutputCurrent()};
		inputs.appliedVoltage = new double[]{leadMotor.getAppliedOutput() * leadMotor.getBusVoltage(),
				followerMotor.getAppliedOutput() * followerMotor.getBusVoltage()};
		inputs.angleRadians = new double[]{leadEncoder.getPosition(), followerEncoder.getPosition()};
	}

	@Override
	public void stop() {
		leadMotor.setVoltage(0);
	}
}

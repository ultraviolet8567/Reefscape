package frc.robot.subsystems.climber;

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
import frc.robot.Constants.ClimberConstants;
import org.littletonrobotics.junction.Logger;

public class ClimberIOSparkMax implements ClimberIO {
	private final SparkMax motor;
	private final SparkMaxConfig config;
	private final RelativeEncoder encoder;
	private final DutyCycleEncoder absoluteEncoder;
	private final PIDController pidController;

	public ClimberIOSparkMax() {
		System.out.println("[Init] Creatd ClimberIOSparkMax");
		motor = new SparkMax(CAN.kClimberMotorPort, MotorType.kBrushless);
		config = new SparkMaxConfig();

		pidController = new PIDController(ClimberConstants.kP, ClimberConstants.kI, ClimberConstants.kD);

		config.idleMode(IdleMode.kBrake);
		config.smartCurrentLimit(80);

		motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		absoluteEncoder = new DutyCycleEncoder(ClimberConstants.kClimberAbsoluteEncoderPort);
		encoder = motor.getEncoder();

		resetEncoder();
	}

	@Override
	public void set(double voltage) {
		motor.setVoltage(voltage);
	}

	@Override
	public void resetEncoder() {
		encoder.setPosition(getAbsoluteRotationRads());
	}

	@Override
	public void updateInputs(ClimberIOInputs inputs) {
		inputs.currentAmps = motor.getOutputCurrent();
		inputs.appliedVoltage = motor.getMotorTemperature();
		inputs.velocityRadsPerSecond = motor.getEncoder().getVelocity();
		inputs.tempCelsius = motor.getMotorTemperature();
		inputs.absoluteEncoderValue = getAbsoluteRotationRads();
		inputs.angleRadians = getRotationRads();

	}
	@Override
	public void stop() {
		motor.setVoltage(0);
	}

	// Moves the elevator to the given height
	@Override
	public void setRads(double rads) {
		// PID computed voltage to move to the given height
		double voltage = MathUtil.clamp(pidController.calculate(getRotationRads(), rads),
				-ClimberConstants.kClimberVoltage, ClimberConstants.kClimberVoltage);

		Logger.recordOutput("Climber/PID Voltage", voltage);

		set(voltage);
	}

	@Override
	public double getRotationRads() {
		return encoder.getPosition();
	}

	public boolean withinRange() {
		return !tooLow() && !tooHigh();
	}

	public boolean tooLow() {
		return getAbsoluteRotationRads() < ClimberConstants.kClimberMinRad;
	}

	public boolean tooHigh() {
		return getAbsoluteRotationRads() > ClimberConstants.kClimberMaxRad;
	}

	public double getAbsoluteRotationRads() {
		double angle = absoluteEncoder.get();
		angle *= 2 * Math.PI;
		angle += ClimberConstants.kClimberAbsoluteEncoderOffset;
		angle = MathUtil.inputModulus(angle, 0, 2 * Math.PI);

		return angle * (ClimberConstants.kClimberAbsoluteEncoderReversed ? -1 : 1);
	}
}

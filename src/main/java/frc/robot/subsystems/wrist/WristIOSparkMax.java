package frc.robot.subsystems.wrist;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.WristConstants;

public class WristIOSparkMax implements WristIO {
	private final SparkMax motor;
	private final SparkMaxConfig config;
	private final PIDController pidController;

	// these values need to be changed in WristConstants
	private final ArmFeedforward feedforward;

	public WristIOSparkMax() {
		motor = new SparkMax(99, MotorType.kBrushless);
		config = new SparkMaxConfig();
		feedforward = new ArmFeedforward(WristConstants.kFFS, WristConstants.kFFG, WristConstants.kFFV);
		pidController = new PIDController(WristConstants.kP, WristConstants.kI, WristConstants.kD);

		config.idleMode(IdleMode.kBrake);
	}

	@Override
	public void updateInputs(WristIOInputs inputs) {
		inputs.currentVoltage = motor.getBusVoltage();
		inputs.appliedVoltage = motor.getAppliedOutput() * motor.getBusVoltage();
		inputs.angleRadians = Math.toRadians(motor.getEncoder().getPosition());
		// i am sorry but they got rid of .setVelocityConversionFactor()
		inputs.velocityRadsPerSecond = (motor.getEncoder().getVelocity() * 2 * Math.PI / 60);
	}

	@Override
	public void setWrist(double position) {
		// set(MathUtil.clamp(pidController.calculate(getAngle(), position),
		// -WristConstants.kWristVoltage, WristConstants.kWristVoltage)
		// + feedforward.calculate(getAngle(), (motor.getEncoder().getVelocity() * 2 *
		// Math.PI / 60)));
		// i have no idea how ff works, but if it works uncomment it?
		set(MathUtil.clamp(pidController.calculate(getAngle(), position), -WristConstants.kWristVoltage,
				WristConstants.kWristVoltage));
	}

	@Override
	public void set(double voltage) {
		motor.setVoltage(voltage);
	}
}

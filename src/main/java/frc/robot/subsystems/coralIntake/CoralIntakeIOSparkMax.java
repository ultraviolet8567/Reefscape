package frc.robot.subsystems.coralIntake;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import frc.robot.Constants.CAN;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class CoralIntakeIOSparkMax implements CoralIntakeIO {

	private final SparkFlex motor;
	private final SparkFlexConfig config;

	public CoralIntakeIOSparkMax() {
		 
		motor = new SparkFlex(CAN.kCoralIntakePort, MotorType.kBrushless);
		config = new SparkFlexConfig()                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              												;

		config.idleMode(IdleMode.kBrake);
		config.smartCurrentLimit(50);

		motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
	}

	@Override
	public void set(double voltage) {
		// Set the power to the motor
		motor.setVoltage(-voltage);
	}

	@Override
	public void stop() {
		motor.setVoltage(0);

	}

}
////////////////Grass//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// (the best comment ❤️)
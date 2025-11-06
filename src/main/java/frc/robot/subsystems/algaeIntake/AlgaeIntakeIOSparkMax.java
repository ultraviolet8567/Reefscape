package frc.robot.subsystems.algaeIntake;

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
import frc.robot.Constants.IntakeConstants;
import org.littletonrobotics.junction.Logger;

// 
public class AlgaeIntakeIOSparkMax implements AlgaeIntakeIO {

	// Attributes go here
	// visibility final? type name = value
	// final is a constant but also not really
	private final SparkMax rightMotor, leftMotor; 
	private final SparkMaxConfig rightConfig, leftConfig;


	// Constructor
	public AlgaeIntakeIOSparkMax() {

        rightMotor = new SparkMax();
		leftMotor = new SparkMax(CAN.kAlgaeLeftMotorPort, MotorType.kBrushless)
		
	}

}


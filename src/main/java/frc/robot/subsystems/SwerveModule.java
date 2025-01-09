package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkRelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.AnalogInput;


public class SwerveModule {
    private final SparkMax driveMotor;
    private final SparkMax turningMotor;
    private final SparkRelativeEncoder driveEncoder;
    private final SparkRelativeEncoder turningEncoder;
    private final PIDController turningPidController;
    public SwerveModulePosition modulePosition;
    private final AnalogInput absoluteEncoder;
    private double absoluteEncoderOffset;
    private final boolean absoluteEncoderReversed;


    public SwerveModule(int driveMotorID, int turningMotorID, boolean driveMotorReversed, boolean turningMotorReversed,
    int absoluteEncoderID, double absoluteEncoderOffset, boolean absoluteEncoderReversed) {
        this.absoluteEncoderOffset = absoluteEncoderOffset;
        this.absoluteEncoderReversed = absoluteEncoderReversed;
        absoluteEncoder = new AnalogInput(absoluteEncoderID);
        driveMotor = new SparkMax(driveMotorID, MotorType.kBrushless);
        driveMotor.enableVoltageCompensation(12.0);
		driveMotor.setSmartCurrentLimit(40);
		driveMotor.setIdleMode(IdleMode.kBrake);
		driveMotor.setInverted(driveMotorReversed);


		driveEncoder = driveMotor.getEncoder();
		turningEncoder = turningMotor.getEncoder();

		driveEncoder.setPositionConversionFactor(ModuleConstants.kDriveEncoderRot2Meter);
		driveEncoder.setVelocityConversionFactor(ModuleConstants.kDriveEncoderRPM2MeterPerSec);
		turningEncoder.setPositionConversionFactor(ModuleConstants.kTurningEncoderRot2Rad);
		turningEncoder.setVelocityConversionFactor(ModuleConstants.kTurningEncoderRPM2RadPerSec);

        turningMotor = new SparkMax(turningMotorID, MotorType.kBrushless);
         turningMotor.enableVoltageCompensation(12.0);
		turningMotor.setSmartCurrentLimit(40);
		turningMotor.setIdleMode(IdleMode.kBrake);
		turningMotor.setInverted(turningMotorReversed);
        
        turningPidController = new PIDController(ModuleConstants.kPTurning, 0, 0);
        turningPidController.enableContinuousInput(-Math.PI, Math.PI);
        
        resetEncoders();
    }

    public double getDrivePosition() {
        return driveEncoder.getPosition();

        } 
    public double getTurningPosition() {
        return turningEncoder.getPosition();
        
        }   
    public double getDriveVelocity() {
        return driveEncoder.getVelocity();
        }

    public double getTurningVelocity() {
        return turningEncoder.getVelocity();
        }

        public double getAbsoluteEncoderAngle() {
		double angle = absoluteEncoder.getAverageVoltage() / RobotController.getVoltage5V();
		angle *= 2 * Math.PI;
		angle += absoluteEncoderOffset;
		angle = MathUtil.inputModulus(angle, -Math.PI, Math.PI);

		return angle * (absoluteEncoderReversed ? -1 : 1);
        }
}

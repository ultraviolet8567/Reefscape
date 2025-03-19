package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.IntakeConstants;
import org.littletonrobotics.junction.Logger;

public class Elevator extends SubsystemBase {
	private final ElevatorIO io;
	private final ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();

	private ElevatorMode mode;

	// Constructor
	public Elevator(ElevatorIO io) {
		this.io = io;

		mode = ElevatorMode.MANUAL;
	}

	// Periodic method called in every cycle (e.g., 20ms)
	@Override
	public void periodic() {
		io.updateInputs(inputs);
		Logger.processInputs("Elevator", inputs);

		Logger.recordOutput("Elevator Mode", mode.toString());
	}

	// Method to set power for the elevator
	public void setVoltage(double voltage) {
		io.setVoltage(voltage);
	}

	public void setHeight(double height) {
		io.setHeight(height);
	}

	// Manually move elevator via joystick input
	public void setMoveSpeed(double factor) {
		double voltage = factor * ElevatorConstants.kManualVoltage;

		// Scale down voltage when moving elevator down
		if (factor < 0) {
			voltage = voltage / 4;
		}

		if (io.withinRange() || io.tooLow() && voltage >= 0 || io.tooHigh() && voltage <= 0) {
			Logger.recordOutput("Elevator/Voltage Setpoint", voltage);
			io.setVoltage(voltage);
		} else {
			stop();
		}
	}

	// Get desired height based on mode
	public double getPresetHeight() {
		switch (mode) {
			case STATION :
				return ElevatorConstants.kHeightStation;
			case PROCESSOR :
				return ElevatorConstants.kHeightProcessor;
			case L1 :
				return ElevatorConstants.kHeightL1;
			case L2 :
				return ElevatorConstants.kHeightL2;
			case L3 :
				return ElevatorConstants.kHeightL3;
			case L4 :
				return ElevatorConstants.kHeightL4;
			case ALGAELOWER :
				return ElevatorConstants.kHeightGrabAlgaeLower;
			case ALGAEHIGHER :
				return ElevatorConstants.kHeightGrabAlgaeHigher;
			case ALGAECANDLESTICK :
				return ElevatorConstants.kHeightAlgaeCandlestick;
			case HIGH :
				return ElevatorConstants.kHeightHigh;
			default :
				return ElevatorConstants.kHeightDefault;
		}
	}

	public double getCoralVoltage() {
		switch (mode) {
			case L1 :
				return IntakeConstants.kCoralVoltageL1;
			case L2 :
				return IntakeConstants.kCoralVoltageL2;
			case L3 :
				return IntakeConstants.kCoralVoltageL3;
			case L4 :
				return IntakeConstants.kCoralVoltageL4;
			default :
				return IntakeConstants.kCoralVoltageDefault;
		}
	}

	public ElevatorMode getMode() {
		return mode;
	}

	public void setMode(ElevatorMode mode) {
		this.mode = mode;
	}

	// Method to stop the elevator
	public void stop() {
		io.stop();
	}

	public static enum ElevatorMode {
		/** Taxi height */
		DEFAULT,
		/** Intaking from station */
		STATION,
		/** Depositing in processor */
		PROCESSOR,
		/** At L1 height */
		L1,
		/** At L1 height */
		L2,
		/** At L1 height */
		L3,
		/** At L1 height */
		L4,
		/** Manual movement */
		MANUAL,
		/** At algae height between L2 & L3 */
		ALGAELOWER,
		/** At algae height between L3 & L4 */
		ALGAEHIGHER,
		/** At algae heght for algae above coral on ground */
		ALGAECANDLESTICK,
		/** At highest permissible preset height */
		HIGH;
	}

	public void resetEncoder() {
		io.resetEncoder();
	}
}

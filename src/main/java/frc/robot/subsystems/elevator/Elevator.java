package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ElevatorConstants;

public class Elevator extends SubsystemBase {

	private final ElevatorIO io;
	// private final ElevatorIOInputsAutoLogged inputs = new
	// ElevatorIOInputsAutoLogged();

	// Constructor
	public Elevator(ElevatorIO io) {
		this.io = io;
	}

	// Method to set power for the elevator
	public void set(double voltage) {
		System.out.println("Elevator position: " + getPositionRads());
		io.set(voltage);
	}

	// Method to stop the elevator
	public void stop() {
		io.stop();
	}

	// Set the elevator to a specific position
	public void setPosition(double position) {
		// System.out.println("Elevator position: " + getPosition());
		io.setPosition(position);
	}

	// Periodic method called in every cycle (e.g., 20ms)
	@Override
	public void periodic() {
		// io.updateInputs(inputs);
	}

	public double getPositionRads() {
		return io.getPositionRads();
	}

	public double getVelocity() {
		return io.getVelocity();
	}

	public void resetPosition() {
		io.resetPosition();
	}

	// for manual control
	public void turn(double factor) {
		double voltage = factor * 4;
		System.out.println("input: " + factor);

		// easier to motor to go down with gravity, so reduce voltage going down
		if (factor < 0) {
			voltage = voltage / 4;
		}

		if (io.getPositionRads() < ElevatorConstants.kElevatorMax
				|| io.getPositionRads() > ElevatorConstants.kElevatorMin
				|| (io.getPositionRads() >= ElevatorConstants.kElevatorMax && voltage <= 0)
				|| (io.getPositionRads() <= ElevatorConstants.kElevatorMin && voltage >= 0)) {
			io.set(voltage);
			System.out.println(io.getVelocity());
		} else {
			stop();
		}
	}
}

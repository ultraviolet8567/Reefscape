package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import org.littletonrobotics.junction.Logger;

public class Elevator extends SubsystemBase {

	private final ElevatorIO io;
	// private final ElevatorIOInputsAutoLogged inputs = new
	// ElevatorIOInputsAutoLogged();

	// Constructor
	public Elevator(ElevatorIO io) {
		this.io = io;
	}

	// Method to set power for the elevator
	public void setVoltage(double voltage) {
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
		// if absolute encoder position isn't past a certain value?
		io.stop();
	}
}

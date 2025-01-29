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

	// Periodic method called in every cycle (e.g., 20ms)
	@Override
	public void periodic() {
		io.updateInputs(inputs);
	}

	// Set the elevator to a specific position
	public void setPosition(double position) {
		io.setPosition(position);
	}

	// Method to stop the elevator
	public void stop() {
		io.stop();
	}
}

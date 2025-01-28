package frc.robot.subsystems.algaeIntake;

public class AlgaeIntakeIOSim implements AlgaeIntakeIO {
	public AlgaeIntakeIOSim() {
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
	}

	@Override
	public double getPosition() {
		// Get the position from the encoder
		return 0.0;
	}

	@Override
	public double getVelocity() {
		// Get the velocity from the encoder
		return 0.0;
	}

	@Override
	public void resetPosition() {
		// Reset the encoder to the specified position
	}

	@Override
	public void setPosition(double position) {
		// Check if this method returns voltage as a parameter of set()
	}

	@Override
	public void stop() {
	}
}

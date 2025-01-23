package frc.robot.subsystems.coralIntake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CoralIntake extends SubsystemBase {
    private final CoralIntakeIO io;

    public CoralIntake(CoralIntakeIO io) {
        this.io = io;
    }

    public void setVoltage(double voltage) {
		io.set(voltage);
	}

	public void stop() {
		io.stop();
	}

	// Periodic method called in every cycle (e.g., 20ms)
	@Override
	public void periodic() {
		// io.updateInputs(inputs);
	}

	public double getPosition() {
		return io.getPosition();
	}

	public double getVelocity() {
		return io.getVelocity();
	}

	public void resetPosition() {
		io.resetPosition();
	}
}
package frc.robot.subsystems.algaeIntake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AlgaeIntake extends SubsystemBase {
	private final AlgaeIntakeIO io;

	public AlgaeIntake(AlgaeIntakeIO io) {
		this.io = io;
	}

	public void setVoltage(double voltage) {
		io.set(voltage);
	}

	public void stop() {
		io.stop();
	}

	/*
	 * public void pickup() { io.set(IntakeConstants.kAlgaeIntakeVoltage); }
	 *
	 * public void drop() { io.set(-IntakeConstants.kAlgaeIntakeVoltage); }
	 */

	// Periodic method called in every cycle (e.g., 20ms)
	@Override
	public void periodic() {
		// io.updateInputs(inputs);
	}
}

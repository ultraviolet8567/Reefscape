package frc.robot.subsystems.coralIntake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CoralIntake extends SubsystemBase {
	private final CoralIntakeIO io;

	// still need to code possible sensor

	public CoralIntake(CoralIntakeIO io) {
		this.io = io;
	}

	// Periodic method called in every cycle (e.g., 20ms)
	@Override
	public void periodic() {
		io.updateInputs(inputs);
	}

	public void pickup() {
		io.setInputVoltage(IntakeConstants.kIntakeVoltage.get());
	}

	public void drop() {
		io.setInputVoltage(-IntakeConstants.kIntakeVoltage.get());
	}

	public void stop() {
		io.stop();
	}
}

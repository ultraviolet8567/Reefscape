package frc.robot.subsystems.algaeIntake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class AlgaeIntake extends SubsystemBase {
	private final AlgaeIntakeIO io;

	public AlgaeIntake(AlgaeIntakeIO io) {
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

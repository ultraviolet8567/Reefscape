package frc.robot.subsystems.coralIntake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class CoralIntake extends SubsystemBase {
	private final CoralIntakeIO io;
	private final CoralIntakeIOInputsAutoLogged inputs = new CoralIntakeIOInputsAutoLogged();

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
		io.set(IntakeConstants.kCoralIntakeVoltage);
	}

	public void drop() {
		io.set(-IntakeConstants.kCoralIntakeVoltage);
	}

	public void stop() {
		io.stop();
	}
}

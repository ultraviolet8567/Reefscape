package frc.robot.subsystems.algaeIntake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import org.littletonrobotics.junction.Logger;

public class AlgaeIntake extends SubsystemBase {
	private final AlgaeIntakeIO io;
	private final AlgaeIntakeIOInputsAutoLogged inputs = new AlgaeIntakeIOInputsAutoLogged();

	public AlgaeIntake(AlgaeIntakeIO io) {
		this.io = io;
	}

	// Periodic method called in every cycle (e.g., 20ms)
	@Override
	public void periodic() {
		io.updateInputs(inputs);
	}

	public void setVoltage(double voltage) {
		io.set(voltage);
	}

	public void stop() {
		io.stop();
	}

	public void pickup() {
		io.set(IntakeConstants.kAlgaeIntakeVoltage);
	}

	public void drop() {
		io.set(-IntakeConstants.kAlgaeIntakeVoltage);
	}

	public void extend() {
		io.setExtension(IntakeConstants.kAlgaeIntakeExtendedPosition);
	}

	public void retract() {
		io.setExtension(IntakeConstants.kAlgaeIntakeRetractedPosition);
	}
}

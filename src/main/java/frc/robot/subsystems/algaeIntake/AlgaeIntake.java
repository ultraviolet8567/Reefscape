package frc.robot.subsystems.algaeIntake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import org.littletonrobotics.junction.Logger;

public class AlgaeIntake extends SubsystemBase {
	private final AlgaeIntakeIO io;
	private final AlgaeIntakeIOInputsAutoLogged inputs = new AlgaeIntakeIOInputsAutoLogged();
	private boolean algaeExtended;

	public AlgaeIntake(AlgaeIntakeIO io) {
		this.io = io;

		algaeExtended = true;
	}

	// Periodic method called in every cycle (e.g., 20ms)
	@Override
	public void periodic() {
		io.updateInputs(inputs);
		Logger.processInputs("AlgaeIntake", inputs);
	}

	public void setVoltage(double voltage) {
		io.set(voltage);
	}

	public void stop() {
		io.stop();
	}

	public void pickup() {
		io.set(-IntakeConstants.kAlgaeIntakeVoltage);
	}

	public void drop() {
		io.set(IntakeConstants.kAlgaeIntakeVoltage);
	}

	public void toggleAlgaeRetraction() {
		if (algaeExtended) {
			retract();
		} else {
			extend();
		}
		algaeExtended = !algaeExtended;
	}

	public void extend() {
		io.stopExtension();
	}

	public void retract() {
		io.setExtension(IntakeConstants.kAlgaeIntakeRetractedPosition);
	}
}

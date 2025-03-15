package frc.robot.subsystems.coralIntake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

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
		Logger.processInputs("CoralIntake", inputs);
	}

	public void pickup(double voltage) {
		io.set(voltage);
	}

	public void drop(double voltage) {
		io.set(-voltage);
		Logger.recordOutput("Coral Intake/Voltage Setpoint", voltage);
	}

	public void stop() {
		io.stop();
	}
}

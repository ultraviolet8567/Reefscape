package frc.robot.subsystems.wrist;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Wrist extends SubsystemBase {
	private final WristIO io;
	private final WristIOInputsAutoLogged inputs = new WristIOInputsAutoLogged();
	// these values will need to be changed

	public Wrist(WristIO io) {
		this.io = io;
	}

	@Override
	public void periodic() {
		io.updateInputs(inputs);
	}

	public void setWrist(double position) {
		io.setWrist(position);
		inputs.angleRadians = Math.toRadians(position);
	}

	public void set(double voltage) {
		io.set(voltage);
	}

	public double getAngle() {
		return inputs.angleRadians;
	}

	public void resetWrist() {
		io.setWrist(0.0);
	}

	// needed:
	// set wrist position (angle)
	// set wrist voltage
	// reset angle
}

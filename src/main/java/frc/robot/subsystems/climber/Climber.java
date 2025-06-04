package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Climber extends SubsystemBase {
	private final ClimberIO io;

	public Climber(ClimberIO io) {
		this.io = io;
	}

	@Override
	public void periodic() {
		// io.setExtsension(algaeExtended);
		Logger.recordOutput("Climber/Rotation", io.getRotationRads());

		io.resetEncoder();
	}

	public void setVoltage(double voltage) {
		io.set(voltage);
	}

	public void setRads(double angle) {
		io.setRads(angle);
	}

	public void stop() {
		io.stop();
	}
}

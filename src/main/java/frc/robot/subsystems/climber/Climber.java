package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;
import org.littletonrobotics.junction.Logger;

public class Climber extends SubsystemBase {
	private final ClimberIO io;
	private double angle;

	public Climber(ClimberIO io) {
		this.io = io;
		this.angle = ClimberConstants.kClimberMinRad;
	}

	@Override
	public void periodic() {
		// io.setExtsension(algaeExtended);
		Logger.recordOutput("Climber/Angle", this.angle);
		Logger.recordOutput("Climber/AbsoluteRotation", io.getRotationRads());

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

	public void setAngle(double angle) {
		this.angle = angle;
		if (this.angle > ClimberConstants.kClimberMaxRad) {
			this.angle = ClimberConstants.kClimberMaxRad;
		} else if (this.angle < ClimberConstants.kClimberMinRad) {
			this.angle = ClimberConstants.kClimberMinRad;
		}
	}

	public double getAngle() {
		return angle;
	}
}

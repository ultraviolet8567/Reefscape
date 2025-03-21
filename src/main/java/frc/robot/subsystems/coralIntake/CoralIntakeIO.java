package frc.robot.subsystems.coralIntake;

import org.littletonrobotics.junction.AutoLog;

public interface CoralIntakeIO {
	@AutoLog
	class CoralIntakeIOInputs {
		public double currentAmps = 0.0;
		public double appliedVoltage = 0.0;
		public double velocityRadsPerSecond = 0.0;
		public double tempCelsius = 0.0;
	}

	public default void updateInputs(CoralIntakeIOInputs inputs) {
	}

	public default void set(double voltage) {
	}

	public default void stop() {
	}
}

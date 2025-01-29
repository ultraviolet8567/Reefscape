package frc.robot.subsystems.coralIntake;

public interface CoralIntakeIO {
	public static class CoralIntakeIOInputs {
		public double currentAmps = 0.0;
		public double velocityRadPerSec = 0.0;
		public double appliedVoltage = 0.0;
		public double tempCelsius = 0.0;
	}

	public default void updateInputs(CoralIntakeIOInputs inputs) {
	}

	public default void set(double voltage) {
	}

	public default void stop() {
	}
}

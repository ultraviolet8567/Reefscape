package frc.robot.subsystems.coralIntake;

public interface CoralIntakeIO {
	public static class CoralIntakeIOInputs {
		public double currentVoltage = 0.0;
		public double appliedVoltage = 0.0;
		public double velocityRadsPerSecond = 0.0;
		public double tempCelsius = 0.0;
	}

	public default void updateInputs(CoralIntakeIOInputs inputs) {
	}

	public default void set(double voltage) {
	}

	public default double getPosition() {
		return 0;
	}

	public default void setPosition(double position) {
	}

	public default double getVelocity() {
		return 0;
	}

	public default void resetPosition() {
	}

	public default void stop() {
	}
}

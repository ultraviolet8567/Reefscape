package frc.robot.subsystems.algaeIntake;

public interface AlgaeIntakeIO {
	public static class AlgaeIntakeIOInputs {
		public double[] currentVoltage = {0.0, 0.0};
		public double[] appliedVoltage = {0.0, 0.0};
		public double[] velocityRadsPerSecond = {0.0, 0.0};
		public double[] tempCelsius = {0.0, 0.0};
	}

	public default void updateInputs(AlgaeIntakeIOInputs inputs) {
	}

	public default void set(double voltage) {
	}

	public default void stop() {
	}
}

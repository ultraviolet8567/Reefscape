package frc.robot.subsystems.algaeIntake;

public interface AlgaeIntakeIO {
	public static class AlgaeIntakeIOInputs {
		public double motorCurrent = 0;
		public double motorVoltage = 0;
		public double motorAngle = 0;
	}

	public default void updateInputs(AlgaeIntakeIOInputs inputs) {
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

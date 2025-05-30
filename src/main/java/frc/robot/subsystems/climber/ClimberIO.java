package frc.robot.subsystems.climber;

public interface ClimberIO {
	class ClimberIOInputs {
		public double currentAmps = 0.0;
		public double appliedVoltage = 0.0;
		public double angleRadians = 0.0;
		public double velocityRadsPerSecond = 0.0;
		public double tempCelsius = 0.0;
		public double absoluteEncoderValue = 0.0;

	}

	public default void updateInputs(ClimberIOInputs inputs) {
	}

	public default void set(double voltage) {
	}

	public default void setRads(double rads) {

	}

	public default void stop() {

	}

	public default void resetEncoder() {

	}

	public default double getRotationRads() {
		return 0.0;
	}
}

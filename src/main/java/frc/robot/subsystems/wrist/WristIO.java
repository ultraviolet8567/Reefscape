package frc.robot.subsystems.wrist;

public interface WristIO {
	public static class WristIOInputs {
		public double currentVoltage = 0.0;
		public double appliedVoltage = 0.0;
		public double angleRadians = 0.0;
		public double velocityRadsPerSecond = 0.0;
	}

	public default void updateInputs(WristIOInputs inputs) {
	}

	public default void setWrist(double position) {
	}

	public default void set(double voltage) {
	}

	public default double getAngle() {
		return 0;
	}
}

package frc.robot.subsystems.algaeIntake;

import org.littletonrobotics.junction.AutoLog;

public interface AlgaeIntakeIO {
	@AutoLog
	class AlgaeIntakeIOInputs {
		public double[] currentVoltage = {0.0, 0.0};
		public double[] appliedVoltage = {0.0, 0.0};
		public double[] velocityRadsPerSecond = {0.0, 0.0};
		public double[] tempCelsius = {0.0, 0.0};
		public double absoluteEncoderValue = 0.0;
	}

	public default void updateInputs(AlgaeIntakeIOInputs inputs) {
	}

	public default void set(double voltage) {
	}

	public default void setExtension(double setpoint) {
	}

	public default void stop() {
	}

	public default void stopExtension() {
	}
}

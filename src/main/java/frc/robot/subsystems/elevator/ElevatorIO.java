package frc.robot.subsystems.elevator;

import org.littletonrobotics.junction.AutoLog;

public interface ElevatorIO {
	@AutoLog
	class ElevatorIOInputs {
		public double[] currentAmps = {0.0, 0.0};	
		public double[] tempCelsius = {0.0, 0.0};
		public double[] appliedVoltage = {0.0, 0.0};
		public double angleRadians = 0.0;
		public double heightMeters = 0.0;
		public double velocity = 0.0;
		public double absoluteEncoderValue = 0.0;
	}

	public default void updateInputs(ElevatorIOInputs inputs) {
	}

	// Gets the current velocity of the elevator
	public default double getVelocity() {
		return 0;
	}

	// Gets the current rotation of the elevator shaft
	public default double getRotationRads() {
		return 0;
	}

	// Gets the current height of the elevator
	public default double getHeight() {
		return 0;
	}

	// Sets the power to the elevator motor
	public default void setVoltage(double voltage) {
	}

	// Moves the elevator to the given height
	public default void setHeight(double height) {
	}

	// Resets the encoder rotation to a specific value
	public default void resetEncoder() {
	}

	public default void stop() {
	}

	public default boolean withinRange() {
		return false;
	}

	public default boolean tooLow() {
		return true;
	}

	public default boolean tooHigh() {
		return true;
	}
}

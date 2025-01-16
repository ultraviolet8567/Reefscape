package frc.robot.subsystems.Intake;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
    @AutoLog
    class IntakeIOInputs {
        public double velocityRadPerSec = 0.0;
        public double appliedVoltage = 0.0;
        public double[] currentAmps = new double[]{};
        public double[] tempCelsius = new double[]{};
    }
    
    public default void updateInputs(IntakeIOInputs inputs) {
    }

    public default void setInputVoltage(double volts) {
    }

    public default void stop() {
    }

    public default void setBrakeMode(boolean brake) {
    }
}
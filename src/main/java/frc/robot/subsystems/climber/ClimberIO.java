package frc.robot.subsystems.climber;

import frc.robot.subsystems.algaeIntake.AlgaeIntakeIO.AlgaeIntakeIOInputs;

public interface ClimberIO {

    class ClimberIOInputs {
        
    }

    public default void updateInputs(AlgaeIntakeIOInputs inputs) {
	}

    public default void set(double voltage) {
	}

    public default void stop() {
        
	}

    
}

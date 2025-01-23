package frc.robot.subsystems.algaeIntake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AlgaeIntake extends SubsystemBase {
    private AlgaeIntakeIO io;

    public AlgaeIntake(AlgaeIntakeIO io) {
        this.io = io;
    }
}

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.coralIntake.CoralIntake;

public class PickupCoral extends Command {
	private final CoralIntake intake;
	private final double voltage;

	public PickupCoral(CoralIntake intake, double voltage) {
		this.intake = intake;
		this.voltage = voltage;

		addRequirements(intake);
	}

	@Override
	public void initialize() {
		intake.pickup(voltage);
	}

	@Override
	public void end(boolean interrupted) {
		intake.stop();
	}
}

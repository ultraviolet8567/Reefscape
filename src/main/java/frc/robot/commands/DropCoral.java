package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.coralIntake.CoralIntake;

public class DropCoral extends Command {
	private final CoralIntake intake;
	private final double voltage;

	public DropCoral(CoralIntake intake, double voltage) {
		this.intake = intake;
		this.voltage = voltage;

		addRequirements(intake);
	}

	@Override
	public void initialize() {
		intake.drop(voltage);
	}

	@Override
	public void end(boolean interrupted) {
		intake.stop();
	}
}

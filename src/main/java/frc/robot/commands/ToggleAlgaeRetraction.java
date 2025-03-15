package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeIntake.AlgaeIntake;

public class ToggleAlgaeRetraction extends Command {
	private final AlgaeIntake intake;

	public ToggleAlgaeRetraction(AlgaeIntake intake) {
		this.intake = intake;

		addRequirements(intake);
	}

	@Override
	public void execute() {
		intake.toggleAlgaeRetraction();
	}

	@Override
	public void end(boolean interrupted) {
		intake.setExtension();
		intake.stop();
	}
}

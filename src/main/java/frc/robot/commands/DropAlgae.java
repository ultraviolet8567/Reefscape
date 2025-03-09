package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeIntake.AlgaeIntake;

public class DropAlgae extends Command {
	private final AlgaeIntake intake;

	public DropAlgae(AlgaeIntake intake) {
		this.intake = intake;

		addRequirements(intake);
	}

	@Override
	public void initialize() {
		intake.drop();

		// Uncomment this after algae extension/retraction is tested
		// intake.extend();
	}

	@Override
	public void end(boolean interrupted) {
		intake.stop();

		// Uncomment this after algae extension/retraction is tested
		// intake.retract();
	}
}

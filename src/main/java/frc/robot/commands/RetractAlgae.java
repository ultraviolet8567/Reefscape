package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeIntake.AlgaeIntake;

public class RetractAlgae extends Command {
	private final AlgaeIntake intake;

	public RetractAlgae(AlgaeIntake intake) {
		this.intake = intake;

		addRequirements(intake);
	}

	@Override
	public void initialize() {
		intake.retract();
	}

	@Override
	public void end(boolean interrupted) {
		intake.stop();
	}
}

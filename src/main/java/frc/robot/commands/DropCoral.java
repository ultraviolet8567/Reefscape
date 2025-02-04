package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.coralIntake.CoralIntake;

public class DropCoral extends Command {
	private final CoralIntake intake;

	public DropCoral(CoralIntake intake) {
		this.intake = intake;

		addRequirements(intake);
	}

	@Override
	public void initialize() {
		intake.setVoltage(-IntakeConstants.kCoralIntakeVoltage);
	}

	@Override
	public void end(boolean interrupted) {
		intake.stop();
	}

}

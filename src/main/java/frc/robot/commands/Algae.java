package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.algaeIntake.AlgaeIntake;

public class Algae extends Command {
	private final AlgaeIntake intake;
	private final boolean drop;

	public Algae(AlgaeIntake intake, boolean drop) {
		this.intake = intake;
		this.drop = drop;

		addRequirements(intake);
	}

	@Override
	public void initialize() {
		if (drop) {
			intake.setVoltage(IntakeConstants.kAlgaeIntakeVoltage);
		} else {
			intake.setVoltage(-IntakeConstants.kAlgaeIntakeVoltage);
		}
	}

	@Override
	public void end(boolean interrupted) {
		intake.stop();
	}

}

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeIntake.AlgaeIntake;

public class AlgaeIntakeCommand extends Command {
	private final AlgaeIntake intake;
	private final int voltage;

	public AlgaeIntakeCommand(AlgaeIntake intake, int voltage) {
		this.intake = intake;
		this.voltage = voltage;

		addRequirements(intake);
	}

	@Override
	public void initialize() {
		intake.setVoltage(voltage);
	}

	@Override
	public void end(boolean interrupted) {
		intake.setVoltage(0);
	}

}

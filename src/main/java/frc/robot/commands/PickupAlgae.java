package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeIntake.AlgaeIntake;

public class PickupAlgae extends Command {
	private final AlgaeIntake intake;
	private final int voltage;

	public PickupAlgae(AlgaeIntake intake) {
		this.intake = intake;

		addRequirements(intake);
	}

	@Override
	public void initialize() {
		intake.pickup();
	}

	@Override
	public void end(boolean interrupted) {
		intake.stop();
	}

}

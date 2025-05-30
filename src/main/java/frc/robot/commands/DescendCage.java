package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ClimberConstants;
import frc.robot.subsystems.climber.Climber;

public class DescendCage extends Command {
	private final Climber climber;

	public DescendCage(Climber climber) {
		this.climber = climber;

		addRequirements(climber);
	}

	@Override
	public void execute() {
		climber.setRads(ClimberConstants.kClimberMinRad);
	}

	@Override
	public void end(boolean interrupted) {
		climber.stop();
	}
}

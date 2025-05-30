package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ClimberConstants;
import frc.robot.subsystems.climber.Climber;

public class ClimbCage extends Command {
	private final Climber climber;

	public ClimbCage(Climber climber) {
		this.climber = climber;

		addRequirements(climber);
	}

	@Override
	public void execute() {
		climber.setRads(ClimberConstants.kClimberMaxRad);
	}

	@Override
	public void end(boolean interrupted) {
		climber.stop();
	}
}

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ClimberConstants;
import frc.robot.subsystems.climber.Climber;
import java.util.function.Supplier;

public class MoveClimber extends Command {
	private final Climber climber;
	private final Supplier<Boolean> upPovSupplier, downPovSupplier;

	public MoveClimber(Climber climber, Supplier<Boolean> upPovSupplier, Supplier<Boolean> downPovSupplier) {
		this.climber = climber;
		this.upPovSupplier = upPovSupplier;
		this.downPovSupplier = downPovSupplier;

		addRequirements(climber);
	}

	@Override
	public void execute() {
		if (upPovSupplier.get()) {
			climber.setAngle(climber.getAngle() - ClimberConstants.kClimberSpeed);
		}
		if (downPovSupplier.get()) {
			climber.setAngle(climber.getAngle() + ClimberConstants.kClimberSpeed);
		}

		climber.setRads(climber.getAngle());
	}

	@Override
	public void end(boolean interrupted) {
		climber.stop();
	}
}

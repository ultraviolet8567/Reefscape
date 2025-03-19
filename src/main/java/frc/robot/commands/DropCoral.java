package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.coralIntake.CoralIntake;
import java.util.function.Supplier;

public class DropCoral extends Command {
	private final CoralIntake intake;
	private final Supplier<Double> voltageSupplier;

	public DropCoral(CoralIntake intake, Supplier<Double> voltageSupplier) {
		this.intake = intake;
		this.voltageSupplier = voltageSupplier;

		addRequirements(intake);
	}

	@Override
	public void initialize() {
		intake.drop(voltageSupplier.get());

		RobotContainer.getDriverJoystick().setRumble(RumbleType.kBothRumble, 0);
		RobotContainer.getOperatorJoystick().setRumble(RumbleType.kBothRumble, 0);
	}

	@Override
	public void end(boolean interrupted) {
		intake.stop();
	}
}

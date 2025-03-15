package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.coralIntake.CoralIntake;
import java.util.function.Supplier;

public class PickupCoral extends Command {
	private final CoralIntake intake;
	private final Supplier<Double> voltageSupplier;

	public PickupCoral(CoralIntake intake, Supplier<Double> voltageSupplier) {
		this.intake = intake;
		this.voltageSupplier = voltageSupplier;

		addRequirements(intake);
	}

	@Override
	public void initialize() {
		intake.pickup(voltageSupplier.get());
	}

	@Override
	public void end(boolean interrupted) {
		intake.stop();
	}
}

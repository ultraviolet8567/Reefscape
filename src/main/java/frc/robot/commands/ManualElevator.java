package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.Elevator;
import java.util.function.Supplier;

public class ManualElevator extends Command {
	private Elevator elevator;
	private Supplier<Double> leftJoystick;
	private final Supplier<Boolean> leftBumper;

	public ManualElevator(Elevator elevator, Supplier<Double> leftJoystick, Supplier<Boolean> leftBumper) {
		this.elevator = elevator;
		this.leftJoystick = leftJoystick;
		this.leftBumper = leftBumper;

		addRequirements(elevator);
	}

	@Override
	public void execute() {
		if (Math.abs(leftJoystick.get()) > 0.1) {
			elevator.turn(leftJoystick.get());
			System.out.println(elevator.getPositionRads());
		} else {
			elevator.turn(0);
		}
	}
}

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.Elevator;
import java.util.function.Supplier;

public class ManualElevator extends Command {
	private Elevator elevator;
	private Supplier<Double> leftJoystick;

	public ManualElevator(Elevator elevator, Supplier<Double> leftJoystick) {
		this.elevator = elevator;
		this.leftJoystick = leftJoystick;

		System.out.println("got here");

		addRequirements(elevator);
	}

	@Override
	public void execute() {
		if (Math.abs(leftJoystick.get()) > 0.1) {
			elevator.turn(leftJoystick.get());
		}
	}
}

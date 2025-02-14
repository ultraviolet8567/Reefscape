package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.Elevator;

public class ManualElevator extends Command {
	private Elevator elevator;
	private double leftY;

	public ManualElevator(Elevator elevator, double leftY) {
		this.elevator = elevator;
		this.leftY = leftY;

		addRequirements(elevator);
	}

	@Override
	public void execute() {
		if (Math.abs(leftY) > 0.1) {
			elevator.turn(leftY);
		}
	}
}

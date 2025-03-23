package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.Elevator.ElevatorMode;

public class AutoElevatorTop extends Command {
	private Elevator elevator;

	public AutoElevatorTop(Elevator elevator) {
		this.elevator = elevator;
		addRequirements(elevator);
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		elevator.setMoveSpeed(0.75);
		elevator.setMode(ElevatorMode.MANUAL);
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		elevator.stop();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return elevator.getHeight() > ElevatorConstants.kElevatorLimit;
	}
}

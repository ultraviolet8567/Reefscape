package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.Elevator.ElevatorMode;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;

public class MoveElevator extends Command {
	private Elevator elevator;
	private Supplier<Double> joystickSupplier;
	private Supplier<Boolean> leftBumper;

	public MoveElevator(Elevator elevator, Supplier<Double> joystickSupplier, Supplier<Boolean> leftBumper) {
		this.elevator = elevator;
		this.joystickSupplier = joystickSupplier;
		this.leftBumper = leftBumper;

		addRequirements(elevator);
	}

	@Override
	public void execute() {
		// The first thing that runs when command is called.
		Logger.recordOutput("isTeleop", DriverStation.isTeleop());

		if ((elevator.getMode() == ElevatorMode.MANUAL)
				|| (Math.abs(joystickSupplier.get()) > OIConstants.kDeadband && DriverStation.isTeleop())) {
			manual();
			Logger.recordOutput("Elevator/AutoMode", false);
		} else {
			Logger.recordOutput("Elevator/AutoMode", true);
			automatic();
		}
	}

	public void manual() {
		if (Math.abs(joystickSupplier.get()) > OIConstants.kDeadband) {
			elevator.setMoveSpeed(joystickSupplier.get());
		} else {
			elevator.setMoveSpeed(0);
		}

		elevator.setMode(ElevatorMode.MANUAL);
	}

	public void automatic() {
		elevator.setHeight(elevator.getPresetHeight());
	}
}

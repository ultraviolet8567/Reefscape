package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ElevatorConstants;
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
		Logger.recordOutput("Auto/Test/ElevatorModeB", elevator.getMode() == ElevatorMode.MANUAL);
		Logger.recordOutput("Auto/Test/JoystickB", Math.abs(joystickSupplier.get()) > OIConstants.kDeadband);
		Logger.recordOutput("Auto/Test/TelopB", DriverStation.isTeleopEnabled());

		if ((elevator.getMode() == ElevatorMode.MANUAL) || (Math.abs(joystickSupplier.get()) > OIConstants.kDeadband)) {
			manual();
		} else if (elevator.getMode() == ElevatorMode.AUTOTIP) {
			tip();
		} else {
			automatic();
		}
	}

	public void manual() {
		Logger.recordOutput("Elevator/ControlSystem", "Open Loop");

		if (Math.abs(joystickSupplier.get()) > OIConstants.kDeadband) {
			elevator.setMoveSpeed(joystickSupplier.get());
		} else {
			elevator.setMoveSpeed(0);
		}

		elevator.setMode(ElevatorMode.MANUAL);
	}

	public void tip() {
		Logger.recordOutput("Elevator/ControlSystem", "Fixed Voltage");
		if (elevator.getHeight() <= ElevatorConstants.kElevatorLimit) {
			elevator.setMoveSpeed(0.75);
		} else {
			elevator.stop();
			elevator.setMode(ElevatorMode.L2); // Should this be default?
		}
	}

	public void automatic() {
		Logger.recordOutput("Elevator/ControlSystem", "Closed Loop");
		elevator.setHeight(elevator.getPresetHeight());
	}
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.*;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.AlgaeIntakeCommand;
import frc.robot.commands.SwerveTeleOp;
import frc.robot.subsystems.Odometry;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.algaeIntake.*;
import frc.robot.subsystems.coralIntake.*;
import frc.robot.subsystems.elevator.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
	// The robot's subsystems and commands are defined here...
	private final Swerve swerve;
	private final Odometry odometry;
	private final Elevator elevator;
	// private final CoralIntake coralIntake;
	private final AlgaeIntake algaeIntake;

	// Replace with CommandPS4Controller or CommandJoystick if needed
	private static final CommandXboxController driverController = new CommandXboxController(
			OperatorConstants.kDriverControllerPort);
	private static final CommandXboxController operatorController = new CommandXboxController(
			OperatorConstants.kOperatorControllerPort);

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		switch (Constants.currentMode) {
			case REAL -> {
				elevator = new Elevator(new ElevatorIOSparkMax());
				// coralIntake = new CoralIntake(new CoralIntakeIOSparkMax());
				algaeIntake = new AlgaeIntake(new AlgaeIntakeIOSparkMax());
			}
			case SIM -> {
				elevator = new Elevator(new ElevatorIOSim());
				// coralIntake = new CoralIntake(new CoralIntakeIOSim());
				algaeIntake = new AlgaeIntake(new AlgaeIntakeIOSim());
			}
			default -> {
				elevator = new Elevator(new ElevatorIO() {
				});
				// coralIntake = new CoralIntake(new CoralIntakeIO() {});
				algaeIntake = new AlgaeIntake(new AlgaeIntakeIO() {
				});
			}
		}

		swerve = new Swerve();
		odometry = new Odometry(swerve);

		// Configure the PathPlanner auto-builder
		// AutoBuilder.configureHolonomic(odometry::getOdometerPose,
		// odometry::resetOdometerPose,
		// swerve::getRobotRelativeSpeeds, swerve::setModuleStates,
		// DriveConstants.kHolonomicConfig, () -> {

		// }, swerve)};

		swerve.setDefaultCommand(new SwerveTeleOp(swerve, odometry, () -> -driverController.getLeftY(),
				() -> -driverController.getLeftX(), () -> -driverController.getRightX(),
				() -> driverController.getHID().getRightBumper()));

		configureBindings();
	}

	/**
	 * Use this method to define your trigger->command mappings. Triggers can be
	 * created via the {@link Trigger#Trigger(java.util.function.BooleanSupplier)}
	 * constructor with an arbitrary predicate, or via the named factories in
	 * {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses
	 * for {@link CommandXboxController
	 * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
	 * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick
	 * Flight joysticks}.
	 */
	private void configureBindings() {
		driverController.b().onTrue(new InstantCommand(() -> elevator.setPosition(10)));
		driverController.a().onTrue(new InstantCommand(() -> elevator.setPosition(12)));
		// one of these is gonna be output
		driverController.rightBumper()
				.whileTrue(new AlgaeIntakeCommand(algaeIntake, IntakeConstants.kAlgaeIntakeVoltage));
		driverController.rightTrigger()
				.whileTrue(new AlgaeIntakeCommand(algaeIntake, -IntakeConstants.kAlgaeIntakeVoltage));
		// left side is gonna be used for coral stuff
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		// An example command will be run in autonomous
		return null;
	}

	public static XboxController getDriverJoystick() {
		return driverController.getHID();
	}

}

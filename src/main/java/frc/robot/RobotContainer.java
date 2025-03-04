// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.DropAlgae;
import frc.robot.commands.ManualElevator;
import frc.robot.commands.PickupAlgae;
import frc.robot.commands.SwerveTeleOp;
import frc.robot.subsystems.AutoChooser;
import frc.robot.subsystems.Odometry;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.algaeIntake.*;
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
	private final AlgaeIntake algaeIntake;
	// private final CoralIntake coralIntake;
	private final AutoChooser autoChooser;
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
				algaeIntake = new AlgaeIntake(new AlgaeIntakeIOSparkMax());
				// coralIntake = new CoralIntake(new CoralIntakeIOSparkMax());
			}
			case SIM -> {
				elevator = new Elevator(new ElevatorIOSim());
				algaeIntake = new AlgaeIntake(new AlgaeIntakeIOSim());
				// coralIntake = new CoralIntake(new CoralIntakeIOSim());
			}
			default -> {
				elevator = new Elevator(new ElevatorIO() {
				});
				algaeIntake = new AlgaeIntake(new AlgaeIntakeIO() {
				});
				// coralIntake = new CoralIntake(new CoralIntakeIO() {
				// });
			}
		}

		swerve = new Swerve();
		odometry = new Odometry(swerve);

		// Configure the PathPlanner auto-builder
		AutoBuilder.configure(odometry::getOdometerPose, odometry::resetOdometerPose, swerve::getRobotRelativeSpeeds,
				(speeds, feedforwards) -> swerve.setModuleStates(speeds),
				new PPHolonomicDriveController(new PIDConstants(0.25, 0, 0), // translational PID
						new PIDConstants(0.5, 0, 0)), // rotational PID
				DriveConstants.kRobotConfig, () -> {
					if (DriverStation.getAlliance().isPresent()) {
						return DriverStation.getAlliance().get() == Alliance.Red;
					}
					return false;
				}, swerve);

		autoChooser = new AutoChooser();

		swerve.setDefaultCommand(new SwerveTeleOp(swerve, odometry, () -> -driverController.getLeftY(),
				() -> -driverController.getLeftX(), () -> -driverController.getRightX(),
				() -> driverController.getHID().getRightBumper()));

		elevator.setDefaultCommand(new ManualElevator(elevator, () -> -operatorController.getLeftY(),
				() -> operatorController.getHID().getLeftBumper()));

		configureBindings();

		// thing we need to put in the shuffleboard:
		// elevator preset position (current) -> do we even need that
		// whether we have a piece (do we have sensors for that?)
		Shuffleboard.getTab("Main");
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
		driverController.back().onTrue(new InstantCommand(() -> swerve.resetEncoders()));
		driverController.start().onTrue(new InstantCommand(() -> swerve.resetEncoders()));

		// ground
		operatorController.a().onTrue(new InstantCommand(() -> elevator.setPosition(0)));
		// processor
		operatorController.x().onTrue(new InstantCommand(() -> elevator.setPosition(2)));
		// L3
		operatorController.b().onTrue(new InstantCommand(() -> elevator.setPosition(4)));
		// L4
		operatorController.y().onTrue(new InstantCommand(() -> elevator.setPosition(6)));

		// right for algae, left for coral
		operatorController.rightBumper().whileTrue(new PickupAlgae(algaeIntake));
		operatorController.rightTrigger().whileTrue(new DropAlgae(algaeIntake));
		// operatorController.leftBumper().whileTrue(new PickupCoral(coralIntake));
		// operatorController.leftTrigger().whileTrue(new DropCoral(coralIntake));
		// right side for algae, left for coral
		// autochooser
		// timer
		// current presets elevator & algae
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		return autoChooser.getSelectedAuto();
	}

	public static XboxController getDriverJoystick() {
		return driverController.getHID();
	}
}

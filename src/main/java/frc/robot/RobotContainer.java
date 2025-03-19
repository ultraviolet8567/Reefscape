// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.*;
import frc.robot.commands.*;
import frc.robot.commands.auto.AutoAlignWithReef;
import frc.robot.commands.auto.AutoDriveOut;
import frc.robot.subsystems.AutoChooser;
import frc.robot.subsystems.Odometry;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.algaeIntake.*;
import frc.robot.subsystems.coralIntake.*;
import frc.robot.subsystems.elevator.*;
import frc.robot.subsystems.elevator.Elevator.ElevatorMode;

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
	private final CoralIntake coralIntake;
	private final AutoChooser autoChooser;
	// Replace with CommandPS4Controller or CommandJoystick if needed
	private static final CommandXboxController driverController = new CommandXboxController(
			OperatorConstants.kDriverControllerPort);
	private static final CommandXboxController operatorController = new CommandXboxController(
			OperatorConstants.kOperatorControllerPort);

	public final UsbCamera driverCam = CameraServer.startAutomaticCapture(0);

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		// Configure camera limitations
		if (RobotBase.isReal()) {
			driverCam.setFPS(60);
			driverCam.setResolution(320, 240);
		}

		// Create subsystems real or simulated depending on mode
		switch (Constants.currentMode) {
			case REAL -> {
				elevator = new Elevator(new ElevatorIOSparkMax());
				algaeIntake = new AlgaeIntake(new AlgaeIntakeIOSparkMax());
				coralIntake = new CoralIntake(new CoralIntakeIOSparkMax());
			}
			case SIM -> {
				elevator = new Elevator(new ElevatorIOSim());
				algaeIntake = new AlgaeIntake(new AlgaeIntakeIOSim());
				coralIntake = new CoralIntake(new CoralIntakeIOSim());
			}
			default -> {
				elevator = new Elevator(new ElevatorIO() {
				});
				algaeIntake = new AlgaeIntake(new AlgaeIntakeIO() {
				});
				coralIntake = new CoralIntake(new CoralIntakeIO() {
				});
			}
		}

		swerve = new Swerve();
		odometry = new Odometry(swerve);

		// Configure the PathPlanner auto-builder
		AutoBuilder.configure(odometry::getOdometerPose, odometry::resetOdometerPose, swerve::getRobotRelativeSpeeds,
				swerve::setModuleStates, new PPHolonomicDriveController(new PIDConstants(0.25, 0, 0), // translational
																										// PID
						new PIDConstants(0.5, 0, 0)), // rotational PID
				DriveConstants.kRobotConfig, () -> {
					if (DriverStation.getAlliance().isPresent()) {
						return DriverStation.getAlliance().get() == Alliance.Red;
					}
					return false;
				}, swerve);

		NamedCommands.registerCommand("DropCoral", new DropCoral(coralIntake, () -> elevator.getCoralVoltage()));
		NamedCommands.registerCommand("PickupCoral", new PickupCoral(coralIntake, () -> elevator.getCoralVoltage()));
		NamedCommands.registerCommand("ElevatorL1", new InstantCommand(() -> elevator.setMode(ElevatorMode.L1)));
		NamedCommands.registerCommand("ElevatorL2", new InstantCommand(() -> elevator.setMode(ElevatorMode.L2)));
		NamedCommands.registerCommand("ElevatorL3", new InstantCommand(() -> elevator.setMode(ElevatorMode.L3)));
		NamedCommands.registerCommand("ElevatorL4", new InstantCommand(() -> elevator.setMode(ElevatorMode.L4)));
		NamedCommands.registerCommand("ElevatorMax", new InstantCommand(() -> elevator.setMode(ElevatorMode.HIGH)));
		NamedCommands.registerCommand("ElevatorIntakeCoral",
				new InstantCommand(() -> elevator.setMode(ElevatorMode.STATION)));

		autoChooser = new AutoChooser();

		swerve.setDefaultCommand(new SwerveTeleOp(swerve, odometry, () -> -driverController.getLeftY(),
				() -> -driverController.getLeftX(), () -> -driverController.getRightX(),
				() -> driverController.getHID().getRightBumperButton(),
				() -> driverController.getHID().getLeftBumperButton()));

		elevator.setDefaultCommand(new MoveElevator(elevator, () -> -operatorController.getLeftY(),
				() -> operatorController.getHID().getLeftBumperButton()));

		elevator.setMode(ElevatorMode.MANUAL);

		configureBindings();

		// Shuffleboard setup
		Shuffleboard.getTab("Main").add("Camera", driverCam).withWidget(BuiltInWidgets.kCameraStream).withSize(4, 4)
				.withPosition(5, 0);
		Shuffleboard.getTab("Main").add("Elevator Mode", elevator.getMode().name()).withWidget(BuiltInWidgets.kTextView)
				.withSize(2, 1).withPosition(2, 1);
	}

	private void configureBindings() {
		// Reset gyro
		driverController.back().onTrue(new InstantCommand(() -> odometry.resetGyrometerHeading()));

		// Toggle algae extension/retraction
		driverController.start().onTrue(new InstantCommand(() -> algaeIntake.toggleAlgaeRetraction()));

		// Auto align with Reef right stalk
		// Made to require holding down the button to allow for failsafe abort (when
		// button is released)
		driverController.povRight().whileTrue(new AutoAlignWithReef(swerve, odometry, true));
		driverController.b().whileTrue(new AutoAlignWithReef(swerve, odometry, true));

		// Auto align with Reef left stalk
		// Made to require holding down the button to allow for failsafe abort (when
		// button is released)
		driverController.povLeft().whileTrue(new AutoAlignWithReef(swerve, odometry, false));
		driverController.x().whileTrue(new AutoAlignWithReef(swerve, odometry, false));

		/* Operator elevator controls */
		// Default (taxi) height
		operatorController.a().onTrue(new InstantCommand(() -> elevator.setMode(ElevatorMode.DEFAULT)));
		// Station intaking height
		operatorController.start().onTrue(new InstantCommand(() -> elevator.setMode(ElevatorMode.STATION)));
		// L1 height
		operatorController.back().onTrue(new InstantCommand(() -> elevator.setMode(ElevatorMode.L1)));
		// L2 height
		operatorController.b().onTrue(new InstantCommand(() -> elevator.setMode(ElevatorMode.L2)));
		// L3 height
		operatorController.x().onTrue(new InstantCommand(() -> elevator.setMode(ElevatorMode.L3)));
		// L4 height
		operatorController.y().onTrue(new InstantCommand(() -> elevator.setMode(ElevatorMode.L4)));
		// Algae lower height
		operatorController.pov(180).onTrue(new InstantCommand(() -> elevator.setMode(ElevatorMode.ALGAELOWER)));
		// Algae higher height
		operatorController.pov(0).onTrue(new InstantCommand(() -> elevator.setMode(ElevatorMode.ALGAEHIGHER)));
		// Algae candlestick height
		operatorController.pov(270).onTrue(new InstantCommand(() -> elevator.setMode(ElevatorMode.ALGAECANDLESTICK)));
		// Processor height
		operatorController.pov(90).onTrue(new InstantCommand(() -> elevator.setMode(ElevatorMode.PROCESSOR)));

		// right for algae, left for coral
		operatorController.rightBumper().whileTrue(new PickupAlgae(algaeIntake));
		operatorController.rightTrigger().whileTrue(new DropAlgae(algaeIntake));
		operatorController.leftBumper().whileTrue(new PickupCoral(coralIntake, () -> elevator.getCoralVoltage()));
		operatorController.leftTrigger().whileTrue(new DropCoral(coralIntake, () -> elevator.getCoralVoltage()));
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		System.out.println(autoChooser.getSelectedAuto().getName());

		return autoChooser.getSelectedAuto().getName().equals("Drive Out")
				? new AutoDriveOut(swerve, odometry)
				: autoChooser.getSelectedAuto();
	}

	public static XboxController getDriverJoystick() {
		return driverController.getHID();
	}

	public static XboxController getOperatorJoystick() {
		return operatorController.getHID();
	}

	public void resetEncoder() {
		elevator.resetEncoder();
	}
}

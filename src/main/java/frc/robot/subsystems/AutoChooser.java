package frc.robot.subsystems;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.util.VirtualSubsystem;
import java.util.HashMap;
import java.util.Map;
import org.littletonrobotics.junction.Logger;

public class AutoChooser extends VirtualSubsystem {
	private static final ShuffleboardTab main = Shuffleboard.getTab("Main");
	private final SendableChooser<String> coralNumber, startPos, side, direction;
	private final GenericEntry autoName;

	private final Map<String, PathPlannerAuto> allAutos = new HashMap<String, PathPlannerAuto>();

	public AutoChooser() {
		System.out.println("[Init] Creating AutoChooser");

		// number of coral
		coralNumber = new SendableChooser<>();
		coralNumber.setDefaultOption("0", "Do Nothing");
		coralNumber.addOption("1 Coral", "Place 1 ");
		coralNumber.addOption("2 Coral", "Place 2 ");
		coralNumber.addOption("3 coral", "Place 3 ");
		coralNumber.addOption("4 coral", "Place 4 ");

		// pos selection
		startPos = new SendableChooser<>();
		startPos.setDefaultOption("None", "");
		startPos.addOption("Center", "Middle ");
		startPos.addOption("Left", "Left ");
		startPos.addOption("Right", "Right ");

		// reef side selection
		side = new SendableChooser<>();
		side.setDefaultOption("None", "");
		side.addOption("Reef Side 0", "Side 0 ");
		side.addOption("Reef Side 1", "Side 1 ");
		side.addOption("Reef Side 2", "Side 2 ");
		side.addOption("Reef Side 3", "Side 3 ");
		side.addOption("Reef Side 4", "Side 4 ");
		side.addOption("Reef Side 5", "Side 5 ");

		direction = new SendableChooser<>();
		direction.setDefaultOption("None", "");
		direction.setDefaultOption("Drive Out", "Drive Out");
		direction.addOption("Right", "R");
		direction.addOption("Left", "L");

		// add selectors to shuffleboard
		main.add("Number of Coral", coralNumber).withWidget(BuiltInWidgets.kComboBoxChooser).withSize(2, 1)
				.withPosition(0, 1);
		main.add("Start Location", startPos).withWidget(BuiltInWidgets.kComboBoxChooser).withSize(2, 1).withPosition(0,
				0);
		main.add("Reef Side", side).withWidget(BuiltInWidgets.kComboBoxChooser).withSize(2, 1).withPosition(0, 2);
		main.add("Direction", direction).withWidget(BuiltInWidgets.kComboBoxChooser).withSize(2, 1).withPosition(0, 3);
		autoName = main.add("Auto Name", "").withWidget(BuiltInWidgets.kTextView).withSize(2, 1).withPosition(0, 4)
				.getEntry();

		for (String pathName : AutoBuilder.getAllAutoNames()) {
			allAutos.put(pathName, new PathPlannerAuto(pathName));
		}

		System.out.println("[Init] Auto routines loaded");
	}

	@Override
	public void periodic() {
		Logger.recordOutput("Auto/Routine", getAutoCommandName());

		autoName.setString(
				allAutos.containsKey(getAutoCommandName()) ? getAutoCommandName() : "Auto routine does not exist");
	}

	// Returns name of pre-defined autonomous command based on Shuffleboard input
	public String getAutoCommandName() {
		if (direction.getSelected().equals("Drive Out")) {
			return "Drive Out";
		} else if (coralNumber.getSelected().equals("Do Nothing")) {
			return "Do Nothing";
		} else {
			return "Start " + startPos.getSelected() + coralNumber.getSelected() + side.getSelected()
					+ direction.getSelected();
		}
	}

	public Pose2d getAutoStartingPose() {
		if (getAutoCommandName().equals("Do Nothing")) {
			return new Pose2d();
		} else {
			return allAutos.get(getAutoCommandName()).getStartingPose();
		}
	}

	public Rotation2d getInitialGyroYaw() {
		return getAutoStartingPose().getRotation();
	}

	public PathPlannerAuto getSelectedAuto() {
		String autoCommandName = getAutoCommandName();

		if (autoCommandName.equals("Do Nothing")) {
			return null;
		} else {
			return allAutos.get(autoCommandName);
		}
	}
}

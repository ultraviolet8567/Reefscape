package frc.robot.subsystems.algaeIntake;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import org.littletonrobotics.junction.Logger;

public class AlgaeIntake extends SubsystemBase {
	private final AlgaeIntakeIO io;
	private final AlgaeIntakeIOInputsAutoLogged inputs = new AlgaeIntakeIOInputsAutoLogged();
	private boolean algaeExtended;
	private GenericEntry algaeExtendedTab;

	public AlgaeIntake(AlgaeIntakeIO io) {
		this.io = io;

		algaeExtended = false;

		algaeExtendedTab = Shuffleboard.getTab("Main").add("Algae Intake Extended", algaeExtended)
				.withWidget(BuiltInWidgets.kBooleanBox).withSize(2, 1).withPosition(2, 2).getEntry();
	}

	// Periodic method called in every cycle (e.g., 20ms)
	@Override
	public void periodic() {
		io.updateInputs(inputs);
		Logger.processInputs("AlgaeIntake", inputs);

		// io.setExtsension(algaeExtended);
		Logger.recordOutput("AlgaeIntake/IntakeExtended", algaeExtended);
		algaeExtendedTab.setBoolean(algaeExtended);
	}

	public void setVoltage(double voltage) {
		io.set(voltage);
	}

	public void setExtensionVoltage(double voltage) {
		io.setExtension(voltage);
	}

	public void stop() {
		io.stop();
	}

	public void pickup() {
		io.set(-IntakeConstants.kAlgaeIntakeVoltage);
	}

	public void drop() {
		io.set(IntakeConstants.kAlgaeIntakeVoltage);
	}

	public void toggleAlgaeRetraction() {
		if (algaeExtended) {
			retract();
		} else {
			extend();
		}
	}

	public void extend() {
		algaeExtended = true;
	}

	public void retract() {
		algaeExtended = false;
	}
}

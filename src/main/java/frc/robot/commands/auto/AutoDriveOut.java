package frc.robot.commands.auto;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.AutoConstants;
import frc.robot.subsystems.Odometry;
import frc.robot.subsystems.Swerve;

public class AutoDriveOut extends Command {
	private Swerve swerve;
	private Odometry odometry;
	private Timer timer;

	public AutoDriveOut(Swerve swerve, Odometry odometry) {
		this.swerve = swerve;
		this.odometry = odometry;
		addRequirements(swerve);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		timer = new Timer();
		timer.start();
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		if (timer.get() > 1 && timer.get() < 10) {
			swerve.setModuleStates(ChassisSpeeds.fromFieldRelativeSpeeds(AutoConstants.kAutoXDriveSpeed,
					AutoConstants.kAutoYDriveSpeed, AutoConstants.kAutoTurningSpeed, odometry.getGyrometerHeading()));
		}
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		swerve.stopModules();
		timer.stop();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return timer.get() >= 15;
	}
}

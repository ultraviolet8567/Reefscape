package frc.robot.subsystems.elevator;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import frc.robot.Constants.ElevatorConstants;

public class ElevatorIOSim implements ElevatorIO {
	// fascinating innit?

	private final ElevatorSim elevatorSim;
	private final PIDController pidController;
	private double appliedVolts;

	// Constructor

	public ElevatorIOSim() {
		System.out.println("[Init] Creating ElevatorIOSim");

		elevatorSim = new ElevatorSim(DCMotor.getNEO(2), ElevatorConstants.kElevatorGearing,
				ElevatorConstants.kElevatorCarriageWeight, ElevatorConstants.kElevatorDrumRadius,
				ElevatorConstants.kElevatorMinHeight, ElevatorConstants.kElevatorMaxHeight, true, 0);
		pidController = new PIDController(ElevatorConstants.kP, ElevatorConstants.kI, ElevatorConstants.kD);

		appliedVolts = 0;
	}

	@Override
	public void set(double voltage) {
		appliedVolts = MathUtil.clamp(voltage, -ElevatorConstants.kElevatorVoltage, ElevatorConstants.kElevatorVoltage);
		elevatorSim.setInputVoltage(appliedVolts);
	}

	@Override
	public void setPosition(double position) {
		set(MathUtil.clamp(pidController.calculate(getPositionRads(), position), -ElevatorConstants.kElevatorVoltage,
				ElevatorConstants.kElevatorVoltage));
	}

	@Override
	public void updateInputs(ElevatorIOInputs inputs) {
		elevatorSim.update(0.02);

		inputs.currentAmps = new double[]{elevatorSim.getCurrentDrawAmps(), elevatorSim.getCurrentDrawAmps()};
		inputs.appliedVoltage = new double[]{appliedVolts, appliedVolts};
	}

	@Override
	public void stop() {
		set(0.0);
	}
}

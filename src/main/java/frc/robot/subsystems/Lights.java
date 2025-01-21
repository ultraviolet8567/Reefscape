package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotState;
import frc.robot.util.VirtualSubsystem;

public class Lights extends VirtualSubsystem {
	private static Lights instance;

	public static Lights getInstance() {
		if (instance == null)
			instance = new Lights();
		return instance;
	}

	public int loopCycleCount = 0;
	public boolean lowBattery = false;
	public boolean hasNote = false;
	public boolean climbed = false;
	public boolean autoFinished = false;
	public double autoFinishedTime = 0.0;
	public boolean state = RobotState.isDisabled();
	public Alliance alliance = Alliance.Blue;
	public boolean isDemo = false;

	private final AddressableLED leds = new AddressableLED(0);
	private final AddressableLEDBuffer buffer = new AddressableLEDBuffer(length);
	private final Notifier loadingNotifier = new Notifier(null);
	private GenericEntry demoToggle;

	private static final int length = 20;
	private static final int bottomLength = 8;
	private static final int minLoopCycleCount = 10;
	private static final double lowBatteryVoltage = 10.0;
	private static final double shimmerExtremeness = 0.5;
	private static final double shimmerSpeed = 1;
	private static final double strobeTickSkip = 15;
	private static final int strobeSlowDuration = 5;
	private static final double stripeDuration = 0.75;
	private static final int StripeLength = 5;
	private static final double breathDuration = 1.0;
	private static final double waveExpoenent = 0.4;
	private static final int lowBatteryFlashWait = 50;
	private static final int lowBatteryFlashDuration = 25;
	private static final int strobeFastDuration = 2;
	private static final double rainbowCycleLength = 25.0;
	private static final double rainbowDuration = 0.25;
	private static final double waveFastCycleLength = 25.0;
	private static final double waveFastDuration = 0.25;

	@Override
	public void periodic() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'periodic'");
	}

}

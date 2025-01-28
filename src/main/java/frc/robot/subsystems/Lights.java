package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;
import frc.robot.util.VirtualSubsystem;

public class Lights extends VirtualSubsystem {
	private static Lights instance;

	public static Lights getInstance() {
		if (instance == null)
			instance = new Lights();
		return instance;
	}
	// Robot state tracking
	public int loopCycleCount = 0;
	public boolean lowBattery = false;
	public boolean climbed = false;
	public boolean autoFinished = false;
	public double autoFinishedTime = 0.0;
	public RobotState state = RobotState.DISABLED;
	public Alliance alliance = Alliance.Blue;
	public boolean isDemo = false;

	// LED IO
	private AddressableLED leds = new AddressableLED(0);
	private AddressableLEDBuffer buffer = new AddressableLEDBuffer(length);
	private Notifier loadingNotifier = new Notifier(null);
	private GenericEntry demoToggle;

	// Constants
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
	private static final double waveSlowCycleLength = 25.0;
	private static final double waveAllianceCycleLength = 15.0;
	private static final double waveAllianceDuration = 2.0;

	public static enum Section {
		FULL, BOTTOM, UPPER;

		private int start() {
			switch (this) {
				case FULL :
					return 0;
				case BOTTOM :
					return 0;
				case UPPER :
					return bottomLength;
				default :
					return 0;
			}
		}

		private int end() {
			switch (this) {
				case FULL :
					return length;
				case BOTTOM :
					return bottomLength;
				case UPPER :
					return length;
				default :
					return 0;
			}
		}
	}

	private Lights() {
		leds = new AddressableLED(0);
		buffer = new AddressableLEDBuffer(length);

		leds.setLength(length);

		if (Constants.lightsExist) {
			System.out.println("[Init] Creating Lights");
			leds.setData(buffer);
			leds.start();
		} else {
			System.out.println("[Init] Lights do not exist");
		}

		// Indicates robot is booting up
		loadingNotifier = new Notifier(() -> {
			synchronized (this) {
				// Breath
				// breath(Section.FULL, Color.kPurple, Color.kBlack, 0.4,
				// System.currentTimeMillis() / 1000.0);
				leds.setData(buffer);
			}
		});
		loadingNotifier.startPeriodic(0.02);

		// demoToggle = Shuffleboard.getTab("Main").add("Demo Mode",
		// false).withWidget(BuiltInWidgets.kToggleSwitch)
		// .withSize(1, 1).withPosition(9, 0).getEntry();
	}

	@Override
	public void periodic() {
		loadingNotifier.stop();

		// if (DriverStation.getAlliance().isPresent()) {
		// alliance = DriverStation.getAlliance().get();

		isDemo = demoToggle.getBoolean(false);

		if (Constants.lightsExist) {
			// Exit during initial cycles
			loopCycleCount++;
			if (loopCycleCount < minLoopCycleCount) {
				return;
			}

			// Default to off
			solid(Section.FULL, Color.kBlack);

			// Disabled
			if (state == RobotState.DISABLED) {
				// Purple and yellow stripes
			}
			// Autonomous
			else if (state == RobotState.AUTO) {
				// Rainbow
				rainbow(Section.FULL);
			}

			else {

			}
		}
	}

	private void breath(Section section, Color c1, Color c2, double duration) {
		breath(section, c1, c2, duration, Timer.getFPGATimestamp());
	}

	private void breath(Section section, Color c1, Color c2, double duration, double timestamp) {
		double x = ((timestamp % breathDuration) / breathDuration) * 2.0 * Math.PI;
		double ratio = (Math.sin(x) + 1.0) / 2.0;
		double red = (c1.red * (1 - ratio)) + (c2.red * ratio);
		double green = (c1.green * (1 - ratio)) + (c2.green * ratio);
		double blue = (c1.blue * (1 - ratio)) + (c2.blue * ratio);
		solid(section, new Color(red, green, blue));
	}

	public void solid(Section section, Color color) {
		for (int i = section.start(); i < section.end(); i++) {
			buffer.setLED(i, color);
		}
	}

	private void rainbow(Section section) {
		for (int i = section.start(); i < section.end(); i++) {
			int hue = ((loopCycleCount * 3) % 180 + (i * 180 / length)) % 180;
			buffer.setHSV(i, hue, 255, 128);
		}
	}

	private void strobe(Section section, Color color) {
		for (int i = section.start(); i < section.end(); i++) {
			if (loopCycleCount % (strobeTickSkip) < strobeSlowDuration) {
				buffer.setLED(i, color);
			} else {
				buffer.setHSV(i, 0, 0, 0);
			}
		}
	}

	public static enum RobotState {
		DISABLED, AUTO, TELEOP;
	}

}

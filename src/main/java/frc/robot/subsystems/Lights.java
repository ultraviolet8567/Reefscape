// package frc.robot.subsystems;

// import edu.wpi.first.networktables.GenericEntry;
// import edu.wpi.first.wpilibj.AddressableLED;
// import edu.wpi.first.wpilibj.AddressableLEDBuffer;
// import edu.wpi.first.wpilibj.DriverStation.Alliance;
// import edu.wpi.first.wpilibj.Notifier;
// import edu.wpi.first.wpilibj.RobotState;
// import frc.robot.util.VirtualSubsystem;
// import frc.robot.Constants;

// public class Lights extends VirtualSubsystem {
// 	private static Lights instance;

// 	public static Lights getInstance() {
// 		if (instance == null)
// 			instance = new Lights();
// 		return instance;
// 	}
// 	//Robot state tracking
// 	public int loopCycleCount = 0;
// 	public boolean lowBattery = false;
// 	public boolean hasNote = false;
// 	public boolean climbed = false;
// 	public boolean autoFinished = false;
// 	public double autoFinishedTime = 0.0;
// 	public boolean state = RobotState.isDisabled();
// 	public Alliance alliance = Alliance.Blue;
// 	public boolean isDemo = false;

// 	//LED IO
// 	private final AddressableLED leds = new AddressableLED(0);
// 	private final AddressableLEDBuffer buffer = new AddressableLEDBuffer(length);
// 	private final Notifier loadingNotifier = new Notifier(null);
// 	private GenericEntry demoToggle;

// 	//Constants
// 	private static final int length = 20;
// 	private static final int bottomLength = 8;
// 	private static final int minLoopCycleCount = 10;
// 	private static final double lowBatteryVoltage = 10.0;
// 	private static final double shimmerExtremeness = 0.5;
// 	private static final double shimmerSpeed = 1;
// 	private static final double strobeTickSkip = 15;
// 	private static final int strobeSlowDuration = 5;
// 	private static final double stripeDuration = 0.75;
// 	private static final int StripeLength = 5;
// 	private static final double breathDuration = 1.0;
// 	private static final double waveExpoenent = 0.4;
// 	private static final int lowBatteryFlashWait = 50;
// 	private static final int lowBatteryFlashDuration = 25;
// 	private static final int strobeFastDuration = 2;
// 	private static final double rainbowCycleLength = 25.0;
// 	private static final double rainbowDuration = 0.25;
// 	private static final double waveFastCycleLength = 25.0;
// 	private static final double waveFastDuration = 0.25;
// 	private static final double waveSlowCycleLength = 25.0;
// 	private static final double waveAllianceCycleLength = 15.0;
// 	private static final double waveAllianceDuration = 2.0;

// 	private Lights() {
// 		leds = new AddressableLED(0);
// 		buffer = new AddressableLEDBuffer(length);

// 		leds.setLength(length);

// 		if (Constants.lightExist) {
// 			System.out.println("[Init] Creating Lights");
// 			leds.setData(buffer);
// 			leds.start();
// 		} else {
// 			System.out.println("[Init] Lights do not exist");
// 		}

// 	}
// 	//Indicates robot is booting up
// 	loadingNotifier = new Notifier(() -> {
// 		synchronized (this) {
// 			breath(Section.FULL, Color.kPurple, Color.kBlack, 0.4, System.currentTimeMillis() / 1000.0);
// 			leds.setData(buffer);
// 		}
// 	});
// 	loadingNotifier.startPeriodic(0.02);

// 	demoToggle = Shuffleboard.getTab("Main").add("Demo Mode", false).withWidget(BuiltInWidgets.kToggleSwitch)
// 		.withSize(1, 1).withPosition(9, 0).getEntry();
// }

// public void periodic() {
// 	loadingNotifier.stop();

// 	Logger.recordOutput("RobotState/HasNote", hasNote);
// 	Logger.recordOutput("RobotState/DemoMode", isDemo);


// 	if (DriverStation.getAlliance().isPresent()) {
// 		alliance = DriverStation.getAlliance().get();
// 	}
	

// 	isDemo = demoToggle.getBoolean(false);

// 	if (Constants.lights.Exist) {
// 		//Exit during initial cycles
// 		loopCycleCount++;
// 		if (loopCycleCount < minLoopCycleCount) {
// 			return;
// 		}

// 		//Default to off
// 		solid(Section.FULL, Color.kBlack);

// 		//Disabled
// 		if (state == RobotState.DISABLED) {
// 			//Purple and yellow stripes
// 		}
// 		//Autonomous
// 		else if (state == RobotState.AUTO) {
// 			//Rainbow
// 		rainbow(Section.FULL);
// 		}

// 		//Teleop
// 		else{
// 			// Alliance colors
// 				// if (alliance == Alliance.Blue) {
// 				// wave(Section.FULL, Color.kLightBlue, Color.kDarkBlue, waveSlowCycleLength,
// 				// waveSlowDuration);
// 				// } else {
// 				// wave(Section.FULL, Color.kFirstRed, Color.kRed, waveSlowCycleLength,
// 				// waveSlowDuration);
// 				// }

// 				//Pickup indicator
// 				if (hasNote) {
// 					solid(Section.FULL, color.KGreen);
// 				}
// 				if (climbed) {
// 					solid(Section.FULL)
// 				}
// 		}
// 	}
// } 
// private void strobe(Section section, Color color) {
// 	for (int i = section.start(); i < section.end(); i++) {
// 		if (loopCycleCount % (strobeTickSkip))
// 	}
// }

// 	@Override
// 	public void periodic() {
// 		// TODO Auto-generated method stub
// 		throw new UnsupportedOperationException("Unimplemented method 'periodic'");
// 	}


// }
//  Crescendo
# Reefscape - FRC Team 8567 (2025 season)

Java 17 command-based FRC robot for the 2025 game **Reefscape**. WPILib and GradleRIO
**2025.3.1**, team `8567`. Main class: `frc.robot.Main`.

This is **last season's code, kept as reference**. The machine only has WPILib 2026 installed,
so Gradle builds will fail here. Read it for patterns, do not try to compile it. If a build is
genuinely needed, install WPILib 2025.3.1 first, or use `..\_archive\2026-Reefscape-import`.

## Layout

```
src/main/java/frc/robot/
  Main.java, Robot.java, RobotContainer.java   entry point, lifecycle, mode switch, bindings
  Constants.java                               all tunables + field geometry (299 lines)
  commands/                                    DropAlgae, DropCoral, MoveClimber, MoveElevator,
                                               PickupAlgae, PickupCoral, SwerveTeleOp
  commands/auto/                               AutoAlignWithCoralStation, AutoAlignWithReef,
                                               AutoAlignWithSetpoint, AutoDriveOut, AutoElevatorTop
  subsystems/
    Swerve.java, SwerveModule.java, Odometry.java, AutoChooser.java, Lights.java
    algaeIntake/   AlgaeIntake + IO, IOSim, IOSparkMax, IOSparkMaxSave
    climber/       Climber + IO, IOSim, IOSparkMax
    coralIntake/   CoralIntake + IO, IOSim, IOSparkMax, IOSparkMaxSave
    elevator/      Elevator + IO, IOSim, IOSparkMax
src/main/deploy/                               PathPlanner autos/paths and field assets
vendordeps/                                    AdvantageKit, PathPlanner 2025.2.5, Phoenix 6 25.3.1,
                                               PhotonLib, REVLib, WPILibNewCommands
```

## Architecture: IO layers

Every mechanism is split into a subsystem plus a hardware IO interface:

- `Elevator` holds control logic and talks only to `ElevatorIO`.
- `ElevatorIOSparkMax` is the real robot implementation.
- `ElevatorIOSim` is the simulation implementation.
- `RobotContainer` picks one based on `Constants.currentMode` (`REAL` / `SIM`), which comes
  from `RobotBase.isReal()`.

Keep new mechanisms in that shape: add `XIO`, `XIOSim`, `XIOSparkMax`, then wire the choice
into the `switch` in `RobotContainer`. Do not put motor objects or `SparkMax` calls directly
in a subsystem.

## Conventions

- **Indentation is tabs.** There is no Spotless in this project. Do not reformat files, and do
  not convert tabs to spaces in untouched code.
- **Constants:** nested `public static final class` blocks in `Constants.java`
  (`ElevatorConstants`, `SwerveConstants`, etc.), `k`-prefixed. Add values there, not inline.
- **Logging:** AdvantageKit is on the classpath; keep `Logger.recordOutput` keys consistent
  with the subsystem name.
- **Field coordinates:** `Constants` documents the 2025 field convention - origin is the right
  corner on your alliance side, +x points away from your wall, +y points left. The 2025 field
  is rotated per alliance rather than mirrored, so do not assume 2026-style symmetry.
- **Bindings:** all controller bindings belong in `RobotContainer`; commands live in
  `commands/`, one per file.
- **Autos:** PathPlanner 2025 (`PathplannerLib-2025.2.5`) with `AutoChooser`, and auto commands
  under `commands/auto/`.

## Git

- Remote: `https://github.com/ultraviolet8567/Reefscape.git`
- Branch: `learning`, tracking `origin/learning` (the newest branch, pushed 2025-12-09).
- `main` on the remote is a stale January-2025 snapshot (10 files). Do not merge it in or
  reset to it; `dev` is also older (39 files).
- History before this clone was imported lives on the remote; this checkout was attached to
  `origin/learning` and already contains one local commit normalizing a PathPlanner filename.

## Safety

- Never run `deploy` against this project; its CAN IDs and mechanism code are from 2025.
- Do not change motor IDs or inversions - they describe a robot that no longer exists.

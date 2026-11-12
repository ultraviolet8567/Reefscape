---
description: Compile the 2025 Reefscape project
---

This is the 2025 season project, kept as reference. It has no unit tests, so verification means a
compile.

1. Set the JDK. WPILib 2025 does not need to be installed: GradleRIO 2025.3.1 downloads the 2025
   artifacts, and this is the same Temurin 17 JDK the 2026 project uses.

    ```powershell
    $env:JAVA_HOME = 'C:\Users\Public\wpilib\2026\jdk'
    ```

2. Compile. `-x spotlessApply` is deliberate: `compileJava` normally runs Spotless first and
   rewrites every source file, which buries a real change in a formatting diff.

    ```powershell
    .\gradlew.bat compileJava -x spotlessApply
    ```

Report `verify: PASSED`, or the failing task plus the first real error and its `file:line`.

Never run `deploy` against this project: its CAN IDs and mechanisms describe a robot that no longer
exists.

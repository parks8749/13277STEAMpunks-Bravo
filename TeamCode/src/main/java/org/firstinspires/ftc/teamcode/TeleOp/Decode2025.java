package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Core.DriveTrain;
import org.firstinspires.ftc.teamcode.Core.FlyWheels;
import org.firstinspires.ftc.teamcode.Core.FrontIntake;
import org.firstinspires.ftc.teamcode.Core.LauncherWheel;
import org.firstinspires.ftc.teamcode.Core.DistanceSensor;

@TeleOp(name="Decode2025", group="TeleOp")
public class Decode2025 extends LinearOpMode {

    public DriveTrain driveTrain;
    public LauncherWheel launcherWheel;
    public FlyWheels flyWheels;
    public FrontIntake frontIntake;
    public DistanceSensor distanceSensor;

    private static final float STICK_DEADZONE = 0.08f;
    private static final double BALL_DETECT_DISTANCE = 4.0;
    private static final double MAX_VALID_DISTANCE = 20.0;

    private long ballDetectedTime = 0;

    @Override
    public void runOpMode() {

        driveTrain = new DriveTrain(
                hardwareMap,
                "leftFront", "leftBack",
                "rightFront", "rightBack"
        );
        launcherWheel = new LauncherWheel(
                hardwareMap.get(DcMotor.class, "LauncherWheel")
        );

        frontIntake = new FrontIntake(
                hardwareMap.get(DcMotor.class, "FrontIntake")
        );

        flyWheels = new FlyWheels(
                hardwareMap.get(DcMotor.class, "leftFly"),
                hardwareMap.get(DcMotor.class, "rightFly")
        );

        distanceSensor = new DistanceSensor(hardwareMap, "DistanceSensor");

        launcherWheel.init();
        flyWheels.init();
        frontIntake.init();
        distanceSensor.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            driveTrain.Drive(gamepad1);

            float leftStick  = applyDeadzone(gamepad2.left_stick_y, STICK_DEADZONE);
            float rightStick = applyDeadzone(gamepad2.right_stick_y, STICK_DEADZONE);

            boolean overrideAll = gamepad2.y;
            boolean shootPressed = gamepad2.a;

            // Distance sensor
            distanceSensor.update();
            double currentDistance = distanceSensor.getDistance();

            boolean ballDetected =
                    currentDistance > 0 &&
                            currentDistance < MAX_VALID_DISTANCE &&
                            currentDistance < BALL_DETECT_DISTANCE;

            // Launcher logic
            if (shootPressed || overrideAll) {
                ballDetectedTime = 0;
                launcherWheel.update(gamepad2.b, overrideAll, gamepad2.a);
            } else if (ballDetected) {
                if (ballDetectedTime == 0) {
                    ballDetectedTime = System.currentTimeMillis();
                }
                if (System.currentTimeMillis() - ballDetectedTime >= 750) { // changed to 750ms
                    launcherWheel.stop();
                }
            } else {
                ballDetectedTime = 0;
                launcherWheel.update(false, true, false);
            }

            // Intake always runs based on stick, unaffected by distance sensor
            frontIntake.update(-gamepad2.right_stick_y, gamepad2.a); // flipped stick direction

            flyWheels.update(
                    gamepad2.right_bumper,
                    gamepad2.left_bumper,
                    gamepad2.x,
                    overrideAll
            );

            if (gamepad2.dpad_up)   flyWheels.adjustTargetRPM(20);
            if (gamepad2.dpad_down) flyWheels.adjustTargetRPM(-20);
            if (gamepad2.dpad_left || gamepad2.dpad_right) flyWheels.setTargetRPM(flyWheels.TARGET_RPM);

            telemetry.addData("Distance (in)", currentDistance);
            telemetry.addData("Ball Detected", ballDetected);
            telemetry.update();

            flyWheels.publishTelemetry(telemetry);

            sleep(10);
        }
    }

    private float applyDeadzone(float val, float dz) {
        return Math.abs(val) < dz ? 0.0f : val;
    }
}
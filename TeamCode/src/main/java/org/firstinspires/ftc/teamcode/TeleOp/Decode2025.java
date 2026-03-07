// rugvedh's decode update last 3/6/25
package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

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

    private boolean launcherStopped = false;

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
                hardwareMap.get(DcMotorEx.class, "leftFly"),
                hardwareMap.get(DcMotorEx.class, "rightFly")
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
            // testing for PIDF values
            if(gamepad1.xWasPressed()){
                flyWheels.changeHighVelocity(10);
            }

            if(gamepad1.yWasPressed()){
                flyWheels.toggleVelocities();
                flyWheels.updateFlywheelChanges(telemetry);
            }

            if(gamepad1.bWasPressed()){
                flyWheels.changeStepIndex();
                flyWheels.updateFlywheelChanges(telemetry);
            }

            if(gamepad1.dpadLeftWasPressed()){
                flyWheels.incrF();
                flyWheels.updateFlywheelChanges(telemetry);
            }

            if(gamepad1.dpadRightWasPressed()){
                flyWheels.decrF();
                flyWheels.updateFlywheelChanges(telemetry);
            }

            if(gamepad1.dpadUpWasPressed()){
                flyWheels.incrP();
                flyWheels.updateFlywheelChanges(telemetry);
            }

            if(gamepad1.dpadDownWasPressed()){
                flyWheels.decrP();
                flyWheels.updateFlywheelChanges(telemetry);
            }


            driveTrain.Drive(gamepad1);

            float leftStick  = applyDeadzone(gamepad2.left_stick_y, STICK_DEADZONE);
            float rightStick = applyDeadzone(gamepad2.right_stick_y, STICK_DEADZONE);

            boolean overrideAll = gamepad2.y;
            boolean shootPressed = gamepad2.a;


            distanceSensor.update();
            double currentDistance = distanceSensor.getDistance();

            boolean ballDetected =
                    currentDistance > 0 &&
                            currentDistance < MAX_VALID_DISTANCE &&
                            currentDistance < BALL_DETECT_DISTANCE;

            //The main stuff for the logic of the launcher
            if (overrideAll || shootPressed) {
                launcherStopped = false;
                launcherWheel.update(gamepad2.b, overrideAll, gamepad2.a);
            } else if (ballDetected && !launcherStopped) {
                // Ball detected — stop instantly
                launcherStopped = true;
                launcherWheel.stop();
            } else if (launcherStopped) {
                launcherWheel.stop();
            } else {
                launcherWheel.update(false, true, false);
            }

            //when the distance sensor senses it, it makes the intake the front intake drag the ball in
            // the thing why its not frontintake setpower and why its .update is because im a bad coder
            //-Rugvedh
            if (overrideAll) {
                frontIntake.update(1f, false);
            } else {
                frontIntake.update(gamepad2.right_stick_y, gamepad2.a);
            }

            flyWheels.update(
                    gamepad2.right_bumper,
                    gamepad2.left_bumper,
                    gamepad2.x,
                    overrideAll
            );

//            if (gamepad2.dpad_up)   flyWheels.adjustTargetRPM(20);
//            if (gamepad2.dpad_down) flyWheels.adjustTargetRPM(-20);
//            if (gamepad2.dpad_left || gamepad2.dpad_right) flyWheels.setTargetRPM(flyWheels.TARGET_RPM);

            telemetry.addData("Distance (in)", currentDistance);
            telemetry.addData("Ball Detected", ballDetected);
            telemetry.addData("Launcher Stopped", launcherStopped);
            telemetry.update();

            flyWheels.getVelocityAndError(telemetry);

            sleep(10);
        }
    }

    private float applyDeadzone(float val, float dz) {
        return Math.abs(val) < dz ? 0.0f : val;
    }
}


package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Core.DriveTrain;
import org.firstinspires.ftc.teamcode.Core.FlyWheels;
import org.firstinspires.ftc.teamcode.Core.FrontIntake;
import org.firstinspires.ftc.teamcode.Core.LauncherWheel;

@TeleOp(name="Decode2025", group="TeleOp")
public class Decode2025 extends LinearOpMode {

    public DriveTrain driveTrain;
    public LauncherWheel launcherWheel;
    public FlyWheels flyWheels;
    public FrontIntake frontIntake;

    private static final float STICK_DEADZONE = 0.08f;
    private long yPressedTime = 0;

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

        launcherWheel.init();
        flyWheels.init();
        frontIntake.init();

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

            if (overrideAll && yPressedTime == 0) {
                yPressedTime = System.currentTimeMillis();
            } else if (!overrideAll) {
                yPressedTime = 0;
            }

            boolean launcherAndIntakeReady = overrideAll &&
                    yPressedTime != 0 &&
                    System.currentTimeMillis() - yPressedTime >= 500;
            if (shootPressed) {
                launcherWheel.stop();
            } else if (launcherAndIntakeReady) {
                launcherWheel.setPower(-1.0);
            } else {
                launcherWheel.setPower(-0.10);
            }
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

            if (gamepad2.dpad_up)   flyWheels.adjustTargetRPM(20);
            if (gamepad2.dpad_down) flyWheels.adjustTargetRPM(-20);
            if (gamepad2.dpad_left || gamepad2.dpad_right) flyWheels.setTargetRPM(flyWheels.TARGET_RPM);

            flyWheels.getVelocityAndError(telemetry);

            telemetry.update();

            flyWheels.publishTelemetry(telemetry);

            sleep(10);
        }
    }

    private float applyDeadzone(float val, float dz) {
        return Math.abs(val) < dz ? 0.0f : val;
    }
}
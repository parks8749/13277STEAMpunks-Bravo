package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Core.ColorSensor;
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

    public ColorSensor sensorColor;

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
        sensorColor = new ColorSensor(hardwareMap, "ColorSensor");

        launcherWheel.init();
        flyWheels.init();
        frontIntake.init();

        distanceSensor.init();
        sensorColor.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if(gamepad1.xWasPressed()){
                flyWheels.changeHighVelocity(10);
            }

            if(gamepad1.yWasPressed() || gamepad1.right_bumper){
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
            sensorColor.update();

            double currentDistance = sensorColor.getDistance();

            boolean ballDetected =
                    currentDistance > 0 &&
                            currentDistance < MAX_VALID_DISTANCE &&
                            currentDistance < BALL_DETECT_DISTANCE;

            if (overrideAll || shootPressed) {
                launcherStopped = false;
                launcherWheel.update(gamepad2.b, overrideAll, gamepad2.a);
            } else if (ballDetected && !launcherStopped) {
                launcherStopped = true;
                launcherWheel.stop();
            } else if (launcherStopped) {
                launcherWheel.stop();
            } else {
                launcherWheel.update(false, true, false);
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
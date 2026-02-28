package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Core.DriveTrain;
import org.firstinspires.ftc.teamcode.Core.FrontIntake;
import org.firstinspires.ftc.teamcode.Core.DistanceSensor;
import org.firstinspires.ftc.teamcode.Core.LauncherWheel;

@TeleOp(name="distanceTest", group="TeleOp")
public class distanceSensorTest extends LinearOpMode {

    private DriveTrain driveTrain;
    private FrontIntake frontIntake;
    private LauncherWheel launcherWheel;
    private DistanceSensor distanceSensor;

    private static final double BALL_DETECT_DISTANCE = 4.0;
    private static final double MAX_VALID_DISTANCE = 20.0;

    @Override
    public void runOpMode() {

        driveTrain = new DriveTrain(
                hardwareMap,
                "leftFront", "leftBack",
                "rightFront", "rightBack"
        );

        frontIntake = new FrontIntake(
                hardwareMap.get(com.qualcomm.robotcore.hardware.DcMotor.class, "FrontIntake")
        );

        launcherWheel = new LauncherWheel(
                hardwareMap.get(com.qualcomm.robotcore.hardware.DcMotor.class, "LauncherWheel")
        );

        distanceSensor = new DistanceSensor(hardwareMap, "DistanceSensor");

        frontIntake.init();
        launcherWheel.init();
        distanceSensor.init();

        telemetry.addLine("Auto Launcher + Intake Ready");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {


            driveTrain.Drive(gamepad1);


            frontIntake.update(1.0f, false);


            distanceSensor.update();
            double currentDistance = distanceSensor.getDistance();

            boolean ballBlocking =
                    currentDistance > 0 &&
                            currentDistance < MAX_VALID_DISTANCE &&
                            currentDistance < BALL_DETECT_DISTANCE;


            if (ballBlocking) {
                sleep(500);
                launcherWheel.stop();
                frontIntake.stop();
            } else {
                launcherWheel.update(true, false, false);
            }

            telemetry.addData("Distance (in)", currentDistance);
            telemetry.addData("Ball Blocking", ballBlocking);
            telemetry.update();

            sleep(10);
        }

        frontIntake.stop();
        launcherWheel.stop();
        distanceSensor.stop();
        driveTrain.Stop();
    }
}
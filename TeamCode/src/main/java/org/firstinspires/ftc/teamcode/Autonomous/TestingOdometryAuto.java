package org.firstinspires.ftc.teamcode.Autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "TestingOdometryAuto")
public class TestingOdometryAuto extends LinearOpMode {

    public CRServo backBottom;
    public CRServo backIntake;
    public CRServo launcherWheel;
    public DcMotor leftFlyWheel;
    public DcMotor rightFlyWheel;
    public CRServo rightBelt;
    public CRServo leftBelt;
    public CRServo frontIntake;

    @Override
    public void runOpMode() throws InterruptedException {

        backBottom = hardwareMap.get(CRServo.class, "BackBottom");
        backIntake = hardwareMap.get(CRServo.class, "BackIntake");
        launcherWheel = hardwareMap.get(CRServo.class, "LauncherWheel");
        leftFlyWheel = hardwareMap.get(DcMotor.class, "leftFly");
        rightFlyWheel = hardwareMap.get(DcMotor.class, "rightFly");
        leftBelt = hardwareMap.get(CRServo.class, "LeftBelt");
        rightBelt = hardwareMap.get(CRServo.class, "RightBelt");
        frontIntake = hardwareMap.get(CRServo.class, "FrontIntake");


        Pose2d beginPose = new Pose2d(new Vector2d(-53, -47), Math.toRadians(234)); // -53, -47, 79.5
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        // --- Create the Trajectory Action ---
        Action path = drive.actionBuilder(beginPose)

                .strafeTo(new Vector2d(-46,-39))
                .splineToLinearHeading(new Pose2d(-11.5,-26, Math.toRadians(270)), Math.toRadians(270))





//                  This code works as intended for the actual robot.
//                .lineToY(-70)
//                .strafeToLinearHeading(new Vector2d(-82,-90), Math.toRadians(118))
//                .stopAndAdd(intakeStack())
//                .lineToY(-50)



//                .stopAndAdd(shootBackIntake())
//                .waitSeconds(2.0)
//                .stopAndAdd(shootFrontIntake())
//                .waitSeconds(4.0)
//                .stopAndAdd(stopAll())






//                .splineToLinearHeading(new Pose2d(-11.5,-27, Math.toRadians(270)), Math.toRadians(270))
////                .lineToXSplineHeading(-12,270)
////                .strafeTo(new Vector2d(-50 + 38, -33)) // for first row (PPG)
////                .lineToY(beginPose.position.y - 23)
//                .stopAndAdd(intakeStack())
//                .lineToY(-55)
////                .lineToY(beginPose.position.y + 23)
//                .stopAndAdd(stopIntake()) // Stop intake
//                .lineToY(-25)
//                // 5. RETURN TO SHOOT
////                .lineToX(beginPose.position.x - 27)
////                .strafeTo(new Vector2d(-50, 50))
////                .turn(Math.toRadians(90))
////                .lineToX(beginPose.position.x - 10)
//                .splineToLinearHeading(new Pose2d(-48,-38, Math.toDegrees(31)), Math.toRadians(90))
//                .stopAndAdd(shootFrontIntake())
//                .waitSeconds(3.0)
//                .stopAndAdd(stopAll())
//                // 6. LEAVE
////                .turn(Math.toRadians(-90))
////                .strafeTo(new Vector2d(-50 + 28, 50))
//
//                .splineToConstantHeading(new Vector2d(-22,-46), Math.toRadians(30))
                .build();

        Actions.runBlocking(new SequentialAction(path));
    }

    public Action shootBackIntake() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                backIntake.setPower(-1.0);
                backBottom.setPower(-1.0);
                launcherWheel.setPower(1.0);
                leftFlyWheel.setPower(-.85);
                rightFlyWheel.setPower(.85);
                return false;
            }
        };
    }

    public Action shootFrontIntake() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {

                rightBelt.setPower(1.0);
                leftBelt.setPower(-1.0);
                backBottom.setPower(1.0);
                launcherWheel.setPower(1.0);
                backIntake.setPower(-1.0);
                leftFlyWheel.setPower(-.85);
                rightFlyWheel.setPower(.85);
                return false;
            }
        };
    }

    public Action intakeStack() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                frontIntake.setPower(1.0);
                rightBelt.setPower(1.0);
                leftBelt.setPower(-1.0);
                launcherWheel.setPower(1.0);
                backIntake.setPower(-1.0);
                return false;
            }
        };
    }

    public Action stopIntake() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                launcherWheel.setPower(0);

                return false;
            }
        };
    }

    public Action stopAll() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                rightBelt.setPower(0);
                leftBelt.setPower(0);
                backBottom.setPower(0);
                launcherWheel.setPower(0);
                backIntake.setPower(0);
                leftFlyWheel.setPower(0.0);
                rightFlyWheel.setPower(0.0);
                return false;
            }
        };
    }
}
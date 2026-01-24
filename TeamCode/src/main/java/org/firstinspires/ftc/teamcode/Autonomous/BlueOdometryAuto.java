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

@Autonomous(name = "BlueOdometryAuto")
public class BlueOdometryAuto extends LinearOpMode {

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


        Pose2d beginPose = new Pose2d(new Vector2d(-53, -47), Math.toRadians(232));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        // --- Create the Trajectory Action ---
        Action path = drive.actionBuilder(beginPose)
                .strafeTo(new Vector2d(-46,-39))
                .stopAndAdd(shootBackIntake())
                .waitSeconds(1)
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(4.5)
                .stopAndAdd(stopAll())

//                .splineToLinearHeading(new Pose2d(-11.5,-28, Math.toRadians(274)), Math.toRadians(270))
                .strafeToLinearHeading(new Vector2d(-11.5,-26), Math.toRadians(274))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(-11.5,-55))
//                .stopAndAdd(stopIntake())
                .strafeToLinearHeading(new Vector2d(-46,-39), Math.toRadians(232))

//                .splineToLinearHeading(new Pose2d(-49,-43, Math.toRadians(240)), Math.toRadians(270)) // -45.9, -41.3
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(3.5)
                .stopAndAdd(stopAll())

                .strafeToLinearHeading(new Vector2d(12, -26), Math.toRadians(274))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(12,-60))
                .stopAndAdd(stopIntake())
                .strafeTo(new Vector2d(12,-50))
                .strafeToLinearHeading(new Vector2d(-46,-39), Math.toRadians(232))
//                .splineToLinearHeading(new Pose2d(-49,-43, Math.toRadians(240)), Math.toRadians(270))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(3)
                .stopAndAdd(stopAll())
//
//                .strafeToLinearHeading(new Vector2d(35.5, -26), Math.toRadians(272))
//                .stopAndAdd(intakeStack())
//                .strafeTo(new Vector2d(35.5,-52))
//                .strafeTo(new Vector2d(35.5,-26))
//                .stopAndAdd(stopIntake())
//                .splineToLinearHeading(new Pose2d(-45.9,-41.3, Math.toRadians(232)), Math.toRadians(270))
//                .stopAndAdd(shootFrontIntake())
//                .waitSeconds(3)
//                .stopAndAdd(stopAll())

                .strafeTo(new Vector2d(-23,-46))
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
                rightBelt.setPower(1.0);
                leftBelt.setPower(-1.0);
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
                leftFlyWheel.setPower(-.8);
                rightFlyWheel.setPower(.8);
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

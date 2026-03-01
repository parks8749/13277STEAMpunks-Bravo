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


public class AprilTag22Blue {

    Driver driver;
    //    public CRServo backBottom;
//    public CRServo backIntake;
    public DcMotor launcherWheel;
    public DcMotor leftFlyWheel;
    public DcMotor rightFlyWheel;
    //    public CRServo rightBelt;
//    public CRServo leftBelt;
    public DcMotor frontIntake;


    public void run(LinearOpMode opMode) {

        // Initialize hardware using opMode
      //  backBottom = opMode.hardwareMap.get(CRServo.class, "BackBottom");
      //  backIntake = opMode.hardwareMap.get(CRServo.class, "BackIntake");
        launcherWheel = opMode.hardwareMap.get(DcMotor.class, "LauncherWheel");
        leftFlyWheel = opMode.hardwareMap.get(DcMotor.class, "leftFly");
        rightFlyWheel = opMode.hardwareMap.get(DcMotor.class, "rightFly");
      //  leftBelt = opMode.hardwareMap.get(CRServo.class, "LeftBelt");
      //  rightBelt = opMode.hardwareMap.get(CRServo.class, "RightBelt");
        frontIntake = opMode.hardwareMap.get(DcMotor.class, "FrontIntake");

        Pose2d beginPose = new Pose2d(new Vector2d(-47, 0), Math.toRadians(232));
        MecanumDrive drive = new MecanumDrive(opMode.hardwareMap, beginPose);

        // Build trajectory
        Action path = drive.actionBuilder(beginPose)
                .splineTo(new Vector2d(12, -25), Math.toRadians(270))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(12,-54))
                .strafeTo(new Vector2d(12,-25))
                .splineToLinearHeading(new Pose2d(-45, -45, Math.toRadians(240)), Math.toRadians(230))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(5)
                .stopAndAdd(stopAll())
                .splineToLinearHeading(new Pose2d(-12, -25, Math.toRadians(270)), Math.toRadians(230))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(-12,-54))
                .splineToLinearHeading(new Pose2d(-45, -45, Math.toRadians(240)), Math.toRadians(230))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(5)
                .stopAndAdd(stopAll())
                .strafeTo(new Vector2d(-60,-33))
                        .build();

        Actions.runBlocking(new SequentialAction(path));
    }

    public Action shootBackIntake() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
//                backIntake.setPower(-1.0);
//                backBottom.setPower(-1.0);
                launcherWheel.setPower(1.0);
                frontIntake.setPower(1.0);
                leftFlyWheel.setPower(-.85);
                rightFlyWheel.setPower(.85);
//                rightBelt.setPower(1.0);
//                leftBelt.setPower(-1.0);
                return false;
            }
        };
    }

    public Action shootFrontIntake() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {

//                rightBelt.setPower(1.0);
//                leftBelt.setPower(-1.0);
//                backBottom.setPower(1.0);
                launcherWheel.setPower(1.0);
//                frontIntake.setPower(1.0);
//                backIntake.setPower(-1.0);
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
//                rightBelt.setPower(1.0);
//                leftBelt.setPower(-1.0);
                launcherWheel.setPower(1.0);
//                backIntake.setPower(-1.0);
                return false;
            }
        };
    }

    public Action stopIntake() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                launcherWheel.setPower(0);
                frontIntake.setPower(0);
                return false;
            }
        };
    }

    public Action stopAll() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
//                rightBelt.setPower(0);
//                leftBelt.setPower(0);
//                backBottom.setPower(0);
                launcherWheel.setPower(0);
//                backIntake.setPower(0);
                leftFlyWheel.setPower(0.0);
                rightFlyWheel.setPower(0.0);
                frontIntake.setPower(0.0);
                return false;
            }
        };
    }
}



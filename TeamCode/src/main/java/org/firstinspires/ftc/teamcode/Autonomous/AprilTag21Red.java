package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import org.firstinspires.ftc.teamcode.MecanumDrive;

// gpp
@Autonomous(name = "AprilTag21Red")
public class AprilTag21Red extends LinearOpMode {
    Driver driver;
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

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
@Autonomous(name = "AprilTag21Blue")
public class AprilTag21Blue extends LinearOpMode {
    Driver driver;
//    public CRServo backBottom;
//    public CRServo backIntake;
    public DcMotor launcherWheel;
   public DcMotor leftFlyWheel;
    public DcMotor rightFlyWheel;
//    public CRServo rightBelt;
//    public CRServo leftBelt;
    public DcMotor frontIntake;

    @Override
    public void runOpMode() throws InterruptedException {

//        backBottom = hardwareMap.get(CRServo.class, "BackBottom");
//        backIntake = hardwareMap.get(CRServo.class, "BackIntake");
        launcherWheel = hardwareMap.get(DcMotor.class, "LauncherWheel");
        leftFlyWheel = hardwareMap.get(DcMotor.class, "leftFly");
        rightFlyWheel = hardwareMap.get(DcMotor.class, "rightFly");
//        leftBelt = hardwareMap.get(CRServo.class, "LeftBelt");
//        rightBelt = hardwareMap.get(CRServo.class, "RightBelt");
        frontIntake = hardwareMap.get(DcMotor.class, "FrontIntake");


        Pose2d beginPose = new Pose2d(new Vector2d(-47, 0), Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        // --- Create the Trajectory Action ---
        Action path = drive.actionBuilder(beginPose)


                // Capture GPP pattern
                .splineToLinearHeading(new Pose2d(35.4, -24, Math.toRadians(272)), Math.toRadians(272))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(35.4,-50)) //2
                .strafeTo(new Vector2d(35.4,-24)) //3

                .splineToLinearHeading(new Pose2d(-46, -39, Math.toRadians(232)), Math.toRadians(220)) //4
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(5)
                .stopAndAdd(stopAll())
                .strafeToLinearHeading(new Vector2d(-12,-24), Math.toRadians(272))

                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(-12,-52)) //6
                .stopAndAdd(shootFrontIntake())
                .stopAndAdd(stopAll())
                .strafeToLinearHeading(new Vector2d(-46, -39), Math.toRadians(232))
                .strafeTo(new Vector2d(-59,-37)) //6
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

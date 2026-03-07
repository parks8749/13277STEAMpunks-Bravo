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

//
        Pose2d beginPose = new Pose2d(new Vector2d(-53, -47), Math.toRadians(232));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        // --- Create the Trajectory Action ---
        Action path = drive.actionBuilder(beginPose)

                .stopAndAdd(activateFlyWheels())
                .strafeTo(new Vector2d(-40,-34))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(2)
                .stopAndAdd(stopAll())
                .strafeToLinearHeading(new Vector2d(-54,0),Math.toRadians(180))



                .strafeToLinearHeading(new Vector2d(50, -15), Math.toRadians(272))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(50,-55))
                .strafeTo(new Vector2d(50,-15))
                .stopAndAdd(activateFlyWheels())
                .strafeToLinearHeading(new Vector2d(-40,-34), Math.toRadians(232))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(2)
                .stopAndAdd(stopAll())
                .strafeToLinearHeading(new Vector2d(0,-10), Math.toRadians(272))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(0,-50))
                .strafeTo(new Vector2d(0,-30))
                .stopAndAdd(activateFlyWheels())
                .strafeToLinearHeading(new Vector2d(-40, -34), Math.toRadians(232))
                .stopAndAdd(shootFrontIntake())
                .stopAndAdd(stopAll())
                .strafeTo(new Vector2d(-56,-34))
                .build();

        Actions.runBlocking(new SequentialAction(path));
    }

public Action shootFrontIntake() {
    return packet -> {
        launcherWheel.setPower(-1.0);
        leftFlyWheel.setPower(-.7);
        rightFlyWheel.setPower(.7);
        frontIntake.setPower(-1.0);
        return false;
    };
}

    public Action intakeStack() {
        return packet -> {
            frontIntake.setPower(-1.0);
            return false;
        };
    }

    public Action stopIntake() {
        return packet -> {
            launcherWheel.setPower(0);
            frontIntake.setPower(0);
            return false;
        };
    }

    public Action stopAll() {
        return packet -> {
            launcherWheel.setPower(0);
            frontIntake.setPower(0);
            leftFlyWheel.setPower(0.0);
            rightFlyWheel.setPower(0.0);
            return false;
        };
    }
    public Action activateFlyWheels()
    {
        return packet -> {
            leftFlyWheel.setPower(-0.7);
            rightFlyWheel.setPower(0.7);
            return false;
        };
    }
}

package org.firstinspires.ftc.teamcode.Autonomous;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.MecanumDrive;


public class AprilTag23Blue {

    public DcMotor launcherWheel;
    public DcMotor leftFlyWheel;
    public DcMotor rightFlyWheel;
    public DcMotor frontIntake;

    public void run(LinearOpMode opMode) {

// Initialize hardware using opMode
        launcherWheel = opMode.hardwareMap.get(DcMotor.class, "LauncherWheel");
        leftFlyWheel = opMode.hardwareMap.get(DcMotor.class, "leftFly");
        rightFlyWheel = opMode.hardwareMap.get(DcMotor.class, "rightFly");
        frontIntake = opMode.hardwareMap.get(DcMotor.class, "FrontIntake");

        Pose2d beginPose = new Pose2d(new Vector2d(-47, 0), Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(opMode.hardwareMap, beginPose);

        // Build trajectory
        Action path = drive.actionBuilder(beginPose)
               //ppg blue
                .strafeToLinearHeading(new Vector2d(3, -10), Math.toRadians(270))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(3,-45))
                .strafeTo(new Vector2d(3,-40))
                .stopAndAdd(activateFlyWheels())
                .strafeToLinearHeading(new Vector2d(-40, -34), Math.toRadians(232))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(2)
                .stopAndAdd(stopAll())
                .strafeToLinearHeading(new Vector2d(30, -10), Math.toRadians(270))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(30,-50))
                .strafeTo(new Vector2d(30,-35))
                .stopAndAdd(activateFlyWheels())
                .strafeToLinearHeading(new Vector2d(-435,-29), Math.toRadians(232))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(2)
                .stopAndAdd(stopAll())
                .strafeTo(new Vector2d(-56,-34))
                .build();

        Actions.runBlocking(new SequentialAction(path));
    }

    public Action shootFrontIntake() {
        return packet -> {
            launcherWheel.setPower(-1.0);
            leftFlyWheel.setPower(-.63);
            rightFlyWheel.setPower(.63);
            frontIntake.setPower(-1.0);
            return false;
        };
    }

    public Action activateFlyWheels()
    {
        return packet -> {
            leftFlyWheel.setPower(-0.63);
            rightFlyWheel.setPower(0.63);
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
    public Action stopFlywheels() {
        return packet -> {
            rightFlyWheel.setPower(0);
            leftFlyWheel.setPower(0);
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
}



package org.firstinspires.ftc.teamcode.Autonomous;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.MecanumDrive;


public class AprilTag22Blue {

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

        Pose2d beginPose = new Pose2d(new Vector2d(-47, 0), Math.toRadians(232));
        MecanumDrive drive = new MecanumDrive(opMode.hardwareMap, beginPose);


        // Build trajectory
        Action path = drive.actionBuilder(beginPose)
                //pgp blue
                .strafeToLinearHeading(new Vector2d(12, -25), Math.toRadians(272))
//                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(12,-54))
                .strafeTo(new Vector2d(12,-35))

                .splineToLinearHeading(new Pose2d(-46, -39, Math.toRadians(232)), Math.toRadians(230))
//                .stopAndAdd(shootFrontIntake())
//                .waitSeconds(5)
//                .stopAndAdd(stopAll())
                .strafeToLinearHeading(new Vector2d(-12,-25), Math.toRadians(272))
//                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(-12,-54))
                .strafeToLinearHeading(new Vector2d(-46,-39), Math.toRadians(232))
//                .stopAndAdd(shootFrontIntake())
//                .waitSeconds(5)
//                .stopAndAdd(stopAll())
                .strafeTo(new Vector2d(-60,-33))
                .build();

        Actions.runBlocking(new SequentialAction(path));
    }

    public Action shootFrontIntake() {
        return packet -> {
            launcherWheel.setPower(1.0);
            leftFlyWheel.setPower(-.8);
            rightFlyWheel.setPower(.8);
            frontIntake.setPower(1.0);
            return false;
        };
    }

    public Action intakeStack() {
        return packet -> {
            frontIntake.setPower(1.0);
            launcherWheel.setPower(1.0);
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
}



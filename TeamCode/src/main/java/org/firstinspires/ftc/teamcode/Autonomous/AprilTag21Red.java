package org.firstinspires.ftc.teamcode.Autonomous;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.MecanumDrive;

public class AprilTag21Red {

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

        Pose2d beginPose = new Pose2d(new Vector2d(-47, 0), Math.toRadians(128)); //change heading for all
        MecanumDrive drive = new MecanumDrive(opMode.hardwareMap, beginPose);

        // Build trajectory
        Action path = drive.actionBuilder(beginPose)
                //GPP Red
                .strafeToLinearHeading(new Vector2d(37, 25), Math.toRadians(92))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(37,60))
                .strafeTo(new Vector2d(37,40))
                .stopAndAdd(activateFlyWheels())
                .splineToLinearHeading(new Pose2d(-40,35,Math.toRadians(128)), Math.toRadians(100))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(2)
                .stopAndAdd(stopAll())
                .strafeToLinearHeading(new Vector2d(-10, 25), Math.toRadians(92))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(-10,55))
                .strafeTo(new Vector2d(-10,50))
                .stopAndAdd(activateFlyWheels())
                .strafeToLinearHeading(new Vector2d(-40, 34), Math.toRadians(128))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(2)
                .stopAndAdd(stopAll())
                .strafeTo(new Vector2d(-56,34))
                .build();

        Actions.runBlocking(new SequentialAction(path));
    }

    public Action shootFrontIntake() {
        return packet -> {
            launcherWheel.setPower(1.0);
            leftFlyWheel.setPower(-.7);
            rightFlyWheel.setPower(.7);
            frontIntake.setPower(1.0);
            return false;
        };
    }

    public Action intakeStack() {
        return packet -> {
            frontIntake.setPower(1.0);
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



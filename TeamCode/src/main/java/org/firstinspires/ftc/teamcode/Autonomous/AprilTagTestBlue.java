package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "AprilTagTestBlue", group = "Autonomous")
public class AprilTagTestBlue extends LinearOpMode {

    private Limelight3A limelight;
    public DcMotor launcherWheel;
    public DcMotor leftFlyWheel;
    public DcMotor rightFlyWheel;
    public DcMotor frontIntake;

    @Override
    public void runOpMode() throws InterruptedException {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.start();
        launcherWheel = hardwareMap.get(DcMotor.class, "LauncherWheel");
        leftFlyWheel = hardwareMap.get(DcMotor.class, "leftFly");
        rightFlyWheel = hardwareMap.get(DcMotor.class, "rightFly");
        frontIntake = hardwareMap.get(DcMotor.class, "FrontIntake");
        Pose2d beginPose = new Pose2d(new Vector2d(-53, -47), Math.toRadians(135));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            Action launchSequence = drive.actionBuilder(beginPose)
                    .splineTo(new Vector2d(-46, -39), Math.toRadians(0))
                    .stopAndAdd(shootFrontIntake())
                    .waitSeconds(3)
                    .stopAndAdd(stopAll())
                    .build();

            Actions.runBlocking(new SequentialAction(launchSequence));
            Pose2d afterLaunchPose = drive.localizer.getPose();
            Action toScanPosition = drive.actionBuilder(afterLaunchPose)
                    .splineTo(new Vector2d(-47, 0), Math.toRadians(232))
                    .build();
            Actions.runBlocking(new SequentialAction(toScanPosition));

            int detectedTag = detectTagByPipelines();
            telemetry.addData("Detected Tag", detectedTag);
            telemetry.update();

            if (detectedTag == 21) {
                new AprilTag21Blue().run(this);
            } else if (detectedTag == 22) {
                new AprilTag22Blue().run(this);
            } else if (detectedTag == 23) {
                new AprilTag23Blue().run(this);
            }
        }
    }
    private int detectTagByPipelines() {
        limelight.pipelineSwitch(7);
        sleep(200);
        LLResult p7 = limelight.getLatestResult();
        if (p7 != null && p7.isValid()) return 21;

        limelight.pipelineSwitch(8);
        sleep(200);
        LLResult p8 = limelight.getLatestResult();
        if (p8 != null && p8.isValid()) return 22;

        limelight.pipelineSwitch(9);
        sleep(200);
        LLResult p9 = limelight.getLatestResult();
        if (p9 != null && p9.isValid()) return 23;

        return 23;
    }
    public Action shootFrontIntake() {
        return packet -> {
            launcherWheel.setPower(-1.0);
            leftFlyWheel.setPower(-0.85);
            rightFlyWheel.setPower(0.85);
            frontIntake.setPower(-1.0);
            return false;
        };
    }
    public Action stopAll() {
        return packet -> {
            launcherWheel.setPower(0);
            frontIntake.setPower(0);
            leftFlyWheel.setPower(0);
            rightFlyWheel.setPower(0);
            return false;
        };
    }
}
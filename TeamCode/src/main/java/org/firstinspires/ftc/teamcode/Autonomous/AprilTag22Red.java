package org.firstinspires.ftc.teamcode.Autonomous;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.MecanumDrive;

public class AprilTag22Red {

    public DcMotor launcherWheel;
    public DcMotor leftFlyWheel;
    public DcMotor rightFlyWheel;
    public DcMotor frontIntake;

    public void run(LinearOpMode opMode) {

        launcherWheel = opMode.hardwareMap.get(DcMotor.class, "LauncherWheel");
        leftFlyWheel = opMode.hardwareMap.get(DcMotor.class, "leftFly");
        rightFlyWheel = opMode.hardwareMap.get(DcMotor.class, "rightFly");
        frontIntake = opMode.hardwareMap.get(DcMotor.class, "FrontIntake");

        Pose2d beginPose = new Pose2d(new Vector2d(-47, 0), Math.toRadians(128));
        MecanumDrive drive = new MecanumDrive(opMode.hardwareMap, beginPose);

        // Build trajectory
        Action path = drive.actionBuilder(beginPose)
                //pgp blue
                .strafeToLinearHeading(new Vector2d(14, 25), Math.toRadians(92))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(14,55))
                .strafeTo(new Vector2d(14,45))
                .stopAndAdd(activateFlyWheels())
                .splineToLinearHeading(new Pose2d(-40,35,Math.toRadians(128)), Math.toRadians(100))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(2)
                .stopAndAdd(stopAll())
                .strafeToLinearHeading(new Vector2d(-9, 20), Math.toRadians(92))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(-9,60))
                .strafeTo(new Vector2d(-9,40))
                .stopAndAdd(activateFlyWheels())
                .strafeToLinearHeading(new Vector2d(-40, 34), Math.toRadians(128))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(2)
                .stopAndAdd(stopAll())
                .strafeTo(new Vector2d(-55,30))
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
            leftFlyWheel.setPower(-0.63);
            rightFlyWheel.setPower(0.63);
            return false;
        };
    }
}


/*
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

@Autonomous(name="AprilTagTestBlue", group="Autonomous")
public class AprilTagTestBlue extends LinearOpMode {

    private Limelight3A limelight;

    public DcMotor launcherWheel;
    public DcMotor leftFlyWheel;
    public DcMotor rightFlyWheel;
    public DcMotor frontIntake;

    @Override
    public void runOpMode() throws InterruptedException {
        // Hardware init
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.start();

        launcherWheel = hardwareMap.get(DcMotor.class, "LauncherWheel");
        leftFlyWheel   = hardwareMap.get(DcMotor.class, "leftFly");
        rightFlyWheel  = hardwareMap.get(DcMotor.class, "rightFly");
        frontIntake    = hardwareMap.get(DcMotor.class, "FrontIntake");

        // Starting pose — update these values to match your actual field position
        Pose2d beginPose = new Pose2d(new Vector2d(-47, 0), Math.toRadians(0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {

            // ── Phase 1: Initial launch sequence via odometry ──────────────────
            // Spin up shooter while driving into shooting position
            Action launchSequence = drive.actionBuilder(beginPose)
                    // Replace this splineTo with the actual coordinates of your shooting position
                    .splineTo(new Vector2d(-45, 45), Math.toRadians(135))
                    .stopAndAdd(shootFrontIntake())
                    .waitSeconds(3)
                    .stopAndAdd(stopAll())
                    .build();

            Actions.runBlocking(new SequentialAction(launchSequence));

            // ── Phase 2: Drive to limelight scan position ───────────────────────
            Pose2d afterLaunchPose = drive.pose; // RoadRunner tracks current pose
            Action toScanPosition = drive.actionBuilder(afterLaunchPose)
                    // Replace with your actual limelight scanning position
                    .splineTo(new Vector2d(-12, 25), Math.toRadians(90))
                    .build();

            Actions.runBlocking(new SequentialAction(toScanPosition));

            // ── Phase 3: Detect AprilTag with Limelight ─────────────────────────
            int detectedTag = detectTagByPipelines();

            telemetry.addData("Detected Tag", detectedTag);
            telemetry.update();

            // ── Phase 4: Run the matching tag's odometry autonomous ──────────────
            if (detectedTag == 21) {
                new AprilTag21Blue().run(this);
            } else if (detectedTag == 22) {
                new AprilTag22Blue().run(this);
            } else if (detectedTag == 23) {
                new AprilTag23Blue().run(this);
            }
        }
    }

    // ── Limelight detection (unchanged logic) ────────────────────────────────
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

        return 23; // default fallback
    }

    // ── Motor actions (same pattern as the odometry files) ───────────────────
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
 */
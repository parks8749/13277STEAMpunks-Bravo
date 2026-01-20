package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@TeleOp(name = "Mecanum IMU Straight", group = "Drive")
public class MecanumImuStraightTeleOp extends LinearOpMode {

    // --- Hardware ---
    private DcMotor motorFL, motorFR, motorBL, motorBR;
    private IMU imu;

    // --- Heading hold state ---
    private double targetHeadingRad = 0.0;

    // --- PID (start values; you may tune) ---
    private double kP = 2.5;
    private double kD = 0.20;

    private double lastErr = 0.0;
    private double lastTime = 0.0;

    // --- Helpers ---
    private static double clip(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    private static double wrapAngle(double a) {
        while (a > Math.PI) a -= 2.0 * Math.PI;
        while (a < -Math.PI) a += 2.0 * Math.PI;
        return a;
    }

    private double getHeadingRad() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }


    @Override
    public void runOpMode() {

        // ----- CHANGE THESE NAMES TO MATCH YOUR DRIVER STATION CONFIG -----
        motorFL = hardwareMap.get(DcMotor.class, "leftFront");
        motorFR = hardwareMap.get(DcMotor.class, "rightFront");
        motorBL = hardwareMap.get(DcMotor.class, "leftBack");
        motorBR = hardwareMap.get(DcMotor.class, "rightBack");

        // IMU name is usually "imu" if you set it that way in config
        imu = hardwareMap.get(IMU.class, "imu");

        // ----- MOTOR DIRECTIONS -----
        // These are the most common for mecanum when motors are mounted mirrored.
        // If your robot drives backwards/sideways, ONLY change these (don’t change math).
        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBR.setDirection(DcMotorSimple.Direction.REVERSE);

        // Brake helps reduce drift when you release stick
        motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Use open-loop power (recommended for TeleOp unless you have a reason)
        motorFL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // ----- IMU INIT -----
        // If your yaw is already correct, this is enough.
        // Optional: reset yaw so “0” points wherever the robot faces at start.
        imu.resetYaw();

        telemetry.addLine("Ready: IMU heading-hold mecanum");
        telemetry.addLine("Left stick = drive, Right stick X = turn, Press Y to re-lock heading");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // Lock initial heading
        targetHeadingRad = getHeadingRad();
        lastTime = getRuntime();
        lastErr = 0.0;

        while (opModeIsActive()) {

            double now = getRuntime();
            double dt = now - lastTime;
            if (dt <= 0) dt = 1e-3;
            lastTime = now;

            // Driver inputs
            double y = -gamepad1.left_stick_y;   // forward
            double x =  gamepad1.right_stick_x;  // strafe
            double driverTurn = gamepad1.left_stick_x;

            // Deadzones (important!)
            if (Math.abs(y) < 0.08) y = 0;
            if (Math.abs(x) < 0.08) x = 0;
            if (Math.abs(driverTurn) < 0.1) driverTurn = 0;

            // Optional: re-lock heading to current with Y button
            if (gamepad1.y) {
                targetHeadingRad = getHeadingRad();
                lastErr = 0.0;
            }

            double heading = getHeadingRad();
            double rot;

            if (driverTurn != 0) {
                // Manual turning: let driver turn, and update target so it doesn't fight you
                rot = driverTurn;
                targetHeadingRad = heading;
                lastErr = 0.0;
            } else {
                // Heading hold: keep robot at targetHeadingRad
                double err = wrapAngle(targetHeadingRad - heading);
                double derr = (err - lastErr) / dt;
                lastErr = err;

                rot = (kP * err) + (kD * derr);

                // Limit correction so it stays smooth
                rot = clip(rot, -0.6, 0.6);
            }

            // Standard mecanum mixing
            double fl = y + x + rot;
            double bl = y - x + rot;
            double fr = y - x - rot;
            double br = y + x - rot;

            // Normalize
            double max = Math.max(1.0,
                    Math.max(Math.abs(fl),
                            Math.max(Math.abs(bl),
                                    Math.max(Math.abs(fr), Math.abs(br)))));

            fl /= max; bl /= max; fr /= max; br /= max;

            motorFL.setPower(fl);
            motorBL.setPower(bl);
            motorFR.setPower(fr);
            motorBR.setPower(br);

            telemetry.addData("Heading (deg)", Math.toDegrees(heading));
            telemetry.addData("Target (deg)", Math.toDegrees(targetHeadingRad));
            telemetry.addData("rot", rot);
            telemetry.update();
        }
    }
}

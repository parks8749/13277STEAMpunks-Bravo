package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "IMU Debug (Yaw/Wz)", group = "Debug")
public class IMUDebug extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        // Name must match DS config exactly
        IMU imu = hardwareMap.get(IMU.class, "imu");

        // Use THE SAME orientation you put in your MecanumDrive params
        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(
                MecanumDrive.PARAMS.logoFacingDirection,
                MecanumDrive.PARAMS.usbFacingDirection
        );

        IMU.Parameters params = new IMU.Parameters(orientation);
        imu.initialize(params);

        telemetry.addLine("IMU initialized. Press start. Then spin robot by hand.");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // Optional: press start then it zeroes yaw once
        imu.resetYaw();

        while (opModeIsActive()) {
            YawPitchRollAngles a = imu.getRobotYawPitchRollAngles();
            AngularVelocity w = imu.getRobotAngularVelocity(AngleUnit.RADIANS);

            telemetry.addData("Yaw (deg)", a.getYaw(AngleUnit.DEGREES));
            telemetry.addData("Pitch (deg)", a.getPitch(AngleUnit.DEGREES));
            telemetry.addData("Roll (deg)", a.getRoll(AngleUnit.DEGREES));

            telemetry.addData("wx (rad/s)", w.xRotationRate);
            telemetry.addData("wy (rad/s)", w.yRotationRate);
            telemetry.addData("wz (rad/s)", w.zRotationRate);

            telemetry.addLine("Spin the robot flat on the floor (turn in place).");
            telemetry.update();
        }
    }
}

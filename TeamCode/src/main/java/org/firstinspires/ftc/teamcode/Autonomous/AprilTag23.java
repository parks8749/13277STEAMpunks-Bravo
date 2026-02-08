package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous(name = "AprilTag23")
public class AprilTag23 extends LinearOpMode {
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
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);
        backBottom   = (hardwareMap.get(CRServo.class, "BackBottom"));
        backIntake   = (hardwareMap.get(CRServo.class, "BackIntake"));
        launcherWheel= (hardwareMap.get(CRServo.class, "LauncherWheel"));
        leftFlyWheel = (hardwareMap.get(DcMotor.class, "leftFly"));
        rightFlyWheel = (hardwareMap.get(DcMotor.class, "rightFly"));
        leftBelt = (hardwareMap.get(CRServo.class, "LeftBelt"));
        rightBelt = (hardwareMap.get(CRServo.class, "RightBelt"));
        frontIntake = (hardwareMap.get(CRServo.class, "FrontIntake"));

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        waitForStart();

        if (opModeIsActive())
        {
            // turn off limelight camera, because it overheats and uses too much energy
            // the first half of this where it goes back ready to almost turn before moves forward
            driver.forward_tiles(-1.5);
            driver.turn_ticks(-90,1);
            driver.forward_tiles(2.3);

            frontIntake.setPower(1.0);
            rightBelt.setPower(1.0);
            leftBelt.setPower(-1.0);
            launcherWheel.setPower(1.0);
            backIntake.setPower(-1.0);
            sleep(500);
//-------------------------------------------------------------------------------
            driver.forward_tiles(-1);
            driver.strafe_tiles(1.5);
            driver.forward_tiles(1);
            driver.turn_ticks(30,1);
//-------------------------------------------------------------------------------
            //shoot artifacts
            rightBelt.setPower(1.0);
            leftBelt.setPower(-1.0);
            backBottom.setPower(1.0);
            launcherWheel.setPower(1.0);
            backIntake.setPower(-1.0);
            leftFlyWheel.setPower(-1.0);
            rightFlyWheel.setPower(1.0);
            sleep(4000);
//-------------------------------------------------------------------------------
            // part c
            driver.forward_tiles(-1);
            driver.turn_ticks(30,1);
            driver.strafe_tiles(1.5);
            driver.forward_tiles(2.3);
            frontIntake.setPower(1.0);
            rightBelt.setPower(1.0);
            leftBelt.setPower(-1.0);
            launcherWheel.setPower(1.0);
            backIntake.setPower(-1.0);
            sleep(500);
            driver.strafe_tiles(2.5);
            driver.turn_ticks(30,1);
            driver.strafe_tiles(2, 1);
            // divider
            //shoot artifacts
            rightBelt.setPower(1.0);
            leftBelt.setPower(-1.0);
            backBottom.setPower(1.0);
            launcherWheel.setPower(1.0);
            backIntake.setPower(-1.0);
            leftFlyWheel.setPower(-1.0);
            rightFlyWheel.setPower(1.0);
            sleep(4000);
        }
    }
}

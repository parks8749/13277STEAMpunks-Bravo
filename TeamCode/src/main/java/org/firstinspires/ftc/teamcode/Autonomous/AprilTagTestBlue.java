package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="AprilTagTestBlue", group="Autonomous")
public class AprilTagTestBlue extends LinearOpMode {
    private Limelight3A limelight;
    Driver driver;
    //public CRServo backBottom;
   // public CRServo backIntake;
    public DcMotor launcherWheel;
   // public DcMotor leftFlyWheel;
   // public DcMotor rightFlyWheel;
   // public CRServo rightBelt;
   // public CRServo leftBelt;
    public DcMotor frontIntake;


    @Override
    public void runOpMode() throws InterruptedException {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.start();

        driver = new Driver(this, hardwareMap);
       // backBottom   = (hardwareMap.get(CRServo.class, "BackBottom"));
      //  backIntake   = (hardwareMap.get(CRServo.class, "BackIntake"));
        launcherWheel= (hardwareMap.get(DcMotor.class, "LauncherWheel"));
       // leftFlyWheel = (hardwareMap.get(DcMotor.class, "leftFly"));
       // rightFlyWheel = (hardwareMap.get(DcMotor.class, "rightFly"));
       // leftBelt = (hardwareMap.get(CRServo.class, "LeftBelt"));
       // rightBelt = (hardwareMap.get(CRServo.class, "RightBelt"));
        frontIntake = (hardwareMap.get(DcMotor.class, "FrontIntake"));

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        waitForStart();

        if (opModeIsActive()) {
            // shoot the 3 pre loaded
            // gotta edit the values to go to the limelight
            driver.forward_tiles(-0.2);
            driver.turn_ticks(450,1);
            driver.strafe_tiles(-2,1);

            int detectedTag = detectTagByPipelines();

            telemetry.addData("Detected Tag", detectedTag);
            telemetry.update();

            if (detectedTag == 21) {
                //
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
}

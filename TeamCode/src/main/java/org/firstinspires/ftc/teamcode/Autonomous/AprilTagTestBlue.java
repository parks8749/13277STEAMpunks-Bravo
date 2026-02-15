package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import org.firstinspires.ftc.teamcode.Autonomous.AprilTag21Blue;
import org.firstinspires.ftc.teamcode.Autonomous.AprilTag22Blue;
import org.firstinspires.ftc.teamcode.Autonomous.AprilTag23Blue;



@Autonomous(name="AprilTagTestBlue", group="Autonomous")
public class AprilTagTestBlue extends LinearOpMode {
    private Limelight3A limelight;
    /*
    make it do the same thing over again at first 2 new seperate pipelines that takes
    the data from the thing 2 aprilTags and make like an if structure where the apriltags
    first like scans the alliance tags and then like does an embedded
     */
    // the main thing you have to add here why the turn 45 degrees and straffee 2 to the right or left

    // Check for time constraints
    @Override
    public void runOpMode() throws InterruptedException {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.start();

        waitForStart();

        if (opModeIsActive()) {

            int detectedTag = detectTagByPipelines();

            telemetry.addData("Detected Tag", detectedTag);
            telemetry.update();

            if (detectedTag == 21) {

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

        return 21;
    }
}



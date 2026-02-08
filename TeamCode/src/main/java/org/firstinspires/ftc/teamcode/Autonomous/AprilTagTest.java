package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;

@Autonomous(name="AprilTagTest", group="Autonomous")
public class AprilTagTest extends LinearOpMode {
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
                // gotta make a new class and link it to that class do this for better organization
            } else if (detectedTag == 22) {
                //
            } else if (detectedTag == 23) {
                //
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

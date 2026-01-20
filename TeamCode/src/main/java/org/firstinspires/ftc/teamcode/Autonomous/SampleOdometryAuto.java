package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.PathBuilder;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "SampleOdometryAuto")
public class SampleOdometryAuto extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException
    {
        // change the values below to get the correct starting position and heading
        Pose2d beginPose = new Pose2d(new Vector2d(-50,50), Math.toRadians(135));

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        // creating autonomous path
        Action path = drive.actionBuilder(beginPose)
                // put code here to do movements
                // then copy and paste the code into MeepMeepTesting to see if the code works as intended before
                // actually testing on actual robot
                .build();
        Actions.runBlocking(new SequentialAction(path));
    }
}

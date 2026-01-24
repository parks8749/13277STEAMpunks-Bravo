package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(680);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();



        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-53, -47, Math.toRadians(232)))
                .strafeTo(new Vector2d(-46,-39))

//                .splineToLinearHeading(new Pose2d(-11.5,-28, Math.toRadians(274)), Math.toRadians(270))
                .strafeToLinearHeading(new Vector2d(-11.5,-26), Math.toRadians(274))
                .strafeTo(new Vector2d(-11.5,-55))
                .strafeToLinearHeading(new Vector2d(-46,-39), Math.toRadians(232))

                .strafeToLinearHeading(new Vector2d(12, -26), Math.toRadians(274))
                .strafeTo(new Vector2d(12,-55))
                .strafeTo(new Vector2d(12,-50))
                .strafeToLinearHeading(new Vector2d(-46,-39), Math.toRadians(232))
//
//                .strafeToLinearHeading(new Vector2d(35.5, -26), Math.toRadians(272))
//                .stopAndAdd(intakeStack())
//                .strafeTo(new Vector2d(35.5,-52))
//                .strafeTo(new Vector2d(35.5,-26))
//                .stopAndAdd(stopIntake())
//                .splineToLinearHeading(new Pose2d(-45.9,-41.3, Math.toRadians(232)), Math.toRadians(270))
//                .stopAndAdd(shootFrontIntake())
//                .waitSeconds(3)
//                .stopAndAdd(stopAll())

                .strafeTo(new Vector2d(-23,-46))
                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}

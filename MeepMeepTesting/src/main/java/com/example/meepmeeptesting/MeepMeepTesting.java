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



        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-53, -47, Math.toRadians(233))) // -53, -47, 79.5
                .strafeTo(new Vector2d(-46,-39))
                .splineToLinearHeading(new Pose2d(-11.5,-26, Math.toRadians(270)), Math.toRadians(270))
                .strafeTo(new Vector2d(-11.5,-55))
                .strafeTo(new Vector2d(-11.5,-26))
                .splineToLinearHeading(new Pose2d(-45.9,-41.3, Math.toRadians(233)), Math.toRadians(270))
                .strafeTo(new Vector2d(-23,-46))
                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}

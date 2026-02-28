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
//                .stopAndAdd(shootFrontIntake())
//                .waitSeconds(5)
//                .stopAndAdd(stopAll())

                .strafeToLinearHeading(new Vector2d(-11.5,-22), Math.toRadians(272))
//                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(-11.5,-55))
                .strafeToLinearHeading(new Vector2d(-46,-39), Math.toRadians(232))





                // starting position and heading for close auto
                // (-53, -47, Math.toRadians(232))
                // (x, y, heading) for first spike mark
                // (-11.5, -22, Math.toRadians(272))
                // (x, y, heading) for second spike mark
                // (12, -26, Math.toRadians(272))
                // (x, y, heading) for third spike mark
                // (35, -26, Math.toRadians(272))

                // starting position and heading for far auto
                // (62, -16, Math.toRadians(180))



                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}

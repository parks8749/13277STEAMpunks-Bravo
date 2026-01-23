package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Core.BackBottom;
import org.firstinspires.ftc.teamcode.Core.BackIntake;
import org.firstinspires.ftc.teamcode.Core.Belts;
import org.firstinspires.ftc.teamcode.Core.DriveTrain;
import org.firstinspires.ftc.teamcode.Core.FlyWheels;
import org.firstinspires.ftc.teamcode.Core.FrontIntake;
import org.firstinspires.ftc.teamcode.Core.LauncherWheel;

@TeleOp(name="Decode2025", group="TeleOp")
public class Decode2025 extends LinearOpMode {

    public DriveTrain driveTrain;
    public BackBottom backBottom;
    public BackIntake backIntake;
    public LauncherWheel launcherWheel;
    public FlyWheels flyWheels;
    public Belts belts;
    public FrontIntake frontIntake;

    private static final float STICK_DEADZONE = 0.08f;

    @Override
    public void runOpMode() {

        driveTrain = new DriveTrain(
                hardwareMap,
                "leftFront", "leftBack",
                "rightFront", "rightBack"
        );

        backBottom = new BackBottom(
                hardwareMap.get(CRServo.class, "BackBottom")
        );

        backIntake = new BackIntake(
                hardwareMap.get(CRServo.class, "BackIntake")
        );

        launcherWheel = new LauncherWheel(
                hardwareMap.get(CRServo.class, "LauncherWheel")
        );

        frontIntake = new FrontIntake(
                hardwareMap.get(CRServo.class, "FrontIntake")
        );

        flyWheels = new FlyWheels(
                hardwareMap.get(DcMotor.class, "leftFly"),
                hardwareMap.get(DcMotor.class, "rightFly")
        );

        belts = new Belts(
                hardwareMap.get(CRServo.class, "LeftBelt"),
                hardwareMap.get(CRServo.class, "RightBelt")
        );

        // Init all subsystems
        backBottom.init();
        backIntake.init();
        launcherWheel.init();
        flyWheels.init();
        belts.init();
        frontIntake.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            driveTrain.Drive(gamepad1);

            float leftStick  = applyDeadzone(gamepad2.left_stick_y, STICK_DEADZONE);
            float rightStick = applyDeadzone(gamepad2.right_stick_y, STICK_DEADZONE);

            boolean overrideAll = gamepad2.y;

            int beltsMode = belts.getMode();
            boolean frontActive = frontIntake.isActive(beltsMode);

            launcherWheel.update(gamepad2.b, overrideAll, gamepad2.a);
            backIntake.update(leftStick, overrideAll, beltsMode, gamepad2.a);


            backBottom.update(
                    beltsMode,
                    gamepad2.left_stick_y,
                    overrideAll,
                    frontActive,
                    gamepad2.a
            );

            flyWheels.update(
                    gamepad2.right_bumper,
                    gamepad2.left_bumper,
                    gamepad2.x,
                    overrideAll
            );

//            if (gamepad2.dpad_up)   flyWheels.adjustTargetRPM(1);
//            if (gamepad2.dpad_down) flyWheels.adjustTargetRPM(-1);


            belts.update(rightStick, gamepad2.a);
            frontIntake.update(beltsMode, gamepad2.a);

            telemetry.addData("Front Intake Active", frontActive);

            telemetry.update();

            flyWheels.publishTelemetry(telemetry);

            sleep(10);
        }
    }

    private float applyDeadzone(float val, float dz) {
        return Math.abs(val) < dz ? 0.0f : val;
    }
}





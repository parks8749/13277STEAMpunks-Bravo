package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class FrontIntake {

    private final DcMotor frontIntake;
//    private boolean lastAPressed = false;

    public FrontIntake(DcMotor frontIntake) {
        this.frontIntake = frontIntake;
    }

    public void init() {
        frontIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        frontIntake.setPower(0.0);
    }

    /**
     * mode == 1 -> power = +1.0
     * mode == 2 -> power = -1.0
     * mode == 0 -> power = 0.0
     */
    public void update(float rightStickY, boolean aPressed) {
        if (rightStickY < 0.0f) {
            frontIntake.setPower(1.0);
        } else if (rightStickY > 0.0f) {
            frontIntake.setPower(-1.0);
        } else {
            frontIntake.setPower(0.0);
        }


        if (aPressed) {
            frontIntake.setPower(1.0);
        }


    }

//    public boolean isActive(int beltsMode) {
//        return beltsMode == 1 || beltsMode == 2;
//    }

    public void stop() {
        frontIntake.setPower(0.0);
    }
}

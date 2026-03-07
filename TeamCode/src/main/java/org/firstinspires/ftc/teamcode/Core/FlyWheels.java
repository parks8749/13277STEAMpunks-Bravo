//package org.firstinspires.ftc.teamcode.Core;
//
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//
//public class FlyWheels {
//
//    private final DcMotor leftFly;
//    private final DcMotor rightFly;
//
//    // default (current) operating power
//    private static final double DEFAULT_POWER = 0.85;
//    // full power when X is pressed
//    private static final double FULL_POWER = 1.0;
//
//    public FlyWheels(DcMotor leftFly, DcMotor rightFly) {
//        this.leftFly = leftFly;
//        this.rightFly = rightFly;
//    }
//
//    public void init() {
//        // set directions so same positive power spins the wheels in the shooting direction
//        leftFly.setDirection(DcMotorSimple.Direction.FORWARD);
//        rightFly.setDirection(DcMotorSimple.Direction.REVERSE);
//        leftFly.setPower(0.0);
//        rightFly.setPower(0.0);
//    }
//
//    /**
//     * @param rightBumper  => spin forward at default power while held
//     * @param leftBumper   => spin reverse at default power while held
//     * @param xPressed     => override and run BOTH flywheels at FULL_POWER (1.0) while held
//     * @param overrideY    => (existing Y override) spins them at DEFAULT_POWER while held
//     */
//    public void update(boolean rightBumper, boolean leftBumper, boolean xPressed, boolean overrideY) {
//
//        // Highest-priority: X forces FULL power
//        if (xPressed) {
//            leftFly.setPower(-FULL_POWER);
//            rightFly.setPower(-FULL_POWER);
//            return;
//        }
//
//        // Next priority: global Y override (keeps previous behavior but at DEFAULT_POWER)
//        if (overrideY) {
//            leftFly.setPower(-DEFAULT_POWER);
//            rightFly.setPower(-DEFAULT_POWER);
//            return;
//        }
//
//        // Normal bumper-driven control (existing-style)
//        if (rightBumper) {
//            leftFly.setPower(-DEFAULT_POWER);
//            rightFly.setPower(-DEFAULT_POWER);
//        } else if (leftBumper) {
//            leftFly.setPower(DEFAULT_POWER);
//            rightFly.setPower(DEFAULT_POWER);
//        } else {
//            leftFly.setPower(0.0);
//            rightFly.setPower(0.0);
//        }
//    }
//
//    public void stop() {
//        leftFly.setPower(0.0);
//        rightFly.setPower(0.0);
//    }
//}


package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FlyWheels {

    private DcMotorEx leftFly;
    private DcMotorEx rightFly;


    // default (current) operating power
    private static final double DEFAULT_POWER = 1.0;
    // full power when X is pressed
    private static final double FULL_POWER = 1.0;

    private static final double TICKS_PER_REV = 103.8;

    public static final double TARGET_RPM = 4500; //1320

    private double targetRPM = TARGET_RPM; //1350

    private double maxRPM = 6000;


    private static final double kP = 0.0015;
    private static final double kI = 0.0;
    private static final double kD = 0.0001;
    private double kF = 32767 / 2900;

    private boolean velocityMode = false;

    private double leftMaxVelocity = 0.0;
    private double rightMaxVelocity = 0.0;

    // fallback encoder-delta tracking (used if DcMotorEx velocity is not available)
    private int lastLeftPos = 0;
    private int lastRightPos = 0;
    private long lastTimeMs = 0;
    private final ElapsedTime clock = new ElapsedTime();

    public double highVelocity = 1000;
    public double lowVelocity = 500;

    double currTargetVelocity = highVelocity;

    double F = 1.6;
    double P = 1.0;

    double[] stepSizes = {10.0, 1.0, 0.1, 0.001};

    int stepIndex = 1;


    public FlyWheels(DcMotorEx leftFly, DcMotorEx rightFly) {
        this.leftFly  = leftFly;
        this.rightFly = rightFly;
    }


    public void init() {
        // set directions so same positive power spins the wheels in the shooting direction
        leftFly.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFly.setDirection(DcMotorSimple.Direction.REVERSE);

        // use RUN_USING_ENCODER so the hub returns velocity measurements
        leftFly.setMode(RunMode.RUN_USING_ENCODER);
        rightFly.setMode(RunMode.RUN_USING_ENCODER);

        leftFly.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        rightFly.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        leftFly.setPower(0.0);
        rightFly.setPower(0.0);

        // initialize fallback tracking
        lastLeftPos = leftFly.getCurrentPosition();
        lastRightPos = rightFly.getCurrentPosition();
        lastTimeMs = System.currentTimeMillis();
        clock.reset();

        double maxTicksPerSec = (maxRPM * TICKS_PER_REV) / 60.0;
        kF = 32767.0 / maxTicksPerSec;

//        if (leftFly != null && rightFly != null) {
//            leftFly.setVelocityPIDFCoefficients(kP, kI, kD, kF);
//            rightFly.setVelocityPIDFCoefficients(kP, kI, kD, kF);
//        }
    }

    /**
     * @param rightBumper  => spin forward at default power while held
     * @param leftBumper   => spin reverse at default power while held
     * @param xPressed     => override and run BOTH flywheels at FULL_POWER (1.0) while held
     * @param overrideY    => (existing Y override) spins them at DEFAULT_POWER while held
     */
    public void update(boolean rightBumper, boolean leftBumper, boolean xPressed, boolean overrideY) {

        if (xPressed) {
            motorSpinOut();
            spinTargetRPM(maxRPM);
            return;
        }

        // Next priority: global Y override (keeps previous behavior but at DEFAULT_POWER)
        if (overrideY) {
//            rightFly.setPower(1.0);
//            leftFly.setPower(-1.0);
            motorSpinOut();
            spinTargetRPM(maxRPM);
            return;
        }

//         Normal bumper-driven control (existing-style)
        if (rightBumper) {
            motorSpinOut();
            spinTargetRPM(targetRPM);
        } else if (leftBumper) {
            motorSpinIn();
            spinTargetRPM(targetRPM);
        } else {
            leftFly.setPower(0.0);
            rightFly.setPower(0.0);
        }
    }

    public void motorSpinOut(){
        this.leftFly.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFly.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void motorSpinIn(){
        this.leftFly.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFly.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void spinTargetRPM(double targetRPM){
        double targetTicks = rpmToTicks(targetRPM);

        if (leftFly != null && rightFly != null) {
            leftFly.setVelocity(targetTicks);
            rightFly.setVelocity(targetTicks);
        }
    }
    public void stop() {
        leftFly.setPower(0.0);
        rightFly.setPower(0.0);
    }

    // 0.9 P
// 1.2 F
// close shoooting

//

    /**
     * Publish flywheel telemetry to the driver station.
     * Call this from your OpMode loop before telemetry.update().
     */
    public void publishTelemetry(Telemetry telemetry) {
        // prefer DcMotorEx.getVelocity() if available (returns ticks/sec), else fallback
        double leftTicksPerSec;
        double rightTicksPerSec;

        if (leftFly != null && rightFly != null) {
            // DcMotorEx.getVelocity() returns ticks per second by default
            leftTicksPerSec  = leftFly.getVelocity();
            rightTicksPerSec = rightFly.getVelocity();
        } else {
            // fallback: compute ticks/sec by delta on current position (less smooth, but works)
            long nowMs = System.currentTimeMillis();
            int curLeft = leftFly.getCurrentPosition();
            int curRight = rightFly.getCurrentPosition();

            long dtMs = Math.max(1, nowMs - lastTimeMs); // avoid div0
            double dtSec = dtMs / 1000.0;

            leftTicksPerSec  = (curLeft - lastLeftPos) / dtSec;
            rightTicksPerSec = (curRight - lastRightPos) / dtSec;

            lastLeftPos = curLeft;
            lastRightPos = curRight;
            lastTimeMs = nowMs;
        }

//        // get ticks-per-rev from the configured motor type (works even if unspecified)
//        double leftTicksPerRev  =103.8;
//        double rightTicksPerRev = 103.8;
//
//        // avoid divide-by-zero if something is misconfigured
//        if (leftTicksPerRev <= 0.0) leftTicksPerRev = 1.0;
//        if (rightTicksPerRev <= 0.0) rightTicksPerRev = 1.0;
//
//        // convert to RPM
//        double leftRPM  = (leftTicksPerSec  / leftTicksPerRev)  * 60.0;
//        if (leftRPM > leftMaxVelocity) leftMaxVelocity = leftRPM;
//
//        double rightRPM = (rightTicksPerSec / rightTicksPerRev) * 60.0;
//        if (rightRPM > rightMaxVelocity) rightMaxVelocity = rightRPM;

    }

    private double rpmToTicks(double rpm){
        return (rpm * TICKS_PER_REV) / 60.0;
    }

    public void adjustTargetRPM(double delta) {
        targetRPM += delta;
        targetRPM = Math.max(600, Math.min(1620, targetRPM));
    }

   public void setTargetRPM(double rpm) {
        targetRPM = rpm;
   }

   public void toggleVelocities(){
        if(currTargetVelocity == highVelocity){
            currTargetVelocity = lowVelocity;
        } else {
            currTargetVelocity = highVelocity;
        }
   }

   public void changeStepIndex(){
        stepIndex = (stepIndex + 1) % stepSizes.length;
   }

   public void incrF(){
        F += stepSizes[stepIndex];
   }

   public void decrF(){
        F -= stepSizes[stepIndex];
   }

   public void incrP(){
        P += stepSizes[stepIndex];

   }

   public void decrP(){
        P -= stepSizes[stepIndex];
   }

   public void updateFlywheelChanges(Telemetry telemetry){
       motorSpinOut();

       PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
       leftFly.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
       rightFly.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

       rightFly.setVelocity(currTargetVelocity);
       leftFly.setVelocity(currTargetVelocity);
   }

   public void getVelocityAndError(Telemetry telemetry){
       double currVelocity = (leftFly.getVelocity() + Math.abs(rightFly.getVelocity()))/2;
       telemetry.addData("Current Velocity", String.format("%.3f", currVelocity));
       double error = (currTargetVelocity - currVelocity);
       telemetry.addData("Error", String.format("%.0f", error));
       telemetry.addData("Target Velocity", String.format("%.3f", currTargetVelocity));
       telemetry.addData("Tuning P", String.format("%.3f", P));
       telemetry.addData("Tuning F", String.format("%.3f", F));
       telemetry.addData("Step Size", String.format("%.3f", stepSizes[stepIndex]));
   }

    public void changeHighVelocity(int increment){
        highVelocity += increment;
    }


}

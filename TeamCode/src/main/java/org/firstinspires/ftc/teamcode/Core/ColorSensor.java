package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ColorSensor {

    private final com.qualcomm.robotcore.hardware.DistanceSensor colorSensor;

    private double currentDistance = 0.0;
    private boolean active = true;

    public ColorSensor(HardwareMap hardwareMap, String deviceName) {
        colorSensor = hardwareMap.get(
                com.qualcomm.robotcore.hardware.DistanceSensor.class,
                deviceName
        );
    }

    public void init() {
        active = true;
        currentDistance = 0.0;
    }

    public void update() {
        if (!active) return;

        currentDistance = colorSensor.getDistance(DistanceUnit.INCH);
    }

    public double getDistance() {
        return currentDistance;
    }

    public double getDistanceCM() {
        return colorSensor.getDistance(DistanceUnit.CM);
    }

    public boolean isValid() {
        return currentDistance > 0 && currentDistance < 100;
    }

    public void stop() {
        active = false;
        currentDistance = 0.0;
    }


}


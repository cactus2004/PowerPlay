package org.firstinspires.ftc.teamcode.subsystems.util;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.arcrobotics.ftclib.hardware.motors.Motor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@Config
@TeleOp

public class slideTuner extends OpMode {
    private Motor liftRight;
    private Motor liftLeft;

    private MotorGroup slides;
    private PIDController controller;

    public static double p = 0.0, i = 0.0, d = 0.0;
    public static int target = 0;

    public void init() {

        liftRight = new Motor(hardwareMap, "liftRight", Motor.GoBILDA.RPM_435);
        liftLeft = new Motor(hardwareMap, "liftLeft", Motor.GoBILDA.RPM_435);

        liftRight.setInverted(false); //this might not be needed; or the left slide should be the one being reversed
        liftLeft.setInverted(true);

        liftLeft.setRunMode(Motor.RunMode.RawPower);
        liftRight.setRunMode(Motor.RunMode.RawPower);

        liftRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        controller = new PIDController(p,i,d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        slides = new MotorGroup(liftRight, liftLeft);

    }

    public void loop(){

        int slidePosition = liftRight.getCurrentPosition();

        double pid = controller.calculate(slidePosition, target);

        // 0.1 is the feedforward component needed to stall slides against gravity
        double power = pid + 0.1;

        slides.set(power);

        telemetry.addData("Lift Position", slidePosition);
        telemetry.addData("Target", target);
        telemetry.update();
    }
}

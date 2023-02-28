package org.firstinspires.ftc.teamcode.subsystems.util;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp

public class colorFinder extends OpMode{

    private Servo lusp;
    public static double target = 0;

    public void init(){

        lusp = hardwareMap.get(Servo.class, "lusp");
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    }

    public void loop(){

        lusp.setPosition(target);

        telemetry.addData("Target", target);
        telemetry.update();

    }
}
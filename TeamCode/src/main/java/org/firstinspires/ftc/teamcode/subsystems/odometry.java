package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.Encoder;

public class odometry {


    static final double radius = 19;
    static final double resolution = 8192;
    // TODO Measure trackwidth in inches
    static final double trackWidth = 5.0;
    static final double inPerTick = (2*Math.PI*radius)/(resolution);


    static int currentLeftPosition = 0;
    static int currentRightPosition = 0;
    static int currentFrontPosition = 0;

    static int initLeftPosition = 0;
    static int initRightPosition = 0;
    static int initFrontPosition = 0;

    private Encoder leftEncoder, rightEncoder, frontEncoder;
    private LinearOpMode myOpMode = null;

    public odometry(LinearOpMode opMode){

        myOpMode = opMode;

        leftEncoder = new Encoder(myOpMode.hardwareMap.get(DcMotorEx.class, "liftLeft"));
        rightEncoder = new Encoder(myOpMode.hardwareMap.get(DcMotorEx.class, "rightEncoder"));
        frontEncoder = new Encoder(myOpMode.hardwareMap.get(DcMotorEx.class, "frontEncoder"));

    }



    public static double getAngle(){

        //TODO math for angle
        return 0;

    }

    public void resetAngle(){


    }

}

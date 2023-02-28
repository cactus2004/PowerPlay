package org.firstinspires.ftc.teamcode.subsystems;


import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.hardware.motors.Motor;

public class delivery {

    private LinearOpMode myOpMode = null;

    public delivery(LinearOpMode opMode) {
        myOpMode = opMode;

        double p = 0.2;
        double i = 0.0;
        double d = 0.005;

        liftRight = myOpMode.hardwareMap.get(DcMotorEx.class,"liftRight");
        liftLeft = myOpMode.hardwareMap.get(DcMotorEx.class, "liftLeft");

        liftRight.setDirection(DcMotorSimple.Direction.FORWARD); //this might not be needed; or the left slide should be the one being reversed
        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        controller = new PIDController(p, i, d);
        reset();

    }

    private DcMotor liftRight;
    private DcMotor liftLeft;

    private PIDController controller;

    public void extend(double liftSpeed) {

        liftRight.setPower(Math.abs(liftSpeed));
        liftLeft.setPower(Math.abs(liftSpeed));

    }

    public void retract(double slideSpeed) {

        liftRight.setPower(-Math.abs(slideSpeed));
        liftLeft.setPower(-Math.abs(slideSpeed));

    }


    public void stall() {

        liftRight.setPower(0.1);
        liftLeft.setPower(0.1);

    }

    public void moveToStage(String stage) {

        int slidePosition = liftRight.getCurrentPosition();

        int Position[] = {-75, 1000, 1750, 2500};
        String Stage[] = {"GROUND", "LOW", "MID", "HIGH"};

        int stageIndex = Byte.MAX_VALUE;
        int res = 0;


        for (int i = 0; i < Stage.length; i++) {

            if (Stage[i].equals(stage)) {
                stageIndex = i;
                break;
            }
        }

        if (stageIndex != Byte.MAX_VALUE) {

            res = Position[stageIndex];
            double pid = controller.calculate(slidePosition, res);

            int error = res - slidePosition;

            double power = pid + 0.1;

            if (Math.abs(error) > 100) {

                liftRight.setPower(power);
                liftLeft.setPower(power);

            } else {

                stall();

            }

            myOpMode.telemetry.addData("error", error);
            myOpMode.telemetry.addData("target", res);
            myOpMode.telemetry.addData("slidePosition", slidePosition);
            myOpMode.telemetry.update();

        }
    }

    public void reset(){

        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }
}
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import org.firstinspires.ftc.teamcode.subsystems.drive;
import org.firstinspires.ftc.teamcode.subsystems.delivery;
import org.firstinspires.ftc.teamcode.subsystems.collection;

@TeleOp

public class teleopBlue extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private ColorSensor colorSensor;
    private ColorSensor colorSensor2;

    String stage = "GROUND";

    public void runOpMode() throws InterruptedException {

        drive drive = new drive(this);
        collection momo = new collection(this);
        delivery slides = new delivery(this);

        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        colorSensor2 = hardwareMap.colorSensor.get("colorSensor2");

        int i = 0;
        int s = 0;

        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {


            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            double pFrontRight = (y + x + rx) / denominator;
            double pBackRight = (y - x + rx) / denominator;
            double pFrontLeft = (y - x - rx) / denominator;
            double pBackLeft = (y + x - rx) / denominator;

            final double fallSpeed = 0.8;
            final double liftSpeed = 1;

            if (gamepad1.left_bumper) {
                drive.drive(pFrontRight - 0.5, pBackRight + 0.5,
                        pFrontLeft + 0.5, pBackLeft - 0.5);
            } else if (gamepad1.right_bumper) {
                drive.drive(pFrontRight + 0.5, pBackRight - 0.5,
                        pFrontLeft - 0.5, pBackLeft + 0.5);
            } else {
                drive.drive(pFrontRight, pBackRight, pFrontLeft, pBackLeft);
            }


            if (gamepad2.left_trigger > 0.1) {
                momo.openClaw();
            } else if (gamepad2.right_trigger > 0.1) {
                momo.closeClaw();
            }

            if (gamepad2.cross) {
                stage = "GROUND";
                s = 1;

            } else if (gamepad2.triangle) {
                stage = "MID";
                s = 1;

            } else if (gamepad2.circle) {
                stage = "HIGH";
                s = 1;

            }
            else if (gamepad2.square){
                stage = "LOW";
                s = 1;
            }

            if(gamepad2.dpad_up){
                slides.extend(liftSpeed);
                s = 0;

            }
            else if (gamepad2.dpad_down){
                slides.retract(fallSpeed);
                s = 0;

            }
            else if(s==1){
                slides.moveToStage(stage);
            }

            else{
                slides.stall();
            }


            if (runtime.seconds() > 80 && runtime.seconds() < 90) {

                momo.flash("yellow","purple", runtime);

            } else {


                if (((DistanceSensor) colorSensor).getDistance(DistanceUnit.MM) < 35 ^
                        ((DistanceSensor) colorSensor2).getDistance(DistanceUnit.MM) < 52) {

                    momo.setColor("yellow");

                    if (i == 0){

                        gamepad1.rumble(500);
                        gamepad2.rumble(500);
                        ++i;

                    }


                }
                else if (((DistanceSensor) colorSensor).getDistance(DistanceUnit.MM) < 35 &&
                        ((DistanceSensor) colorSensor2).getDistance(DistanceUnit.MM) < 52){

                    momo.setColor("green");
                    if(i==1){

                        gamepad1.rumble(500);
                        gamepad2.rumble(500);
                        ++i;

                    }
                }



                else {

                    momo.setColor("dark blue");
                    i = 0;

                }
            }

        }
    }
}
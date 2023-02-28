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

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

@TeleOp

public class fieldCentric extends LinearOpMode {

    BNO055IMU imu;


    private ElapsedTime runtime = new ElapsedTime();
    private ColorSensor colorSensor;
    private ColorSensor colorSensor2;


    String stage = "GROUND";

    public void runOpMode() throws InterruptedException {

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        drive drive = new drive(this);
        collection momo = new collection(this);
        delivery slides = new delivery(this);

        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        colorSensor2 = hardwareMap.colorSensor.get("colorSensor2");

        byte i = 0;
        byte q = 0;

        boolean pidOn = true;

        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {

            double angle = Math.toRadians(imu.getAngularOrientation().firstAngle);

            telemetry.addData("angle", Math.toDegrees(angle));
            telemetry.update();

            if(gamepad1.options && q==0){
                imu.initialize(parameters);
            }


            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

            double rotx = x*Math.cos(-angle) - y*Math.sin(-angle);
            double roty = x*Math.sin(-angle) + y*Math.cos(-angle);

            double denominator = Math.max(Math.abs(roty) + Math.abs(rotx) + Math.abs(rx), 1);

            double pFrontRight = (roty + rotx + rx) / denominator;
            double pBackRight = (roty - rotx + rx) / denominator;
            double pFrontLeft = (roty - rotx - rx) / denominator;
            double pBackLeft = (roty + rotx - rx) / denominator;

            final double fallSpeed = 0.8;
            final double liftSpeed = 1;



            drive.drive(pFrontRight, pBackRight, pFrontLeft, pBackLeft);



            if (gamepad1.left_trigger > 0.1) {
                momo.openClaw();
            } else if (gamepad1.right_trigger > 0.1) {
                momo.closeClaw();
            }

            if (gamepad1.cross) {
                stage = "GROUND";
                pidOn = true;

            } else if (gamepad1.triangle) {
                stage = "MID";
                pidOn = true;

            } else if (gamepad1.circle) {
                stage = "HIGH";
                pidOn = true;

            }
            else if (gamepad1.square){
                stage = "LOW";
                pidOn = true;
            }

            if(gamepad1.left_bumper){
                slides.extend(liftSpeed);
                pidOn = false;
            }
            else if (gamepad1.right_bumper){
                slides.retract(fallSpeed);
                pidOn = false;
            }

            else if(pidOn){
                slides.moveToStage(stage);
            }

            else{
                slides.stall();
            }


            if (runtime.seconds() > 1 && runtime.seconds() < 11) {

                momo.flash("yellow","purple", runtime);

            } else {


                if (((DistanceSensor) colorSensor).getDistance(DistanceUnit.MM) < 35 ^
                        ((DistanceSensor) colorSensor2).getDistance(DistanceUnit.MM) < 35) {

                    momo.setColor("yellow");

                    if (i == 0){

                        gamepad1.rumble(500);
                        gamepad2.rumble(400);
                        ++i;

                    }


                }
                else if (((DistanceSensor) colorSensor).getDistance(DistanceUnit.MM) < 35 &&
                        ((DistanceSensor) colorSensor2).getDistance(DistanceUnit.MM) < 35){

                    momo.setColor("green");
                    if(i==1){

                        gamepad1.rumble(500);
                        gamepad2.rumble(500);
                        ++i;

                    }
                }

                else {

                    momo.setColor("pink");
                    i = 0;

                }
            }

        }
    }
}
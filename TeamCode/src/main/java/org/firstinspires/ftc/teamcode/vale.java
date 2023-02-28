
package org.firstinspires.ftc.teamcode;

//import com.qualcomm.hardware.bosch.BNO055IM77;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

@TeleOp
@Disabled

public class vale extends LinearOpMode {


    private ColorSensor colorSensor;
    private DcMotor frontleft;
    private DcMotor frontright;
    private DcMotor backleft;
    private DcMotor backright;

    private DcMotorEx intake;
    private DcMotor carousel;
    private DcMotor carousel2;
    private DcMotorEx linearSlide;
    private Servo claw;
    private Servo capstone;
    private Servo SidePlates;
    private Servo OuttakeServo;


    private DigitalChannel redLED;
    private DigitalChannel greenLED;
    private DigitalChannel redLED2;
    private DigitalChannel greenLED2;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {

        frontleft = hardwareMap.dcMotor.get("frontleft");
        frontright = hardwareMap.dcMotor.get("frontright");
        backleft = hardwareMap.dcMotor.get("backleft");
        backright = hardwareMap.dcMotor.get("backright");

        colorSensor = hardwareMap.colorSensor.get("colorSensor");

        frontleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        redLED = hardwareMap.get(DigitalChannel.class, "red1");
        greenLED = hardwareMap.get(DigitalChannel.class, "green1");
        redLED2 = hardwareMap.get(DigitalChannel.class, "red2");
        greenLED2 = hardwareMap.get(DigitalChannel.class, "green2");

        intake = (DcMotorEx) hardwareMap.dcMotor.get("intake");
        carousel = hardwareMap.dcMotor.get("carousel");
        carousel2 = hardwareMap.dcMotor.get("carousel2");
        linearSlide = (DcMotorEx) hardwareMap.dcMotor.get("linearSlide");

        linearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        claw = hardwareMap.servo.get("claw");
        claw.setPosition(.85);
        capstone = hardwareMap.servo.get("capstone");
        capstone.setPosition(1);
        SidePlates = hardwareMap.servo.get("Side plates");
        SidePlates.setPosition(1);
        OuttakeServo= hardwareMap.servo.get("OuttakeServo");
        OuttakeServo.setPosition(0);


        waitForStart();
        SidePlates.setPosition(.5);
        capstone.setPosition(.80);
        redLED.setMode(DigitalChannel.Mode.OUTPUT);
        greenLED.setMode(DigitalChannel.Mode.OUTPUT);
        redLED2.setMode(DigitalChannel.Mode.OUTPUT);
        greenLED2.setMode(DigitalChannel.Mode.OUTPUT);

        while (!isStopRequested()) {


            if (colorSensor instanceof DistanceSensor) {
                telemetry.addData("Distance (cm)", "%.3f", ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM));

                if (((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM) < 4) {
                    telemetry.addLine("Freight Detected");
                    greenLED.setState(false);
                    redLED.setState(true);
                    greenLED2.setState(false);
                    redLED2.setState(true);
                } else {
                    telemetry.addLine("No Freight Detected");
                    redLED.setState(false);
                    greenLED.setState(true);
                    redLED2.setState(false);
                    greenLED2.setState(true);

                }
            }


            telemetry.update();


            if (gamepad1.right_bumper) {
                frontleft.setPower(.7);
                backright.setPower(-.7);
                backleft.setPower(-.7);
                frontright.setPower(.7);
            } else if (gamepad1.left_bumper) {
                frontleft.setPower(-.7);
                backright.setPower(.7);
                backleft.setPower(.7);
                frontright.setPower(-.7);
            } else {
                backleft.setPower(-gamepad1.left_stick_y * .7);
                backright.setPower(gamepad1.right_stick_y * .7);
                frontleft.setPower(-gamepad1.left_stick_y * .7);
                frontright.setPower(gamepad1.right_stick_y * .7);
            }

            if (gamepad2.dpad_up) {
                linearSlide.setVelocity(400000);
            } else {
                linearSlide.setVelocity(0);
            }

            if (gamepad2.dpad_down) {
                linearSlide.setVelocity(-40000);
            } else {
                linearSlide.setVelocity(0);
            }

            if (gamepad2.left_bumper) {
                carousel.setPower(.43);
                carousel2.setPower(-.43);
            } else {
                carousel.setPower(0);
                carousel2.setPower(0);
            }
            if (gamepad2.left_trigger > .1) {
                carousel.setPower(-.35);
                carousel2.setPower(.35);
            } else {
                carousel.setPower(0);
                carousel2.setPower(0);
            }

            if (gamepad1.dpad_up) {
                claw.setPosition(.5);
            } else if (gamepad1.dpad_down) {
                claw.setPosition(.85);
            } else if (gamepad1.dpad_left) {
                claw.setPosition(0);
            }

            /*if (gamepad2.right_bumper) {
                intake.setPower(1);
            }else {
                intake.setPower(0);

            }
            if (gamepad2.right_trigger > .1) {
                intake.setPower(-1);
            } else {
                intake.setPower(0);
            }*/


            if (gamepad2.right_bumper) {
                intake.setVelocity(5000);
            } else {
                intake.setVelocity(0);
            }
            if (gamepad2.right_trigger > .1) {
                intake.setVelocity(-5000);
            } else {
                intake.setVelocity(0);
            }

            if (gamepad2.a) {
                capstone.setPosition(.75);
            } else if (gamepad2.b) {
                capstone.setPosition(.62);
            } else {
                capstone.setPosition(.97);


                if (runtime.seconds() > 80 && runtime.seconds() < 90) {

                    for (int i =0; i > 10; i++) {
                        SidePlates.setPosition(.67);
                        sleep(1000);
                        SidePlates.setPosition(.76);
                        sleep(1000);
                    }
                } else {
                    SidePlates.setPosition(.71);
                }


                if (runtime.seconds() > 80 && runtime.seconds() < 90) {
                    if (runtime.seconds() % 2 == 0){
                        SidePlates.setPosition(.67);
                    } else {
                        SidePlates.setPosition(.76);
                    }
                }

                telemetry.addData("Elapsed Time", runtime.seconds());
                telemetry.update();


            }

        }

    }
}


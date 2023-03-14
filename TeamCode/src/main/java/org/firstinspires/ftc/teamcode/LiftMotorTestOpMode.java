package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class LiftMotorTestOpMode extends LinearOpMode {

    public void runOpMode() {
        DcMotor liftRight = hardwareMap.get(DcMotor.class, "liftRight");
        DcMotor liftLeft = hardwareMap.get(DcMotor.class, "liftLeft");

        waitForStart();

        liftRight.setPower(gamepad1.left_stick_y);
        liftLeft.setPower(-gamepad1.left_stick_y);
    }
}
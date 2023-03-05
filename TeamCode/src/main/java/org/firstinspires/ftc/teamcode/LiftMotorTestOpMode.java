package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class LiftMotorTestOpMode extends LinearOpMode {
    public void runOpMode() {
        DcMotorEx liftRight = hardwareMap.get(DcMotorEx.class, "liftRight");
        DcMotorEx liftLeft = hardwareMap.get(DcMotorEx.class, "liftLeft");

        waitForStart();

        liftRight.setPower(gamepad1.left_stick_y);
        liftLeft.setPower(-gamepad1.left_stick_y);
    }
}
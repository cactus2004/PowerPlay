package org.firstinspires.ftc.teamcode.subsystems.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.util.Encoder;

@TeleOp
public class odoTest extends LinearOpMode {

    private Encoder leftEncoder, rightEncoder, frontEncoder;

    public void runOpMode() {

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "liftLeft"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rightEncoder"));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "frontEncoder"));

        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {


            int leftPos = leftEncoder.getCurrentPosition();
            int rightPos = rightEncoder.getCurrentPosition();
            int frontPos = frontEncoder.getCurrentPosition();

            telemetry.addData("left", leftPos);
            telemetry.addData("right", rightPos);
            telemetry.addData("front", frontPos);


        }
    }
}

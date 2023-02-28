package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class collection {

    private LinearOpMode myOpMode = null;

    public collection(LinearOpMode opMode) {

        myOpMode = opMode;


        lusp = myOpMode.hardwareMap.get(Servo.class, "lusp");
        clawRight = myOpMode.hardwareMap.get(Servo.class, "clawRight");
        clawLeft = myOpMode.hardwareMap.get(Servo.class, "clawLeft");

        clawLeft.setDirection(Servo.Direction.REVERSE);

        clawRight.setPosition(0);
        clawLeft.setPosition(0);

    }

    private Servo clawRight;
    private Servo clawLeft;
    private Servo lusp;



    public void openClaw(){

        clawRight.setPosition(0);
        clawLeft.setPosition(0);

    }

    public void closeClaw(){
        clawRight.setPosition(1);
        clawLeft.setPosition(1);
    }

    public void setColor(String Color) {

        String color[] = {"green", "red", "rainbow", "blue", "purple", "dark blue", "pink", "white", "yellow"};
        double colorID[] = {0.71, 0.67, 0.22, 0.65, 0.75, 0.73, 0.66, 0.77, 0.69};

        int colorIndex = Byte.MAX_VALUE;
        double res = 0;

        for (int i = 0; i < color.length; i++) {

            if (color[i].equals(Color)) {
                colorIndex = i;
                break;
            }
        }

        if(colorIndex != Byte.MAX_VALUE){

            res = colorID[colorIndex];
            lusp.setPosition(res);

        }
    }

    public void flash(String color1, String color2, ElapsedTime runTime) {

        if((int)runTime.seconds() %2 != 0){
            setColor(color1);

        }else{

            setColor(color2);
        }

    }

    public void clawTest(){

        if(myOpMode.gamepad2.cross) {

            clawRight.setPosition(clawRight.getPosition() + 0.01);
            clawLeft.setPosition(clawLeft.getPosition() + 0.01);

            myOpMode.telemetry.addData("<CR:", clawRight.getPosition());
            myOpMode.telemetry.addData("<CL:", clawLeft.getPosition());

            myOpMode.telemetry.update();
        }

        else if (myOpMode.gamepad2.triangle){

            clawRight.setPosition(clawRight.getPosition() - 0.01);
            clawLeft.setPosition(clawLeft.getPosition() - 0.01);

            myOpMode.telemetry.addData("<CR:", clawRight.getPosition());
            myOpMode.telemetry.addData("<CL:", clawLeft.getPosition());

            myOpMode.telemetry.update();
        }

        else{
            myOpMode.telemetry.addData("<CR:", clawRight.getPosition());
            myOpMode.telemetry.addData("<CL:", clawLeft.getPosition());

            myOpMode.telemetry.update();
        }
    }

}
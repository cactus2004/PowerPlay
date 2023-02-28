package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.bosch.BNO055IMU;


public class drive {

    private LinearOpMode myOpMode = null;

    private DcMotorEx driveFrontRight;
    private DcMotorEx driveBackRight;
    private DcMotorEx driveFrontLeft;
    private DcMotorEx driveBackLeft;

    private BNO055IMU imu;

    private ElapsedTime runtime = new ElapsedTime();


    static final double     motorTicks = 384.5;
    static final double     driveGearReduction = 1.0 ;
    static final double     wheelDiameter = 96/25.4 ;
    static final double     countsPerInch = (motorTicks * driveGearReduction) / (wheelDiameter * Math.PI);

    static final int         rest = 100;

    public drive (LinearOpMode opmode) {
        myOpMode = opmode;

        driveFrontRight  = myOpMode.hardwareMap.get(DcMotorEx.class, "frontRight");
        driveBackRight = myOpMode.hardwareMap.get(DcMotorEx.class, "backRight");
        driveFrontLeft  = myOpMode.hardwareMap.get(DcMotorEx.class, "frontLeft");
        driveBackLeft = myOpMode.hardwareMap.get(DcMotorEx.class, "backLeft");


        driveFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        driveBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        driveFrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        driveBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);


        driveFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        driveFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        driveFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        myOpMode.telemetry.addData(">", "we cookin");
        myOpMode.telemetry.update();
    }


    public void moveToPosition(double speed, double distanceVertical, double distanceHorizontal, double timeout) {

        /*
         creates new variables to be used for each of the wheels target position, it is important for each wheel to have its own target position
         as the moveToPosition method is used to move straight up/down, strafe left/right and diagonally at any angle all in one method.
        */

        int newTargetFrontRight;
        int newTargetBackRight;
        int newTargetFrontLeft;
        int newTargetBackLeft;


        // uses the pythagorean theorem to calculate the total distance the robot actually travels instead of its horizontal/vertical distance vectors
        double resultant = Math.pow (Math.pow(distanceHorizontal, 2 )  + Math.pow ( distanceVertical , 2 ), 0.5 );

        // typecasts the total distance from a double to an integer to that it can be used as a target distance later (which only takes integers for its parameter)
        int distanceTotal = (int) Math.round(resultant);


        /*
         uses inverse the inverse tangent function using vertical distance and horizontal distance parameters to find the angle of the trajectory

         also uses atan2 instead of atan since atan has a domain of [0,pi/2] and atan2 has a domain of [0,2pi], which is more useful for this application where
         we are trying to move diagonally.

         */

        double angle = Math.atan2(distanceVertical, distanceHorizontal);

        /*
        sin and cos are trig functions that will always return a double between [0,1] and represent the vertical/ horizontal components of an angle
        similar math is used in the teleop, but instead of sines and cosines, its the sticks x value and y value that are passed to determine power
         */


        double powerFrontRight = Math.sin(angle) - Math.cos(angle);
        double powerBackRight = Math.sin(angle) + Math.cos(angle);
        double powerFrontLeft = Math.sin(angle) + Math.cos(angle);
        double powerBackLeft = Math.sin(angle) - Math.cos(angle);





        if (myOpMode.opModeIsActive()){

            // this sets the target position of each motor AND its direction that it will travel using the RUN_TO_POSITION method later on
            // Math.signum is used to find the sign (either + or -) of each motors power

            newTargetFrontRight = driveFrontRight.getCurrentPosition() + (int)(distanceTotal * countsPerInch * Math.signum(powerFrontRight));
            newTargetBackRight = driveBackRight.getCurrentPosition() + (int)(distanceTotal * countsPerInch * Math.signum(powerBackRight));
            newTargetFrontLeft = driveFrontLeft.getCurrentPosition() + (int)(distanceTotal * countsPerInch * Math.signum(powerFrontLeft));
            newTargetBackLeft = driveBackLeft.getCurrentPosition() + (int)(distanceTotal * countsPerInch * Math.signum(powerBackLeft));

            driveFrontRight.setTargetPosition(newTargetFrontRight);
            driveBackRight.setTargetPosition(newTargetBackRight);

            driveFrontLeft.setTargetPosition(newTargetFrontLeft);
            driveBackLeft.setTargetPosition(newTargetBackLeft);


            /* The RUN_TO_POSITION method will have the motors rotate in whatever direction it takes to get to its target position, so a negative target position
            will have the motors spin in the negative direction and a positive target position, so its important to use Math.signum when setting up the
            target position so the motors know whether to apply negative or positive power to get to their target position.
             */

            driveFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();

            driveFrontRight.setPower(Math.abs(powerFrontRight * speed));
            driveBackRight.setPower(Math.abs(powerBackRight * speed));
            driveFrontLeft.setPower(Math.abs(powerFrontLeft * speed));
            driveBackLeft.setPower(Math.abs(powerBackLeft * speed));


            // this while loop just prevents the code from progressing while the motors complete the RUN_TO_POSITION method
            while (myOpMode.opModeIsActive() &&
                    (runtime.seconds() < timeout) &&
                    (   driveFrontRight.isBusy() && driveBackRight.isBusy()
                            && driveFrontLeft.isBusy() && driveBackLeft.isBusy() )) {}

            driveFrontRight.setPower(0);
            driveBackRight.setPower(0);
            driveFrontLeft.setPower(0);
            driveBackLeft.setPower(0);

            driveFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            driveFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            myOpMode.sleep(rest);

        }
    }

    public void drive(double frontRightPower,double backRightPower, double frontLeftPower, double backLeftPower){

        driveFrontRight.setPower(frontRightPower);
        driveBackRight.setPower(backRightPower);
        driveFrontLeft.setPower(frontLeftPower);
        driveBackLeft.setPower(backLeftPower);

    }
    public void turnToRight(double speed, double distanceVertical, double distanceHorizontal, double timeout) {

        /*
         creates new variables to be used for each of the wheels target position, it is important for each wheel to have its own target position
         as the moveToPosition method is used to move straight up/down, strafe left/right and diagonally at any angle all in one method.
        */


        int newTargetFrontRight;
        int newTargetBackRight;
        int newTargetFrontLeft;
        int newTargetBackLeft;


        // uses the pythagorean theorem to calculate the total distance the robot actually travels instead of its horizontal/vertical distance vectors
        double resultant = Math.pow (Math.pow(distanceHorizontal, 2 )  + Math.pow ( distanceVertical , 2 ), 0.5 );

        // typecasts the total distance from a double to an integer to that it can be used as a target distance later (which only takes integers for its parameter)
        int distanceTotal = (int) Math.round(resultant);


        /*
         uses inverse the inverse tangent function using vertical distance and horizontal distance parameters to find the angle of the trajectory

         also uses atan2 instead of atan since atan has a domain of [0,pi/2] and atan2 has a domain of [0,2pi], which is more useful for this application where
         we are trying to move diagonally.

         */

        double angle = Math.atan2(distanceVertical, distanceHorizontal);

        /*
        sin and cos are trig functions that will always return a double between [0,1] and represent the vertical/ horizontal components of an angle
        similar math is used in the teleop, but instead of sines and cosines, its the sticks x value and y value that are passed to determine power
         */


        double powerFrontRight = Math.sin(angle) - Math.cos(angle);
        double powerBackRight = Math.sin(angle) + Math.cos(angle);
        double powerFrontLeft = Math.sin(angle) + Math.cos(angle);
        double powerBackLeft = Math.sin(angle) - Math.cos(angle);




        if (myOpMode.opModeIsActive()){

            // this sets the target position of each motor AND its direction that it will travel using the RUN_TO_POSITION method later on
            // Math.signum is used to find the sign (either + or -) of each motors power

            newTargetFrontRight = driveFrontRight.getCurrentPosition() + (int)(distanceTotal * countsPerInch);
            newTargetBackRight = driveBackRight.getCurrentPosition() + (int)(distanceTotal * countsPerInch);
            newTargetFrontLeft = driveFrontLeft.getCurrentPosition() + (int)(distanceTotal * countsPerInch);
            newTargetBackLeft = driveBackLeft.getCurrentPosition() + (int)(distanceTotal * countsPerInch);


            driveFrontRight.setTargetPosition(newTargetFrontRight);
            driveBackRight.setTargetPosition(newTargetBackRight);

            driveFrontLeft.setTargetPosition(-newTargetFrontLeft);
            driveBackLeft.setTargetPosition(-newTargetBackLeft);


            /* The RUN_TO_POSITION method will have the motors rotate in whatever direction it takes to get to its target position, so a negative target position
            will have the motors spin in the negative direction and a positive target position, so its important to use Math.signum when setting up the
            target position so the motors know whether to apply negative or positive power to get to their target position.
             */

            driveFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();

            driveFrontRight.setPower(Math.abs(powerFrontRight * speed));
            driveBackRight.setPower(Math.abs(powerBackRight * speed));
            driveFrontLeft.setPower(Math.abs(powerFrontLeft * speed));
            driveBackLeft.setPower(Math.abs(powerBackLeft * speed));


            // this while loop just prevents the code from progressing while the motors complete the RUN_TO_POSITION method
            while (myOpMode.opModeIsActive() &&
                    (runtime.seconds() < timeout) &&
                    (   driveFrontRight.isBusy() && driveBackRight.isBusy()
                            && driveFrontLeft.isBusy() && driveBackLeft.isBusy() )) {}

            driveFrontRight.setPower(0);
            driveBackRight.setPower(0);
            driveFrontLeft.setPower(0);
            driveBackLeft.setPower(0);

            driveFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            driveFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            myOpMode.sleep(rest);
        }
    } public void turnToPosition(double speed, double distanceVertical, double distanceHorizontal, double timeout) {

        /*
         creates new variables to be used for each of the wheels target position, it is important for each wheel to have its own target position
         as the moveToPosition method is used to move straight up/down, strafe left/right and diagonally at any angle all in one method.
        */


        int newTargetFrontRight;
        int newTargetBackRight;
        int newTargetFrontLeft;
        int newTargetBackLeft;


        // uses the pythagorean theorem to calculate the total distance the robot actually travels instead of its horizontal/vertical distance vectors
        double resultant = Math.pow (Math.pow(distanceHorizontal, 2 )  + Math.pow ( distanceVertical , 2 ), 0.5 );

        // typecasts the total distance from a double to an integer to that it can be used as a target distance later (which only takes integers for its parameter)
        int distanceTotal = (int) Math.round(resultant);


        /*
         uses inverse the inverse tangent function using vertical distance and horizontal distance parameters to find the angle of the trajectory

         also uses atan2 instead of atan since atan has a domain of [0,pi/2] and atan2 has a domain of [0,2pi], which is more useful for this application where
         we are trying to move diagonally.

         */

        double angle = Math.atan2(distanceVertical, distanceHorizontal);

        /*
        sin and cos are trig functions that will always return a double between [0,1] and represent the vertical/ horizontal components of an angle
        similar math is used in the teleop, but instead of sines and cosines, its the sticks x value and y value that are passed to determine power
         */


        double powerFrontRight = Math.sin(angle) - Math.cos(angle);
        double powerBackRight = Math.sin(angle) + Math.cos(angle);
        double powerFrontLeft = Math.sin(angle) + Math.cos(angle);
        double powerBackLeft = Math.sin(angle) - Math.cos(angle);




        if (myOpMode.opModeIsActive()){

            // this sets the target position of each motor AND its direction that it will travel using the RUN_TO_POSITION method later on
            // Math.signum is used to find the sign (either + or -) of each motors power

            newTargetFrontRight = driveFrontRight.getCurrentPosition() + (int)(distanceTotal * countsPerInch);
            newTargetBackRight = driveBackRight.getCurrentPosition() + (int)(distanceTotal * countsPerInch);
            newTargetFrontLeft = driveFrontLeft.getCurrentPosition() + (int)(distanceTotal * countsPerInch);
            newTargetBackLeft = driveBackLeft.getCurrentPosition() + (int)(distanceTotal * countsPerInch);


            driveFrontRight.setTargetPosition(-newTargetFrontRight);
            driveBackRight.setTargetPosition(-newTargetBackRight);

            driveFrontLeft.setTargetPosition(newTargetFrontLeft);
            driveBackLeft.setTargetPosition(newTargetBackLeft);


            /* The RUN_TO_POSITION method will have the motors rotate in whatever direction it takes to get to its target position, so a negative target position
            will have the motors spin in the negative direction and a positive target position, so its important to use Math.signum when setting up the
            target position so the motors know whether to apply negative or positive power to get to their target position.
             */

            driveFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();

            driveFrontRight.setPower(Math.abs(powerFrontRight * speed));
            driveBackRight.setPower(Math.abs(powerBackRight * speed));
            driveFrontLeft.setPower(Math.abs(powerFrontLeft * speed));
            driveBackLeft.setPower(Math.abs(powerBackLeft * speed));


            // this while loop just prevents the code from progressing while the motors complete the RUN_TO_POSITION method
            while (myOpMode.opModeIsActive() &&
                    (runtime.seconds() < timeout) &&
                    (   driveFrontRight.isBusy() && driveBackRight.isBusy()
                            && driveFrontLeft.isBusy() && driveBackLeft.isBusy() )) {}

            driveFrontRight.setPower(0);
            driveBackRight.setPower(0);
            driveFrontLeft.setPower(0);
            driveBackLeft.setPower(0);

            driveFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            driveFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            myOpMode.sleep(rest);
        }
    }

}
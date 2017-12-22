package org.firstinspires.ftc.teamcode;

/**
 * Team: Dark Matters #10338
 * Velocity Vortex
 * Hardware definitions
 * 7 dc motors - 4 drive, 2 picthing machine, 1 elevator
 * 1 servo
 * color sensor
 * range finder
 * drive functions
 * encoderDrive
 * driveStick
 * drive
 */


import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


public abstract class Competition_Hardware extends LinearOpMode {
    /* basic hardware for demo */
    public DcMotor motor1 = null;
    public DcMotor motor2 = null;
    public DcMotor motor3 = null;
    public DcMotor motor4 = null;
    public DcMotor pMotor1 = null;
    public DcMotor pMotor2 = null;
    public DcMotor beMotor = null;
    public DcMotor fkMotor = null;

    double speed;
    double angle;

    public ElapsedTime runtime = new ElapsedTime();

    public VuforiaLocalizer vuforiaLocalizer;
    public VuforiaLocalizer.Parameters parameters;
    public VuforiaTrackables visionTargets;
    public VuforiaTrackable target;
    public VuforiaTrackable vuforiaWheels;
    public VuforiaTrackable vuforiaTools;
    public VuforiaTrackable vuforiaLegos;
    public VuforiaTrackable vuforiaGears;

    public VuforiaTrackableDefaultListener listener;

    public OpenGLMatrix lastKnownLocation;
    public OpenGLMatrix phoneLocation;
    public OpenGLMatrix latestLocation;
    public float[] coordinates;

    public static final String VUFORIA_KEY = "AZNyeJT/////AAAAGcEyNak4ykAkhL+InR+WdKUGDQVzF/FELSuZi1yDVXXgcq8IBY9YUrq/i8CblYxOVZ1f8p3FSqUGHisyj6X2Z/fzTkrhRxyigB1hzK2ua8R5PtjFMrb5bruaTXH0rPs59nmx7OPKDr3rrp74XAKU2Twxt+wRaGCssmWtpwUC2Fk6xz9CRkejMEPhenzNpjd/z4tiQRDAe37LEfpJvos/6QVLZZkamkozBN9gdR8+6JLthq3HL22qwlX21RIbwlJmMoi41qhzcaeyFHk0CamDUHgxVcB1VC5i8Hin3f7Y/EPGGALbPpb4AJUhx2nddSQQVI3nDNoNIhHP5sBJ0OG9WPy5dTvDNGaqK7LQfjbyze2x"; // Insert your own key here

    public float robotX = 0;
    public float robotY = 0;
    public float robotAngle = 0;







    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.4;
    static final double TURN_SPEED = 0.5;
    static final double HEADING_THRESHOLD = 1;      // As tight as we can make it with an integer gyro
    static final double P_TURN_COEFF = 0.1;     // Larger is more responsive, but also less stable
    static final double P_DRIVE_COEFF = 0.01;     // Larger is more responsive, but also less stable


    final double SPIN_SPEED = 1;
    final double ELEVATOR_SPEED = .75;
    final double FORKLIFT_SPEED = .3;



    int newLeftTarget;
    int newRightTarget;
    int moveCounts;
    double max;
    double error;
    double steer;
    double leftSpeed;
    double rightSpeed;


    //public Servo servo1 = null;
    //public Servo servo2 = null;

    // Not used anymore
    ModernRoboticsI2cGyro gyro = null;

    OpticalDistanceSensor ods = null;
    double odsReading;

    OpticalDistanceSensor ods2 = null;
    double ods2Reading;

    public String teamColor;

    ColorSensor colorSensor;

    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();
    ModernRoboticsI2cRangeSensor rangeSensor;


    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        try {
            // Save reference to Hardware map
            hwMap = ahwMap;

            // Define and Initialize Motors
            motor1 = hwMap.dcMotor.get("motor1");
            motor2 = hwMap.dcMotor.get("motor2");
            motor3 = hwMap.dcMotor.get("motor3");
            motor4 = hwMap.dcMotor.get("motor4");

            pMotor1 = hwMap.dcMotor.get("pMotor1");
            pMotor2 = hwMap.dcMotor.get("pMotor2");
            beMotor = hwMap.dcMotor.get("beMotor");
            fkMotor = hwMap.dcMotor.get("fkMotor");


            // Set to FORWARD
            motor1.setDirection(DcMotor.Direction.FORWARD);
            motor2.setDirection(DcMotor.Direction.FORWARD);
            motor3.setDirection(DcMotor.Direction.FORWARD);
            motor4.setDirection(DcMotor.Direction.FORWARD);

            pMotor1.setDirection(DcMotor.Direction.FORWARD);
            pMotor2.setDirection(DcMotor.Direction.FORWARD);
            beMotor.setDirection(DcMotor.Direction.FORWARD);
            fkMotor.setDirection(DcMotor.Direction.FORWARD);


            // Set all motors to zero power
            motor1.setPower(0);
            motor2.setPower(0);
            motor3.setPower(0);
            motor4.setPower(0);

            pMotor1.setPower(0);
            pMotor2.setPower(0);
            beMotor.setPower(0);
            fkMotor.setPower(0);


            // Set all motors to run without encoders.
            // only use RUN_USING_ENCODERS in autonomous
            motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            pMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            pMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            beMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fkMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
/*
            gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");
            colorSensor = hardwareMap.colorSensor.get("color");
            rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");
            ods = hardwareMap.opticalDistanceSensor.get("ods");
            ods2 = hardwareMap.opticalDistanceSensor.get("ods2");
*/
            // Define and initialize ALL installed servos.
            //servo1 = hwMap.servo.get("servo1");

            runtime.reset();

        } catch (Exception e) {
            telemetry.addData("runOpMode ERROR", e.toString());
            telemetry.update();
        }

    }

    /***
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {

        long remaining = periodMs - (long) period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }

    public void ODSdrive() {
        /*https://ftc-tricks.com/proportional-line-follower/ is the website that we got part of the

        you want to follow the line along edge of white tape

        to do this, you need to get two reflected light values - full white and full gray
        - remember -white is reflecting more light than grey

        Then take 1/2 of the difference and that is the perfect value.  That value is what we will strive for
        the perfect value will be defined outside of this function.  Will want to adjust this at competition.

        The greater value should be full white
        so if current value is greater than perfect value, we move towards grey

        if current value is less than perfect value, we move towards white

        We will only ge going one direction.

         */

        /*
        //so your code will be something like this
        int currentODSLightValue = int ods.getLightDetected()
        if (currentODSLightValue > perfectODSLightValue){
             //give more power to left motors

        }else if (currentODSLightValue < perfectODSLightValue){
            //give more power to right motors
            // reverse speeds from above
        }else{
            // all would be equal
        }

    */


    }
    public void encoderDriveLineUp(double eSpeed) throws InterruptedException {
        try {
            if (opModeIsActive()) {

                resetEncoders("encoder");
                double inches = 25;
                int currentPosMotor1 = 0;
                int currentPosMotor2 = 0;
                int currentPosMotor3 = 0;
                int currentPosMotor4 = 0;

                int targetPosMotor1 = 0;
                int targetPosMotor2 = 0;
                int targetPosMotor3 = 0;
                int targetPosMotor4 = 0;

                // Stop all motion;
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);
                motor4.setPower(0);

                // change directions so encoders are all positive
                //Shows all possible directions and controls.
                if (ods.getRawLightDetected() < .1 && ods2.getRawLightDetected() < .1){
                    return;
                }

                if (ods2.getRawLightDetected() < .1 ){
                  // circle right
                        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
                        motor2.setDirection(DcMotorSimple.Direction.REVERSE);
                        motor3.setDirection(DcMotorSimple.Direction.REVERSE);
                        motor4.setDirection(DcMotorSimple.Direction.REVERSE);


                } else if (ods.getRawLightDetected() < .1){
                   //circel left
                        motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                        motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                        motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                        motor4.setDirection(DcMotorSimple.Direction.FORWARD);

                }else{
                    return;
                }
                //Getting position of each driving motor

                currentPosMotor1 = motor1.getCurrentPosition();
                currentPosMotor2 = motor2.getCurrentPosition();
                currentPosMotor3 = motor3.getCurrentPosition();
                currentPosMotor4 = motor4.getCurrentPosition();

                targetPosMotor1 = currentPosMotor1 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor2 = currentPosMotor2 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor3 = currentPosMotor3 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor4 = currentPosMotor4 + (int) (inches * COUNTS_PER_INCH);

                motor1.setTargetPosition(targetPosMotor1);
                motor2.setTargetPosition(targetPosMotor2);
                motor3.setTargetPosition(targetPosMotor3);
                motor4.setTargetPosition(targetPosMotor4);


                //sets motors needed to make robot go different directions

                motor1.setPower(eSpeed);
                motor2.setPower(eSpeed);
                motor3.setPower(eSpeed);
                motor4.setPower(eSpeed);

                // keep looping while we are still active, and there is time left, and both motors are running.

                runtime.reset();

                while (opModeIsActive()  && (motor1.isBusy() && motor2.isBusy())) {
                    if (ods.getRawLightDetected() > .1 && ods2.getRawLightDetected() > .1){
                        break;
                    }
                    if (ods.getRawLightDetected() < .1 && ods2.getRawLightDetected() < .1){
                        break;
                    }
                    // Display it for the driver.
                    telemetry.addData("Mot 1234", "%5.2f:%5.2f:%5.2f:%5.2f", motor1.getPower(), motor2.getPower(), motor3.getPower(), motor4.getPower());
                    telemetry.update();

                    // Allow time for other processes to run.
                    idle();
                }

                // Stop all motion;
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);
                motor4.setPower(0);

                // reset directions
                motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                motor4.setDirection(DcMotorSimple.Direction.FORWARD);

                // Turn off RUN_TO_POSITION
                motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            }
        } catch (Exception e) {
            telemetry.addData("ERROR", e.toString());
        }
    }

    public void encoderDriveWhiteLine(double eSpeed, String robotDirection) throws InterruptedException {
        try {
            if (opModeIsActive()) {

                resetEncoders("encoder");
                double inches = 25;
                int currentPosMotor1 = 0;
                int currentPosMotor2 = 0;
                int currentPosMotor3 = 0;
                int currentPosMotor4 = 0;

                int targetPosMotor1 = 0;
                int targetPosMotor2 = 0;
                int targetPosMotor3 = 0;
                int targetPosMotor4 = 0;

                // Stop all motion;
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);
                motor4.setPower(0);

                // change directions so encoders are all positive
                //Shows all possible directions and controls.
                if (robotDirection == "up") {
                    motor1.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor4.setDirection(DcMotorSimple.Direction.REVERSE);
                } else if (robotDirection == "down") {
                    motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor2.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor3.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor4.setDirection(DcMotorSimple.Direction.FORWARD);
                } else if (robotDirection == "right") {
                    motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor3.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor4.setDirection(DcMotorSimple.Direction.REVERSE);
                } else if (robotDirection == "left") {
                    motor1.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor2.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor4.setDirection(DcMotorSimple.Direction.FORWARD);
                } else if (robotDirection == "circle right") {
                    motor1.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor2.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor3.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor4.setDirection(DcMotorSimple.Direction.REVERSE);

                } else if (robotDirection == "circle left"){
                    motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor4.setDirection(DcMotorSimple.Direction.FORWARD);

                }
                //Getting position of each driving motor

                currentPosMotor1 = motor1.getCurrentPosition();
                currentPosMotor2 = motor2.getCurrentPosition();
                currentPosMotor3 = motor3.getCurrentPosition();
                currentPosMotor4 = motor4.getCurrentPosition();

                targetPosMotor1 = currentPosMotor1 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor2 = currentPosMotor2 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor3 = currentPosMotor3 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor4 = currentPosMotor4 + (int) (inches * COUNTS_PER_INCH);

                motor1.setTargetPosition(targetPosMotor1);
                motor2.setTargetPosition(targetPosMotor2);
                motor3.setTargetPosition(targetPosMotor3);
                motor4.setTargetPosition(targetPosMotor4);


                //sets motors needed to make robot go different directions

                motor1.setPower(eSpeed);
                motor2.setPower(eSpeed);
                motor3.setPower(eSpeed);
                motor4.setPower(eSpeed);

                // keep looping while we are still active, and there is time left, and both motors are running.

                runtime.reset();

                while (opModeIsActive()  &&
                        (motor1.isBusy() && motor2.isBusy())) {
                    if (ods.getRawLightDetected() > .1 || ods2.getRawLightDetected() > .1){
                        break;
                    }



                    // Display it for the driver.
                    telemetry.addData("Encoder Direction", robotDirection);
                    telemetry.addData("Mot 1234", "%5.2f:%5.2f:%5.2f:%5.2f", motor1.getPower(), motor2.getPower(), motor3.getPower(), motor4.getPower());
                    telemetry.update();

                    // Allow time for other processes to run.
                    idle();
                }

                // Stop all motion;
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);
                motor4.setPower(0);

                // reset directions
                motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                motor4.setDirection(DcMotorSimple.Direction.FORWARD);

                // Turn off RUN_TO_POSITION
                motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            }
        } catch (Exception e) {
            telemetry.addData("ERROR", e.toString());
        }
    }

    public void encoderDriveRange(double eSpeed, String robotDirection,  double rangeDistance) throws InterruptedException {
        try {
            if (opModeIsActive()) {

                resetEncoders("encoder");
                double inches = 25;
                int currentPosMotor1 = 0;
                int currentPosMotor2 = 0;
                int currentPosMotor3 = 0;
                int currentPosMotor4 = 0;

                int targetPosMotor1 = 0;
                int targetPosMotor2 = 0;
                int targetPosMotor3 = 0;
                int targetPosMotor4 = 0;

                // Stop all motion;
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);
                motor4.setPower(0);

                // change directions so encoders are all positive
                //Shows all possible directions and controls.
                if (robotDirection == "up") {
                    motor1.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor4.setDirection(DcMotorSimple.Direction.REVERSE);
                } else if (robotDirection == "down") {
                    motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor2.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor3.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor4.setDirection(DcMotorSimple.Direction.FORWARD);
                } else if (robotDirection == "right") {
                    motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor3.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor4.setDirection(DcMotorSimple.Direction.REVERSE);
                } else if (robotDirection == "left") {
                    motor1.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor2.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor4.setDirection(DcMotorSimple.Direction.FORWARD);
                } else if (robotDirection == "circle right") {
                    motor1.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor2.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor3.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor4.setDirection(DcMotorSimple.Direction.REVERSE);

                } else if (robotDirection == "circle left"){
                    motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor4.setDirection(DcMotorSimple.Direction.FORWARD);

                }
                //Getting position of each driving motor

                currentPosMotor1 = motor1.getCurrentPosition();
                currentPosMotor2 = motor2.getCurrentPosition();
                currentPosMotor3 = motor3.getCurrentPosition();
                currentPosMotor4 = motor4.getCurrentPosition();

                targetPosMotor1 = currentPosMotor1 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor2 = currentPosMotor2 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor3 = currentPosMotor3 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor4 = currentPosMotor4 + (int) (inches * COUNTS_PER_INCH);

                motor1.setTargetPosition(targetPosMotor1);
                motor2.setTargetPosition(targetPosMotor2);
                motor3.setTargetPosition(targetPosMotor3);
                motor4.setTargetPosition(targetPosMotor4);


                //sets motors needed to make robot go different directions

                motor1.setPower(eSpeed);
                motor2.setPower(eSpeed);
                motor3.setPower(eSpeed);
                motor4.setPower(eSpeed);

                // keep looping while we are still active, and there is time left, and both motors are running.

                runtime.reset();

                while (opModeIsActive()  &&
                        (motor1.isBusy() && motor2.isBusy())) {
                    if (rangeDistance> 0){
                        if (rangeSensor.rawUltrasonic()  < rangeDistance){
                            break;
                        }
                    }



                    // Display it for the driver.
                    telemetry.addData("Encoder Direction", robotDirection);
                    telemetry.addData("Mot 1234", "%5.2f:%5.2f:%5.2f:%5.2f", motor1.getPower(), motor2.getPower(), motor3.getPower(), motor4.getPower());
                    telemetry.update();

                    // Allow time for other processes to run.
                    idle();
                }

                // Stop all motion;
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);
                motor4.setPower(0);

                // reset directions
                motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                motor4.setDirection(DcMotorSimple.Direction.FORWARD);

                // Turn off RUN_TO_POSITION
                motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            }
        } catch (Exception e) {
            telemetry.addData("ERROR", e.toString());
        }
    }

    public void function_yaw(int x, int y,int gyro){
        /* Allows the robot move forward, left, right or down according to driver as an adjustment to x and y coordinates of joystick
        Step 1 - Find distance of joystick from center using distance formula and x and y coordinates from the joystick
        Step 2 - Find the angle of the joystick from the center based to determine direction robot will move using inverse tangent
        Step 3 - Subtract calculated angle from angle given by the gyroscope
        Step 4 - Use the new angle in trigonometric relations to find the new x and y coordinates for the robot to adjust to from basic directions (left, right, etc.)
         */


    }


    public void encoderDrive(double eSpeed, String robotDirection, double inches, double timeoutS) throws InterruptedException {
        try {
            if (opModeIsActive()) {

                resetEncoders("encoder");

                int currentPosMotor1 = 0;
                int currentPosMotor2 = 0;
                int currentPosMotor3 = 0;
                int currentPosMotor4 = 0;

                int targetPosMotor1 = 0;
                int targetPosMotor2 = 0;
                int targetPosMotor3 = 0;
                int targetPosMotor4 = 0;

                // Stop all motion;
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);
                motor4.setPower(0);

                // change directions so encoders are all positive
                //Shows all possible directions and controls.
                if (robotDirection == "up") {
                    motor1.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor4.setDirection(DcMotorSimple.Direction.REVERSE);
                } else if (robotDirection == "down") {
                    motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor2.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor3.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor4.setDirection(DcMotorSimple.Direction.FORWARD);
                } else if (robotDirection == "right") {
                    motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor3.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor4.setDirection(DcMotorSimple.Direction.REVERSE);
                } else if (robotDirection == "left") {
                    motor1.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor2.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor4.setDirection(DcMotorSimple.Direction.FORWARD);
                } else if (robotDirection == "circle right") {
                    motor1.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor2.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor3.setDirection(DcMotorSimple.Direction.REVERSE);
                    motor4.setDirection(DcMotorSimple.Direction.REVERSE);

                } else if (robotDirection == "circle left"){
                    motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                    motor4.setDirection(DcMotorSimple.Direction.FORWARD);

                }
                //Getting position of each driving motor

                currentPosMotor1 = motor1.getCurrentPosition();
                currentPosMotor2 = motor2.getCurrentPosition();
                currentPosMotor3 = motor3.getCurrentPosition();
                currentPosMotor4 = motor4.getCurrentPosition();

                targetPosMotor1 = currentPosMotor1 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor2 = currentPosMotor2 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor3 = currentPosMotor3 + (int) (inches * COUNTS_PER_INCH);
                targetPosMotor4 = currentPosMotor4 + (int) (inches * COUNTS_PER_INCH);

                motor1.setTargetPosition(targetPosMotor1);
                motor2.setTargetPosition(targetPosMotor2);
                motor3.setTargetPosition(targetPosMotor3);
                motor4.setTargetPosition(targetPosMotor4);


                //sets motors needed to make robot go different directions

                motor1.setPower(eSpeed);
                motor2.setPower(eSpeed);
                motor3.setPower(eSpeed);
                motor4.setPower(eSpeed);

                // keep looping while we are still active, and there is time left, and both motors are running.

                runtime.reset();

                while (opModeIsActive() &&
                        (runtime.seconds() < timeoutS) &&
                        (motor1.isBusy() && motor2.isBusy())) {

                    // Display it for the driver.
                    telemetry.addData("Encoder Direction", robotDirection);
                    telemetry.addData("Mot 1234", "%5.2f:%5.2f:%5.2f:%5.2f", motor1.getPower(), motor2.getPower(), motor3.getPower(), motor4.getPower());
                    telemetry.update();

                    // Allow time for other processes to run.
                    idle();
                }

                // Stop all motion;
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);
                motor4.setPower(0);

                // reset directions
                motor1.setDirection(DcMotorSimple.Direction.FORWARD);
                motor2.setDirection(DcMotorSimple.Direction.FORWARD);
                motor3.setDirection(DcMotorSimple.Direction.FORWARD);
                motor4.setDirection(DcMotorSimple.Direction.FORWARD);

                // Turn off RUN_TO_POSITION
                motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            }
        } catch (Exception e) {
            telemetry.addData("ERROR", e.toString());
        }
    }


    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - gyro.getIntegratedZValue();
        while (robotError > 180) robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }


    public void detectAndActiveBeaconColor() throws InterruptedException {

        telemetry.addData("Red", colorSensor.red());
        telemetry.addData("Blue", colorSensor.blue());
        telemetry.update();

        speed = .2;
        //if color detected is red...
        if (colorSensor.red() > colorSensor.blue()) {
            if (teamColor == "blue") {
                runtime.reset();
                while (runtime.seconds() < 4){
                    // delay to wait till we can touch button again
                }

                encoderDriveRange(DRIVE_SPEED, "down",5);

                encoderDrive(DRIVE_SPEED, "up", 2, 9);
            }
            //if the color detected is blue...
        } else if (colorSensor.blue() > colorSensor.red()) {
            if (teamColor == "red") {
                runtime.reset();
                while (runtime.seconds() < 4){

                }
                encoderDriveRange(DRIVE_SPEED, "down", 5);

               encoderDrive(DRIVE_SPEED, "up", 2, 9);
            }
        }

    }

    public void gotoWhiteLineAndHitBeacon() {
        speed = .1; // go slow for line detection

        // drive till either of the ODS sensors detect the white line
        while (ods.getRawLightDetected() < .1 && ods2.getRawLightDetected() < .1) {
            drive("left");
        }
        drive("stop");

        // then figure out which one is one the white line
        // and turn robot till other sensor is on line
        if (ods2.getRawLightDetected() < .1){
            while (ods2.getRawLightDetected() < .1) {
                drive("circle right");
            }
        }else if (ods.getRawLightDetected() < .1){
            while (ods.getRawLightDetected() < .1) {
                drive("circle left");
            }
        }

        drive("stop");
        speed = .2;
        while (rangeSensor.rawUltrasonic() > 5) {
            // drive down till robot hits button for the first time
            drive("down");
        }
        drive("stop");

        // back up just a bit
        while (rangeSensor.rawUltrasonic() < 10) {
            drive("up");
        }
        drive("stop");
    }



    void driveStick(float x, float y) {

        // speed is greater value of x or y
        //Uses the value of the joystick like the direction of motion does, only to set speed and divides it in half
        speed = (Math.abs(x) > Math.abs(y) ? Math.abs(x) : Math.abs(y)) / 1.5;

        telemetry.addData("y", y);
        telemetry.addData("x", x);

        //One program to combine 8 directions of motion on one joystick using ranges of x and y values
        if (y > .10) {
            drive("left");
        } else if (y < -.10) {
            drive("right");
        } else if (x > .10) {
            drive("down");
        } else if (x < -.10) {
            drive("up");
        } else {
            drive("stop");
        }


    }

    void resetEncoders(String resetType) {
        telemetry.addData("resetEncoders", resetType);
        telemetry.update();

        if (resetType == "drive") {
            sleep(10);
            motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor1.setDirection(DcMotor.Direction.FORWARD);

            sleep(10);
            motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor2.setDirection(DcMotor.Direction.FORWARD);

            sleep(10);
            motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor3.setDirection(DcMotor.Direction.FORWARD);

            sleep(10);
            motor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor4.setDirection(DcMotor.Direction.FORWARD);

        } else { // if (resetType == "encoder"){
            sleep(10);
            motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            sleep(10);
            motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            sleep(10);
            motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            sleep(10);
            motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    void drive(String robotDirection, double seconds ){
        runtime.reset();
        while (runtime.seconds() < seconds){
            drive(robotDirection);
        }
    }

    void drive(String robotDirection) {
        try {

            telemetry.addData("direction", robotDirection);
            //sets speed needed for motors to run different directions
            //Uses four motor to move robot ten different directions
            //Negative speed moves motor backwards and positive speed moves motor forward

            // Turn off RUN_TO_POSITION

            if (motor1.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
                resetEncoders("drive");
            }

            if (robotDirection == "up") {
                motor1.setPower(-speed);
                motor2.setPower(speed);
                motor3.setPower(speed);
                motor4.setPower(-speed);


            } else if (robotDirection == "up left") {
                motor1.setPower(0);
                motor2.setPower(speed);
                motor3.setPower(0);
                motor4.setPower(-speed);

            } else if (robotDirection == "up right") {
                motor1.setPower(0);
                motor2.setPower(-speed);
                motor3.setPower(0);
                motor4.setPower(speed);

            } else if (robotDirection == "down") {
                motor1.setPower(speed);
                motor2.setPower(-speed);
                motor3.setPower(-speed);
                motor4.setPower(speed);

            } else if (robotDirection == "down left") {
                motor1.setPower(speed);
                motor2.setPower(0);
                motor3.setPower(-speed);
                motor4.setPower(0);
            } else if (robotDirection == "down right") {
                motor1.setPower(-speed);
                motor2.setPower(0);
                motor3.setPower(speed);
                motor4.setPower(0);

            } else if (robotDirection == "left") {
                motor1.setPower(speed);
                motor2.setPower(speed);
                motor3.setPower(-speed);
                motor4.setPower(-speed);
            } else if (robotDirection == "right") {
                motor1.setPower(-speed);
                motor2.setPower(-speed);
                motor3.setPower(speed);
                motor4.setPower(speed);

            } else if (robotDirection == "circle left") {
                motor1.setPower(speed);
                motor2.setPower(speed);
                motor3.setPower(speed);
                motor4.setPower(speed);

            } else if (robotDirection == "circle right") {
                motor1.setPower(-speed);
                motor2.setPower(-speed);
                motor3.setPower(-speed);
                motor4.setPower(-speed);

            } else {
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);
                motor4.setPower(0);
            }

            telemetry.addData("Mot 1234", "%5.2f:%5.2f:%5.2f:%5.2f", motor1.getPower(), motor2.getPower(), motor3.getPower(), motor4.getPower());

            /*
            telemetry.addData("motor1", motor1.getPower());
            telemetry.addData("motor2", motor2.getPower());
            telemetry.addData("motor3", motor3.getPower());
            telemetry.addData("motor4", motor4.getPower());
            telemetry.update();
            */


        } catch (Exception p_exception) {
            telemetry.addData("drive error", p_exception.toString());
            telemetry.update();
        }

    }

    void gyroDrive(String gyroDirection, double gSpeed, double distance) {
        try {
            telemetry.addData("direction", gyroDirection);

            DcMotor flMotor = null;
            DcMotor frMotor = null;
            DcMotor blMotor = null;
            DcMotor brMotor = null;

            if (gyroDirection == "down") {
                flMotor = motor1;
                frMotor = motor2;
                brMotor = motor3;
                blMotor = motor4;


            } else if (gyroDirection == "right") {
                flMotor = motor2;
                frMotor = motor3;
                brMotor = motor4;
                blMotor = motor1;


            } else if (gyroDirection == "up") {
                flMotor = motor3;
                frMotor = motor4;
                brMotor = motor1;
                blMotor = motor2;


            } else if (gyroDirection == "left") {
                flMotor = motor4;
                frMotor = motor1;
                brMotor = motor2;
                blMotor = motor3;
            }


            // Ensure that the opmode is still active
            if (opModeIsActive()) {

                flMotor.setDirection(DcMotor.Direction.FORWARD);
                flMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(10);

                blMotor.setDirection(DcMotor.Direction.FORWARD);
                blMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                sleep(10);

                frMotor.setDirection(DcMotor.Direction.REVERSE);
                frMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(10);

                brMotor.setDirection(DcMotor.Direction.REVERSE);
                brMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                sleep(10);

                // Determine new target position, and pass to motor controller
                moveCounts = (int) (distance * COUNTS_PER_INCH);
                newLeftTarget = flMotor.getCurrentPosition() + moveCounts;
                newRightTarget = frMotor.getCurrentPosition() + moveCounts;

                // Set Target and Turn On RUN_TO_POSITION
                flMotor.setTargetPosition(newLeftTarget);
                frMotor.setTargetPosition(newRightTarget);


                // start motion.
                gSpeed = Range.clip(Math.abs(gSpeed), 0.0, 1.0);

                flMotor.setPower(gSpeed);
                frMotor.setPower(gSpeed);
                brMotor.setPower(gSpeed);
                blMotor.setPower(gSpeed);


                // keep looping while we are still active, and BOTH motors are running.
                while (opModeIsActive() &&
                        flMotor.isBusy() && frMotor.isBusy()) {

                    // adjust relative speed based on headine3sg error.
                    error = getError(0);
                    steer = getSteer(error, P_DRIVE_COEFF);

                    // if driving in reverse, the motor correction also needs to be reversed
                    if (distance < 0)
                        steer *= -1.0;

                    leftSpeed = gSpeed - steer;
                    rightSpeed = gSpeed + steer;

                    // Normalize speeds if any one exceeds +/- 1.0;

                    max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                    if (max > 1) {
                        leftSpeed /= max;
                        rightSpeed /= max;
                    }

                    flMotor.setPower(leftSpeed );
                    blMotor.setPower(leftSpeed );

                    frMotor.setPower(rightSpeed);
                    brMotor.setPower(rightSpeed );

                    // Display drive status for the driver.
                    telemetry.addData("gyro direction", gyroDirection);
                    telemetry.addData("Mot 1234", "%5.2f:%5.2f:%5.2f:%5.2f", motor1.getPower(), motor2.getPower(), motor3.getPower(), motor4.getPower());

                    telemetry.update();


                    idle();
                }

                flMotor.setPower(0);
                frMotor.setPower(0);

                brMotor.setPower(0);
                blMotor.setPower(0);

                // Turn off RUN_TO_POSITION
                flMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                sleep(10);
                frMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                sleep(10);
                blMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                sleep(10);
                brMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                sleep(10);
            }
        } catch (Exception p_exception) {

        }

    }


    public void setupVuforia() {
        // Setup parameters to create localizer
        //parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId); // To remove the camera view from the screen, remove the R.id.cameraMonitorViewId
        parameters = new VuforiaLocalizer.Parameters(); // To remove the camera view from the screen, remove the R.id.cameraMonitorViewId
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        parameters.useExtendedTracking = false;
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        // These are the vision targets that we want to use
        // The string needs to be the name of the appropriate .xml file in the assets folder
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);

        // Setup the target to be tracked

        vuforiaWheels = visionTargets.get(0);
        vuforiaWheels.setName("wheels");  // wheels - blue 1

        vuforiaTools = visionTargets.get(1);
        vuforiaTools.setName("tools");  // Tools = red 2

        vuforiaLegos = visionTargets.get(2);
        vuforiaLegos.setName("legos");  // legos - blue 2

        vuforiaGears = visionTargets.get(3);
        vuforiaGears.setName("gears");  // Gears - red 1

        if (teamColor == "blue") {
            target = vuforiaWheels;
        } else {
            target = vuforiaGears;
        }

        target.setLocation(createMatrix(0, 500, 0, 90, 0, 90));

        // Set phone location on robot - not set
        phoneLocation = createMatrix(0, 225, 0, 90, 0, 0);

        // Setup listener and inform it of phone information
        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);
    }

    // Creates a matrix for determining the locations and orientations of objects
    // Units are millimeters for x, y, and z, and degrees for u, v, and w
    public OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w) {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }

    // Formats a matrix into a readable string
    public String formatMatrix(OpenGLMatrix matrix) {
        return matrix.formatAsTransform();
    }
}




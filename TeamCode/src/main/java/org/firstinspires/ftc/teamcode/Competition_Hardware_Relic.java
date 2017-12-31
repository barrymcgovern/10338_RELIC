package org.firstinspires.ftc.teamcode;



import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by kids on 10/9/2017.
 *
 *
 * 7 Dc motors
 * 4 drive motors, motorfl, motorfr, motorl, and motorfr. F=front, B=back, L=left, R=right
 * 3 cryptobox motors, stackmotorl and stackmotorr move up and down simultaneously, slidemotor moves left and right
 *
 * 2 servos
 * clawl and clawr open and close on the glyphs
 *
 *
 *
 *
 * RevhubA: 0-motorfl 1-motorfr 2-motorbl 3-motorbr servo 0-clawl 1-clawr
 *
 * RevhubB: 0-stackmotorl 1-stackmotorr 2-slidemotor
 */


public abstract class Competition_Hardware_Relic extends LinearOpMode {

    public DcMotor motorfl = null;
    public DcMotor motorfr = null;
    public DcMotor motorbl = null;
    public DcMotor motorbr = null;
    public DcMotor stackmotor = null;



    public Servo clawl = null;
    public Servo clawr = null;
    public double clawLStart = 0.5;
    public double clawRStart = 0.5;
    public double clawLEnd =1;
    public double clawREnd =0;
    public int down = 0;

    public VuforiaLocalizer vuforia;
    public int cameraMonitorViewId;

    public Servo servoColorLeft = null;
   // public Servo servoColorRight = null;
    public Servo servoJewelLeft = null;
    //public Servo servoJewelRight = null;

    public static final String VUFORIA_KEY = "AZNyeJT/////AAAAGcEyNak4ykAkhL+InR+WdKUGDQVzF/FELSuZi1yDVXXgcq8IBY9YUrq/i8CblYxOVZ1f8p3FSqUGHisyj6X2Z/fzTkrhRxyigB1hzK2ua8R5PtjFMrb5bruaTXH0rPs59nmx7OPKDr3rrp74XAKU2Twxt+wRaGCssmWtpwUC2Fk6xz9CRkejMEPhenzNpjd/z4tiQRDAe37LEfpJvos/6QVLZZkamkozBN9gdR8+6JLthq3HL22qwlX21RIbwlJmMoi41qhzcaeyFHk0CamDUHgxVcB1VC5i8Hin3f7Y/EPGGALbPpb4AJUhx2nddSQQVI3nDNoNIhHP5sBJ0OG9WPy5dTvDNGaqK7LQfjbyze2x";


    public ElapsedTime runtime = new ElapsedTime();
    
    public double speed =  0;


    public NormalizedColorSensor colorSensor;
    NormalizedRGBA colors;



    public String team;


    HardwareMap hwMap = null;

    OpenGLMatrix lastLocation = null;

    public void autoMode(String colorSwitch) {
        try {

            clawl.setPosition(clawLStart);
            clawr.setPosition(clawRStart);
            runtime.reset();
            while (runtime.seconds() < 1) {

            }
            stackmotor.setPower(50);
            runtime.reset();
            while (runtime.seconds() < .25) {

            }
            stackmotor.setPower(0);



            VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
            VuforiaTrackable relicTemplate = relicTrackables.get(0);
            relicTemplate.setName("relicVuMarkTemplate");
            relicTrackables.activate();
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {


                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "%s visible", vuMark);

                /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. */
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                telemetry.addData("Pose", matFormat(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                }
            }
            else {
                telemetry.addData("VuMark", "not visible");
            }

            telemetry.update();
    } catch (Exception e) {
            telemetry.addData("init SERVO ERROR", e.toString());
            telemetry.update();
        }



            if (colorSwitch == "redTurn") {

                try {

                    servoColorLeft.setPosition(1);
                    runtime.reset();
                    while (runtime.seconds() < .5) {

                    }

                    runtime.reset();
                    while (runtime.seconds() < 5) {
                        colors = colorSensor.getNormalizedColors();
                        if (colors.red > colors.blue) {
                            telemetry.addData("color", "red");

                        }else {
                            telemetry.addData("color", "blue");
                        }
                        telemetry.addData("blue", colors.blue);
                        telemetry.addData("red", colors.red);
                        telemetry.update();
                    }
                    //Determines which color Jewel the servo needs to turn to to knock off the correct color
                    runtime.reset();
                        if (colors.red > colors.blue) {
                            drive_code(0,1,0);
                            while (runtime.seconds() < .1){

                            }
                            runtime.reset();
                            drive_code(0,-1,0);
                            while (runtime.seconds() < .1){

                            }

                            telemetry.update();
                        } else if (colors.blue > colors.red) {
                            drive_code(0,-1,0);
                            while (runtime.seconds() < .5){

                            }
                            telemetry.update();
                        }
                        runtime.reset();
                        while (runtime.seconds() < 1) {

                        }

                } catch (Exception e) {
                    telemetry.addData("color ERROR", e.toString());
                    telemetry.update();

                }

                runtime.reset();
                while (runtime.seconds() < .5) {

                }
                servoColorLeft.setPosition(.8);
                runtime.reset();
                while (runtime.seconds() < .5){

                }

                servoColorLeft.setPosition(1);
                runtime.reset();
                while (runtime.seconds() < .5) {

                }

                telemetry.update();
                runtime.reset();


                drive_code(0, -1, 0);
                while (runtime.seconds() < 1) {

                }
                //Wait
                runtime.reset();
                drive_code(0, 0, 0);
                while (runtime.seconds() < .5) {

                }
                //Turn Step
                runtime.reset();
                drive_code(0, 0, -1);
                while (runtime.seconds() < .65) {

                }
                runtime.reset();
                drive_code(0, 0, 0);
                while (runtime.seconds() < .5) {

                }
                //Go to the block
                runtime.reset();
                drive_code(0, -1, 0);
                while (runtime.seconds() < .25) {

                }
                drive_code(0, 0, 0);

//Version of Autonomous for red alliance when the robot does not need to turn to deliver Glyph
            } else if (colorSwitch == "redStraight") {

                servoColorLeft.setPosition(1);
                runtime.reset();
                while (runtime.seconds() < .5) {

                }
                colors = colorSensor.getNormalizedColors();
                runtime.reset();
                while (runtime.seconds() < 5) {
                    colors = colorSensor.getNormalizedColors();
                    if (colors.red > colors.blue) {
                        telemetry.addData("color", "red");

                    }else {
                        telemetry.addData("color", "blue");
                    }
                    telemetry.addData("blue", colors.blue);
                    telemetry.addData("red", colors.red);
                    telemetry.update();
                }
                runtime.reset();

                if (colors.red > colors.blue) {
                   drive_code(0, 1, 0);
                    while (runtime.seconds() < .1){

                    }
                    runtime.reset();
                    drive_code(0,-1,0);
                    while (runtime.seconds() < .1){

                    }
                    telemetry.update();
                } else if (colors.blue > colors.red) {
                    drive_code(0, -1, 0);
                    while (runtime.seconds() < .1){

                    }
                    runtime.reset();
                    drive_code(0, 1, 0);
                    while (runtime.seconds() < .1){

                    }
                        telemetry.update();


                runtime.reset();
                while (runtime.seconds() < .5) {

                }
                servoColorLeft.setPosition(.8);
                runtime.reset();
                while (runtime.seconds() < .5) {

                }



                servoColorLeft.setPosition(0);
                runtime.reset();
                while (runtime.seconds() < .5) {

                }

                telemetry.update();
                //Determines which color Jewel the servo needs to turn to to knock off the correct color

                runtime.reset();
                drive_code(0, -1, 0);
                while (runtime.seconds() < .75) {

                }

                runtime.reset();
                drive_code(-1, 0, 0);
                while (runtime.seconds() < .5){

                }

                runtime.reset();
                drive_code(0,-1,0);
                while (runtime.seconds() < .5){

                }

                drive_code(0, 0, 0);
                //Version of Autonomous for blue alliance when the robot does not need to turn to deliver Glyph
            } else if (colorSwitch == "blueStraight") {
                    telemetry.update();


                    servoColorLeft.setPosition(0);
                    runtime.reset();
                    while (runtime.seconds() < .5) {

                    }

                    runtime.reset();
                    while (runtime.seconds() < 5) {
                        colors = colorSensor.getNormalizedColors();
                        if (colors.red > colors.blue) {
                            telemetry.addData("color", "red");

                        } else {
                            telemetry.addData("color", "blue");
                        }
                        telemetry.addData("blue", colors.blue);
                        telemetry.addData("red", colors.red);
                        telemetry.update();

                    }
                }

                if (colors.red > colors.blue) {
                    drive_code(0, -1, 0);
                    while (runtime.seconds() < .1) {
                    }
                    runtime.reset();
                    drive_code(0, 1, 0);
                    while (runtime.seconds() < .1){

                    }
                    telemetry.update();
                } else if (colors.blue > colors.red) {
                    drive_code(0,1,0);
                    while (runtime.seconds() < .1){

                    }
                    runtime.reset();
                    drive_code(0, -1, 0);
                    while (runtime.seconds() < .1){

                    }
                    telemetry.update();
                }

                runtime.reset();
                while (runtime.seconds() < .5) {

                }
                servoColorLeft.setPosition(.8);
                runtime.reset();
                while (runtime.seconds() < .5) {

                }
                servoJewelLeft.setPosition(.5);
                runtime.reset();
                while (runtime.seconds() < .5) {

                }

                servoColorLeft.setPosition(0);
                runtime.reset();
                while (runtime.seconds() < .5) {

                }
                //Delivers the Glyph
                runtime.reset();
                drive_code(0, 1, 0);
                while (runtime.seconds() < .75) {

                }
                runtime.reset();
                drive_code(-1,0,0);
                while (runtime.seconds() < .75){

                }
                runtime.reset();
                drive_code(0, 1, 0);
                while (runtime.seconds() < .5){

                }
                //Stop; end of autonomous
                drive_code(0, 0, 0);

            } else if (colorSwitch == "blueTurn") {
                //Grabs the glyph
                servoColorLeft.setPosition(0);
                runtime.reset();
                while (runtime.seconds() < .5) {

                }

                runtime.reset();
                while (runtime.seconds() < 5) {
                    colors = colorSensor.getNormalizedColors();
                    if (colors.red > colors.blue) {
                        telemetry.addData("color", "red");

                    }else {
                        telemetry.addData("color", "blue");
                    }
                    telemetry.addData("blue", colors.blue);
                    telemetry.addData("red", colors.red);
                    telemetry.update();
                }
                //Determines which color Jewel the servo needs to turn to to knock off the correct color
                if (colors.red > colors.blue) {
                    drive_code(0, -1, 0);
                    while (runtime.seconds() < .1) {
                    }
                    runtime.reset();
                    drive_code(0, 1, 0);
                    while (runtime.seconds() < .1){

                    }
                    telemetry.update();
                } else if (colors.blue > colors.red) {
                    drive_code(0, 1, 0);
                    while (runtime.seconds() < .5) {
                    }
                    runtime.reset();
                    drive_code(0, -1, 0);
                    while (runtime.seconds() < .1){

                    }
                    telemetry.update();
                }
                runtime.reset();
                while (runtime.seconds() < .5) {

                }
                servoColorLeft.setPosition(.8);
                runtime.reset();
                while (runtime.seconds() < 1) {

                }
                servoJewelLeft.setPosition(.5);
                runtime.reset();
                while (runtime.seconds() < 1) {

                }

                servoColorLeft.setPosition(1);
                runtime.reset();
                while (runtime.seconds() < 1) {

                }

                telemetry.update();
                runtime.reset();
                drive_code(0, 1, 0);
                while (runtime.seconds() < 1.10) {
                    {

                    }
                    //Wait
                    runtime.reset();
                    drive_code(0, 0, 0);
                    while (runtime.seconds() < .5) {

                    }
                    //Turn Step
                    runtime.reset();
                    drive_code(0, 0, -1);
                    while (runtime.seconds() < .65) {

                    }
                    runtime.reset();
                    drive_code(0, 0, 0);
                    while (runtime.seconds() < .5) {

                    }
                    //Deliver the block
                    runtime.reset();
                    drive_code(0, 1, 0);
                    while (runtime.seconds() < .25) {

                    }
                    drive_code(0, 0, 0);

                }
            }
       }



    String matFormat(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

    public void init(HardwareMap ahwMap) {
        try {
            // Save reference to Hardware map
            hwMap = ahwMap;
            // Define and Initialize Motors
            telemetry.addLine("starting");
            telemetry.update();
            try{
                motorfl = hwMap.dcMotor.get("motorfl");
                motorfr= hwMap.dcMotor.get("motorfr");
                motorbl = hwMap.dcMotor.get("motorbl");
                motorbr = hwMap.dcMotor.get("motorbr");

                motorfl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorfr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorbl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorbr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                motorfl.setDirection(DcMotor.Direction.FORWARD);
                motorfr.setDirection(DcMotor.Direction.REVERSE);
                motorbl.setDirection(DcMotor.Direction.FORWARD);
                motorbr.setDirection(DcMotor.Direction.REVERSE);

                motorfl.setPower(0);
                motorfr.setPower(0);
                motorbl.setPower(0);
                motorbr.setPower(0);



            }catch (Exception e){
                telemetry.addData("init motors ERROR", e.toString());

            }
            try{

                stackmotor = hwMap.dcMotor.get("stackmotor");
                stackmotor.setPower(0);
                stackmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


            }catch (Exception e){
                telemetry.addData("init stackmotor ERROR", e.toString());

            }
            try{
                // Vuforia init code

                cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
                VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
                parameters.vuforiaLicenseKey = VUFORIA_KEY;

                parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
                this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);


            } catch (Exception e){
                telemetry.addData("init vuforia ERROR", e.toString());
            }


            try{
                servoJewelLeft = hardwareMap.get(Servo.class, "servoJewelLeft");
                servoColorLeft = hardwareMap.get(Servo.class, "servoColorLeft");
                telemetry.addData("servoJewelLeft",servoJewelLeft.getPosition());
                telemetry.addData("servoColorLeft",servoColorLeft.getPosition());
            }catch (Exception e){
                telemetry.addData("init SERVO ERROR", e.toString());
            }

                try{
                clawl = hardwareMap.get(Servo.class, "clawl");
                clawr = hardwareMap.get(Servo.class, "clawr");
                telemetry.addData("clawr",clawr.getPosition());
                telemetry.addData("clawl",clawl.getPosition());





                /*
                clawl.setPosition(5);
                clawr.setPosition(5);

                // motor ports 0 and 1 Hub A
                servoColorLeft.setPosition(0);
                //servoColorRight.setPosition(0);

                //motor ports 2 and 3 hub A
                servoJewelLeft.setPosition(0);
                //servoJewelRight.setPosition(0);
                */
            }catch (Exception e){
                telemetry.addData("init SERVO ERROR", e.toString());
            }

            try{
                // I2C port 0 hub A
                colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensorColorLeft");
                // If possible, turn the light on in the beginning (it might already be on anyway,
                // we just make sure it is if we can).
                if (colorSensor instanceof SwitchableLight) {
                    ((SwitchableLight)colorSensor).enableLight(true);
                }
            }catch (Exception e){
                telemetry.addData("init colorsensor ERROR", e.toString());

            }
            telemetry.addLine("done with init");
            telemetry.addData("x,y = " ,gamepad1.left_stick_x + " " + gamepad1.right_stick_y);
            telemetry.addData("fl",motorfl.getDirection() + " " + motorfl.getPower());
            telemetry.addData("fr",motorfr.getDirection() + " " + motorfr.getPower());
            telemetry.addData("bl",motorfl.getDirection() + " " + motorbl.getPower());
            telemetry.addData("br",motorbr.getDirection() + " " + motorbr.getPower());
            telemetry.update();

            runtime.reset();

        }catch (Exception e){
            telemetry.addData("init ERROR", e.toString());
            telemetry.update();

        }
    }
/*
   void distance(float x, float y) {
       p = (x * x);
       r = (y * y);
       d = Math.sqrt(r + p);
   }

    void  joystick_angle( float x, float y, double a){
        a= Math.toDegrees(Math.atan2(y, x));

    }
*/
    void drive_code (float x,float y, float z){
        try{

            double r = Math.hypot(x,y);
            double robotAngle = Math.atan2(y,-x) - Math.PI / 4;
            double rightX = z;
            final double v1 = r * Math.cos(robotAngle) + rightX;
            final double v2 = r * Math.sin(robotAngle) - rightX;
            final double v3 = r * Math.sin(robotAngle) + rightX;
            final double v4 = r * Math.cos(robotAngle) - rightX;

            motorfl.setPower(v1);
            motorfr.setPower(v2);
            motorbl.setPower(v3);
            motorbr.setPower(v4);

        }catch (Exception e){
            telemetry.addData("drive ERROR", e.toString());

        }

    }




    void drive(String robotDirection) {
        try {

            telemetry.addData("direction", robotDirection);
            //sets speed needed for motors to run different directions
            //Uses four motor to move robot ten different directions
            //Negative speed moves motor backwards and positive speed moves motor forward

            // Turn off RUN_TO_POSITION




            if (robotDirection == "up") {
                motorfl.setPower(-speed);
                motorfr.setPower(speed);
                motorbl.setPower(speed);
                motorbr.setPower(-speed);


            } else if (robotDirection == "up left") {
                motorfl.setPower(0);
                motorfr.setPower(speed);
                motorbl.setPower(0);
                motorbr.setPower(-speed);

            } else if (robotDirection == "up right") {
                motorfl.setPower(0);
                motorfr.setPower(-speed);
                motorbl.setPower(0);
                motorbr.setPower(speed);

            } else if (robotDirection == "down") {
                motorfl.setPower(speed);
                motorfr.setPower(-speed);
                motorbl.setPower(-speed);
                motorbr.setPower(speed);

            } else if (robotDirection == "down left") {
                motorfl.setPower(speed);
                motorfr.setPower(0);
                motorbl.setPower(-speed);
                motorbr.setPower(0);
            } else if (robotDirection == "down right") {
                motorfl.setPower(-speed);
                motorfr.setPower(0);
                motorbl.setPower(speed);
                motorbr.setPower(0);

            } else if (robotDirection == "left") {
                motorfl.setPower(speed);
                motorfr.setPower(speed);
                motorbl.setPower(-speed);
                motorbr.setPower(-speed);
            } else if (robotDirection == "right") {
                motorfl.setPower(-speed);
                motorfr.setPower(-speed);
                motorbl.setPower(speed);
                motorbr.setPower(speed);

            } else if (robotDirection == "circle left") {
                motorfl.setPower(speed);
                motorfr.setPower(speed);
                motorbl.setPower(speed);
                motorbr.setPower(speed);

            } else if (robotDirection == "circle right") {
                motorfl.setPower(-speed);
                motorfr.setPower(-speed);
                motorbl.setPower(-speed);
                motorbr.setPower(-speed);

            } else {
                motorfl.setPower(0);
                motorfr.setPower(0);
                motorbl.setPower(0);
                motorbr.setPower(0);
            }

            telemetry.addData("Mot 1234", "%5.2f:%5.2f:%5.2f:%5.2f", motorfl.getPower(), motorfr.getPower(), motorbl.getPower(), motorbr.getPower());

            /*
            telemetry.addData("motorfl", motorfl.getPower());
            telemetry.addData("motorfr", motorfr.getPower());
            telemetry.addData("motorbl", motorbl.getPower());
            telemetry.addData("motorbr", motorbr.getPower());
            telemetry.update();
            */


        } catch (Exception p_exception) {
            telemetry.addData("drive error", p_exception.toString());
            telemetry.update();
        }

    }
    
    void driveStick(float x, float y) {

        // speed is greater value of x or y
        //Uses the value of the joystick like the direction of motion does, only to set speed and divides it in half
        speed = (Math.abs(x) > Math.abs(y) ? Math.abs(x) : Math.abs(y)) / 1.5;

        telemetry.addData("y", y);
        //no
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



}


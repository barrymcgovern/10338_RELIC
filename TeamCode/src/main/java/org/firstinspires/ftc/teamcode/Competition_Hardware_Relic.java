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
    public DcMotor ledLights = null;


    public Servo clawl = null;
    public Servo clawr = null;

    public double clawLStart = 0.25;
    public double clawRStart = 0.75;
    public double clawLEnd =0;
    public double clawREnd =1;
    public double vuSeconds = 0;


    public int down = 0;
    public int minPosStackMotor = 0;
    public int maxPosStackMotor = 0;
    public int row1 = 0;
    public int row2 = 0;
    public int row3 = 0;
    public int row4 = 0;
    public int rowToGoTo = 0;
    public int targetPosition = 0;
    public String rowDirection = "";
    public String vuDirection = "";

    public boolean startSpin;

    public VuforiaLocalizer vuforia;
    public int cameraMonitorViewId;

    public Servo servoColorLeft = null;
   // public Servo servoColorRight = null;


    public static final String VUFORIA_KEY = "AZNyeJT/////AAAAGcEyNak4ykAkhL+InR+WdKUGDQVzF/FELSuZi1yDVXXgcq8IBY9YUrq/i8CblYxOVZ1f8p3FSqUGHisyj6X2Z/fzTkrhRxyigB1hzK2ua8R5PtjFMrb5bruaTXH0rPs59nmx7OPKDr3rrp74XAKU2Twxt+wRaGCssmWtpwUC2Fk6xz9CRkejMEPhenzNpjd/z4tiQRDAe37LEfpJvos/6QVLZZkamkozBN9gdR8+6JLthq3HL22qwlX21RIbwlJmMoi41qhzcaeyFHk0CamDUHgxVcB1VC5i8Hin3f7Y/EPGGALbPpb4AJUhx2nddSQQVI3nDNoNIhHP5sBJ0OG9WPy5dTvDNGaqK7LQfjbyze2x";
    public RelicRecoveryVuMark vuMark;
    public VuforiaTrackables relicTrackables ;
    public VuforiaTrackable relicTemplate ;

    public ElapsedTime runtime = new ElapsedTime();
    
    public double speed =  0;


    public NormalizedColorSensor colorSensor;
    NormalizedRGBA colors;



    public String team;


    HardwareMap hwMap = null;

    OpenGLMatrix lastLocation = null;

    public void autoMode(String colorSwitch) {
        try {
            try {
                servoColorLeft.setPosition(1);
            }catch (Exception e){
                telemetry.addData("servor ERROR", e.toString());
                telemetry.update();
            }
            runtime.reset();
            while (runtime.seconds() < 2) {

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


        } catch (Exception e) {
            telemetry.addData("init SERVO ERROR", e.toString());
            telemetry.update();
        }
        try{
            runtime.reset();
            //get claw out
            runtime.reset();
            while (runtime.seconds() < .5) {
                stackmotor.setPower(-.5);
            }
            stackmotor.setPower(-.1);
            clawl.setPosition(clawLStart);
            clawr.setPosition(clawRStart);
            runtime.reset();
            while (runtime.seconds() < 1) {}
            runtime.reset();
            while (runtime.seconds() < 2) {
                stackmotor.setPower(.05);
            }
            clawl.setPosition(clawLEnd);
            clawr.setPosition(clawREnd);
            runtime.reset();
            while (runtime.seconds() < .5) {
                stackmotor.setPower(-.5);
            }
            stackmotor.setPower(-.1);
            drive_code(0,0,0);
        }catch (Exception e){
            telemetry.addData("run ERROR", e.toString());
            telemetry.update();

        }

        try {
            if (colorSwitch == "redTurn" || colorSwitch == "redStraight") {
                if (colors.red > colors.blue) {

                    runtime.reset();
                    while (runtime.seconds() < .3) {
                        drive_auto (.3, 0);
                    }
                    runtime.reset();
                    while (runtime.seconds() < .3) {
                        drive_auto (-.3, 0);
                    }
                    runtime.reset();
                    while (runtime.seconds() < .05) {
                        drive_auto(.3, 0);
                    }
                    runtime.reset();


                } else if (colors.red < colors.blue) {
                    runtime.reset();
                    while (runtime.seconds() < .3) {
                        drive_auto(-.3, 0);
                    }
                    runtime.reset();
                    while (runtime.seconds() < .3) {
                        drive_auto(.3, 0);
                    }
                    runtime.reset();
                    while (runtime.seconds() < .05) {
                        drive_auto(-.3, 0);
                    }

                }
            } else {
                if (colors.blue > colors.red) {
                    runtime.reset();
                    while (runtime.seconds() < .3) {
                        drive_auto(-.3, 0);
                    }
                    runtime.reset();
                    while (runtime.seconds() < .3) {
                        drive_auto(.3, 0);
                    }
                    runtime.reset();
                    while (runtime.seconds() < .05) {
                        drive_auto(-.3, 0);
                    }


                } if (colors.blue < colors.red){
                    runtime.reset();
                    while (runtime.seconds()< .3) {
                        drive_auto(.3, 0);
                    }runtime.reset();
                    while (runtime.seconds()< .3){
                        drive_auto(-.3, 0);
                    }
                    runtime.reset();
                    while (runtime.seconds()<.05){
                        drive_auto(.3,0);
                    }

                }

            }

            drive_code(0,0,0);

        }catch (Exception e){
            drive_code(0,0,0);
            telemetry.addData("run ERROR", e.toString());
            telemetry.update();
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
            try {
                ledLights = hwMap.dcMotor.get("ledLights");
                ledLights.setPower(1);
            }catch (Exception e){
                telemetry.addData("init LED Motor ERROR", e.toString());
            }
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

            try {
                minPosStackMotor = stackmotor.getCurrentPosition();
                row1 = minPosStackMotor;
                row2 = row1- 281;
                row3 = row2- 221 ;
                row4 = row3- 183;
                maxPosStackMotor = row4 - 50;


                telemetry.addData("minPosStackMotor", minPosStackMotor);
                telemetry.addData("maxPosStackMotor", maxPosStackMotor);
                telemetry.update();

            } catch (Exception e){
                telemetry.addData("init stackmotor ERROR", e.toString());
            }

            try{
                // Vuforia init code

                cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
                VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
                parameters.vuforiaLicenseKey = VUFORIA_KEY;

                parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
                this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

                RelicRecoveryVuMark vuMark;
                relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
                relicTemplate = relicTrackables.get(0);
                relicTemplate.setName("relicVuMarkTemplate");


            } catch (Exception e){
                telemetry.addData("init vuforia ERROR", e.toString());
            }


            try{

                servoColorLeft = hardwareMap.get(Servo.class, "servoColorLeft");
                 telemetry.addData("servoColorLeft",servoColorLeft.getPosition());
            }catch (Exception e){
                telemetry.addData("init SERVO jewel ERROR", e.toString());
            }

             try{
                clawl = hardwareMap.get(Servo.class, "clawl");
                clawr = hardwareMap.get(Servo.class, "clawr");

                telemetry.addData("clawr",clawr.getPosition());
                telemetry.addData("clawl",clawl.getPosition());

            }catch (Exception e){
                telemetry.addData("init SERVO claw ERROR", e.toString());
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

    public void vuforia_Drive(String colorSwitch) {


        vuMark = RelicRecoveryVuMark.from(relicTemplate);
        telemetry.addData("vuMark",vuMark);
        telemetry.update();
        runtime.reset();
        while(runtime.seconds() < 3){
            // show mark for a few seconds
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            telemetry.addData("vuMark",vuMark);
            telemetry.update();
        }
        runtime.reset();
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            /* redstraight
                - turn left a few seconds
                  go straight
             bluestraight
                - turn right a few seconds
                - go straing
            redturn
                - go straight
                -
            blueturn
            */
            if (colorSwitch.contains("red")){
                vuDirection = "Right";
            }
            if (colorSwitch.contains("blue")){
                vuDirection = "Left";
            }
              if (vuMark == RelicRecoveryVuMark.CENTER) {
                //middle amount
                 if (colorSwitch.contains("Straight")){
                     vuSeconds = .4;
                 } else if (colorSwitch.contains("Turn")){
                     vuSeconds = .75;
                 }

                } else if (vuMark == RelicRecoveryVuMark.LEFT){
                    if (colorSwitch == "redStraight"){
                        vuSeconds = .6;
                    } else if (colorSwitch == "blueStraight"){
                        vuSeconds = .2;
                    } else if (colorSwitch == "redTurn") {
                        vuSeconds = 1;
                    } else if (colorSwitch == "blueTurn"){
                        vuSeconds = .5;
                    }

                } else if (vuMark == RelicRecoveryVuMark.RIGHT){

                   if (colorSwitch == "redStraight"){
                       vuSeconds = .2;
                   } else if (colorSwitch == "blueStraight"){
                       vuSeconds = .6;
                   } else if (colorSwitch == "redTurn"){
                       vuSeconds = .5;
                   } else if (colorSwitch == "blueTurn"){
                       vuSeconds = 1;
                   }
                }



            if (colorSwitch.contains("Straight")) {
                runtime.reset();
                // turn [direction] for [secondsToTurn]

                // need direction to turn
                //if turn = left


                drive_code(0, 0, 1);

                // use secondsToTurn
                while (runtime.seconds() < vuSeconds) {

                }
                // stop
                drive_code(0, 0, 0);


                runtime.reset();
                // drive straight

                drive_code(0, 1, 0);

                // need # of seconds to drive straight -
                while (runtime.seconds() < .75) {

                }

                //stop
                drive_code(0, 0, 0);



            }else { // turn
                // go straigt for [seconds]

                // turn [direction]

                // go straight
            }


        }
    }
    void drive_auto (double x, double y){
        try {


            motorfl.setPower(x);
            motorfr.setPower(x);
            motorbl.setPower(x);
            motorbr.setPower(x);
        }catch (Exception e ){
            telemetry.addData("auto drive error", e.toString());
            telemetry.update();
        }
    }


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

}


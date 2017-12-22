package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

/**
 * Created by kids on 11/2/2017.
 * BLUE 1 AUTONOMOUS
 */
@Autonomous(name="Auto: Relic", group= "Pushbot")
public class Competition_Auto_Relic  extends Competition_Hardware_Relic {
    @Override
    public void runOpMode() throws InterruptedException {




        init(hardwareMap);

        telemetry.update();
        waitForStart();

/*  servo arm will go down, get color reading of left jewel
    if color=blue, turn 90 degrees clockwise, then turn 90 degrees counter clockwise
    if color=red, turn 90 degrees counter clockwise, then turn 90 degrees clockwise
    go forward 48 inches
    set clawl to 180 and clawr to 0


 */
        // values is a reference to the hsvValues array.

        float[] hsvValues = new float[3];
        final float values[] = hsvValues;

        if (colorSensor instanceof SwitchableLight) {
            SwitchableLight light = (SwitchableLight) colorSensor;
            light.enableLight(!light.isLightOn());
        }




        while (opModeIsActive()) {
            clawl.setPosition(90);
            clawr.setPosition(90);

            servoColorLeft.setPosition(120);

            wait(2000);

            NormalizedRGBA colors = colorSensor.getNormalizedColors();
//Determines the color of the Jewel the color sensor is facing and moves in a direction according to alliance color
            if (team == "B1"){
                if (colors.red > 0) {
                    servoJewelLeft.setPosition(180);
                }else if (colors.blue > 0) {
                    servoJewelLeft.setPosition(0);
                }
            } else if (team == "B2"){
                if (colors.red > 0) {
                    servoJewelLeft.setPosition(180);
                }else if (colors.blue > 0) {
                    servoJewelLeft.setPosition(0);
                }
            }
//Resets the position of the post used to knock the Jewel off of the platform
            servoJewelLeft.setPosition(90);
            wait(2000);
            servoColorLeft.setPosition(0);

           runtime.reset();
            if (runtime.seconds() > 2) {
              drive_code(1,0,0);
            }
//If the robot is on two of the balance pads it needs to spin to face the crypto box, whereas this is not necessary for the
// other two placements
            runtime.reset();
            if (team == "B2"){
                if (runtime.seconds() > 1){
                    drive("circle left");
                }
            } else if (team == "R2") {
                if (runtime.seconds() > 1){
                    drive("circle right");
                }
            } else if (team == "B1"){
                if (runtime.seconds() > 2){
                    drive_code(1,0,0);
                }
            } else if (team == "R1"){
                if (runtime.seconds() > 2){
                    drive_code(-1,0,0);
                }
            }

        wait(1000);

            runtime.reset();
            if (team == "B1"){
                while  (runtime.seconds() > .5){
                    drive_code(0,1,0);
                }
            } else if (team == "R1"){
                if (runtime.seconds() > .5){
                    drive("up");
                }
            } else if (team == "R2"){
                if (runtime.seconds() > 2){
                    drive("up");
                }
            } else if (team == "B2"){
                if (runtime.seconds() > 2){
                    drive("up");
                }
            }

       wait(1000);

            clawr.setPosition(0);
            clawl.setPosition(0);

            stop();





            /** Use telemetry to display feedback on the driver station. We show the conversion
             * of the colors to hue, saturation and value, and display the the normalized values
             * as returned from the sensor.
             * @see <a href="http://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html">HSV</a>*/

            Color.colorToHSV(colors.toColor(), hsvValues);
            telemetry.addLine()
                    .addData("H", "%.3f", hsvValues[0])
                    .addData("S", "%.3f", hsvValues[1])
                    .addData("V", "%.3f", hsvValues[2]);
            telemetry.addLine()
                    .addData("a", "%.3f", colors.alpha)
                    .addData("r", "%.3f", colors.red)
                    .addData("g", "%.3f", colors.green)
                    .addData("b", "%.3f", colors.blue);

            /** We also display a conversion of the colors to an equivalent Android color integer.
             * @see Color */
            int color = colors.toColor();
            telemetry.addLine("raw Android color: ")
                    .addData("a", "%02x", Color.alpha(color))
                    .addData("r", "%02x", Color.red(color))
                    .addData("g", "%02x", Color.green(color))
                    .addData("b", "%02x", Color.blue(color));

            // Balance the colors. The values returned by getColors() are normalized relative to the
            // maximum possible values that the sensor can measure. For example, a sensor might in a
            // particular configuration be able to internally measure color intensity in a range of
            // [0, 10240]. In such a case, the values returned by getColors() will be divided by 10240
            // so as to return a value it the range [0,1]. However, and this is the point, even so, the
            // values we see here may not get close to 1.0 in, e.g., low light conditions where the
            // sensor measurements don't approach their maximum limit. In such situations, the *relative*
            // intensities of the colors are likely what is most interesting. Here, for example, we boost
            // the signal on the colors while maintaining their relative balance so as to give more
            // vibrant visual feedback on the robot controller visual display.
            float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
            colors.red /= max;
            colors.green /= max;
            colors.blue /= max;
            color = colors.toColor();

            telemetry.addLine("normalized color:  ")
                    .addData("a", "%02x", Color.alpha(color))
                    .addData("r", "%02x", Color.red(color))
                    .addData("g", "%02x", Color.green(color))
                    .addData("b", "%02x", Color.blue(color));
            telemetry.update();

            // convert the RGB values to HSV values.
            Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), hsvValues);
        }


    }
}


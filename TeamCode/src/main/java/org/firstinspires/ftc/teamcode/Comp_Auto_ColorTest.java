package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by kids on 11/2/2017.
 * BLUE 1 AUTONOMOUS
 */
@Autonomous(name="Auto: Color Test", group= "Pushbot")
public class Comp_Auto_ColorTest extends Competition_Hardware_Relic {

    @Override  public void runOpMode() {

        telemetry.addData("start","before init");
        init(hardwareMap);
        relicTrackables.activate();
        waitForStart();


        runtime.reset();
        // put in code to check ball color


            drive_code(0,0,0,1);




    }
}
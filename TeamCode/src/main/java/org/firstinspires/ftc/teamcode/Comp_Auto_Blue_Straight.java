package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by kids on 12/3/2017.
 */

@Autonomous(name="Auto: Blue Straight", group= "Pushbot")
public class Comp_Auto_Blue_Straight extends Competition_Hardware_Relic {

    @Override  public void runOpMode() {

        telemetry.addData("start","before init");
        init(hardwareMap);
        relicTrackables.activate();
        waitForStart();


        //driving up for 2 seconds
        runtime.reset();
        autoMode("blueStraight");

        drive_code(0,0,0);




    }
}

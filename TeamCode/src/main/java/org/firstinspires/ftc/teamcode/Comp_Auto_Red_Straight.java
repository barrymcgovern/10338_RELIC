package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by kids on 12/3/2017.
 */

@Autonomous(name="Auto: Red Straight", group= "Pushbot")
public class Comp_Auto_Red_Straight extends Competition_Hardware_Relic {

    @Override  public void runOpMode() {

        telemetry.addData("start","before init");
        init(hardwareMap);

        try{
            relicTrackables.activate();
        }catch (Exception e){
            telemetry.addData("relic trackers ERROR", e.toString());
            telemetry.update();

        }
        waitForStart();


        //driving up for 2 seconds
        runtime.reset();
        autoMode("redStraight");

        drive_code(0,0,0,1);




    }
}
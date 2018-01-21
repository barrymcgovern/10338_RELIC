package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by kids on 11/2/2017.
 * BLUE 1 AUTONOMOUS
 */
@Autonomous(name="Auto: VUF Test", group= "Pushbot")
public class Comp_Auto_VUFTest extends Competition_Hardware_Relic {

    @Override  public void runOpMode() {

        telemetry.addData("start","before init");
        init(hardwareMap);
        relicTrackables.activate();
        waitForStart();


        //driving up for 2 seconds
        runtime.reset();
        vuforia_Drive("redStraight");

        drive_code(0,0,0);




    }
}
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by kids on 11/2/2017.
 * BLUE 1 AUTONOMOUS
 */
//@Autonomous(name="Auto: Expand Test", group= "Pushbot")
public class Comp_Auto_ExpandTest extends Competition_Hardware_Relic {

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
        drive_code(0,0,0,1);
        }catch (Exception e){
            telemetry.addData("run ERROR", e.toString());
            telemetry.update();

        }



    }
}
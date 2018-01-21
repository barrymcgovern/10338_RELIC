package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by kids on 11/2/2017.
 * BLUE 1 AUTONOMOUS
 */
@Autonomous(name="Auto: Red Turn", group= "Pushbot")
public class Comp_Auto_Red_Turn extends Competition_Hardware_Relic {

    @Override  public void runOpMode() {

        telemetry.addData("start","before init");
        init(hardwareMap);
        relicTrackables.activate();
        waitForStart();


        //driving up for 2 seconds
        runtime.reset();
        autoMode("redTurn");

        drive_code(0,0,0);




    }
}
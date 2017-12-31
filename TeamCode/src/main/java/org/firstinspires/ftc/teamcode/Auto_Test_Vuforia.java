package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by kids on 12/29/2017.
 */
@Autonomous (name="Auto: Test_Vuforia", group= "Pushbot")
public class Auto_Test_Vuforia extends Competition_Hardware_Relic {
    @Override  public void runOpMode() {
        RelicRecoveryVuMark vuMark;
        telemetry.addData("start", "before init");
        init(hardwareMap);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        waitForStart();
        relicTrackables.activate();
        while (opModeIsActive()){
            try{

            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                telemetry.addData("Vuforia Position", vuMark);
                }else{
                telemetry.addData("pos","unknown");
            }
            }catch (Exception e){
                telemetry.addData(" ERROR", e.toString());

            }
            telemetry.update();


        }

    }
}

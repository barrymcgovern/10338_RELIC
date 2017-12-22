package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

/**
 * Created by kids on 10/11/2017.
 */

@TeleOp(name="Comp: Relic Main", group= "Pushbot")

public class Competition_Op_Mode_Relic extends Competition_Hardware_Relic  {
    NormalizedRGBA colors;
    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            try{

                if (gamepad1.dpad_left){
                    drive_code(-1, 0, 0);

                } else if (gamepad1.dpad_right){
                    drive_code(1, 0, 0);
                } else {
                    drive_code(gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x);
                }


            } catch (Exception p_exception) {
                telemetry.addData("op mode error", "drive " + p_exception.toString());

            }

            try {
                colors = colorSensor.getNormalizedColors();
                telemetry.addData("blue", colors.blue);
                telemetry.addData("red", colors.red);
            } catch (Exception p_exception) {
                telemetry.addData("op mode error","color " +  p_exception.toString());

            }


            try{

                if (gamepad2.left_stick_y > .1){
                    stackmotor.setPower(-50);
                    telemetry.addData("down","t");

                    down = 1;

                } else if (gamepad2.left_stick_y < -.1) {
                    stackmotor.setPower(50);
                    down = 0;
                    telemetry.addData("down","f");
                }else {
                    if (down > 0 && down < 3) {
                        stackmotor.setPower(75);
                        telemetry.addData("down","stopping");
                        down ++;
                    } else {
                        stackmotor.setPower(0);
                        telemetry.addData("down","false");
                        down = 0;
                    }
                }
            } catch (Exception p_exception) {
                telemetry.addData("op mode error","stacker " +  p_exception.toString());

            }
            try{
                telemetry.addData("servo color left", servoColorLeft.getPosition());
                if (gamepad2.b){
                    servoColorLeft.setPosition(0);
                }
            } catch (Exception p_exception) {
                telemetry.addData("op mode error", "servo color" + p_exception.toString());
            }

            try {
                telemetry.addData("claw right", clawr.getPosition());
                telemetry.addData("claw left", clawl.getPosition());
                if (gamepad2.left_bumper) {
                    clawr.setPosition(clawREnd);
                    clawl.setPosition(clawLEnd);

                } else if (gamepad2.right_bumper) {
                    clawr.setPosition(clawRStart);
                    clawl.setPosition(clawLStart);
                }


            } catch (Exception p_exception) {
                telemetry.addData("op mode error", "claw" + p_exception.toString());
            }

            telemetry.addData("y x" ,gamepad1.left_stick_x + "  " + gamepad1.right_stick_y);
            telemetry.addData("motor front left",motorfl.getDirection() + "  " + motorfl.getPower());
            telemetry.addData("motor front right",motorfr.getDirection() + "  " + motorfr.getPower());
            telemetry.addData("motor back left",motorbl.getDirection() + "  " + motorbl.getPower());
            telemetry.addData("motor back right",motorbr.getDirection() + "  " + motorbr.getPower());
           telemetry.update();

        }



    }

}




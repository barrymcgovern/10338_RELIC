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
        drive_code(0, 0, 0,1);
        while (opModeIsActive()) {

            try{
                if (gamepad1.dpad_left){
                    drive_code(1, 0, 0,1);

                } else if (gamepad1.dpad_right){
                    drive_code(-1, 0, 0,1);
                } else {
                    drive_code(-gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x,1);
                }

            } catch (Exception p_exception) {
                telemetry.addData("op mode error", "drive " + p_exception.toString());

            }

            try{
                telemetry.addData("position", stackmotor.getCurrentPosition());
                telemetry.addData("max ",maxPosStackMotor);
                telemetry.addData("min",minPosStackMotor);
                telemetry.addData("stackmotor",stackmotor.getPower());
                if (gamepad2.left_stick_y < -.1){
                    rowToGoTo = 0;
                    telemetry.addData("stack dire","up");

                    if (stackmotor.getCurrentPosition() > maxPosStackMotor) {
                        stackmotor.setPower(-.5);
                    }else{
                        telemetry.addData("stack dire","up to far");
                        stackmotor.setPower(-.15);
                    }


                } else if (gamepad2.left_stick_y > .1) {
                    rowToGoTo = 0;
                    telemetry.addData("stack dire","down");

                    if (stackmotor.getCurrentPosition() < minPosStackMotor) {
                        stackmotor.setPower(.05);

                    }else{
                        telemetry.addData("stack dire","down to far");
                        stackmotor.setPower(-.15);
                    }

                }else {
                    try{

                        if (gamepad2.a) {
                            rowToGoTo = 1;
                            targetPosition = row1;
                            if (targetPosition > stackmotor.getCurrentPosition()) {
                                rowDirection = "down";
                            }  else  if (targetPosition < stackmotor.getCurrentPosition()) {
                                rowDirection = "up";
                            }

                        }
                        else if (gamepad2.b){
                            rowToGoTo = 2;
                            targetPosition = row2;
                            if (targetPosition > stackmotor.getCurrentPosition()) {
                                rowDirection = "down";
                            }  else  if (targetPosition < stackmotor.getCurrentPosition()) {
                                rowDirection = "up";
                            }

                        }
                        else if (gamepad2.x){
                            rowToGoTo =3;
                            targetPosition = row3;
                            if (targetPosition > stackmotor.getCurrentPosition()) {
                                rowDirection = "down";
                            }  else  if (targetPosition < stackmotor.getCurrentPosition()) {
                                rowDirection = "up";
                            }
                        }
                        else if (gamepad2.y) {
                            rowToGoTo = 4;
                            targetPosition = row4;
                            if (targetPosition > stackmotor.getCurrentPosition()) {
                                rowDirection = "down";
                            }  else  if (targetPosition < stackmotor.getCurrentPosition()) {
                                rowDirection = "up";
                            }
                        }
                        telemetry.addData("rowToGoTo", rowToGoTo);
                        telemetry.addData("rowDirection", rowDirection);
                        telemetry.addData("stackPower", stackmotor.getPower());
                        if(rowToGoTo >0){
                            telemetry.addData("stack dire","rowtogoto" + rowToGoTo);

                            if (rowDirection == "up"){
                                if(targetPosition > stackmotor.getCurrentPosition()){
                                    rowToGoTo=0;
                                    stackmotor.setPower(-.15);

                                }else{
                                    stackmotor.setPower(-.5);
                                }

                            }else if (rowDirection == "down"){
                                if(targetPosition < stackmotor.getCurrentPosition()){
                                    rowToGoTo=0;
                                    stackmotor.setPower(-.15);
                                }else{
                                    stackmotor.setPower(.05);
                                }

                            }
                        }else{
                            telemetry.addData("stack dire","holding");
                            stackmotor.setPower(-.15);
                        }


                    } catch (Exception p_exception){
                        telemetry.addData("op mode error", "stacker buttons" + p_exception.toString());
                    }

                }
            } catch (Exception p_exception) {
                telemetry.addData("op mode error","stacker " +  p_exception.toString());

            }

            try {
                  /*

                    if not startSpin then
                              startPin = true;
                             startSpin time = run.reset

                    if runtime < spinTime - turen

                     */


                if (gamepad1.a) {
                    startSpin = true;
                    runtime.reset();
                }
                if (startSpin == true) {
                    if (runtime.seconds() < 3) {
                        drive_code(0, 0, -1,1);
                    }
                    if (runtime.seconds() > 3){
                        startSpin = false;
                        drive_code(0, 0, 0,1);
                    }
                }

            }catch (Exception p_exception){
                telemetry.addData("op mode error","spin" + p_exception.toString());
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
                clawLCurPos = clawl.getPosition();
                clawRCurPos = clawr.getPosition();

                telemetry.addData("claw right", clawRCurPos);
                telemetry.addData("claw left", clawLCurPos);

                if (gamepad2.left_bumper) {
                    clawr.setPosition(clawREnd); // 1
                    clawl.setPosition(clawLEnd); // 0

                } else if (gamepad2.right_bumper) {
                    clawr.setPosition(clawRStart); // .75
                    clawl.setPosition(clawLStart); // .25
                }else{
                    if (gamepad2.left_trigger > .1) {// squezzing
                        if (clawRCurPos< 1){
                            clawr.setPosition( (clawRCurPos + .01));
                        }
                       if (clawLCurPos > 0){
                           clawl.setPosition( ( clawLCurPos - .01)) ;
                        }

                    }else if (gamepad2.right_trigger > .1) {// opening
                        if (clawRCurPos > .5){
                            clawr.setPosition((clawRCurPos - .01));
                        }
                        if (clawLCurPos < .5){
                            clawl.setPosition((clawLCurPos + .01));
                        }

                    }
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




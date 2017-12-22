package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


/**
 * Team: Dark Matters #10338
 * Velocity Vortex Competition
 *
 * Competition Op Mode
 * Our primary program for running tele op mode
 * GamePad 1 uses stick to drive.
 *  left stick drives up down right left
 *  right stick will spin the robot
 * GamePad 2 operates the elevator and pitching machine
 *
 *
 *
 */
@TeleOp(name="Comp: Old Robot", group= "Pushbot")
//Hardware with circling and manipulator controls.
public class Competition_Op_Mode extends Competition_Hardware {

    @Override
    public void runOpMode() throws InterruptedException {
        init(hardwareMap);


        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            //uses left and right direction of right joystick to circle left or right
            if (gamepad1.right_stick_x > 0) {
                speed = (gamepad1.right_stick_x);
                drive("circle left");
            } else if (gamepad1.right_stick_x < 0) {
                speed = -(gamepad1.right_stick_x);
                drive("circle right");
            } else {
                //A program that will control driving direction and speed using left joystick
                driveStick(gamepad1.left_stick_x, gamepad1.left_stick_y);
            }

            //Left joystick on gamepad 2 controls pitching machine and ball elevator
            if (gamepad2.left_stick_y < 0) {
                beMotor.setPower(-ELEVATOR_SPEED);

            } else if (gamepad2.left_stick_y > 0) {
                beMotor.setPower(ELEVATOR_SPEED);

            } else {
                beMotor.setPower(0);

            }
            /*
            //Manipulator controller's dpad controls the forklift motion
            if (gamepad2.dpad_up) {
                fkMotor.setPower(-FORKLIFT_SPEED);

            } else if (gamepad2.dpad_down) {
                fkMotor.setPower(FORKLIFT_SPEED);

            } else {
                fkMotor.setPower(0);
            }
            */
            //Right joystick on gamepad 2 operates pitching machine

            if (gamepad2.right_stick_y < 0) {
                pMotor1.setPower(SPIN_SPEED);
                pMotor2.setPower(-SPIN_SPEED);

            } else {
                pMotor1.setPower(0);
                pMotor2.setPower(0);
            }
            telemetry.update();
        }
    }
}



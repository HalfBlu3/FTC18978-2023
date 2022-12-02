package org.firstinspires.ftc.teamcode.opmodeIGuess;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "LinearTest", group = "whatever")
public class LinearTest extends LinearOpMode {
    DcMotor FLeft, FRight, BLeft, BRight;

    @Override
    public void runOpMode() {
        FLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        FRight = hardwareMap.get(DcMotor.class, "FrontRight");
        BLeft = hardwareMap.get(DcMotor.class, "BackLeft");
        BRight = hardwareMap.get(DcMotor.class, "BackRight");
        double max;
        waitForStart();
        while (opModeIsActive() && !b()) {
            double pa = leftStickX();
            double px = -leftStickY();
            double py = -rightStickX();

            if (Math.abs(pa) < 0.05) pa = 0;
            double pFLeft = -px + py - pa;
            double pFRight = px + py -pa;
            double pBLeft = -px + py + pa;
            double pBRight = px + py + pa;
            max = Math.max(1.0, Math.max(Math.abs(pFLeft), Math.max(Math.abs(pFRight), Math.max(Math.abs(pBLeft), Math.abs(pBRight)))));
            pFLeft /= max;
            pFRight /= max;
            pBLeft /= max;
            pBRight /= max;
            FLeft.setPower(pFLeft);
            FRight.setPower(pFRight);
            BLeft.setPower(pBLeft);
            BRight.setPower(pBRight);
        }
    }

    public boolean RightTrigger(){return(gamepad1.right_trigger == 0 || gamepad2.right_trigger == 0);}

    public boolean LeftTrigger(){return(gamepad1.left_trigger == 0 || gamepad2.left_trigger == 0);}

    public boolean a(){return gamepad1.a || gamepad2.a;}

    public boolean b(){return gamepad1.b || gamepad2.b;}

    public boolean dUp(){return gamepad1.dpad_up || gamepad2.dpad_up;}

    public boolean dDown(){return gamepad1.dpad_down || gamepad2.dpad_down;}

    public boolean dLeft(){return gamepad1.dpad_left || gamepad2.dpad_left;}

    public boolean dRight(){return gamepad1.dpad_right || gamepad2.dpad_right;}

    public double leftStickX(){if (gamepad1.left_stick_x == 0) return gamepad2.left_stick_x; else return gamepad1.left_stick_x;}

    public double leftStickY(){if (gamepad1.left_stick_y == 0) return gamepad2.left_stick_y; else return gamepad1.left_stick_y;}

    public double rightStickX(){if (gamepad1.right_stick_x == 0) return gamepad2.right_stick_x; else return gamepad1.right_stick_x;}

    public double rightStickY(){if (gamepad1.right_stick_y == 0) return gamepad2.right_stick_y; else return gamepad1.right_stick_y;}

    //triggers spin table
    //bumpers control claw
    //dap to move arm
}

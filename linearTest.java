package org.firstinspires.ftc.teamcode.opmodeIGuess;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "LinearTest", group = "whatever")
public class LinearTest extends LinearOpMode {
    DcMotor FLeft, FRight, BLeft, BRight, Arm, Turret, Patrick;
    Servo Claw;

    @Override
    public void runOpMode() {
        FLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        FRight = hardwareMap.get(DcMotor.class, "FrontRight");
        BLeft = hardwareMap.get(DcMotor.class, "BackLeft");
        BRight = hardwareMap.get(DcMotor.class, "BackRight");
        LowerArm = hardwareMap.get(DcMotor.class, "Arm");
        Turret = hardwareMap.get(DcMotor.class, "Turret");
        UpperArm = hardwareMap.get(DcMotor.class, "Patrick");

        Claw = hardwareMap.get(Servo.class, "Claw");
        double max;
        waitForStart();
        while (opModeIsActive() && !x()) {
            double pa = leftStickX();
            double px = Math.round(-leftStickY()/2);
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

            Claw.setPosition(a() ? 1 : 0.2);
            Arm.setPower(dUp() ? 1 : dDown() ? 1 : 0);
            Turret.setPower(lBumper() ? 0.5 : rBumper() ? -0.5 : 0);
            Patrick.setPower(LTrigger() ? 1 : RTrigger() ? -1 : 0);
        }
    }

    public boolean RTrigger(){return(gamepad1.right_trigger != 0 || gamepad2.right_trigger != 0);}
    public boolean LTrigger(){return(gamepad1.left_trigger != 0 || gamepad2.left_trigger != 0);}
    public boolean a(){return gamepad1.a || gamepad2.a;}
    public boolean b(){return gamepad1.b || gamepad2.b;}
    public boolean x(){return gamepad1.x || gamepad2.x;}
    public boolean y(){return gamepad1.y || gamepad2.y;}
    public boolean dUp(){return gamepad1.dpad_up || gamepad2.dpad_up;}
    public boolean dDown(){return gamepad1.dpad_down || gamepad2.dpad_down;}
    public boolean dLeft(){return gamepad1.dpad_left || gamepad2.dpad_left;}
    public boolean dRight(){return gamepad1.dpad_right || gamepad2.dpad_right;}
    public boolean rBumper(){return gamepad1.right_bumper || gamepad2.right_bumper;}
    public boolean lBumper(){return gamepad1.left_bumper || gamepad2.left_bumper;}
    public double leftStickX(){if (gamepad1.left_stick_x == 0) return gamepad2.left_stick_x; else return gamepad1.left_stick_x;}
    public double leftStickY(){if (gamepad1.left_stick_y == 0) return gamepad2.left_stick_y; else return gamepad1.left_stick_y;}
    public double rightStickX(){if (gamepad1.right_stick_x == 0) return gamepad2.right_stick_x; else return gamepad1.right_stick_x;}
    public double rightStickY(){if (gamepad1.right_stick_y == 0) return gamepad2.right_stick_y; else return gamepad1.right_stick_y;}
}

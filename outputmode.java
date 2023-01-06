package org.firstinspires.ftc.teamcode.opmodeIGuess;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "outputmode", group = "whatever")
public class outputmode extends LinearOpMode {
    DcMotor FLeft, FRight, BLeft, BRight, UpperArm, Turret, LowerArm;
    Servo Claw;

    @Override
    public void runOpMode() {
        FLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        FRight = hardwareMap.get(DcMotor.class, "FrontRight");
        BLeft = hardwareMap.get(DcMotor.class, "BackLeft");
        BRight = hardwareMap.get(DcMotor.class, "BackRight");
        LowerArm = hardwareMap.get(DcMotor.class, "LowerArm");
        Turret = hardwareMap.get(DcMotor.class, "Turret");
        UpperArm = hardwareMap.get(DcMotor.class, "UpperArm");
        Claw = hardwareMap.get(Servo.class, "Claw");
        double max;
        double sri=0;
        waitForStart();
        while (opModeIsActive() && (!x() && !gamepad2.x)) {
            //gamepad1
            double pa = leftStickX();
            double px = Math.round(-leftStickY()/2);
            double py = -rightStickX();

            if (Math.abs(pa) < 0.05) pa = 0;
            double pFLeft = -px + py -pa;
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

            //gamepad2
            LowerArm.setPower(gamepad2.left_stick_y);
            UpperArm.setPower(gamepad2.right_stick_y);
            Turret.setPower((gamepad2.left_trigger > 0) ? 0.5 : (gamepad2.right_trigger > 0) ? -0.5 : 0);
            Claw.setPosition((gamepad2.right_bumper || gamepad2.left_bumper) ? 1 : 0.2);

            //print stuff or something
            if (a() || gamepad2.a) {
                telemetry.addData("Upper Arm", UpperArm.getCurrentPosition());
                telemetry.addData("Lower Arm", LowerArm.getCurrentPosition());
                telemetry.addData("Turret", Turret.getCurrentPosition());
                telemetry.update();
            }

            if (y() || gamepad2.y){
                UpperArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                LowerArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                UpperArm.setTargetPosition(0);
                LowerArm.setTargetPosition(0);
                UpperArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                LowerArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            }
        }
    }
    public boolean RTrigger(){return(gamepad1.right_trigger != 0);}
    public boolean LTrigger(){return(gamepad1.left_trigger != 0);}
    public boolean a(){return gamepad1.a;}
    public boolean b(){return gamepad1.b;}
    public boolean x(){return gamepad1.x;}
    public boolean y(){return gamepad1.y;}
    public boolean dUp(){return gamepad1.dpad_up;}
    public boolean dDown(){return gamepad1.dpad_down;}
    public boolean dLeft(){return gamepad1.dpad_left;}
    public boolean dRight(){return gamepad1.dpad_right;}
    public boolean rBumper(){return gamepad1.right_bumper;}
    public boolean lBumper(){return gamepad1.left_bumper;}
    public double leftStickX(){return gamepad1.left_stick_x;}
    public double leftStickY(){return gamepad1.left_stick_y;}
    public double rightStickX(){return gamepad1.right_stick_x;}
    public double rightStickY(){return gamepad1.right_stick_y;}
}

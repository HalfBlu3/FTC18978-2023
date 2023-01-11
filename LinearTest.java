package org.firstinspires.ftc.teamcode.opmodeIGuess;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name = "outputmode", group = "whatever")
public class outputmode extends LinearOpMode {
    DcMotor FLeft, FRight, BLeft, BRight, UpperArm, Turret, LowerArm;
    Servo Claw, Wrist;
    TouchSensor ButtonOne, ButtonTwo;

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
        Wrist = hardwareMap.get(Servo.class, "Wrist");
        ButtonOne = hardwareMap.touchSensor.get("ButtonOne");
        ButtonTwo = hardwareMap.touchSensor.get("ButtonTwo");

        double max;
        final int UpperPos = UpperArm.getCurrentPosition();
        final int LowerPos = LowerArm.getCurrentPosition();
        waitForStart();
        while (opModeIsActive() && (!(gamepad1.x || gamepad2.x))) {
            double py = gamepad1.right_stick_x;
            double px = Math.round(gamepad1.left_stick_x);
            double pa = -gamepad1.left_stick_y;

            if (Math.abs(pa) < 0.05) pa = 0;
            double pFLeft = px + py -pa;
            double pFRight = -px + py + pa;
            double pBLeft = -px + py - pa;
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
            if (gamepad1.a || gamepad2.a) {
                telemetry.addData("Upper Arm", UpperArm.getCurrentPosition());
                telemetry.addData("Lower Arm", LowerArm.getCurrentPosition());
                telemetry.addData("Turret", Turret.getCurrentPosition());
                telemetry.addData("ButtonOne", ButtonOne.isPressed());
                telemetry.addData("ButtonTwo", ButtonTwo.isPressed());
                telemetry.update();
            }
            if (gamepad2.y){
                while (UpperArm.getCurrentPosition() != UpperPos) {
                    if (UpperArm.getCurrentPosition() > UpperPos){
                        UpperArm.setPower(-0.4);
                    } else {
                        UpperArm.setPower(0.4);
                    }
                }
                UpperArm.setPower(0);
                while (LowerArm.getCurrentPosition() != LowerPos) {
                    if (LowerArm.getCurrentPosition() > LowerPos){
                        LowerArm.setPower(-0.4);
                    } else {
                        LowerArm.setPower(0.4);
                    }
                }
                LowerArm.setPower(0);
                Wrist.setPosition(0);
            }
        }
    }
}

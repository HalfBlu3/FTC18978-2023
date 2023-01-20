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
        final int TurretPos = Turret.getCurrentPosition();
        final double WristPos = Wrist.getPosition();

        //target positions for arm presets
        final int UpperArmDown = UpperPos - 2091;
        final int LowerArmDown = LowerPos + 8522;
        final int UpperArmUp = 0; //TODO
        final int LowerArmUp = 0; //TODO
        int mode = 0;
        waitForStart();
        while (opModeIsActive() && (!(gamepad1.x || gamepad2.x))) {
            //mechanum math stuff
            double py = -gamepad1.right_stick_x;
            double px = Math.round(gamepad1.left_stick_x);
            double pa = -gamepad1.left_stick_y;
            if (Math.abs(pa) < 0.05) pa = 0;
            double pFLeft = px + py - pa;
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

            //turret and arm controls
            //sets turret to how far the trigger is pressed, but all the way is too fast. I might add a hard limit and have a separate speed button
            Turret.setPower((gamepad2.left_trigger > 0) ? gamepad2.left_trigger : (gamepad2.right_trigger > 0) ? -gamepad2.right_trigger : 0);
            Claw.setPosition(gamepad2.right_bumper ? 0.5 : -0.1);
            Wrist.setPosition((gamepad2.left_bumper) ? 0 : 1); //wrist doesn't currently work

            if (mode ==0){ //no preset
                //currently you cannot manually control motors while preset is being enacted. this limit is for testing and will be removed later
                LowerArm.setPower(gamepad2.left_stick_y);
                UpperArm.setPower(gamepad2.right_stick_y);
                //only check for preset input if we are not currently moving to a preset
                mode = (gamepad2.dpad_down ? 1 : gamepad2.dpad_up ? 2: 0);
            } else if (mode == 1){ //low preset
                UpperArm.setPower(UpperArm.getCurrentPosition() > UpperArmDown ? -1 : 0);
                LowerArm.setPower(LowerArm.getCurrentPosition() < LowerArmDown ? 1 : 0);
                if (UpperArm.getCurrentPosition() <= UpperArmDown && LowerArm.getCurrentPosition() >= LowerArmDown) mode = 0;
            } else if (mode == 2){ //high preset
                UpperArm.setPower(UpperArm.getCurrentPosition() < UpperArmUp ? 1 : 0);
                LowerArm.setPower(LowerArm.getCurrentPosition() > LowerArmUp ? -1 : 0);
                if (UpperArm.getCurrentPosition() >= UpperArmUp && LowerArm.getCurrentPosition() <= LowerArmUp) mode = 0;
            }

            //print encoder info
            if (gamepad1.a || gamepad2.a) {
                telemetry.addData("Upper Arm start", UpperPos);
                telemetry.addData("Upper Arm current", UpperArm.getCurrentPosition());
                telemetry.addData("Lower Arm start", LowerPos);
                telemetry.addData("Lower Arm current", LowerArm.getCurrentPosition());
                telemetry.addData("Turret", Turret.getCurrentPosition());
                telemetry.addData("ButtonOne", ButtonOne.isPressed());
                telemetry.addData("ButtonTwo", ButtonTwo.isPressed());
                telemetry.addData("Claw" , Claw.getPosition());
                telemetry.update();
            }

            //arm reset
            if (gamepad1.y || gamepad2.y){
                //do upper arm first and then lower arm
                //I might eventually be able to condense this to one ternary in a WHILE loop, I just haven't gotten around to it
                while (UpperArm.getCurrentPosition() != UpperPos) UpperArm.setPower(UpperArm.getCurrentPosition() > UpperPos ? -0.7 : 0.7);
                UpperArm.setPower(0);
                while (LowerArm.getCurrentPosition() != LowerPos) LowerArm.setPower(LowerArm.getCurrentPosition() > LowerPos ? -0.7 : 0.7);
                LowerArm.setPower(0);
                Wrist.setPosition(WristPos); //wrist doesn't actually work
            }
        }
    }
}

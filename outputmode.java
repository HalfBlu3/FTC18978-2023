package org.firstinspires.ftc.teamcode.opmodeIGuess;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@SuppressWarnings("unused")
@TeleOp(name = "outputmode", group = "whatever")
public class outputmode extends LinearOpMode {
    DcMotor FLeft, FRight, BLeft, BRight, UpperArm, Turret, LowerArm, Wrist;
    Servo Claw;
    TouchSensor ButtonOne, ButtonTwo;
    public final int forgivenessBuffer = 15;

    @Override
    public void runOpMode() {
        FLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        FRight = hardwareMap.get(DcMotor.class, "FrontRight");
        BLeft = hardwareMap.get(DcMotor.class, "BackLeft");
        BRight = hardwareMap.get(DcMotor.class, "BackRight");
        LowerArm = hardwareMap.get(DcMotor.class, "LowerArm");
        Turret = hardwareMap.get(DcMotor.class, "Turret");
        UpperArm = hardwareMap.get(DcMotor.class, "UpperArm");
        Wrist = hardwareMap.get(DcMotor.class, "Wrist");
        Claw = hardwareMap.get(Servo.class, "Claw");
        ButtonOne = hardwareMap.touchSensor.get("ButtonOne");
        ButtonTwo = hardwareMap.touchSensor.get("ButtonTwo");

        double max;
        final int UpperPos = UpperArm.getCurrentPosition();
        final int LowerPos = LowerArm.getCurrentPosition();
        final int TurretPos = Turret.getCurrentPosition();
        final int WristPos = Wrist.getCurrentPosition();
        double speed;

        //target positions for arm presets
        final int UpperArmDown = UpperPos - 2091;
        final int LowerArmDown = LowerPos + 8522;
        final int UpperArmUp = UpperPos - 4396;
        final int LowerArmUp = LowerPos + 4154;
        final int WristTarget = (int) Math.round(WristPos + (384.5/4));
        int mode = 0;

        waitForStart();

        /*
        while (Wrist.getCurrentPosition() != WristTarget){
            Wrist.setPower(value(0.7, Wrist.getCurrentPosition(), WristTarget));
        }
        Wrist.setPower(0);
        */

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
            speed = (gamepad2.b ? 1.6 : 1);
            Turret.setPower((gamepad2.left_trigger > 0 && !ButtonTwo.isPressed()) ? 0.5*speed : (gamepad2.right_trigger > 0 && !ButtonOne.isPressed()) ? -0.5*speed : 0);
            Claw.setPosition(gamepad2.right_bumper ? 0.8 : 0.0);
            Wrist.setPower(gamepad1.left_bumper ? 1 : gamepad1.right_bumper? -1 : 0);

            //currently you cannot manually control motors while preset is being enacted. this limit is for testing and will be removed later
            UpperArm.setPower(mode == 0 ? gamepad2.right_stick_y : mode == 1 && !InRange(UpperArm.getCurrentPosition(), UpperArmDown) ? calcVal(1, UpperArm.getCurrentPosition(), UpperArmDown) : mode == 2 && !InRange(UpperArm.getCurrentPosition(), UpperArmUp) ? calcVal(1, UpperArm.getCurrentPosition(), UpperArmDown) : 0);
            LowerArm.setPower(mode == 0 ? gamepad2.left_stick_y : mode == 1 && !InRange(LowerArm.getCurrentPosition(), LowerArmDown) ? calcVal(1, UpperArm.getCurrentPosition(), UpperArmDown) : mode == 2 && !InRange(LowerArm.getCurrentPosition(), LowerArmUp) ? calcVal(1, UpperArm.getCurrentPosition(), UpperArmDown) : 0);
            mode = (mode == 0 && gamepad2.dpad_down ? 1 : mode == 0 && gamepad2.dpad_up ? 2 : (mode == 1 && InRange(UpperArm.getCurrentPosition(), UpperArmDown) && InRange(LowerArm.getCurrentPosition(), LowerArmDown)) || (mode == 2 && InRange(UpperArm.getCurrentPosition(), UpperArmUp) && InRange(LowerArm.getCurrentPosition(), LowerArmUp)) ? 0 : mode);

            //print encoder info
            if (gamepad1.a || gamepad2.a) {
                telemetry.addData("Mode", mode);
                telemetry.addData("Upper Arm start", UpperPos);
                telemetry.addData("Upper Arm current", UpperArm.getCurrentPosition());
                telemetry.addData("Lower Arm start", LowerPos);
                telemetry.addData("Lower Arm current", LowerArm.getCurrentPosition());
                telemetry.addData("Turret", Turret.getCurrentPosition());
                telemetry.addData("ButtonOne", ButtonOne.isPressed());
                telemetry.addData("ButtonTwo", ButtonTwo.isPressed());
                telemetry.addData("Claw" , Claw.getPosition());
                telemetry.addData("Wrist", Wrist.getCurrentPosition());
                telemetry.addData("WristPos", WristPos);
                telemetry.update();
            }

            //arm reset
            if (gamepad1.y || gamepad2.y){
                //moves the turret, then upper arm, then lower arm, then wrist
                while (Turret.getCurrentPosition() != TurretPos) Turret.setPower(Turret.getCurrentPosition() > TurretPos ? -0.6 : 0.6);
                Turret.setPower(0);
                while (UpperArm.getCurrentPosition() != UpperPos) UpperArm.setPower(UpperArm.getCurrentPosition() > UpperPos ? -1 : 1);
                UpperArm.setPower(0);
                while (LowerArm.getCurrentPosition() != LowerPos) LowerArm.setPower(LowerArm.getCurrentPosition() > LowerPos ? -1 : 1);
                LowerArm.setPower(0);
                /*
                while (Wrist.getCurrentPosition() != WristPos) Wrist.setPower(value(0.7, Wrist.getCurrentPosition(), WristPos));
                Wrist.setPower(0);
                */
            }
        }
    }

    public boolean InRange(double position, double target){
        return ((position <= target+forgivenessBuffer) && (position >= target-forgivenessBuffer));
    }

    public double calcVal(double speed, int position, int target){
        //if (InRange(position, target)) return 0;
        if ((position <= target+300) && (position >= target-300)){
            double newSpeed = speed/(300.00/(target-position));
            return (Math.max(newSpeed, 0.1));
        } else return speed;
    }

    public double calcVal(double speed, int threshold, int position, int target){
        //if (InRange(position, target)) return 0;
        if ((position <= target+threshold) && (position >= target-threshold)){
            double newSpeed = speed/(100.00/(target-position));
            return (Math.max(newSpeed, 0.1));
        } else return speed;
    }
}

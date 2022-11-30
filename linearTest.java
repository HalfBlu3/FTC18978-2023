package org.firstinspires.ftc.teamcode.opmodeIGuess;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class linearTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        waitForStart();
        while (!gamepad1.a) sleep(100);
        telemetry.addData("buttonA", "pressed");
        telemetry.update(); //actually updates the console
    }
}

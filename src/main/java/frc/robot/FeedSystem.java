package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class FeedSystem implements Runnable {
    public static int count2 = 0;
    //declares motor controllers and assigns
    static WPI_TalonSRX leftGrab = new WPI_TalonSRX(Drive.motorPorts[8]);
  static  WPI_TalonSRX rightGrab = new WPI_TalonSRX(Drive.motorPorts[9]);
    static WPI_TalonFX elevator = new WPI_TalonFX(Drive.motorPorts[10]);
    public static int count = 0;
    public void run() {

        while(true){{
if(Robot.joystickButton(6)){
    leftGrab.set(.8);


}else{
    leftGrab.set(0);
}

        if(Robot.joystickButton(3)){
           
            
            
            rightGrab.set(.3);
        }else{
            leftGrab.set(0);
            rightGrab.set(0);
        }


        if(Robot.joystickButton(4)){
            while(!(Robot.controller0.getRawButtonReleased(4))){
             //  System.out.println("let go");
            }

            if(count == 0){
                        count = 1;
                        elevator.set(ControlMode.PercentOutput, .24);
        }else{
                    elevator.set(0);
                    count = 0;
            }
        }

        if(Robot.joystickButton(5)){
            while(!(Robot.controller0.getRawButtonReleased(5))){
               // System.out.println("let go");
            }
            if(count2 == 0){
                count2 = 1;
                elevator.set(ControlMode.PercentOutput, -.24);
            }else{
                count2 = 0;
                elevator.set(ControlMode.PercentOutput, 0);
            }

            }
        }
    }
    }
}


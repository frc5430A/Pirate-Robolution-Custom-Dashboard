package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class Shooter implements Runnable {
    //assigns shooter ports
   public static WPI_TalonFX leftMotor = new WPI_TalonFX(Drive.motorPorts[6]);
   public static WPI_TalonFX rightMotor = new WPI_TalonFX(Drive.motorPorts[7]);
   public static int count = 0;
   //changable radius value
   public static int radius = 2;
   //creates a circumference based on radius
   public static double kCircumference2 = (Math.PI * 2 * radius);
   public static boolean loop = true;
   static TalonFXConfiguration configs = new TalonFXConfiguration();
  private static double deltaV =0;
  private static boolean test = true;
    public void run(){
        deltaV = VariableSpeed.getMultiplier();
        while(loop){

            //if while structure used to make sure only 1 input is gotten everytime we press the button.
       if(Robot.joystickButton(1) && count == 0 || deltaV != VariableSpeed.getMultiplier()){

           while(test | !(Robot.controller0).getRawButtonReleased(1)){
               if(deltaV != VariableSpeed.getMultiplier() ){
                    test = false;
               }
              
           }
           

           //speed changed {to 0.7 4/1/22 (previously .9) {changed back to .9}
if(!(leftMotor.get() == 0) |!(Robot.controller0).getRawButtonReleased(1) ){
    deltaV = VariableSpeed.getMultiplier();
           leftMotor.set(ControlMode.PercentOutput, .9 * VariableSpeed.getMultiplier() );
           rightMotor.set(ControlMode.PercentOutput,.9 * VariableSpeed.getMultiplier() );
           count = 1;
}
       }

       if((Robot.joystickButton(1) && count == 1)){
            while(!(Robot.controller0).getRawButtonReleased(1)){

            }
            leftMotor.set(0);
            rightMotor.set(0);
            count = 0;

    }
        }
    }
public static double roughVelo; 
    public static double ShooterEncoder(int port){
roughVelo =0;
        configs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
switch(port){
    case 0:
     leftMotor.setInverted(TalonFXInvertType.Clockwise);
     leftMotor.setNeutralMode(NeutralMode.Coast);
    leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
    leftMotor.configAllSettings(configs);

    roughVelo =  Math.abs((leftMotor.getSelectedSensorVelocity()/2048) * kCircumference2);
    break;
    case 1:
    //geneal encoder setup
    rightMotor.setInverted(TalonFXInvertType.Clockwise);
    rightMotor.setNeutralMode(NeutralMode.Coast);
   rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
   rightMotor.configAllSettings(configs);
   //math is a little sketchy, but we can probably still consistantly get a maximum.
  roughVelo = Math.abs((rightMotor.getSelectedSensorVelocity()/2048) * kCircumference2);

    break;
}
return roughVelo;
    }
	
        
}

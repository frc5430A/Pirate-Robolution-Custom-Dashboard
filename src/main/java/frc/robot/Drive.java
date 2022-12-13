package frc.robot;

//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.SpeedController;
//import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

//imports functions from java libaries
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;



public class Drive implements Runnable {
  
  //Declares motor ports
  public static int[] motorPorts = { 0, 1, 2, 3, 4, 5, 6, 7, 8,9,10,11 };
  static boolean loop = true; //enables loop for teleop
  static Timer _timer = new Timer(); //declares a timer for the smooth in/out system
  //declares motorcontrollers
  static MotorController backRightMotor = new WPI_TalonFX(motorPorts[2]);
  static MotorController backLeftMotor = new WPI_TalonFX(motorPorts[3]);
  static MotorController frontRightMotor = new WPI_TalonFX(motorPorts[4]);
  static MotorController frontLeftMotor = new WPI_TalonFX(motorPorts[5]);
  //organizes motor conrollers into groups, left and right respectively
  static MotorControllerGroup leftGroup = new MotorControllerGroup(backLeftMotor, frontLeftMotor);
  static MotorControllerGroup rightGroup = new MotorControllerGroup(backRightMotor, frontRightMotor);
  //creates a differential (tank) drive out of the two motor controller groups
  static DifferentialDrive driveTrain = new DifferentialDrive(leftGroup, rightGroup);
  static TalonFXConfiguration configs = new TalonFXConfiguration();
  final TalonFXInvertType left = TalonFXInvertType.Clockwise;
  final static TalonFXInvertType right = TalonFXInvertType.CounterClockwise;
  static boolean auton = false;
  final static double kRadius = 3;
  final static double kCircumference = (Math.PI) * 2 * kRadius;
  final int units = 2048;
  static double power1 = 0;
  static double power2 = 0;

  public void run() {
    //frontRightMotor.setInverted(true);
    //while teleop is looping
    while (loop) {
   
      if (!auton) { //if not auton

       
        if ((Robot.joystickLeft.getRawAxis(1) > .02) || Robot.joystickLeft.getRawAxis(1) < -.02 //if there is input over 0.02 on the left joystick
            || Robot.joystickRight.getRawAxis(1) > .02 || Robot.joystickRight.getRawAxis(1) < -.02) { //or the right stick
               _timer.start(); //start the timer
              if(_timer.get() > .5){ //if the timer is greater than .5 seconds
                  _timer.stop(); //stop the timer
                     }          
          driveTrain.tankDrive(-(Robot.joystickLeft.getRawAxis(1)  * VariableSpeed.getMultiplier() /*_timer.get()*2  */ ) ,(Robot.joystickRight.getRawAxis(1) * VariableSpeed.getMultiplier() /*_timer.get()*2 */ )); //drive train takes inputs from joystick and multiplies by two as well as the timer, creating a smoothing system
        } else { //if there is not input
          driveTrain.tankDrive(0, 0); //set both sides to 0
          _timer.reset(); //reset the smoothing timer
          // System.out.println("check"); */
        }
      } else {
        driveTrain.tankDrive(power1, power2);
        // System.out.println("check");
      }

  
      }
  } 

  public static Double position;
  static Double velocity;

  public static Double encoder(int option, String velOrPos) {
    configs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    position = 0.0;
    velocity = 0.0;
    switch (option) {

      case 0:
      
        // ((TalonFX) backRightMotor).setInverted(TalonFXInvertType.Clockwise);
        ((TalonFX) backRightMotor).setNeutralMode(NeutralMode.Coast);
        ((TalonFX) backRightMotor).setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
        ((TalonFX) backRightMotor).configAllSettings(configs);
      velocity = Math.abs((((TalonFX) backRightMotor).getSelectedSensorVelocity()/2048) * kCircumference);
        position = (((((TalonFX) backRightMotor).getSelectedSensorPosition()) / 2048) * kCircumference);
        break;

      case 1:
       // ((TalonFX) frontRightMotor).setInverted(TalonFXInvertType.Clockwise);
        ((TalonFX) frontRightMotor).setNeutralMode(NeutralMode.Coast);
        ((TalonFX) frontRightMotor).setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
        ((TalonFX) frontRightMotor).configAllSettings(configs);
        velocity = Math.abs((((TalonFX) frontRightMotor).getSelectedSensorVelocity()/2048) * kCircumference);
        position = (((((TalonFX) frontRightMotor).getSelectedSensorPosition()) / 2048) * kCircumference);
        break;

      case 2:
       // ((TalonFX) backLeftMotor).setInverted(TalonFXInvertType.CounterClockwise);
        ((TalonFX) backLeftMotor).setNeutralMode(NeutralMode.Coast);
        ((TalonFX) backLeftMotor).setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
        ((TalonFX) backLeftMotor).configAllSettings(configs);
        velocity = Math.abs((((TalonFX) backLeftMotor).getSelectedSensorVelocity()/2048) * kCircumference);
        position = (((((TalonFX) backLeftMotor).getSelectedSensorPosition()) / 2048) * kCircumference);
        break;

      case 3:
       // ((TalonFX) frontLeftMotor).setInverted(TalonFXInvertType.CounterClockwise);
        ((TalonFX) frontLeftMotor).setNeutralMode(NeutralMode.Coast);
        ((TalonFX) frontLeftMotor).setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
        ((TalonFX) frontLeftMotor).configAllSettings(configs);
        position = Math.abs(((((TalonFX) frontLeftMotor).getSelectedSensorPosition()) / 2048) * kCircumference);
        velocity = ((((TalonFX) frontLeftMotor).getSelectedSensorVelocity()/2048) * kCircumference);
        break;
    }
  if(velOrPos.equalsIgnoreCase("pos")){
return position;
  }else{
return velocity;
  }

    
  }

  
  public static void stopper() {
    loop = false;
}
}
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

//NOTE: Code was ripped off the 2021 ver. of the Eclipse Java Code. Only changes are lack of shooter and gate motors and change from Victor to Talon

package frc.robot;


import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.cameraserver.CameraServer;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
/*
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
*/


/**
 * The VM is configured to automatically runtele this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
Timer s_timer = new Timer();
  //public int hertzToSeconds = 0;
  //variables in order to check if running teleop or autom
  public int runtele = 0;
  public int runauto = 0;
  public boolean autoDone = false;
  public double basicRunTime = 1;


  EncoderPrint read = new EncoderPrint();
  Thread printables = new Thread(read);

  // JL, These variables define automs as strings, which is used in a switch case later on
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "MyAuto";
  private static final String kJiosAuto = "JioAuto";
  private static final String kEEE = "EthanAuto";
  private static final String kCLassic = "ClassicAuto";
  private static final String kGoForwards = "GoForwards";
  private static final String kGoBackwards = "GoBackwards";
  // JL, the currently selected autom as a string. Used later
  private String m_autoSelected;
  // JL, defines m_chooser as a sendable chooser, essentially a dropdown. (note that it is defines to take Strings since thats how we are running the autom system)
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  Drive driveRef =  new Drive();
  Shooter shoot = new Shooter();
  Thread shooter = new Thread(shoot); //creates a thread for shooting
  FeedSystem feed = new FeedSystem(); 
  Thread feedSystem = new Thread(feed); //creates a thread for feeding
public Thread drive = new Thread(driveRef); //creates a thread for drivetrain
VariableSpeed safety = new VariableSpeed();
private Thread speedSafety = new Thread(safety);
  Pneumatics compressor = new Pneumatics();
public Thread pneumatics = new Thread(compressor); //creats thread for pneumatics

//Joysticks
public static Joystick joystickLeft = new Joystick(1);
public static Joystick joystickRight = new Joystick(2);

//Double meant to read the value of the pneumatics
public static double pneu;

//-JL- Copy pasted code from "Writing a simple Network Table" on WPILib



//public NetworkTableEntry xEntry;
//public NetworkTableEntry yEntry;
  // Second port is for back right, Third is for back left, fourth is for front
  // right, fifth is for front left

  public int[] motorPorts = { 0, 1, 2, 3, 4, 5 };

  //Declares the compressor and names it "compressor"
  
  //Declares motors and assigns them to ports
 
  int toggle2 = 0;
  //Puts motor controllers into groups for Differential Drive
  
  //Initializes the Joystick and names it controller0
  public static Joystick controller0 = new Joystick(0);
  //joystickButton = new JoystickButton(controller0, 2); extra code for joystick button if needed
  //Initializes the tankdrive and names it driveTrain
  
  double x = 0;
  double y = 0;

  
 
  /**
   * This function is runtele when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    /*
    // JL, gets default instance of network table which is generated automatically
    // on program start
    NetworkTableInstance inst = NetworkTableInstance.getDefault();

    //Gets a table from the instance
    NetworkTable table = inst.getTable("datatable");
    */

    // JL, These m_chooser pieces are only usable in robotInit (i think, it broke when i used it elsewhere)
    // These pieces will add options to the m_chooser SendableChooser object, essentially adding options to the dropdown
    // First field is name of option seen, the next is the actual string defined as the chosen auto
    m_chooser.setDefaultOption("Default", kDefaultAuto);
    m_chooser.addOption("MyAuto", kCustomAuto);
    m_chooser.addOption("JioAuto", kJiosAuto);
    m_chooser.addOption("EthanAuto", kEEE);
    m_chooser.addOption("ClassicAuto", kCLassic);
    m_chooser.addOption("GoForwards", kGoForwards);
    m_chooser.addOption("GoBackwards", kGoBackwards);
    
    CameraServer.startAutomaticCapture(0);
    CameraServer.startAutomaticCapture(1);

    // JL, Finally, it puts the dropdown into the shuffleboard with the name "Auto Choices"

    SmartDashboard.putData("Auto Choices", m_chooser);
    SmartDashboard.putNumber("Forward/Backward Autom Runtime", basicRunTime);
    

    

    /*
    SmartDashboard.putString("DB/String 2", "My 21 Char TestString");

    xEntry = table.getEntry("X");
    yEntry = table.getEntry("Y");

    */
    

    //Below code checks whether the compressor is enabled, the presure switch's value, and the current of the Compressor.

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *;

   * <p>This runteles after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    // JL, these fields directly put information into the shuffleboard, the first field is the name
    // If theres a widget with the same name already on the dashboard, it'll just update that widget
    // Second field is the value
    // Keep in mind to specify what you're putting in the function

    //SmartDashboard.putNumber("Joystick X value", joystickLeft.getRawAxis(1));
    SmartDashboard.putNumber("Speed Multiplier", VariableSpeed.getMultiplier());
    SmartDashboard.putNumber("Intake Count2", FeedSystem.count2);
    SmartDashboard.putNumber("Intake Count1", FeedSystem.count);
    SmartDashboard.putNumber("Shooter Count", Shooter.count);
    SmartDashboard.putNumber("Pneumatic Current", Pneumatics.current);
    SmartDashboard.putBoolean("Compressor On", Pneumatics.enabled);
    SmartDashboard.putBoolean("Solenoid Status", Pneumatics.solenoidStatus);

    /*
      Beta code for a Java --> Labview Dashboard

    SmartDashboard.putString("DB/String 2",  Double.toString(Pneumatics.current));
    SmartDashboard.putString("DB/String 3", Double.toString(joystickLeft.getRawAxis(1)));
    SmartDashboard.putString("DB/StringApple", Double.toString(joystickRight.getRawAxis(1)));

    xEntry.setDouble(x);
    yEntry.setDouble(y);
    x += 0.05;
    y += 1.0;
    */

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {

    

    //SmartDashboard.putData(m_chooser);
    runauto = 0;
   // drive.start();
    //m_autoSelected = m_chooser.getSelected();
    //m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);

    // JL, puts into the dashboard which autom is currently selected. Done for debug purposes
    SmartDashboard.putString("Autom Read", m_chooser.getSelected());
    // JL, takes current chooser value and outputs it into the console. (keep in mind this is in autoInit, so itll only run once)
    System.out.println(m_chooser.getSelected());
    //ignore this --> SmartDashboard.getEntry(m_chooser);

    // JL, updates the m_autoSelected field by grabbing the string from the m_chooser field. I think this is broke, so ignore
    m_autoSelected = SmartDashboard.getString(m_chooser.getSelected(), kDefaultAuto);
    // JL, takes value seen by m_autoSelected and outputs it
    System.out.println("Auto selected: " + m_autoSelected);
    // JL, sets autoDone to false, which is used so that after auto runs once it wont be able to run again.
    autoDone = false;

  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  
    // JL, will only run the code if autoDone is false. (meaning it didnt run yet)
  if (autoDone == false){
    switch (m_chooser.getSelected()){
      case kCustomAuto:
      // JL, you can put autom code in here. Don't forget the break when youre done~
        break;
      case kJiosAuto:
        // JL, this is my autom, it pastes a string field onto the dashboard.
        SmartDashboard.putString("My message to you and your mom", "Go play Pokemon Mystery Dungeon!");
        break;
      case kEEE:
        // JL, this is Ethan's autom, it pastes a string just like mines.
        SmartDashboard.putString("Stan Twice", "Stan Chaeyoung");
        break;
      case kCLassic:
        // JL, this is an actual, tangible autom. Runs via a timer. It is the old autom used in Lancaster 2022.
      s_timer.reset(); //sets timer to 0 so everything can play normally
      s_timer.start(); //starts timer so that everything plays normally
      while (s_timer.get() < 1){ //while timer less than 1 (code plays for 1 second)
        Drive.leftGroup.set(-0.2); //move left motors counterclockwise (backwards)
        Drive.rightGroup.set(0.2); //move right motors clockwise (backwards)
      }
      s_timer.reset(); //resets timer to 0
      while(s_timer.get() < 2){ //while timer less that 2 (code plays for 2 seconds) *Original is 2]]* {changed from 2.5 to 2}
      Shooter.leftMotor.set(-.7); //move left shooter motor counterclockwise (away) *Original is -.7* {changed to -.8}
      Shooter.rightMotor.set(.7); //move right shooter motor clockwise (away) *Original is -.7* {changed to -.8}
      } 
      s_timer.reset(); //reset timer to 0 again
      while(s_timer.get() < 2){ //while timer less than 2 (code plays for 2 seconds)
      FeedSystem.elevator.set(.7); //move elevator motor clockwise (upwards)
      }
      Shooter.leftMotor.set(0); //below code turns off left motor
      Shooter.rightMotor.set(0); //and right motor
      FeedSystem.elevator.set(0); //and the elevator as well
      s_timer.reset();//reset timer once more
      while(s_timer.get() < 1.25){ //while timer less than 1.25 (code plays for 1.25 seconds)
        Drive.leftGroup.set(0.2); //move left motor clockwise
        Drive.rightGroup.set(0.2); //move right motor clockwise
      } //with the way the motors work, having both sides turning clockwise means they turn in opposite directions, which turns the robot
      s_timer.reset(); //another timer reset
      while(s_timer.get() < 2){ //while timer less than 3 (code runs for 3 seconds)
        Drive.leftGroup.set(0.2); //move left motor clockwise (forwards)
        Drive.rightGroup.set(-0.2); //move right motor counterclockwise (forwards)
      }
      Drive.leftGroup.set(0); //code turns off the left motor group
      Drive.rightGroup.set(0); //and right group

        break;
      case kGoForwards:

        
        basicRunTime = SmartDashboard.getNumber("Forward/Backward Autom Runtime", 1);
      
        s_timer.reset(); //sets timer to 0 so everything can play normally
        s_timer.start(); //starts timer so that everything plays normally
        while (s_timer.get() < basicRunTime){ //while timer less than basicRunTime (takes input from ShuffleBoard)
        Drive.leftGroup.set(0.2); //move left motors clockwise (forwards)
        Drive.rightGroup.set(-0.2); //move right motors counter-clockwise (forwards)
        }
      break;
      case kGoBackwards:

        
        basicRunTime = SmartDashboard.getNumber("Forward/Backward Autom Runtime", 1);
      
        s_timer.reset(); //sets timer to 0 so everything can play normally
        s_timer.start(); //starts timer so that everything plays normally
        while (s_timer.get() < basicRunTime){ //while timer less than basicRunTime (takes input from ShuffleBoard)
        Drive.leftGroup.set(-0.2); //move left motors clockwise (forwards)
        Drive.rightGroup.set(0.2); //move right motors counter-clockwise (forwards)
        }
      break;
      


      case kDefaultAuto:
      //this is the default auto, and so it is specified after the case. it should have ran legacy, but the code's commented out so it skips right to the break.
      default:
    
if(runauto == 0){
  
 /* runauto =1; //sets run auto to 1, telling the rest of the robot that auto is running
  s_timer.reset(); //sets timer to 0 so everything can play normally
  s_timer.start(); //starts timer so that everything plays normally
  while (s_timer.get() < 1){ //while timer less than 1 (code plays for 1 second)
    Drive.leftGroup.set(-0.2); //move left motors counterclockwise (backwards)
    Drive.rightGroup.set(0.2); //move right motors clockwise (backwards)
  }
  s_timer.reset(); //resets timer to 0
  while(s_timer.get() < 2){ //while timer less that 2 (code plays for 2 seconds) *Original is 2]]* {changed from 2.5 to 2}
  Shooter.leftMotor.set(-.7); //move left shooter motor counterclockwise (away) *Original is -.7* {changed to -.8}
  Shooter.rightMotor.set(.7); //move right shooter motor clockwise (away) *Original is -.7* {changed to -.8}
  } 
  s_timer.reset(); //reset timer to 0 again
  while(s_timer.get() < 2){ //while timer less than 2 (code plays for 2 seconds)
  FeedSystem.elevator.set(.7); //move elevator motor clockwise (upwards)
  }
  Shooter.leftMotor.set(0); //below code turns off left motor
  Shooter.rightMotor.set(0); //and right motor
  FeedSystem.elevator.set(0); //and the elevator as well
  s_timer.reset();//reset timer once more
  while(s_timer.get() < 1.25){ //while timer less than 1.25 (code plays for 1.25 seconds)
    Drive.leftGroup.set(0.2); //move left motor clockwise
    Drive.rightGroup.set(0.2); //move right motor clockwise
  } //with the way the motors work, having both sides turning clockwise means they turn in opposite directions, which turns the robot
  s_timer.reset(); //another timer reset
  while(s_timer.get() < 2){ //while timer less than 3 (code runs for 3 seconds)
    Drive.leftGroup.set(0.2); //move left motor clockwise (forwards)
    Drive.rightGroup.set(-0.2); //move right motor counterclockwise (forwards)
  }
  Drive.leftGroup.set(0); //code turns off the left motor group
  Drive.rightGroup.set(0); //and right group

  /* ((TalonFX)Drive.backRightMotor).setSelectedSensorPosition(0);
     // Shooter.leftMotor.set(.9);
    //  Shooter.rightMotor.set(.9);
      Drive.driveTrain.tankDrive(.9, (-.9 * .8));
     // hertzToSeconds =0;
      while(Drive.encoder(0, "pos") <= (7 * Drive.kCircumference)){
      
        Drive.driveTrain.tankDrive(.9, (-.9 *.8));

      }
    //  Drive.driveTrain.tankDrive(.9,-.9);
   //   ((TalonFX)Drive.backRightMotor).setSelectedSensorPosition(0);
//while(Drive.encoder(0, "pos") <= (2.3 * Drive.kCircumference)){
 // Drive.driveTrain.tankDrive(.9,-.9);
//}
Drive.driveTrain.tankDrive(0,0);
//FeedSystem.elevator.set(1);

//s_timer.start();
//while(s_timer.get() <= 4){
  
//}
//FeedSystem.elevator.set(0);
*/
runtele = 1;
}

        // JL, after all's said and done, it reaches the break;
        break;
      }
        // JL, now that we're done with automs, we can close the switch statement and set autoDone to true, preventing it from running ever again.
      autoDone = true;

    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    ((TalonFX)Drive.backRightMotor).setSelectedSensorPosition(0);
runtele = 1;

// JL, ignore this, its test code which will run on teleop.
//SmartDashboard.putString("DB/String 2", "My 21 Char TestString");

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //only runteles once, that way we dont have thousands of threads overloading the rio.
    //we don't put it in init, because all the inits runtele at once, this has been tested.
    //the reason why the above comment is a problem is that we want NO human intereference during auton. 
if(runtele == 1){
speedSafety.start();
  drive.start();
  pneumatics.start();
  printables.start();
  shooter.start();
  feedSystem.start();
  runtele = 0;
}

    //FeedSystem.leftGrab.set(.5);
  }
  
  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
//Drive.stopper();
//EncoderPrint.stopper();
//Pneumatics.stopper();
//Shooter.loop = false;
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

public static double joystickVal(int port){
  return controller0.getRawAxis(port);
}

public static boolean joystickButton(int button){
  return controller0.getRawButton(button);
}

}

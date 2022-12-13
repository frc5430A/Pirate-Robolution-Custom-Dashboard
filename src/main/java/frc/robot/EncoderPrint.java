package frc.robot;

public class EncoderPrint implements Runnable{
    static boolean loop = true;

    public void run() {
        while (loop) {
           System.out.println("Position: " + Drive.encoder(0, "pos"));
           System.out.println("Velocity (Train): " + Drive.encoder(0, "vel"));
            //Drive.encoder(1);
           // Drive.encoder(2);
           // Drive.encoder(3);
            System.out.println("Velocity: " + Shooter.ShooterEncoder(0));
            try {
                Thread.sleep(5000);
            } catch (Exception e) {

            }
        }
    }

    public static void stopper() {
        loop = false;
    }
}

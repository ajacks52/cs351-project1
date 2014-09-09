package antworld.dynamicmap;



import java.util.Timer;
import java.util.TimerTask;
 
public class TimerSample {
 
    public static void main(String[] args) {
        //1- Taking an instance of Timer class.
        Timer timer = new Timer("Printer");
        MyTask t = new MyTask();
 
 
        //TimerTask is a class implements Runnable interface so
        //You have to override run method with your certain code black
 
        //Second Parameter is the specified the Starting Time for your timer in
        //MilliSeconds or Date
 
        //Third Parameter is the specified the Period between consecutive
        //calling for the method.
 
        timer.schedule(t, 0, 2000);
 
 
    }
}
// 
//class MyTask extends TimerTask {
//    //times member represent calling times.
//    private int times = 0;
// 
// 
//    public void run() {
//        times++;
//        if (times <= 5) {
//            System.out.println("I'm alive...");
//        } else {
//            System.out.println("Timer stops now...");
// 
//            //Stop Timer.
//            this.cancel();
//        }
//    }
//}


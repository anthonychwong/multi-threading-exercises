package b_bolatile;

import java.util.concurrent.ThreadLocalRandom;

public class BVolatile {
  static volatile int value = 0;

  public static void main(String[] args) throws Throwable{
    Thread write = new Thread(() -> {
      for(;;){
        value = ThreadLocalRandom.current().nextInt(10,100);
        System.out.println("new value written: " + String.valueOf(value));
        try {
          Thread.sleep(100);
        } catch (InterruptedException exp){
          break;
        }
      }
    });

    write.start();

    for(;;) {
      System.out.println("current value is: " + String.valueOf(value));
      Thread.sleep(25);
    }
  }
}

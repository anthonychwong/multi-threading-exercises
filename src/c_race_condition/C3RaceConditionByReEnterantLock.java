package c_race_condition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class C3RaceConditionByReEnterantLock {
  private static int value = 0;
  private static ReentrantLock objLock = new ReentrantLock();

  public static void main(String[] args){
    int parallelism = 4;
    ExecutorService executor = Executors.newFixedThreadPool(parallelism);

    for(int i = 0; i < 50; i++){
      executor.execute(new AdderWithId(i));
    };

    executor.shutdown();

    try {
      if(executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)){
        System.out.println("final value: " + String.valueOf(value));
      } else {
        throw new TimeoutException();
      }
    } catch (Throwable e){
      e.printStackTrace(); // at least for both timeout and interrupt
    }
  }

  private static class AdderWithId implements Runnable{
    int _id;

    AdderWithId(int id){
      _id = id;
    }

    @Override
    public void run() {
      boolean isSucceed = false;
      int retryCount = 0;

      while(!isSucceed && retryCount < 10) {
        try {
          if (objLock.tryLock(5, TimeUnit.SECONDS)) {
            System.out.println(String.valueOf(_id) + ": new value is " + String.valueOf(++value));
//            objLock.unlock(); // un-comment to behave, else to try the timeout
            isSucceed = true;
          } else {
            retryCount++;
            System.out.println(String.valueOf(_id) + ": timeout when acquiring lock, retried: " + String.valueOf(retryCount));
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      if(!isSucceed){
        System.out.println("Not succeed");
      }
    }
  }
}

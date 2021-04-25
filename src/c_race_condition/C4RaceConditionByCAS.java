package c_race_condition;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class C4RaceConditionByCAS {
  private static AtomicInteger value = new AtomicInteger(0);

  public static void main(String[] args){
    int parallelism = 4;
    ExecutorService executor = Executors.newFixedThreadPool(parallelism);

    for(int i = 0; i < 50; i++){
      executor.execute(new AdderWithId(i));
    };

    executor.shutdown();

    try {
      if(executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)){
        System.out.println("final value: " + String.valueOf(value.get()));
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
      System.out.println(String.valueOf(_id) + ": new value is " + String.valueOf(value.incrementAndGet()));
    }
  }
}

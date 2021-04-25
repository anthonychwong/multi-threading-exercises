package c_race_condition;

import java.util.concurrent.*;

public class C1RaceConditionProblem {
  private static volatile int value = 0;

  public static void main(String[] args){
    // for some reason, both parallel stream and fork join pool give correct result
    // maybe they both ends up as single thread?

//    IntStream.range(0, 50).parallel().forEach((i) -> {
//      value ++;
//      System.out.println(String.valueOf(i) + ": new value is " + String.valueOf(value));
//    });

    int parallelism = 4;
//    ExecutorService forkJoinPool = new ForkJoinPool(parallelism);
    ExecutorService forkJoinPool = Executors.newFixedThreadPool(parallelism);

    for(int i = 0; i < 50; i++){
      forkJoinPool.submit(new AdderWithId(i));
    };

    forkJoinPool.shutdown();
    try {
      forkJoinPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e){
      e.printStackTrace(); //what else?
    }

    System.out.println("final value: " + String.valueOf(value));
  }

  private static class AdderWithId implements Runnable{
    int _id;

    AdderWithId(int id){
      _id = id;
    }

    @Override
    public void run() {
      value ++;
      System.out.println(String.valueOf(_id) + ": new value is " + String.valueOf(value));
    }
  }
}

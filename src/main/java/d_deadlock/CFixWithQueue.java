package d_deadlock;

import d_deadlock.common.Holder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CFixWithQueue {
  public static void main(String[] args) throws InterruptedException {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    Holder<Integer> holderA = new Holder<>();
    Holder<Integer> holderB = new Holder<>();

    holderA.value = 10;
    holderB.value = 20;

    executor.execute(() -> SwapHolderValue(holderA, holderB));
    executor.execute(() -> SwapHolderValue(holderB, holderA));
    executor.execute(() -> SwapHolderValue(holderA, holderB));

    executor.shutdown();

    if(executor.awaitTermination(1000, TimeUnit.MICROSECONDS)) {
      System.out.println(holderA.value);
      System.out.println(holderB.value);
    } else {
      System.out.println("Timeout for some reason.");
    }
  }

  private static <T> void SwapHolderValue(Holder<T> a, Holder<T> b){
    T tempValue = a.value;
    a.value = b.value;
    b.value = tempValue;
  }
}

package d_deadlock;

import d_deadlock.common.Holder;

public class AProblem {
  public static void main(String[] args) throws InterruptedException {
    Holder<Integer> holderA = new Holder<>();
    Holder<Integer> holderB = new Holder<>();

    holderA.value = 10;
    holderB.value = 20;

    Thread swapAB = new Thread(() -> SwapHolderValue(holderA, holderB));
    Thread swapBA = new Thread(() -> SwapHolderValue(holderB, holderA));

    swapAB.start();
    swapBA.start();

    swapAB.join();
    swapBA.join();
  }


  private static <T> void SwapHolderValue(Holder<T> a, Holder<T> b){
    synchronized (a){
      T tempValue = a.value;

      try {
        Thread.sleep(1000);
      } catch (InterruptedException interruptedException) {
        // nothing to do yet
      }

      synchronized (b){
        a.value = b.value;
        b.value = tempValue;
      }
    }
  }
}

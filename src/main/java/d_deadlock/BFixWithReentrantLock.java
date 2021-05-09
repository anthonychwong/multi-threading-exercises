package d_deadlock;

import d_deadlock.common.Holder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BFixWithReentrantLock {
  public static void main(String[] args) throws InterruptedException {
    HolderWithLock<Integer> holderWithLockA = new HolderWithLock<>();
    HolderWithLock<Integer> holderWithLockB = new HolderWithLock<>();

    holderWithLockA.lock = new ReentrantLock();
    holderWithLockB.lock = new ReentrantLock();

    holderWithLockA.value = 10;
    holderWithLockB.value = 20;

    Thread swapAB = new Thread(() -> SwapHolderValue(holderWithLockA, holderWithLockB));
    Thread swapBA = new Thread(() -> SwapHolderValue(holderWithLockB, holderWithLockA));
    Thread swapAgain = new Thread(() -> SwapHolderValue(holderWithLockA, holderWithLockB));

    swapAB.start();
    swapBA.start();
    swapAgain.start();

    swapAB.join();
    swapBA.join();
    swapAgain.join();

    System.out.println(holderWithLockA.value);
    System.out.println(holderWithLockB.value);
  }

  private static class HolderWithLock<T> extends Holder<T> {
    ReentrantLock lock;
  }

  private static <T> void SwapHolderValue(HolderWithLock<T> a, HolderWithLock<T> b) {
    boolean isSuccess = false;

    while (!isSuccess) {
      try {
        if (a.lock.tryLock(100, TimeUnit.MICROSECONDS)) {
          try {
            T tempValue = a.value;

            try {
              Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
              // nothing to do yet
            }

            if (b.lock.tryLock(100, TimeUnit.MICROSECONDS)) {
              try {
                a.value = b.value;
                b.value = tempValue;

                isSuccess = true;
              } finally {
                b.lock.unlock();
              }
            }
          } finally {
            a.lock.unlock();
          }
        }
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      }
    }
  }
}

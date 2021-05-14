package e_robot_laboratory;

import support.robot.Body;
import support.robot.Head;
import support.robot.Robot;

import java.util.concurrent.CountDownLatch;

public class ByCountDownLatch {
  static private Head head;
  static private Body body;

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(2);

    new Thread(() -> {
      Head result = new Head();
      result.description = "just a head";

      head = result;
      latch.countDown();
    }).start();

    new Thread(() -> {
      Body result = new Body();
      result.description = "just a body";

      body = result;
      latch.countDown();
    }).start();

    latch.await();

    Robot product = new Robot();
    product.head = head;
    product.body = body;
    product.printContent(System.out);
  }

}

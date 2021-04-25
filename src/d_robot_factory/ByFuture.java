package d_robot_factory;

import d_robot_factory.common.Body;
import d_robot_factory.common.Head;
import d_robot_factory.common.Robot;

import java.util.concurrent.*;

public class ByFuture {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    int parallelism = 2;
    ExecutorService executor = Executors.newFixedThreadPool(parallelism);

    Future<Head> makeHead = executor.submit(new MakeHead());
    Future<Body> makeBody = executor.submit(new MakeBody());

    executor.shutdown();
    if(executor.awaitTermination(1, TimeUnit.HOURS)) {
      Robot product = new Robot();
      product.head = makeHead.get();
      product.body = makeBody.get();
      product.printContent();
    }
  }

  private static class MakeHead implements Callable<Head> {
    @Override
    public Head call() throws Exception {
      Thread.sleep(1000);

      Head result = new Head();
      result.description = "just a head";

      return result;
    }
  }

  private static class MakeBody implements Callable<Body>{
    @Override
    public Body call() throws Exception {
      Thread.sleep(1000);

      Body result = new Body();
      result.description = "just a body";

      return result;
    }
  }
}

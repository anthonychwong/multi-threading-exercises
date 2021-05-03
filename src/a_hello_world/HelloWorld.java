package a_hello_world;

public class HelloWorld {
  public static void main(String[] args) throws Throwable{
    ExtendByThread extendByThread = new ExtendByThread();

    Thread withRunnable = new Thread(()->{
      System.out.println("Hello world by thread with runnable");
    });

    extendByThread.start();
    withRunnable.start();

    extendByThread.join();
    withRunnable.join();
  }

  static class ExtendByThread extends Thread{
    @Override
    public void run() {
      System.out.println("Hello world by extending Thread");
    }
  }
}

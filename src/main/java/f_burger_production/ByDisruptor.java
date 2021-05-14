package f_burger_production;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import support.burger.Bun;
import support.burger.Burger;
import support.burger.Meat;

public class ByDisruptor {
  public static void main(String[] args){
    int bufferSize = 1024;
    Disruptor<Burger> disruptor = new Disruptor<Burger>(
        Burger::new,
        bufferSize,
        DaemonThreadFactory.INSTANCE
    );

    disruptor.handleEventsWith(
      (b, sequence, endOfBatch) -> b.setMeat(new Meat("Meat " + sequence)),
      (b, sequence, endOfBatch) -> b.setBun(new Bun("Bun " + sequence))
    ).then(
      (b, sequence, endOfBatch) -> b.printContent(System.out)
    );

    disruptor.start();

    RingBuffer<Burger> ringBuffer = disruptor.getRingBuffer();

    for (long l = 0; l < 100; l++) {
      ringBuffer.publishEvent((burger, sequence) -> burger.setDescription("Burger " + sequence));
      try {
        Thread.sleep(100);
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      }
    }

    disruptor.shutdown();
  }
}

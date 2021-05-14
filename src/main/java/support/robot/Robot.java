package support.robot;

import java.io.PrintStream;

public class Robot {
  public Head head;
  public Body body;

  public void printContent(PrintStream output){
    output.println(head.description);
    output.println(body.description);
  }
}

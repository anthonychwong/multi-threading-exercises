package support.robot;

public class Robot {
  public Head head;
  public Body body;

  public void printContent(){
    System.out.println(head.description);
    System.out.println(body.description);
  }
}

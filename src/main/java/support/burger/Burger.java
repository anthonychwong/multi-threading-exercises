package support.burger;

import java.io.PrintStream;

public class Burger {
  String description;
  Meat meat;
  Bun bun;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Burger(){
    this(null, null);
  }

  public Burger(Meat meat, Bun bun) {
    this.meat = meat;
    this.bun = bun;
  }

  public Meat getMeat() {
    return meat;
  }
  public void setMeat(Meat value) {
    this.meat = value;
  }

  public Bun getBun() {
    return bun;
  }
  public void setBun(Bun bun) {
    this.bun = bun;
  }

  public void printContent(PrintStream out){
    out.println("Content of " + description);
    out.println(bun.description);
    out.println(meat.description);
  }
}

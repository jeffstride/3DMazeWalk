public class Wall {

  private int x1, y1, x2, y2;

  private boolean end;

  public Wall(int x1, int y1, int x2, int y2, boolean end) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.end = end;
  }

  public int getx1() {
    return x1;
  }

  public int getx2() {
    return x2;
  }

  public int gety1() {
    return y1;
  }

  public int gety2() {
    return y2;
  }

  public boolean getEnd() {
    return end;
  }

}
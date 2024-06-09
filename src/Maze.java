import java.util.*;

public class Maze {

  private static int length = 0;
  private static int endLength = 0;
  private static int id = 0;

  private boolean end = false;

  private static ArrayList<Wall> walls = new ArrayList<>();

  public Maze() {
    buildMaze();
  }

  public void setIdMaze(int id) {
    this.id = id;
    buildMaze();
  }

  public void buildMaze() {

    end = false;
    walls.clear();

    walls.add(new Wall(0, 0, 5, Driver.HEIGHT, end));
    walls.add(new Wall(0, 0, Driver.WIDTH, 5, end));
    walls.add(new Wall(Driver.WIDTH - 20, 0, Driver.WIDTH, Driver.HEIGHT, end));
    walls.add(new Wall(0, Driver.HEIGHT - 66, Driver.WIDTH, Driver.HEIGHT, end));

    if (id == 0) {
      length = 100;
      for (int i = 0; i < length; i++) {
        int ran1, ran2, ran3, ran4;
        boolean overlap;
        do {
          ran1 = (int) (Math.random() * Driver.WIDTH);
          ran2 = (int) (Math.random() * Driver.HEIGHT);
          ran3 = (int) (Math.random() * 95) + 5;
          ran4 = (int) (Math.random() * 95) + 5;
          int centerX = Driver.WIDTH / 2;
          int centerY = Driver.HEIGHT / 2;
          overlap = Math.abs(ran1 - centerX) <= 100 && Math.abs(ran2 - centerY) <= 100;
        } while (overlap);
        walls.add(new Wall(ran1, ran2, ran3, ran4, end));
      }
    } else if (id == 1) {
      // Horizontal walls
          walls.add(new Wall(0, 240, 65, 5, end));
          walls.add(new Wall(60, 360, 120, 5, end));
          walls.add(new Wall(60, 300, 65, 5, end));
          walls.add(new Wall(120, 240, 120, 5, end));
          walls.add(new Wall(180, 300, 60, 5, end));
          walls.add(new Wall(180, 120, 180, 5, end));
          walls.add(new Wall(180, 180, 120, 5, end));
          walls.add(new Wall(240, 420, 240, 5, end));
          walls.add(new Wall(0, 420, 185, 5, end));
          walls.add(new Wall(0, 480, 485, 5, end));

          // Vertical walls
          walls.add(new Wall(60, 0, 5, 180, end));
          walls.add(new Wall(60, 300, 5, 60, end));
          walls.add(new Wall(120, 60, 5, 240, end));
          walls.add(new Wall(240, 300, 5, 120, end));
          walls.add(new Wall(180, 0, 5, 60, end));
          walls.add(new Wall(180, 360, 5, 60, end));
          walls.add(new Wall(240, 60, 5, 60, end));
          walls.add(new Wall(300, 0, 5, 60, end));
          walls.add(new Wall(360, 0, 5, 360, end));
          walls.add(new Wall(300, 180, 5, 240, end));
          walls.add(new Wall(420, 60, 5, 360, end));
          walls.add(new Wall(480, 0, 5, 480, end));
          length = walls.size() - 4;
    } else if (id == 2) {
      length = 0;
    } else if (id == 3) {
      length = 0;
    }

    end = true;

    if (id == 0) {
      endLength = 20;
      for (int i = 0; i < endLength; i++) {
        int ran1, ran2, ran3, ran4;
        boolean overlap;
        do {
          ran1 = (int) (Math.random() * Driver.WIDTH);
          ran2 = (int) (Math.random() * Driver.HEIGHT);
          ran3 = (int) (Math.random() * 95) + 5;
          ran4 = (int) (Math.random() * 95) + 5;
          int centerX = Driver.WIDTH / 2;
          int centerY = Driver.HEIGHT / 2;
          overlap = Math.abs(ran1 - centerX) <= 100 && Math.abs(ran2 - centerY) <= 100;
        } while (overlap);
        walls.add(new Wall(ran1, ran2, ran3, ran4, end));
      }
    } else if (id == 1) {
      endLength = 1;
          walls.add(new Wall(437, 437, 30, 30, end));
    } else if (id == 2) {
      endLength = 0;
    } else if (id == 3) {
      endLength = 0;
    }
  }

  public static ArrayList<Wall> getWalls() {
    return walls;
  }

  public int getLength() {
    return length + 4;
  }

  public int getEndLength() {
    return endLength;
  }
}

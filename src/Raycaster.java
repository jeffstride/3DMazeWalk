import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Raycaster {

  private List<Line2D.Double> rayList = new ArrayList<>();
  private ArrayList<Double> rayDist = new ArrayList<>();
  private ArrayList<Boolean> rayID = new ArrayList<>();
  private static final int MAXDIST = 3000;
  private static final int RES_ORI = 8;
  private static final int FOV_ORI = 90;
  private static int RES = RES_ORI;
  private static int FOV = FOV_ORI;
  private static int scanLines = 480 / RES;
  private static double steps = 2;
  private boolean end = false;

  public List<Line2D.Double> getRays() {
    return rayList;
  }

  public ArrayList<Double> getDist() {
    return rayDist;
  }

  public boolean getID(int index) {
    return rayID.get(index);
  }

  public void raycast(double playerX, double playerY, double playerDir, ArrayList<Wall> walls, int mazeLength,
      int changeRes, int changeFov) {
    List<Wall> wallsCopy;
    synchronized (walls) {
      wallsCopy = new ArrayList<>(walls);
    }
    rayList.clear();
    rayDist.clear();
    rayID.clear();
    double startDir = -playerDir + Math.PI / 2 - Math.toRadians(FOV / 2);
    double stepSize = Math.toRadians(FOV) / scanLines;
    boolean end = false;
    for (int i = 0; i <= scanLines; i++) {
      double rayDir = startDir + i * stepSize;
      double minDist = MAXDIST;
      for (Wall wall : wallsCopy) {
        if (wall == null) {
          continue;
        }
        double dist = getRayCast(playerX, playerY, -rayDir, wall, MAXDIST);
        if (dist < minDist && dist > 0) {
          minDist = dist;
          end = wall.getEnd();
        }
      }
      double endX = playerX + Math.cos(rayDir) * minDist;
      double endY = playerY + Math.sin(rayDir) * minDist;
      rayList.add(new Line2D.Double(playerX, playerY, endX, endY));
      rayDist.add(minDist * Math.cos(-playerDir + Math.PI / 2 - rayDir));
      rayID.add(end);
      end = false;
    }

    if (changeRes == -1 && RES < 480) {
      RES += 2;
      scanLines = 480 / RES;
    }
    if (changeRes == 1 && RES > 3) {
      RES -= 2;
      scanLines = 480 / RES;
    }
    if (changeRes == 2) {
      RES = RES_ORI;
      scanLines = 480 / RES;
    }
    if (changeFov == -1 && FOV > 3) {
      FOV -= 2;
    }
    if (changeFov == 1 && FOV < 178) {
      FOV += 2;
    }
    if (changeFov == 2) {
      FOV = FOV_ORI;
    }
  }

  private double getRayCast(double x, double y, double dir, Wall wall, int maxDist) {
    double dx = steps * Math.sin(dir + Math.PI / 2);
    double dy = steps * Math.cos(dir + Math.PI / 2);
    double[] coord = tryMove(x, y, dx, dy, wall, maxDist);
    double xNew = coord[0];
    double yNew = coord[1];
    return dist(x, y, xNew, yNew);
  }

  private double[] tryMove(double x, double y, double dx, double dy, Wall wall, int maxDist) {
    double counter = 0;
    while (!rayTouchMaze(x + dx, y + dy, wall) && counter < maxDist) {
      x += dx;
      y += dy;
      counter += Math.sqrt(dx * dx + dy * dy);
    }
    for (int i = 0; i < 10; i++) {
      dx /= 2;
      dy /= 2;
      if (!rayTouchMaze(x + dx, y + dy, wall) && counter < maxDist) {
        x += dx;
        y += dy;
        counter += Math.sqrt(dx * dx + dy * dy);
      }
    }
    return new double[] { x, y };
  }

  private boolean rayTouchMaze(double x, double y, Wall wall) {
    if (wall == null) {
      return false;
    }
    int x1 = wall.getx1();
    int y1 = wall.gety1();
    int x2 = x1 + wall.getx2();
    int y2 = y1 + wall.gety2();
    // System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
    return x >= x1 && x <= x2 && y >= y1 && y <= y2;
  }

  public static double dist(double x1, double y1, double x2, double y2) {
    return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
  }
}
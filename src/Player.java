import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Player extends AnimatedPanel implements KeyListener {
  private static final long serialVersionUID = 1L;

  private int id = 0;
  private int idMaze = 0;

  private final double V = 1; // Linear velocity
  private final double W = 0.14; // Angular velocity
  private final double U = 0.8; // Friction
  private final int SIZE = 10;
  private final int HEIGHT = 15000;
  private int changeRes = 0;
  private int changeFov = 0;

  private static double x = Driver.WIDTH / 2, y = Driver.HEIGHT / 2;
  private double vel = 0;
  private double dir = Math.PI;
  private boolean moveUp, moveDown, moveLeft, moveRight;

  private static ArrayList<Integer> x1 = new ArrayList<>();
  private static ArrayList<Integer> x2 = new ArrayList<>();
  private static ArrayList<Integer> y1 = new ArrayList<>();
  private static ArrayList<Integer> y2 = new ArrayList<>();

  Maze maze = new Maze();
  Raycaster rays = new Raycaster();

  @Override
  public void updateAnimation() {
    movePlayer();
    raycast();
  }

  public void setView(int id) {
    this.id = id;
  }

  public void raycast() {
    rays.raycast(x, y, dir, Maze.getWalls(), maze.getLength(), changeRes, changeFov);
  }

  public Player() {
    addEventHandlers();
    createMaze();
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setIdMaze(int idMaze) {
    if (idMaze == 0) {
      x = Driver.WIDTH / 2;
      y = Driver.HEIGHT / 2;
      vel = 0;
      dir = Math.PI;
    } else if (idMaze == 1) {
      x = 30;
      y = 30;
      vel = 0;
      dir = 2 * Math.PI;
    }
    this.idMaze = idMaze;
    maze.setIdMaze(idMaze);
    createMaze();
  }

  public synchronized void createMaze() {
    x1.clear();
    x2.clear();
    y1.clear();
    y2.clear();
    for (int i = 0; i < maze.getLength() + maze.getEndLength(); i++) {
      Wall wall = Maze.getWalls().get(i);
      if (wall == null) {
        continue;
      }
      x1.add(wall.getx1());
      x2.add(wall.getx2());
      y1.add(wall.gety1());
      y2.add(wall.gety2());
    }
  }

  public void addEventHandlers() {
    this.addKeyListener(this);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Print background
    Graphics2D g2d = (Graphics2D) g;

    if (id == 0) {
      GradientPaint gradient = new GradientPaint(0, Driver.HEIGHT / 2, Color.BLACK, 0, Driver.HEIGHT,
          Color.GREEN);
      g2d.setPaint(gradient);
      g2d.fillRect(0, (int) (Driver.HEIGHT / 2), Driver.WIDTH, Driver.HEIGHT);
      gradient = new GradientPaint(0, 0, Color.WHITE, 0, Driver.HEIGHT, Color.BLUE);
      g2d.setPaint(gradient);
      g2d.fillRect(0, 0, Driver.WIDTH, (int) (Driver.HEIGHT / 2));

      // Print height
      int step = 0;
      int xPos = 0;
      int counter = 0;
      try {
        step = Driver.WIDTH / (rays.getDist().size() - 1);
      } catch (Exception e) {
      }

      ArrayList<Double> distList = new ArrayList<>(rays.getDist());
      int distListSize = distList.size();
      counter = 0;
      try {
        step = Driver.WIDTH / (distListSize - 1);
      } catch (Exception e) {
      }
      for (int i = 0; i < distListSize; i++) {
        double dist = distList.get(i);
        int height = (int) (HEIGHT / dist);
        int shade = (int) (255 / (dist * 0.02));
        shade = Math.max(0, Math.min(255, shade));
        try {
          if (rays.getID(counter)) {
            Color strokeColor = new Color(shade, 0, 0);
            g2d.setColor(strokeColor);
          } else {
            Color strokeColor = new Color(0, 0, shade);
            g2d.setColor(strokeColor);
          }
        } catch (Exception e) {
        }
        counter++;
        g2d.setStroke(new BasicStroke(step + 1));
        g2d.drawLine(xPos, (int) (Driver.HEIGHT / 2 - height + step / 2), xPos,
            (int) (Driver.HEIGHT / 2 + height - step / 2));
        xPos += step;
      }
      g2d.setStroke(new BasicStroke(1));

      // Print info
      Color transGray = new Color(100, 100, 100, 100);
      g.setColor(transGray);

      g.fillRect(0, 0, 175, 340);

      g.setColor(Color.WHITE);

      Font font = new Font("Roboto", Font.BOLD | Font.ITALIC, 30);
      g.setFont(font);
      g.drawString("RES:", 15, 40);
      g.drawString("FOV:", 15, 205);

      font = new Font("Roboto", Font.ITALIC, 25);
      g.setFont(font);
      g.drawString("[z]  >  -", 15, 70);
      g.drawString("[x]  >  +", 15, 105);
      g.drawString("[c]  >  reset", 15, 140);
      g.drawString("[a]  >  -", 15, 235);
      g.drawString("[s]  >  +", 15, 270);
      g.drawString("[d]  >  reset", 15, 305);

    } else {

      // Print maze
      g.setColor(Color.BLACK);
      for (int i = 0; i < maze.getLength(); i++) {
        Wall wall = Maze.getWalls().get(i);
        g.drawRect(wall.getx1(), wall.gety1(), wall.getx2(), wall.gety2());
        g.fillRect(wall.getx1(), wall.gety1(), wall.getx2(), wall.gety2());
      }

      g.setColor(Color.GREEN);
      for (int i = 0; i < maze.getEndLength(); i++) {
        Wall wall = Maze.getWalls().get(maze.getLength() + i);
        g.drawRect(wall.getx1(), wall.gety1(), wall.getx2(), wall.gety2());
        g.fillRect(wall.getx1(), wall.gety1(), wall.getx2(), wall.gety2());
      }

      // Print rays
      g.setColor(Color.RED);
      ArrayList<Line2D.Double> copyOfRays = new ArrayList<>(rays.getRays());
      for (Line2D.Double ray : copyOfRays) {
        g.drawLine((int) ray.x1, (int) ray.y1, (int) ray.x2, (int) ray.y2);
      }

      // Print info
      Color transGray = new Color(100, 100, 100, 100);
      g.setColor(transGray);

      g.fillRect(0, 0, 175, 340);

      g.setColor(Color.WHITE);

      Font font = new Font("Roboto", Font.BOLD | Font.ITALIC, 30);
      g.setFont(font);
      g.drawString("RES:", 15, 40);
      g.drawString("FOV:", 15, 205);

      font = new Font("Roboto", Font.ITALIC, 25);
      g.setFont(font);
      g.drawString("[z]  >  -", 15, 70);
      g.drawString("[x]  >  +", 15, 105);
      g.drawString("[c]  >  reset", 15, 140);
      g.drawString("[a]  >  -", 15, 235);
      g.drawString("[s]  >  +", 15, 270);
      g.drawString("[d]  >  reset", 15, 305);

      // Print player
      Rectangle rect = new Rectangle((int) Math.round(x - SIZE / 2), (int) Math.round(y - SIZE / 2), SIZE, SIZE);
      g2d.setColor(Color.GREEN);
      g2d.rotate(-dir, x, y);
      g2d.draw(rect);
      g2d.fill(rect);

    }
  }

  private void movePlayer() {
    if (moveUp) {
      vel += V;
    }
    if (moveDown) {
      vel -= V;
    }
    if (moveLeft) {
      dir += W;
    }
    if (moveRight) {
      dir -= W;
    }
    move(vel);
    vel *= U;
  }

  private void move(double steps) {
    tryMove(steps * Math.sin(dir), 0);
    tryMove(0, steps * Math.cos(dir));
  }

  private void tryMove(double dx, double dy) {
    if (!playerTouchMaze(x + dx, y + dy)) {
      x += dx;
      y += dy;
    } else {
      try {
        tryMove(dx / 2, dy / 2);
      } catch (StackOverflowError e) {
        x = Math.random() * Driver.WIDTH;
        y = Math.random() * Driver.HEIGHT;
      }
    }
  }

  public boolean playerTouchMaze(double x, double y) {
    for (int i = 0; i < maze.getLength() + maze.getEndLength(); i++) {
      int x1 = Maze.getWalls().get(i).getx1();
      int x2 = x1 + Maze.getWalls().get(i).getx2();
      int y1 = Maze.getWalls().get(i).gety1();
      int y2 = y1 + Maze.getWalls().get(i).gety2();
      boolean cond = x + SIZE / 2 > x1 && y + SIZE / 2 > y1 && x - SIZE / 2 < x2 && y - SIZE / 2 < y2;
      if (cond && i >= maze.getLength()) {
        System.exit(0);
      }
      if (cond) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub
    int keyCode = e.getKeyCode();
    switch (keyCode) {
    case KeyEvent.VK_UP:
      moveUp = true;
      break;
    case KeyEvent.VK_DOWN:
      moveDown = true;
      break;
    case KeyEvent.VK_LEFT:
      moveLeft = true;
      break;
    case KeyEvent.VK_RIGHT:
      moveRight = true;
      break;
    case KeyEvent.VK_Z:
      changeRes = -1;
      break;
    case KeyEvent.VK_X:
      changeRes = 1;
      break;
    case KeyEvent.VK_C:
      changeRes = 2;
      break;
    case KeyEvent.VK_A:
      changeFov = -1;
      break;
    case KeyEvent.VK_S:
      changeFov = 1;
      break;
    case KeyEvent.VK_D:
      changeFov = 2;
      break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // TODO Auto-generated method stub
    int keyCode = e.getKeyCode();
    switch (keyCode) {
    case KeyEvent.VK_UP:
      moveUp = false;
      break;
    case KeyEvent.VK_DOWN:
      moveDown = false;
      break;
    case KeyEvent.VK_LEFT:
      moveLeft = false;
      break;
    case KeyEvent.VK_RIGHT:
      moveRight = false;
      break;
    case KeyEvent.VK_Z:
      changeRes = 0;
      break;
    case KeyEvent.VK_X:
      changeRes = 0;
      break;
    case KeyEvent.VK_C:
      changeRes = 0;
      break;
    case KeyEvent.VK_A:
      changeFov = 0;
      break;
    case KeyEvent.VK_S:
      changeFov = 0;
      break;
    case KeyEvent.VK_D:
      changeRes = 0;
      break;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  public static double getXComp() {
    return x;
  }

  public static double getYComp() {
    return y;
  }
}

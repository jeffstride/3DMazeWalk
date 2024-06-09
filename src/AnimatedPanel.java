import javax.swing.JPanel;

public abstract class AnimatedPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public AnimatedPanel() {
    super(false);
  }

  public abstract void updateAnimation();
}

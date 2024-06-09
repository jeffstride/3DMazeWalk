import java.awt.Color;
import javax.swing.*;

public class Driver extends JFrame {
	private static final long serialVersionUID = 1L;
	private static volatile boolean done = false;
	public static final int WIDTH = 1200, HEIGHT = 700;
	public static int delay = 20;

	private Player panel;
	private int currentView = -1;
	private int currentMaze = -1;

	public static void main(String[] args) throws InterruptedException {
		SwingUtilities.invokeLater(() -> {
			Driver theGUI = new Driver();
			theGUI.createFrame(theGUI);
			theGUI.startAnimationLoop();
		});
	}

	public void createFrame(Object semaphore) {
		this.setTitle("Raycasting");
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setBackground(Color.BLACK);
		addMenuBar();

		panel = new Player();
		panel.setBounds(0, 0, WIDTH, HEIGHT);
		add(panel);

		this.currentView = 0; // Set initial view (3D View)
		this.currentMaze = 0; // Set initial maze (random)
		panel.setId(currentView);
		panel.setIdMaze(currentMaze);
		panel.setVisible(true);
		this.setVisible(true);

		// Done creating our frame
		synchronized (semaphore) {
			semaphore.notify();
		}
	}

	public void startAnimationLoop() {
		new Thread(() -> {
			while (true) {
				startAnimation();
			}
		}).start();
	}

	public void startAnimation() {
		Driver.done = false;
		try {
			panel.requestFocusInWindow();
			while (!Driver.done) {
				if (currentView != -1) {
					panel.updateAnimation();
					SwingUtilities.invokeLater(this::repaint);
					Thread.sleep(Driver.delay);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void showPanel(int viewIndex) {
		Driver.done = true;

		while (!done) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		currentView = viewIndex;
		panel.setId(currentView);

		Driver.done = false;
	}

	private void showPanel(int viewIndex, int mazeIndex) {
		Driver.done = true;

		while (!done) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		currentView = viewIndex;
		currentMaze = mazeIndex;
		panel.setId(currentView);
		panel.setIdMaze(currentMaze);

		Driver.done = false;
	}

	private JMenu createPerspectiveMenu() {
		JMenu menu = new JMenu("Perspective");
		menu.setMnemonic('P');

		JMenuItem item = new JMenuItem("2D View", '2');
		item.addActionListener(e -> showPanel(1)); // Show 2D View
		menu.add(item);

		item = new JMenuItem("3D View", '3');
		item.addActionListener(e -> showPanel(0)); // Show 3D View
		menu.add(item);

		return menu;
	}

	private void addMenuBar() {
		JMenuBar bar = new JMenuBar();
		this.setJMenuBar(bar);

		bar.add(createPerspectiveMenu());
		bar.add(createMazeMenu());
	}

	private JMenu createMazeMenu() {
		JMenu menu = new JMenu("Map");
		menu.setMnemonic('M');

		JMenuItem item = new JMenuItem("Generate Random Map", 'G');
		item.addActionListener(e -> showPanel(currentView, 0));
		menu.add(item);

		item = new JMenuItem("Map 1", 'M');
		item.addActionListener(e -> showPanel(currentView, 1));
		menu.add(item);

//		item = new JMenuItem("Map 2", 'A');
//		item.addActionListener(e -> showPanel(currentView, 2));
//		menu.add(item);
//
//		item = new JMenuItem("Map 3", 'P');
//		item.addActionListener(e -> showPanel(currentView, 3));
//		menu.add(item);

		return menu;
	}

}

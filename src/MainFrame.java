import java.awt.Container;

import javax.swing.JFrame;

/*
 * 메인 프레임
 */

public class MainFrame extends JFrame {
	private GamePanel gamePanel = new GamePanel();

	public MainFrame() {
		setTitle("Valleyball game");

		Container c = getContentPane();
		c.setLayout(null);
		c.add(gamePanel);

		setSize(1600, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new MainFrame();
	}

}

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

/*
 * 메인 프레임
 */

public class MainFrame extends JFrame {
	private GamePanel gamePanel = new GamePanel();
	private StartPanel menuPanel = new StartPanel();

	public MainFrame() {
		setTitle("Valleyball game");

		gamePanel.setVisible(false);
		add(menuPanel);
		add(gamePanel);
		menuPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				menuPanel.setVisible(false);
				gamePanel.setVisible(true);
				gamePanel.requestFocus();
				gamePanel.setFocusable(true);
			}
		});

		setSize(1500, 1000);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new MainFrame();
	}

}

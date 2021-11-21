import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class StartPanel extends JPanel {
	Toolkit tk = Toolkit.getDefaultToolkit();
	Image img = tk.getImage("startScreen.png");

	public StartPanel() {
		setSize(1500, 1000);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, this);
	}
}

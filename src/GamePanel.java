import javax.swing.JPanel;

/*
 * 게임이 진행되는 패널
 * 게임에 필요한 요소들 부착
 */

public class GamePanel extends JPanel {
	private Player rightPlayer = new Player(1);
	private Player leftPlayer = new Player(2);
	private Ball ball;

	public GamePanel() {
		setLayout(null);
		add(rightPlayer);		// 오른쪽 플레이어 부착
		add(leftPlayer);		// 왼쪽 플레이어 부착

		setSize(1600, 800);
	}
}

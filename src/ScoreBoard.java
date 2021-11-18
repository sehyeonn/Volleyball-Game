import java.awt.Image;
import java.awt.Toolkit;

public class ScoreBoard {
	int leftScore;		// 왼쪽 플레이어 점수
	int rightScore;		// 오른쪽 플레이어 점수

	Toolkit tk = Toolkit.getDefaultToolkit();
	Image img;

	int width = 200;
	int height = 90;

	public ScoreBoard() {
		leftScore = 0;
		rightScore = 0;

		img = tk.getImage("score_board.png");
	}
}

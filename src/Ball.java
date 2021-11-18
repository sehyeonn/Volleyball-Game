import java.awt.Image;
import java.awt.Toolkit;

public class Ball {
	/*
	 * 공 클래스
	 * 공의 튕김 등 움직임 구현
	 */
	final int BALL_UNIT = 5;
	int speed;

	int x;
	int y;
	int moveX;	// 공 이동 시 x 증가값
	int moveY;	// 공 이동 시 y 증가값

	Image img;
	Toolkit tk = Toolkit.getDefaultToolkit();

	int width;
	int height;

	int centerX = x + width;
	int centerY = y + height;

	public Ball(Player player) {
		x = player.x + 5;
		y = 100;
		speed = 1;
		moveX = 0;
		moveY = BALL_UNIT;

		img = tk.getImage("ball.png");
		width = 140;
		height = 140;
	}

	// 게임 재시작 시 공의 위치 지정, 전 게임에서 점수를 낸 플레이어가 선공
	public void ballSet(Player winnerPlayer) {
		x = winnerPlayer.x + 5;
		y = 200;
		moveX = 0;
		moveY = BALL_UNIT;
	}
}

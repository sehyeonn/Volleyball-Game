import java.awt.Image;
import java.awt.Toolkit;

public class Player {
	/*
	 * 플레이어 클래스 구현
	 * 처음 좌표 및 이미지 기본 정보
	 * 점프 기능 따로 구현
	 */
	final int PLAYER_UNIT = 5;		// 플레이어가 한 번에 이동하는 픽셀

	int whatPlayer;		// 왼쪽 플레이어인지 오른쪽 플레이어인지 구분
	final int RIGHT_PLAYER = 1;		// 오른쪽 플레이어
	final int LEFT_PLAYER = 2;		// 왼쪽 플레이어

	int x;	// 플레이어 x좌표
	int y;	// 플레이어 y좌표
	int groundY;	// 플레이어가 서 있는 바닥을 나타내는 y좌표

	int gravity = 0;	// 중력

	Image img;
	Toolkit tk = Toolkit.getDefaultToolkit();
	int width;
	int height;

	// true이면 점프 중
	private boolean isJump = false;

	boolean isSpike = false;	// 플레이어가 스파이크를 했는가

	// 플레이어 생성자, 오른쪽 플레이어(1) 왼쪽 플레이어(2)를 매개 변수로 받아 처음 위치 지정
	public Player(int p) {
		if(p == RIGHT_PLAYER) {
			x = 1250;	// 오른쪽 플레이어 위치 지정
			whatPlayer = RIGHT_PLAYER;
		}
		else if(p == LEFT_PLAYER) {
			x = 100;	// 오른쪽 플레이어 위치 지정
			whatPlayer = LEFT_PLAYER;
		}
		groundY = 750;
		y = 750;

		img = tk.getImage("player_removebg.png");
		width = 151;
		height = 151;
	}

	// 게임 재시작 시 호출
	public void playerSet() {
		if(whatPlayer == RIGHT_PLAYER)
			x = 1250;	// 오른쪽 플레이어 위치 지정
		else if(whatPlayer == LEFT_PLAYER)
			x = 100;	// 오른쪽 플레이어 위치 지정
		y = groundY;

		isJump = false;
	}

	public void jump() {
		Thread th = new JumpThread();
		if(isJump == false)
			th.start();
	}

	// 점프 스레드
	private class JumpThread extends Thread {
		@Override
		public void run() {
			gravity = 0;
			isJump = true;
			// 점프
			img = tk.getImage("player_jump_removebg.png");	// 플레이어 점프 이미지로 변경
			try {
				do {
					y -= PLAYER_UNIT;
					y += gravity++/20;	// 중력을 받아 서서히 떨어짐
					Thread.sleep(2);
				} while(y < groundY && isJump);	// 서서히 떨어지다 바닥에 도달하면 점프 끝
			} catch (InterruptedException e) { e.printStackTrace(); }
			isJump = false;
			y = groundY;
			img = tk.getImage("player_removebg.png");;	// 플레이어 이미지 원래대로
		}
	}

}

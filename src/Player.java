import java.awt.Image;
import java.awt.Toolkit;

public class Player {
	/*
	 * 플레이어 클래스 구현
	 * 처음 좌표 및 이미지 기본 정보
	 * 점프 기능 따로 구현
	 */
	final int PLAYER_UNIT = 5;		// 플레이어 이동 속도
	private final int PLAYER_JUMP = 280;	// 플레이어 점프 높이

	int whatPlayer;		// 왼쪽 플레이어인지 오른쪽 플레이어인지 구분
	final int RIGHT_PLAYER = 1;		// 오른쪽 플레이어
	final int LEFT_PLAYER = 2;		// 왼쪽 플레이어

	int x;	// 플레이어 x좌표
	int y;	// 플레이어 y좌표
	int groundY;	// 플레이어가 서 있는 바닥을 나타내는 y좌표

	double gravity;	// 중력
	int fallSpeed;

	Image img;
	Toolkit tk = Toolkit.getDefaultToolkit();
	int width;
	int height;

	int centerX = x + width/2;		// 플레이어의 중심 x 좌표
	int centerY = y + height/2;		// 플레이어의 중심 y 좌표

	// true이면 점프 중
	private boolean isJump = false;
	private boolean isFall = false;

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
		y = 750;
		groundY = 750;

		fallSpeed = 0;

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
		y = 750;
	}

	public void jump() {
		Thread th = new JumpThread();
		if(isJump == false && isFall == false)
			th.start();
	}

	// 점프 스레드
	private class JumpThread extends Thread {
		@Override
		public void run() {
			fallSpeed = 0;
			isJump = true;
			isFall = false;
			// 점프
			img = tk.getImage("player_jump_removebg.png");	// 플레이어 점프 이미지로 변경
			try {
				while(y >= groundY - PLAYER_JUMP) {
					y = y - PLAYER_UNIT;
					Thread.sleep(1 + fallSpeed/6);
					fallSpeed += 1;		// 중력을 받는 것처럼 구현 올라가는 속도가 점점 느려짐
				}
				Thread.sleep(40);
				fallSpeed = 0;
				isFall = true;
				isJump = false;
				// 점프 후 떨어짐
				while(y <= groundY) {
					y = y + PLAYER_UNIT;
					Thread.sleep(9 - fallSpeed/6);
					fallSpeed += 1;		// 중력을 받는 것처럼 구현 떨어지는 속도가 점점 빨라짐
				}
			} catch (InterruptedException e) { e.printStackTrace(); }

			isFall = false;
			img = tk.getImage("player_removebg.png");;	// 플레이어 이미지 원래대로
		}
	}

}

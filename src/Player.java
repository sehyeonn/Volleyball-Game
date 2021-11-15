import java.awt.Image;
import java.awt.Toolkit;

public class Player {
	/*
	 * 플레이어 클래스 구현
	 * 처음 좌표 및 이미지 기본 정보
	 * 점프 기능 따로 구현
	 */
	final int PLAYER_UNIT = 5;		// 플레이어 이동 속도
	private final int PLAYER_JUMP = 200;	// 플레이어 점프 높이

	private int whatPlayer;		// 왼쪽 플레이어인지 오른쪽 플레이어인지 구분
	private final int RIGHT_PLAYER = 1;		// 오른쪽 플레이어
	private final int LEFT_PLAYER = 2;		// 왼쪽 플레이어

	int x;	// 플레이어 x좌표
	int y;	// 플레이어 y좌표
	Image img;
	Toolkit tk = Toolkit.getDefaultToolkit();
	int width;
	int height;

	// true이면 점프 중
	private boolean isJump = false;
	private boolean isFall = false;

	// 플레이어 생성자, 오른쪽 플레이어(1) 왼쪽 플레이어(2)를 매개 변수로 받아 처음 위치 지정
	public Player(int p) {
		if(p == RIGHT_PLAYER) {
			x = 1350;	// 오른쪽 플레이어 위치 지정
			y = 550;
			whatPlayer = RIGHT_PLAYER;
		}
		else if(p == LEFT_PLAYER) {
			x = 100;	// 오른쪽 플레이어 위치 지정
			y = 550;
			whatPlayer = LEFT_PLAYER;
		}

		img = tk.getImage("player.png");
		width = img.getWidth(null);
		height = img.getHeight(null);
	}

	public void jump() {
		Thread th = new JumpThread();
		if(isJump == false && isFall == false) {
			System.out.println((whatPlayer==RIGHT_PLAYER ? "right" : "left") + "player jump");
			th.start();
		}
	}

	// 점프 스레드
	private class JumpThread extends Thread {
		@Override
		public void run() {
			isJump = true;
			isFall = false;
			// 점프
			tk.getImage("player_jump.png");	// 플레이어 점프 이미지로 변경
			while(y >= y - PLAYER_JUMP) {
				y = y - PLAYER_UNIT;
				try {
					Thread.sleep(PLAYER_UNIT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			isFall = true;
			isJump = false;
			// 점프 후 떨어짐
			while(y <= y - PLAYER_JUMP) {
				y = y + PLAYER_UNIT;
				try {
					Thread.sleep(PLAYER_UNIT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			isFall = false;
			tk.getImage("player.png");;	// 플레이어 이미지 원래대로
		}
	}

}

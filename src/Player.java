import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Player extends JLabel {
	/*
	 * 플레이어 클래스 구현
	 * 점프 이동 구현
	 */
	private Player player = this;
	private int whatPlayer;		// 왼쪽 플레이어인지 오른쪽 플레이어인지 구분
	
	private final int PLAYER_UNIT = 10;		// 플레이어 이동 속도
	private final int PLAYER_JUMP = 100;	// 플레이어 점프 높이
	
	private final int RIGHT_PLAYER = 1;		// 오른쪽 플레이어
	private final int LEFT_PLAYER = 2;		// 왼쪽 플레이어

	private final int RIGHT_P_X = 900;		// 오른쪽 플레이어 위치
	private final int LEFT_P_X = 100;		// 왼쪽 플레이어 위치
	private final int PLAYER_Y = 400;		// 플레이어 y좌표 위치
	
	// 좌우 이동 락
	private boolean moveLockLeft = false;
	private boolean moveLockRight = false;
	
	// true이면 이동 가능
	private boolean isLeft = false;
	private boolean isRight = false;
	private boolean isJump = false;
	private boolean isFall = false;

	private ImageIcon playerImage, playerJumpImage;		// 플레이어 이미지, 점프 이미지

	// 플레이어 생성자
	public Player(int p) {
		if(p == RIGHT_PLAYER) {
			setLocation(RIGHT_P_X, PLAYER_Y);	// 오른쪽 플레이어 위치 지정
			whatPlayer = RIGHT_PLAYER;
		}
		else if(p == LEFT_PLAYER) {
			setLocation(LEFT_P_X, PLAYER_Y);	// 왼쪽 플레이어 위치 지정
			whatPlayer = LEFT_PLAYER;
		}

		setIcon(playerImage);	// 플레이어 이미지 설정

		playerImage = new ImageIcon("player.png");
		playerJumpImage = new ImageIcon("player_jump.png");
	}

	
	public void moveLeft() {
		Thread th = new LeftMoveThread();
		if(isLeft == false) {	// 이 메소드를 한 번만 호출하기 위한 조건문
			System.out.println((whatPlayer==RIGHT_PLAYER ? "right" : "left") + "player move left");
			th.start();
		}
	}
	
	public void moveRight() {
		Thread th = new RightMoveThread();
		if(isRight == false) {
			System.out.println((whatPlayer==RIGHT_PLAYER ? "right" : "left") + "player move right");
			th.start();
		}
	}
	
	public void jump() {
		
	}
	
	// 왼쪽 이동 스레드
	private class LeftMoveThread extends Thread {
		@Override
		public void run() {
			isRight = false;
			isLeft = true;
			
			// isLeft가 true일 동안 이동
			while(isLeft == true)
				player.setLocation(player.getX() - PLAYER_UNIT, player.getY());
		}
	}
	
	// 오른쪽 이동 스레드
	private class RightMoveThread extends Thread {
		@Override
		public void run() {
			isLeft = false;
			isRight = true;
			
			// isRight가 true일 동안 이동
			while(isRight == true)
				player.setLocation(player.getX() + PLAYER_UNIT, player.getY());
		}
	}
	
	// 점프 스레드
	private class JumpThread extends Thread {
		@Override
		public void run() {
			
		}
	}
	
	// 중력 스레드
	private class FallThread extends Thread {
		@Override
		public void run() {
			
		}
	}

	private class PlayerKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar() == 'q')
				System.exit(0);

			int keyCode = e.getKeyCode();
			switch(keyCode) {
				case KeyEvent.VK_UP:	// �젏�봽
					player.setLocation(player.getX(), player.getY()-PLAYER_JUMP); break;
				case KeyEvent.VK_LEFT:
					player.setLocation(player.getX()-PLAYER_UNIT, player.getY()); break;
				case KeyEvent.VK_RIGHT:
					player.setLocation(player.getX()+PLAYER_UNIT, player.getY()); break;
			}
		}
	}

}

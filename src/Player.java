import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Player extends JLabel {
	/*
	 * 플레이어 클래스 구현
	 * 점프 및 이동 구현
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

	// 플레이어 생성자, 오른쪽 플레이어(1) 왼쪽 플레이어(2)를 매개 변수로 받아 처음 위치 지정
	public Player(int p) {
		if(p == RIGHT_PLAYER) {
			setLocation(RIGHT_P_X, PLAYER_Y);	// 오른쪽 플레이어 위치 지정
			whatPlayer = RIGHT_PLAYER;
		}
		else if(p == LEFT_PLAYER) {
			setLocation(LEFT_P_X, PLAYER_Y);	// 왼쪽 플레이어 위치 지정
			whatPlayer = LEFT_PLAYER;
		}
		
		playerImage = new ImageIcon("player.png");
		playerJumpImage = new ImageIcon("player_jump.png");
		
		setSize(playerImage.getIconWidth(), playerImage.getIconHeight());
		setIcon(playerImage);	// 플레이어 이미지 설정
		addKeyListener(new PlayerKeyListener());	// 키보드 리스너 달기
		
		this.requestFocus();
		this.setFocusable(true);
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
		Thread th = new JumpThread();
		if(isJump == false) {
			System.out.println((whatPlayer==RIGHT_PLAYER ? "right" : "left") + "player jump");
			th.start();
		}		
	}
	
	// 왼쪽 이동 스레드
	private class LeftMoveThread extends Thread {
		@Override
		public void run() {
			isRight = false;
			isLeft = true;
			
			// isLeft가 true일 동안 이동
			while(isLeft == true) {
				player.setLocation(player.getX() - PLAYER_UNIT, player.getY());
				player.repaint();
			}
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
			isJump = true;
			isFall = false;
			// 점프
			player.setIcon(playerJumpImage);	// 플레이어 점프 이미지로 변경
			while(player.getY() >= PLAYER_Y - PLAYER_JUMP) {
				player.setLocation(player.getX(), player.getY() - PLAYER_UNIT);
			}
			
			isFall = true;
			isJump = false;
			// 점프 후 떨어짐
			while(player.getY() <= PLAYER_Y) {
				player.setLocation(player.getX(), player.getY() + PLAYER_UNIT);
			}
			player.setIcon(playerImage);	// 플레이어 이미지 원래대로
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
			// 오른쪽 플레이어 키 누름
			if(whatPlayer == 1) {				
				int keyCode = e.getKeyCode();
				switch(keyCode) {
				case KeyEvent.VK_UP:
					player.jump(); break;
				case KeyEvent.VK_LEFT:
					player.moveLeft(); break;
				case KeyEvent.VK_RIGHT:
					player.moveRight(); break;
				}
			}
			
			// 왼쪽 플레이어 키 누름
			else {
				int keyCode = e.getKeyChar();
				switch(keyCode) {
					case 'w':
						player.jump(); break;
					case 'a':
						player.moveLeft(); break;
					case 'd':
						player.moveRight(); break;
				}
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			// 오른쪽 플레이어 키 뗌
			if(whatPlayer == 1) {				
				int keyCode = e.getKeyCode();
				switch(keyCode) {
				case KeyEvent.VK_UP:
					player.isJump = false; break;
				case KeyEvent.VK_LEFT:
					player.isLeft = false; break;
				case KeyEvent.VK_RIGHT:
					player.isRight = false; break;
				}
			}
			
			// 왼쪽 플레이어 키 뗌
			else {
				int keyCode = e.getKeyChar();
				switch(keyCode) {
					case 'w':
						player.isJump = false; break;
					case 'a':
						player.isLeft = false; break;
					case 'd':
						player.isRight = false; break;
				}
			}
		}
	}

}

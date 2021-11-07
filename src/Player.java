import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Player extends JLabel {
	/*
	 * 플레이어 클래스
	 * 이동 점프 구현
	 * 오른쪽 플레이어(1)일 경우, 왼쪽 플레이어(2)일 경우 나뉘어짐
	 */
	private Player player = this;
	private final int PLAYER_UNIT = 10;		// 이동 속도
	private final int PLAYER_JUMP = 100;	// 점프 정도

	private final int RIGHT_PLAYER = 1;		// 오른쪽 플레이어
	private final int LEFT_PLAYER = 2;		// 왼쪽 플레이어

	private final int RIGHT_P_X = 900;		// 오른쪽 플레이어 시작 x 좌표
	private final int LEFT_P_X = 100;		// 왼쪽 플레이어 시작 x 좌표
	private final int PLAYER_Y = 400;		// 양쪽 플레이어 y 좌표

	private ImageIcon playerImage, playerJumpImage;		// 플레이어 이미지, 점프 시 이미지

	// 플레이어 생성자, p 매개변수를 통해 오른쪽 플레이어 생성인가 왼쪽 플레이어 생성인가 결정
	public Player(int p) {
		if(p == RIGHT_PLAYER) {
			setLocation(RIGHT_P_X, PLAYER_Y);	// 오른쪽 플레이어 기본 위치 설정
		}
		else if(p == LEFT_PLAYER) {
			setLocation(LEFT_P_X, PLAYER_Y);	// 왼쪽 플레이어 기본 위치 설정
		}

		setIcon(playerImage);	// 기본 플레이어 이미지 설정

		playerImage = new ImageIcon("player.png");
		playerJumpImage = new ImageIcon("player_jump.png");
	}

	private void jump() {
		player.setLocation(player.getX(), player.getY()-PLAYER_JUMP);
		if(player.getY() > 100) {		// 바닥보다 위에 있으면
			while(player.getY() <= 100) {
				player.setLocation(player.getX(), player.getY()+PLAYER_UNIT);
			}
		}
	}

	private class PlayerKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar() == 'q')
				System.exit(0);

			int keyCode = e.getKeyCode();
			switch(keyCode) {
				case KeyEvent.VK_UP:	// 점프
					player.setLocation(player.getX(), player.getY()-PLAYER_JUMP); break;
				case KeyEvent.VK_LEFT:
					player.setLocation(player.getX()-PLAYER_UNIT, player.getY()); break;
				case KeyEvent.VK_RIGHT:
					player.setLocation(player.getX()+PLAYER_UNIT, player.getY()); break;
			}
		}
	}

}

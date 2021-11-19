import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

/*
 * 게임이 진행되는 패널
 * 게임에 필요한 요소들 부착
 */

public class GamePanel extends JPanel {
	private Player rightPlayer;
	private Player leftPlayer;
	private Ball ball;
	private ScoreBoard scoreBoard;
	private int floor = 100;

	Image buffImg;
	Graphics buffG;

	Toolkit tk = Toolkit.getDefaultToolkit();
	Image netImg = tk.getImage("net.png");
	Image backgroundImg = tk.getImage("background.png");
	Image floorImg = tk.getImage("floor.png");

	private PlayerController playerController = new PlayerController();
	Thread pth = new Thread(playerController);
	Thread bth = new BallThread();

	boolean rightKeyUp;
    boolean rightKeyLeft;
    boolean rightKeyRight;
    boolean leftKeyUp;
    boolean leftKeyLeft;
    boolean leftKeyRight;

	public GamePanel() {
		rightPlayer = new Player(1);
		leftPlayer = new Player(2);
		scoreBoard = new ScoreBoard();
		ball = new Ball(rightPlayer);	// 오른쪽 플레이어 선공

		setLayout(null);

		setSize(1500, 1000);

		pth.start();
		bth.start();

		addKeyListener(playerController);
		this.requestFocus();
		this.setFocusable(true);
	}

	private void gameReset(Player winnerPlayer) {
		try {
			Thread.sleep(1000);
			rightPlayer.playerSet();
			leftPlayer.playerSet();
			ball.ballSet(winnerPlayer);
			Thread.sleep(1000);
		} catch (InterruptedException e) { e.printStackTrace(); }
	}

	private void gameFinish(Player winnerPlayer) {

	}


	@Override
	public void paint(Graphics g) {
		buffImg = createImage(getWidth(),getHeight()); // 버퍼링용 이미지 ( 도화지 )
		buffG = buffImg.getGraphics(); // 버퍼링용 이미지에 그래픽 객체를 얻어야 그릴 수 있다고 한다. ( 붓? )

		// 배경 그리기
		buffG.drawImage(backgroundImg, 0, 0, this);

		// 플레이어 그리기
		buffG.drawImage(rightPlayer.img, rightPlayer.x, rightPlayer.y, this);
		buffG.drawImage(leftPlayer.img, leftPlayer.x, leftPlayer.y, this);

		// 점수판 그리기
		buffG.drawImage(scoreBoard.img, getWidth()/2 - scoreBoard.width/2, 10, this);
		buffG.setColor(Color.WHITE);
		buffG.setFont(new Font("맑은 고딕", Font.BOLD, 50));
		buffG.drawString(Integer.toString(scoreBoard.leftScore), getWidth()/2 - scoreBoard.width/2 + 20, 75);
		buffG.drawString(Integer.toString(scoreBoard.rightScore), getWidth()/2 - scoreBoard.width/2 + 20 + scoreBoard.width/2, 75);

		// 네트 그리기
		buffG.drawImage(netImg, getWidth()/2-10, getHeight()-400, this);

		// 바닥 그리기
		buffG.drawImage(floorImg, 0, getHeight()-floor, this);

		// 공 그리기
		buffG.drawImage(ball.img, ball.x, ball.y, this);

		g.drawImage(buffImg, 0, 0, this); // 화면 버퍼(buffG)에 그려진 이미지(buffImg)옮김. ( 도화지에 이미지를 출력 )
//		update(g);
	}

	@Override
	public void update(Graphics g) {
//		repaint();
		paint(g);
	}


	class BallThread extends Thread {
		@Override
		public void run() {
			int ballRightPlayerDistance;
			int ballLeftPlayerDistance;
			int ballCenterX;
			int ballCenterY;
			int rightPlayerCenterX;
			int rightPlayerCenterY;
			int leftPlayerCenterX;
			int leftPlayerCenterY;

			while(true) {
				ballCenterX = ball.x + ball.width;
				ballCenterY = ball.y + ball.height;
				rightPlayerCenterX = rightPlayer.x + rightPlayer.width;
				rightPlayerCenterY = rightPlayer.y + rightPlayer.height;
				leftPlayerCenterX = leftPlayer.x + leftPlayer.width;
				leftPlayerCenterY = leftPlayer.y + leftPlayer.height;

				ballRightPlayerDistance 	// 공과 오른쪽 플레이어와의 거리
				= (int) Math.sqrt((ballCenterX - rightPlayerCenterX)*(ballCenterX - rightPlayerCenterX) + (ballCenterY - rightPlayerCenterY)*(ballCenterY - rightPlayerCenterY));

				ballLeftPlayerDistance 	// 공과 오른쪽 플레이어와의 거리
				= (int) Math.sqrt((ballCenterX - leftPlayerCenterX)*(ballCenterX - leftPlayerCenterX) + (ballCenterY - leftPlayerCenterY)*(ballCenterY - leftPlayerCenterY));

				// 공이 플레이어와 충돌할 경우
				if(ballRightPlayerDistance <= rightPlayer.width/2 + ball.width/2) {
					ball.moveX = (ballCenterX - rightPlayerCenterX)/3/7;
					ball.moveY = (ballCenterY - rightPlayerCenterY)/3/7;
					ball.gravity = 0;	// 플레이어와 충돌 시 공 중력 초기화
				}
				if(ballLeftPlayerDistance <= leftPlayer.width/2 + ball.width/2) {
					ball.moveX = (ballCenterX - leftPlayerCenterX)/3/7;
					ball.moveY = (ballCenterY - leftPlayerCenterY)/3/7;
					ball.gravity = 0;	// 플레이어와 충돌 시 공 중력 초기화
				}

				// 공이 벽과 충돌할 경우
//				if(ball.y <= 0) {		// 천장 충돌
//					ball.moveY = -ball.moveY;
//					ball.gravity = 0;	// 천장과 충돌 시 공 중력 초기화
//				}
				if(ball.x <= 0)		// 왼쪽 벽 충돌
					ball.moveX = -ball.moveX;
				if(ball.x >= getWidth() - ball.width)	// 오른쪽 벽 충돌
					ball.moveX = -ball.moveX;

				// 네트와 충돌할 경우
				if((ball.x <= getWidth()/2+10 && ball.x >= getWidth()/2-10) && ball.y + ball.height/2 >= getHeight()-400)
					ball.moveX = -ball.moveX;
				if((ball.x + ball.width >= getWidth()/2-10 && ball.x + ball.width <= getWidth()/2) && ball.y + ball.height/2 >= getHeight()-400)
					ball.moveX = -ball.moveX;

				// 공이 바닥에 닿을 경우
				if(ball.y >= getHeight() - floor - ball.height) {
					if(ball.x >= 0 && ballCenterX <= getWidth()/2) {	// 오른쪽 플레이어 득점
						scoreBoard.rightScore += 1;
						if(scoreBoard.rightScore >= 15) {
							gameFinish(rightPlayer);
							break;
						}
						gameReset(rightPlayer);		// 초기화
					}
					else {								// 왼쪽 플레이어 득점
						scoreBoard.leftScore += 1;
						if(scoreBoard.leftScore >= 15) {
							gameFinish(leftPlayer);
							break;
						}
						gameReset(leftPlayer);		// 초기화
					}
				}

				// 공 이동
				ball.x += ball.moveX;
				ball.y += ball.moveY;

				ball.y += ball.gravity++/35;	// 공 중력 받음
				repaint();

				try {
					Thread.sleep(1, 70000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	// 플레이어 이동 및 점프를 위한 키리스너
	class PlayerController extends KeyAdapter implements Runnable {
		// 키에 대한 처리.
		public void keyProcess() {
			if(rightKeyLeft) {
				if(rightPlayer.x > getWidth()/2)
					rightPlayer.x -= rightPlayer.PLAYER_UNIT;
			}

			if(rightKeyRight) {
				if(rightPlayer.x < getWidth() - rightPlayer.width)
					rightPlayer.x += rightPlayer.PLAYER_UNIT;
			}

			if(leftKeyLeft) {
				if(leftPlayer.x > 0)
					leftPlayer.x -= leftPlayer.PLAYER_UNIT;
			}

			if(leftKeyRight) {
				if(leftPlayer.x < getWidth()/2 - leftPlayer.width)
					leftPlayer.x += leftPlayer.PLAYER_UNIT;
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {	// 오른쪽 플레이어 키 입력
			case KeyEvent.VK_UP :
				rightPlayer.jump();
				break;
			case KeyEvent.VK_LEFT :
				rightKeyLeft = true;
				break;
			case KeyEvent.VK_RIGHT :
				rightKeyRight = true;
				break;
			}
			switch (e.getKeyCode()) {	// 왼쪽 플레이어 키 입력
			case KeyEvent.VK_W :
				leftPlayer.jump();
				break;
			case KeyEvent.VK_A :
				leftKeyLeft = true;
				break;
			case KeyEvent.VK_D :
				leftKeyRight = true;
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {	// 오른쪽 플레이어 키 뗌
			case KeyEvent.VK_LEFT :
				rightKeyLeft = false;
				break;
			case KeyEvent.VK_RIGHT :
				rightKeyRight = false;
				break;
			}
			switch (e.getKeyCode()) {	// 왼쪽 플레이어 키 뗌
			case KeyEvent.VK_A :
				leftKeyLeft = false;
				break;
			case KeyEvent.VK_D :
				leftKeyRight = false;
				break;
			}
		}

		@Override
		public void run() {
			try {
				while(true) {
					keyProcess();
					repaint();
					Thread.sleep(3);
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}

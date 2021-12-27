import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
	private int floor = 60;
	private int setScore = 15;	// 해당 점수를 먼저 내는 플레이어의 승리

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

    boolean isFinished;		// 게임이 끝났는지 확인하는 변수

    String winMessage = "";

	public GamePanel() {
		rightPlayer = new Player(1);
		leftPlayer = new Player(2);
		scoreBoard = new ScoreBoard();
		ball = new Ball(rightPlayer);	// 오른쪽 플레이어 선공

		isFinished = false;

		setLayout(null);

		setSize(1500, 1000);

		pth.start();
		bth.start();

		addKeyListener(playerController);
		addMouseListener(new MouseAdapter() {	// 게임 종료 후 화면 클릭 시 재시작
			@Override
			public void mouseClicked(MouseEvent e) {
				if(isFinished) {
					scoreBoard.leftScore = 0;
					scoreBoard.rightScore = 0;
					winMessage = "";		// 게임 결과 메세지 초기화
					gameReset(rightPlayer);

					bth = new BallThread();
					bth.start();

					isFinished = false;
				}
			}
		});
	}

	private void gameReset(Player winnerPlayer) {
		try {
			Thread.sleep(500);
			rightPlayer.playerSet();
			leftPlayer.playerSet();
			ball.ballSet(winnerPlayer);
			Thread.sleep(500);
		} catch (InterruptedException e) { e.printStackTrace(); }
	}

	private void gameFinish(Player winnerPlayer) {
		isFinished = true;
		winMessage = winnerPlayer.whatPlayer + "P WIN! click to restart";
		repaint();
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

		// 게임 종료 시 승리 메세지 출력
		buffG.setColor(Color.BLACK);
		buffG.setFont(new Font("맑은 고딕", Font.BOLD, 70));
		buffG.drawString(winMessage, 320, 300);

		// 공 그리기
		buffG.drawImage(ball.img, ball.x, ball.y, this);

		// 네트 그리기
		buffG.drawImage(netImg, getWidth()/2-10, getHeight()-400, this);

		// 바닥 그리기
		buffG.drawImage(floorImg, 0, getHeight()-floor, this);

		g.drawImage(buffImg, 0, 0, this); // 화면 버퍼(buffG)에 그려진 이미지(buffImg)옮김. ( 도화지에 이미지를 출력 )
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}


	class BallThread extends Thread {
		@Override
		public void run() {
			int ballRightPlayerDistance;	// 공과 오른쪽 플레이어의 거리
			int ballLeftPlayerDistance;		// 공과 왼쪽 플레이어의 거리
			int ballCenterX;				// 공의 중심 좌표
			int ballCenterY;				// 공의 중심 좌표
			int rightPlayerCenterX;			// 오른쪽 플레이어의 중심 좌표
			int rightPlayerCenterY;			// 오른쪽 플레이어의 중심 좌표
			int leftPlayerCenterX;			// 왼쪽 플레리어의 중심 좌표
			int leftPlayerCenterY;			// 왼쪽 플레이어의 중심 좌표

			while(true) {
				// 공의 중심 좌표와 공와 플레이어 사이의 거리를 공이 이동할 때마다 구함
				ballCenterX = ball.x + ball.width/2;
				ballCenterY = ball.y + ball.height/2;
				rightPlayerCenterX = rightPlayer.x + rightPlayer.width/2;
				rightPlayerCenterY = rightPlayer.y + rightPlayer.height/2;
				leftPlayerCenterX = leftPlayer.x + leftPlayer.width/2;
				leftPlayerCenterY = leftPlayer.y + leftPlayer.height/2;

				ballRightPlayerDistance 	// 공과 오른쪽 플레이어와의 거리
				= (int) Math.sqrt((ballCenterX - rightPlayerCenterX)*(ballCenterX - rightPlayerCenterX) + (ballCenterY - rightPlayerCenterY)*(ballCenterY - rightPlayerCenterY));

				ballLeftPlayerDistance 	// 공과 오른쪽 플레이어와의 거리
				= (int) Math.sqrt((ballCenterX - leftPlayerCenterX)*(ballCenterX - leftPlayerCenterX) + (ballCenterY - leftPlayerCenterY)*(ballCenterY - leftPlayerCenterY));

				// 공이 플레이어와 충돌할 경우, 공과 플레이어의 각 좌표의 차를 이용해 충돌한 각도의 방향으로 공이 튀기게 함
				if(ballRightPlayerDistance <= rightPlayer.width/2 + ball.width/2) {
					//if(rightPlayer.isSpike) {	// 스파이크일 시
					//	ball.moveX = (ballCenterX - rightPlayerCenterX)/3/3;
					//	ball.moveY = (ballCenterY - rightPlayerCenterY)/3/11;
					//	ball.gravity = 0;	// 플레이어와 충돌 시 공 중력 초기화
					//	ball.img = tk.getImage("spiked_ball.png");
					//}
					//else {
					ball.moveX = (ballCenterX - rightPlayerCenterX)/3/7;
					ball.moveY = (ballCenterY - rightPlayerCenterY)/3/7;
					ball.gravity = 0;	// 플레이어와 충돌 시 공 중력 초기화
					//	ball.img = tk.getImage("ball.png");
					//}
				}
				if(ballLeftPlayerDistance <= leftPlayer.width/2 + ball.width/2) {
					//if(leftPlayer.isSpike) {	// 스파이크일 시
					//	ball.moveX = (ballCenterX - leftPlayerCenterX)/3/3;
					//	ball.moveY = (ballCenterY - leftPlayerCenterY)/3/11;
					//	ball.gravity = 0;	// 플레이어와 충돌 시 공 중력 초기화
					//	ball.img = tk.getImage("spiked_ball.png");
					//}
					//else {
						ball.moveX = (ballCenterX - leftPlayerCenterX)/3/7;
						ball.moveY = (ballCenterY - leftPlayerCenterY)/3/7;
						ball.gravity = 0;	// 플레이어와 충돌 시 공 중력 초기화
					//	ball.img = tk.getImage("ball.png");
					//}
				}

				if(ball.x <= 0)		// 왼쪽 벽 충돌
					ball.moveX = -ball.moveX;
				if(ball.x >= getWidth() - ball.width)	// 오른쪽 벽 충돌
					ball.moveX = -ball.moveX;

				// 공이 네트와 충돌할 경우
				if((ball.x <= getWidth()/2+3 && ball.x >= getWidth()/2-3) && (ballCenterY + 20 >= getHeight()-400))
					ball.moveX = -ball.moveX;
				if((ball.x + ball.width >= getWidth()/2-3 && ball.x + ball.width <= getWidth()/2 + 3) && (ballCenterY + 20 >= getHeight()-400))
					ball.moveX = -ball.moveX;

				// 공이 바닥에 닿을 경우
				if(ball.y >= getHeight() - floor - ball.height - 5) {
					if(ballCenterX <= getWidth()/2) {	// 오른쪽 플레이어 득점
						scoreBoard.rightScore += 1;
						if(scoreBoard.rightScore >= setScore) {	// setSore 점수에 도달했다면 오른쪽 플레이어의 승리, 게임 종료
							gameFinish(rightPlayer);
							break;
						}
						try {	// 효과음 재생
							AudioInputStream as = AudioSystem.getAudioInputStream(new File("get_score.wav"));
							Clip clip = AudioSystem.getClip();
							clip.stop();
							clip.open(as);
							clip.start();
						}catch(Exception e) { e.printStackTrace(); }

						gameReset(rightPlayer);		// 초기화
					}
					else {								// 왼쪽 플레이어 득점
						scoreBoard.leftScore += 1;
						if(scoreBoard.leftScore >= setScore) {	// setSore 점수에 도달했다면 왼쪽 플레이어의 승리, 게임 종료
							gameFinish(leftPlayer);
							break;
						}
						try {	// 효과음 재생
							AudioInputStream as = AudioSystem.getAudioInputStream(new File("get_score.wav"));
							Clip clip = AudioSystem.getClip();
							clip.stop();
							clip.open(as);
							clip.start();
						}catch(Exception e) { e.printStackTrace(); }

						gameReset(leftPlayer);		// 초기화
					}
				}

				// 공 이동
				ball.x += ball.moveX;
				ball.y += ball.moveY;
				ball.y += ball.gravity++/35;	// 공 중력 받음
				repaint();

				try {
					Thread.sleep(1, 200000);	// 공의 속도, 해당 시간마다 이동
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
			case KeyEvent.VK_ENTER:
				rightPlayer.isSpike = true;
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						rightPlayer.isSpike = false;
						timer.cancel();
					}
				};
				timer.schedule(task, 300);
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
			case KeyEvent.VK_T:
				leftPlayer.isSpike = true;
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						leftPlayer.isSpike = false;
						timer.cancel();
					}
				};
				timer.schedule(task, 300);
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
					Thread.sleep(3);	// 플레이어의 속도, 해당 시간마다 이동
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}

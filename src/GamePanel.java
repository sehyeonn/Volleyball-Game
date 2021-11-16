import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

/*
 * 게임이 진행되는 패널
 * 게임에 필요한 요소들 부착
 */

public class GamePanel extends JPanel implements KeyListener, Runnable {
	private Player rightPlayer = new Player(1);
	private Player leftPlayer = new Player(2);
	private Ball ball;

	Image buffImg;
	Graphics buffG;

	Thread th = new Thread(this);
	boolean rightKeyUp;
    boolean rightKeyLeft;
    boolean rightKeyRight;
    boolean leftKeyUp;
    boolean leftKeyLeft;
    boolean leftKeyRight;

	public GamePanel() {
		setLayout(null);
		setSize(1600, 800);
		th.start();
		addKeyListener(this);
		this.requestFocus();
		this.setFocusable(true);
	}

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
	public void keyTyped(KeyEvent e) {}

	@Override
	public void run() {
		try {
    		while(true) {
    			keyProcess();
    			Thread.sleep(7);
    		}
    	} catch (Exception e){
    		e.printStackTrace();
    	}
	}

	@Override
	public void paint(Graphics g) {
		buffImg = createImage(getWidth(),getHeight()); // 버퍼링용 이미지 ( 도화지 )
		buffG = buffImg.getGraphics(); // 버퍼링용 이미지에 그래픽 객체를 얻어야 그릴 수 있다고 한다. ( 붓? )
		update(g);
	}

	@Override
	public void update(Graphics g) {
		buffG.clearRect(0, 0, getWidth(), getHeight()); // 백지화
		buffG.drawImage(rightPlayer.img, rightPlayer.x, rightPlayer.y, this); // 플레이어 그리기.
		buffG.drawImage(leftPlayer.img, leftPlayer.x, leftPlayer.y, this);
		g.drawImage(buffImg, 0, 0, this); // 화면 버퍼(buffG)에 그려진 이미지(buffImg)옮김. ( 도화지에 이미지를 출력 )
		repaint();
	}
}

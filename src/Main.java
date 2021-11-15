import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

class Game_View extends JFrame implements Runnable {
	boolean keyUp;
    boolean keyDown;
    boolean keyLeft;
    boolean keyRight;

    boolean keyUp2;
    boolean keyDown2;
    boolean keyLeft2;
    boolean keyRight2;

    // 이미지 파일 불러오는 툴킷.
    Toolkit imageTool = Toolkit.getDefaultToolkit();
    Image flight = imageTool.getImage("player.png");
    Image flight2 = imageTool.getImage("player.png");

    // 이미지 버퍼
    Image buffImg;
    Graphics buffG;

    // 플레이어 비행기의 위치값.
    int xpos = 100;
    int ypos = 100;
    int x = 400;
    int y = 100;

    public Game_View(){
        // 프레임의 대한 설정.
        setTitle("JFrame 테스트"); // 프레임 제목 설정.
        setSize(854,480); // 프레임의 크기 설정.
        setResizable(false); // 프레임의 크기 변경 못하게 설정.
        setVisible(true); // 프레임 보이기;
//        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 프레임의 x버튼 누르면 프레임스레드 종료.
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 프레임의 x버튼 누르면 프로세스 종료.

        Thread th = new Thread(this);
        th.start();

        // 키 어댑터 ( 키 처리용 )
        addKeyListener(new KeyAdapter() {
        	@Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP :
                        keyUp = true;
                        break;
                    case KeyEvent.VK_DOWN :
                        keyDown = true;
                        break;
                    case KeyEvent.VK_LEFT :
                        keyLeft = true;
                        break;
                    case KeyEvent.VK_RIGHT :
                        keyRight = true;
                        break;
                }
                switch (e.getKeyCode()){
	                case KeyEvent.VK_W :
	                    keyUp2 = true;
	                    break;
	                case KeyEvent.VK_S :
	                    keyDown2 = true;
	                    break;
	                case KeyEvent.VK_A :
	                    keyLeft2 = true;
	                    break;
	                case KeyEvent.VK_D :
	                    keyRight2 = true;
	                    break;
	            }
            }

            // 키가 눌렀다 때졌을 때.
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP :
                        keyUp = false;
                        break;
                    case KeyEvent.VK_DOWN :
                        keyDown = false;
                        break;
                    case KeyEvent.VK_LEFT :
                        keyLeft = false;
                        break;
                    case KeyEvent.VK_RIGHT :
                        keyRight = false;
                        break;
                }
                switch (e.getKeyCode()){
	                case KeyEvent.VK_W :
	                    keyUp2 = false;
	                    break;
	                case KeyEvent.VK_S :
	                    keyDown2 = false;
	                    break;
	                case KeyEvent.VK_A :
	                    keyLeft2 = false;
	                    break;
	                case KeyEvent.VK_D :
	                    keyRight2 = false;
	                    break;
	            }
            }
        });

    }
    // 키에 대한 처리.
    public void keyProcess() {
    	if(this.keyUp){
    		if(ypos >= 26)
    			ypos -= 5;
    	}

    	if(this.keyDown){
    		if(ypos < 465)
    			ypos += 5;
    	}

    	if(this.keyLeft){
    		if(xpos > 0)
    			xpos -= 5;
    	}

    	if(this.keyRight){
    		if(xpos < 790)
    			xpos += 5;
    	}

    	if(this.keyUp2){
    		if(y >= 26)
    			y -= 5;
    	}

    	if(this.keyDown2){
    		if(y < 465)
    			y += 5;
    	}

    	if(this.keyLeft2){
    		if(x > 0)
    			x -= 5;
    	}

    	if(this.keyRight2){
    		if(x < 790)
    			x += 5;
    	}
    }

    @Override
    public void run() {
    	try {
    		while(true){
    			keyProcess();
    			Thread.sleep(20);
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
        buffG.clearRect(0, 0, 854, 480); // 백지화
        buffG.drawImage(flight,xpos,ypos, this); // 유저 비행기 그리기.
        buffG.drawImage(flight2,x,y, this);
        g.drawImage(buffImg,0,0,this); // 화면g애 버퍼(buffG)에 그려진 이미지(buffImg)옮김. ( 도화지에 이미지를 출력 )
        repaint();
    }
}

public class Main {

    public static void main(String[] args){
        Game_View game_view = new Game_View();
    }
}
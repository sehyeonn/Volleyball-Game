import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Player implements KeyListener {
	private final int PLAYER_UNIT = 10;
	private final int PLAYER_JUMP = 100;
	private JLabel player;

	public Player() {
		player = new JLabel(new ImageIcon("player.png"));
	}

	private void jump() {
		player.setLocation(player.getX(), player.getY()-PLAYER_JUMP);
		if(player.getY() > 100) {		// 바닥보다 위에 있으면
			while(player.getY() <= 100) {
				player.setLocation(player.getX(), player.getY()+PLAYER_UNIT);
			}
		}
	}

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

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

}

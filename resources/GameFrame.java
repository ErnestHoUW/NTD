/*
 * GameFrame
 * This program initializes and animates the game.
 */

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class GameFrame extends JFrame implements KeyListener, MouseListener, MouseMotionListener {
    private final GamePanel gamePanel;

    /**
     * GameFrame
     * This method initializes the GameFrame in order to boot up the game.
     */
    GameFrame() {
        super("NTD");
        gamePanel = new GamePanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        getContentPane().add(gamePanel);
        this.setVisible(true);
        this.setSize(400, 800);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        Thread t = new Thread(() -> animate()); // start the gameLoop
        t.start();
    }

    /**
     * animate
     * This method animates the game.
     */
    private void animate() {
        while (true) {
            try {
                Thread.sleep(1);
            } catch (Exception ignored) {
            }
            this.repaint();
        }
    }

    /**
     * keyPressed
     * This method detects when a key on the keyboard is pressed.
     *
     * @param e A KeyEvent that holds the information on the key that was pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.getPlayer().keyPressed(KeyEvent.getKeyText(e.getKeyCode()));
    }

    /**
     * keyReleased
     * This method detects when a key has been released after being pressed down.
     *
     * @param e A KeyEvent that holds the information on the key that was released.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        gamePanel.getPlayer().keyReleased(KeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gamePanel.getPlayer().mousePressed(e.getX(), e.getY());

    }

    /**
     * mouseDragged
     * This method detects when the mouse is dragged across the screen while it is being pressed down.
     *
     * @param e A MouseEvent that holds the information on where the mouse is being dragged.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        gamePanel.getPlayer().mouseDragged(e.getX(), e.getY());

    }

    /**
     * mouseReleased
     * This method detects when the mouse is released after previously being held down.
     *
     * @param e A MouseEvent that holds the information on when the mouse was released.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        gamePanel.getPlayer().mouseReleased();

    }

    /**
     * keyTyped
     * This method must be overridden on order to use the other functions of KeyListener.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * mouseClicked
     * This method must be overridden on order to use the other functions of MouseListener
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * mouseMoved
     * This method must be overridden on order to use the other functions of MouseMotionListener.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * mouseEntered
     * This method must be overridden on order to use the other functions of MouseMotionListener.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * mouseExited
     * This method must be overridden on order to use the other functions of MouseMotionListener.
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }


}

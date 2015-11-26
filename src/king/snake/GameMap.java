package king.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * ����
 * ע��Swing�ĺ�����Ϊx�ᣬ������Ϊy�ᡣ
 */
public class GameMap extends JPanel implements KeyListener {
    public static final int GRID = 20; //ÿ��С���ӵĿ��
    public static final int LEN = 30; //ÿ�С��еĸ�����
    public static final int WIDTH = LEN * GRID;
    public static final int HEIGHT = LEN * GRID;
    
    private Snake snake = new Snake(this); //��
    
    public GameMap(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        JFrame f = new JFrame("����̰����");
        f.add(this);
        f.addKeyListener(this);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new GameMap();
            }
        });
    }
    
    /**
     * --------------------------------��Ϸ����--------------------------------
     */
    public void setGameOver(){
        JOptionPane.showMessageDialog(this, "���ź�����ʧ���ˣ����ٽ�������");
        snake.init();
    }
    
    /**
     * --------------------------------����������ת����Swing��ʵ�����ꡣ--------------------------------
     * @return
     */
    public int getXY(int coordinate){
        return coordinate*GRID;
    }
    
    /**
     * ��дpaintComponent
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);        
        //���濪ʼ����
        g.setColor(Color.red);
        Point[] p = snake.getPoints();
        for(int i = p.length-1; i >= 1; i--){
            g.fill3DRect(getXY(p[i].x), getXY(p[i].y), GRID, GRID, true);
        }
        g.fillOval(getXY(p[0].x), getXY(p[0].y), GRID, GRID);
        //���滭ʳ��
        g.setColor(new Color(211, 54, 127));
        Point food = snake.getFood();
        g.fill3DRect(getXY(food.x), getXY(food.y), GRID, GRID, true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int direction = -1;
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                direction = Snake.UP;
                break;
            case KeyEvent.VK_DOWN :
                direction = Snake.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                direction = Snake.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                direction = Snake.RIGHT;
                break;
            default:
                break;
        }
        if (direction != -1){
            snake.changeDirection(direction);
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent arg0) {}
}

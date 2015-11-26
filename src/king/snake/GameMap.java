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
 * 窗口
 * 注意Swing的横坐标为x轴，纵坐标为y轴。
 */
public class GameMap extends JPanel implements KeyListener {
    public static final int GRID = 20; //每个小格子的宽度
    public static final int LEN = 30; //每行、列的格子数
    public static final int WIDTH = LEN * GRID;
    public static final int HEIGHT = LEN * GRID;
    
    private Snake snake = new Snake(this); //蛇
    
    public GameMap(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        JFrame f = new JFrame("欢乐贪吃蛇");
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
     * --------------------------------游戏结束--------------------------------
     */
    public void setGameOver(){
        JOptionPane.showMessageDialog(this, "很遗憾，您失败了，请再接再厉！");
        snake.init();
    }
    
    /**
     * --------------------------------把数组坐标转化成Swing的实际坐标。--------------------------------
     * @return
     */
    public int getXY(int coordinate){
        return coordinate*GRID;
    }
    
    /**
     * 重写paintComponent
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);        
        //下面开始画蛇
        g.setColor(Color.red);
        Point[] p = snake.getPoints();
        for(int i = p.length-1; i >= 1; i--){
            g.fill3DRect(getXY(p[i].x), getXY(p[i].y), GRID, GRID, true);
        }
        g.fillOval(getXY(p[0].x), getXY(p[0].y), GRID, GRID);
        //下面画食物
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

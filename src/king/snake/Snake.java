package king.snake;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.Timer;

/**
 * 蛇
 * 注意Swing的横坐标为x轴，纵坐标为y轴。
 */
public class Snake implements ActionListener{
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    
    //用于保存蛇身
    private LinkedList<Point> list = new LinkedList<Point>();
    
    //蛇的各项属性，需要复位故把初始化过程写入init()
    private int length; //长度
    private int direction; //蛇的方向
    private Point food; //食物
    
    //不需要写入init()的其他属性
    private int delay = 600; //移动间隔
    private Timer timer = new Timer(delay, this);
    private Random rand = new Random();
    private GameMap gameMap;
    private boolean gameOver = false;
    
    /**
     * -----------------------构造器-----------------------
     */
    public Snake(GameMap gameMap){
        this.gameMap = gameMap;
        init();
    }
    
    /**
     * -----------------------初始化--------------------------
     * 设置初始蛇身长度为4，方向随机
     */
    public void init(){
        gameOver = false;
        list.clear();
        length = 4; //长度
        Point[] p = new Point[length];
        direction = rand.nextInt(4); // 方向
        p[0] = new Point(rand.nextInt(11) + 10, rand.nextInt(11)+10);
        list.add(p[0]);
        int d; //用于初始化时计算位移
        switch (direction){
            case UP:
            case DOWN:
                d = direction == UP ? 1 : -1;
                for(int i = 1; i <= 3; i++){
                    p[i] = new Point(p[0].x, p[i-1].y + d);
                    list.add(p[i]);
                }
                break;
            case LEFT:
            case RIGHT:
                d = direction == LEFT ? 1 : -1;
                for(int i = 1; i <= 3; i++){
                    p[i] = new Point(p[i-1].x + d, p[0].y);
                    list.add(p[i]);
                }
                break;
            default:
                break;
        }
        randomFood();
        if (timer.isRunning()){
            timer.restart();
        }else{
            timer.start();
        }
//        timer.start();
        gameMap.repaint();
    }
    
    /**
     * -------------------随机生成食物----------------------
     * 不能覆盖现有的蛇身
     * 验证食物是正确的位置才予以赋值
     * 改变了食物的位置，故需要repaint()
     */
    private void randomFood(){
        Point tmp = null;
        do{
            tmp = new Point(rand.nextInt(GameMap.LEN), rand.nextInt(GameMap.LEN));
        }while(isPointCoverSnake(tmp));
        food = tmp; 
        gameMap.repaint();
    }
    
    /**
     * -----------改变蛇的方向-------------
     * 在delay期限内只能改变一次方向，否则多次按键可以使蛇头直接掉转180度。
     * @param direction 方向
     */
    public void changeDirection(int direction){
        //如果传入的方向与现有方向相反
        // 或者间隔时间内已经换了一次方向，则什么都不做
        long now = System.currentTimeMillis();
        if (Math.abs(this.direction - direction) == 2) return; //现有设置正好满足此条件
        this.direction = direction;
        advance();
    }
    
    /**
     * --------------------蛇是否已经死亡------------------------
     */
    private boolean isDead(){
        //若撞墙，则死亡
        //正常范围应该是0~LEN-1
        Point head = list.get(0); //蛇头
        if (head.x < 0 || head.x >= GameMap.LEN || head.y < 0 || head.y >= GameMap.LEN) return true;
        return isHeadCoverSnake(head); // 若蛇头盖住了蛇身，也死
    }
    
    /**
     * ---------------------判断食物是否盖住了蛇身-----------------------
     * 包括蛇头
     */
    public boolean isPointCoverSnake(Point point){
        for(Point p : list){
            if (p.equals(point)) return true;
        }
        return false;
    }
    
    /**
     * ---------------------判断蛇头是否盖住了蛇身-----------------------
     * 不包括蛇头本身
     */
    public boolean isHeadCoverSnake(Point point){
        LinkedList<Point> tmpList = new LinkedList<Point>(list);
        tmpList.poll();
        for(Point p : tmpList){
            if (p.equals(point)) return true;
        }
        return false;
    }
    
    /**
     * -------------------蛇前进一格-----------------
     * 如果不是进食状态，只需要把头前加一个点，移除蛇尾
     * 如果是进食状态，则仅在蛇头加点
     * 这一步改变了蛇的位置，故需要repaint()
     */
    private void advance(){
        if (gameOver) return; //游戏结束则什么都不做
        Point head = list.get(0); //蛇头
        Point nextHead = null;
        switch (direction){
            case UP:
                nextHead = new Point(head.x, head.y - 1);
                break;
            case DOWN:
                nextHead = new Point(head.x, head.y + 1);
                break;
            case LEFT:
                nextHead = new Point(head.x - 1, head.y);
                break;
            case RIGHT:
                nextHead = new Point(head.x + 1, head.y);
                break;
            default:
                break;
        }
        list.addFirst(nextHead); //增加蛇头
        if (!isHeadCoverFood()){
            list.pollLast(); //移除蛇尾
        }else{
            // 如果吃了食物，则重新生成食物
            randomFood();
        }
        gameMap.repaint();
        timer.restart(); //手动前进后就重新计时
        //判断是否已经游戏结束
        if (isDead()){
            timer.stop();
            gameMap.setGameOver();
        }
    }
    
    /**
     * ----------------蛇头是否盖住食物-------------------
     */
    private boolean isHeadCoverFood(){
        Point head = list.get(0);
        if (head.equals(food)) return true;
        return false;
    }
    
    /**
     * ---------------------取得所有点，用于绘制-------------------------
     */
    public Point[] getPoints(){
        Point[] pointArray = new Point[list.size()];
        int i = 0;
        for(Point p : list){
            pointArray[i++] = p;
        }
        return pointArray;
    }
    
    /**
     * 获取食物，用于绘制
     */
    public Point getFood(){
        return food;
    }

    
    /**
     * --------------------事件监听器，小蛇前进一格-----------------------------
     * 同时判断蛇是否已经死亡，若是则改变GameMap中的参数
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        advance();        
    }

}

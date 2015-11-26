package king.snake;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.Timer;

/**
 * ��
 * ע��Swing�ĺ�����Ϊx�ᣬ������Ϊy�ᡣ
 */
public class Snake implements ActionListener{
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    
    //���ڱ�������
    private LinkedList<Point> list = new LinkedList<Point>();
    
    //�ߵĸ������ԣ���Ҫ��λ�ʰѳ�ʼ������д��init()
    private int length; //����
    private int direction; //�ߵķ���
    private Point food; //ʳ��
    
    //����Ҫд��init()����������
    private int delay = 600; //�ƶ����
    private Timer timer = new Timer(delay, this);
    private Random rand = new Random();
    private GameMap gameMap;
    private boolean gameOver = false;
    
    /**
     * -----------------------������-----------------------
     */
    public Snake(GameMap gameMap){
        this.gameMap = gameMap;
        init();
    }
    
    /**
     * -----------------------��ʼ��--------------------------
     * ���ó�ʼ������Ϊ4���������
     */
    public void init(){
        gameOver = false;
        list.clear();
        length = 4; //����
        Point[] p = new Point[length];
        direction = rand.nextInt(4); // ����
        p[0] = new Point(rand.nextInt(11) + 10, rand.nextInt(11)+10);
        list.add(p[0]);
        int d; //���ڳ�ʼ��ʱ����λ��
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
     * -------------------�������ʳ��----------------------
     * ���ܸ������е�����
     * ��֤ʳ������ȷ��λ�ò����Ը�ֵ
     * �ı���ʳ���λ�ã�����Ҫrepaint()
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
     * -----------�ı��ߵķ���-------------
     * ��delay������ֻ�ܸı�һ�η��򣬷����ΰ�������ʹ��ͷֱ�ӵ�ת180�ȡ�
     * @param direction ����
     */
    public void changeDirection(int direction){
        //�������ķ��������з����෴
        // ���߼��ʱ�����Ѿ�����һ�η�����ʲô������
        long now = System.currentTimeMillis();
        if (Math.abs(this.direction - direction) == 2) return; //���������������������
        this.direction = direction;
        advance();
    }
    
    /**
     * --------------------���Ƿ��Ѿ�����------------------------
     */
    private boolean isDead(){
        //��ײǽ��������
        //������ΧӦ����0~LEN-1
        Point head = list.get(0); //��ͷ
        if (head.x < 0 || head.x >= GameMap.LEN || head.y < 0 || head.y >= GameMap.LEN) return true;
        return isHeadCoverSnake(head); // ����ͷ��ס������Ҳ��
    }
    
    /**
     * ---------------------�ж�ʳ���Ƿ��ס������-----------------------
     * ������ͷ
     */
    public boolean isPointCoverSnake(Point point){
        for(Point p : list){
            if (p.equals(point)) return true;
        }
        return false;
    }
    
    /**
     * ---------------------�ж���ͷ�Ƿ��ס������-----------------------
     * ��������ͷ����
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
     * -------------------��ǰ��һ��-----------------
     * ������ǽ�ʳ״̬��ֻ��Ҫ��ͷǰ��һ���㣬�Ƴ���β
     * ����ǽ�ʳ״̬���������ͷ�ӵ�
     * ��һ���ı����ߵ�λ�ã�����Ҫrepaint()
     */
    private void advance(){
        if (gameOver) return; //��Ϸ������ʲô������
        Point head = list.get(0); //��ͷ
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
        list.addFirst(nextHead); //������ͷ
        if (!isHeadCoverFood()){
            list.pollLast(); //�Ƴ���β
        }else{
            // �������ʳ�����������ʳ��
            randomFood();
        }
        gameMap.repaint();
        timer.restart(); //�ֶ�ǰ��������¼�ʱ
        //�ж��Ƿ��Ѿ���Ϸ����
        if (isDead()){
            timer.stop();
            gameMap.setGameOver();
        }
    }
    
    /**
     * ----------------��ͷ�Ƿ��סʳ��-------------------
     */
    private boolean isHeadCoverFood(){
        Point head = list.get(0);
        if (head.equals(food)) return true;
        return false;
    }
    
    /**
     * ---------------------ȡ�����е㣬���ڻ���-------------------------
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
     * ��ȡʳ����ڻ���
     */
    public Point getFood(){
        return food;
    }

    
    /**
     * --------------------�¼���������С��ǰ��һ��-----------------------------
     * ͬʱ�ж����Ƿ��Ѿ�������������ı�GameMap�еĲ���
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        advance();        
    }

}

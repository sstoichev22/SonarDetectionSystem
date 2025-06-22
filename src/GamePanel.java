import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{
    Input input;
    Thread thread;
    Point p, middle;
    float opacity = 0;
    float opMulti = 0.00833333333f;
    final int maxDistance;
    int time = 0;
    ArrayList<Circle> arr;
    BufferedImage bi;
    GamePanel(Input input, int maxDistance){
        this.input = input;
        this.maxDistance = maxDistance;
        setSize();
        getImage();
        arr = new ArrayList<>();
        this.setBackground(Color.BLACK);
        p = new Point(-1, -1);
        middle = new Point(550, 550);
    }

    private void setSize(){
        Dimension d = new Dimension(1100, 600);
        this.setMinimumSize(d);
        this.setPreferredSize(d);
        this.setMaximumSize(d);
    }

    @Override
    public void run() {
        long previousTime = System.nanoTime();
        double fps = 1_000_000_000.0/120;
        double delta = 0;
        int frames = 0;
        long lastCheck = System.currentTimeMillis();

        while(thread != null){
            long currentTime = System.nanoTime();
            delta += (currentTime-previousTime)/fps;
            previousTime = currentTime;
            if(delta >= 1){
                delta--;
                update();
                repaint();
                frames++;

            }
            if(System.currentTimeMillis()-lastCheck >= 1_000){
                System.out.println("FPS: " + frames);
                frames = 0;
                lastCheck = System.currentTimeMillis();
            }
        }
    }
    private void update(){
        if(input.connected){
            time--;
            if(time < 0) time = 0;
            int angle = input.getAngle();
            int distance = input.getDistance();
            if(distance <= maxDistance && time == 0){
                float dz = (float) distance / maxDistance;
                int x = middle.x + (int)(dz * 500 * Math.cos(Math.toRadians(angle)));
                int y = middle.y + (int)(dz * 500 * Math.sin(Math.toRadians(angle)));
                if(y <= middle.y + Circle.radius) {
                    arr.add(new Circle(x, y, 1));
                    time += 10;
                    System.out.println("added circ");
                }
            }
            p.x = middle.x + (int) (500 * Math.cos(Math.toRadians(angle)));
            p.y = middle.y + (int) (500 * Math.sin(Math.toRadians(angle)));
            opacity = 1;
        } else {
            if(opacity + opMulti < 0) opacity = 0;
            if(opacity + opMulti > 1) opacity = 1;
            if(opacity == 0 || opacity == 1){
                opMulti *= -1;
            }
            opacity += opMulti;
        }
        for(int i = 0 ; i < arr.size(); i++){
            arr.get(i).opacity -= arr.get(i).circOp;
            if(arr.get(i).opacity <= 0){
                arr.remove(i);
                i--;
            }
        }

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Color lime = new Color(0, 255, 69);
        g.setColor(lime);
        g.drawLine(50, 550, 550, 550);
        g.drawLine(550, 550, 1050, 550);
        g.drawArc(50, 50, 1000, 1000, 0, 180);
        if(input.connected){
            g.drawLine(p.x, p.y, 550, 550);
        }else{
            Graphics2D g2 = (Graphics2D) g;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2.drawImage(bi, (this.getWidth()-bi.getWidth())/2, (this.getHeight()-bi.getHeight())/2, null);
            g2.dispose();
        }
        Graphics2D g2 = (Graphics2D)g;
        for(int i = 0 ; i < arr.size(); i++){
            Circle c = arr.get(i);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, c.opacity));
            g2.setColor(lime);
            g2.fillOval(c.x, c.y, Circle.radius, Circle.radius);
        }
        g2.dispose();

        g.dispose();
    }
    public void startThread(){
        thread = new Thread(this);
        thread.start();
    }
    private void getImage(){
        try{
            bi = ImageIO.read(getClass().getResourceAsStream("/no_port_found.png"));
        } catch(IOException ignored){}
    }
}
class Circle{
    int x, y;
    static int radius = 30;
    float opacity, circOp = 0.00416666666666666666666666666667f;
    Circle(int x, int y, float opacity){
        this.x = x;
        this.y = y;
        this.opacity = opacity;
    }
}

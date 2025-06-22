import com.fazecast.jSerialComm.SerialPort;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Scanner;

public class Input implements Runnable, MouseListener {
    Thread thread;
    int angle = 0;
    int distance;
    boolean connected = false;

    public void startThread(){
        thread = new Thread(this);
        thread.start();
    }
    @Override
    public void run() {
        while(thread != null){
            SerialPort[] commPorts = SerialPort.getCommPorts();
            while(commPorts.length == 0){
                commPorts = SerialPort.getCommPorts();
                connected = false;
                distance = Integer.MAX_VALUE;
                angle = 0;
            }

            SerialPort sp = commPorts[0];
            sp.setComPortParameters(9600, 8, 1, 0);
            sp.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
            if(sp.openPort()) System.out.println("Port Opened.");
            else{
                System.out.println("Unable to Open Port.");
                return;
            }
            Scanner scanner = new Scanner(sp.getInputStream());
            while(commPorts.length != 0){
                commPorts = SerialPort.getCommPorts();
                angle = scanner.nextInt();
                distance = scanner.nextInt();
                connected = true;
            }
            sp.closePort();


        }
    }

    public int getDistance(){
        return distance;
    }

    public int getAngle(){
        return angle + 180;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {


    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}


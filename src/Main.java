import com.fazecast.jSerialComm.*;

import javax.swing.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Radar go brrrrrr");
        Input input = new Input();
        GamePanel gamePanel = new GamePanel(input, 100);
        frame.add(gamePanel);
        frame.pack();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.startThread();
        input.startThread();





    }
}

package com.Elham.jvc2;

import javax.swing.*;
import java.awt.*;

//the frame is like the container containing the panel and the frame also containing the close, maximize button
public class GameFrame extends JFrame {

    GamePanel panel;

    GameFrame(){

        panel=new GamePanel();
        this.add(panel);
        this.setTitle("Pong Game");
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);//this sets the frame in the middle of the screen
    }
}

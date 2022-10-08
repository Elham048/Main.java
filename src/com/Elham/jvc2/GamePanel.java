package com.Elham.jvc2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

//the panel is the main body of the game where all actions happens
public class GamePanel extends JPanel implements Runnable{

    static final int GAME_WIDTH=1200;//here this is defined static so that if we have multiple instances of GamePanel class,they
                                     //will not have copies of the GAME_WIDTH,they will share the same GAME_WIDTH
    static final int GAME_HEIGHT=(int)(GAME_WIDTH*(0.555));
    static final Dimension SCREEN_SIZE=new Dimension(GAME_WIDTH,GAME_HEIGHT);
    static final int BALL_DIAMETER=20;
    static final int PADDLE_WIDTH=25;
    static final int PADDLE_HEIGHT=100;

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    GamePanel(){

        newPaddles();
        newBall();
        score=new Score( GAME_WIDTH, GAME_HEIGHT);//calling constructor from score class
        this.setFocusable(true);
        this.addKeyListener(new ActionListener());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread=new Thread(this);
        gameThread.start();
    }

    //the methods newBall() and newPaddle() are called if we want to reset the level of the game
    public void newBall(){
        random=new Random();
        ball=new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);//the last 2 are width and height and with a perfect circle they are the same
        //here for the height we give random value for which the ball will not always start at the middle of the screen
    }

    public void newPaddles(){
        paddle1=new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);//creating the paddle1 at the position of right side at the middle height
        paddle2=new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);//creating the paddle2 at the position of right side at the middle height
    }

    public void paint(Graphics g){
        image=createImage(getWidth(),getHeight());
        graphics=image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }

    public void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }

    //this method makes the movement of the paddles more smoother
    public void move(){
        paddle1.move();
        paddle2.move();
        ball.move();
    }


    public void checkCollision(){
        //stops the paddles at the edge of the window
        if(paddle1.y<=0){
            paddle1.y=0;
        }
        if(paddle1.y>=(GAME_HEIGHT-PADDLE_HEIGHT)){
            paddle1.y=GAME_HEIGHT-PADDLE_HEIGHT;
        }


        if(paddle2.y<=0){
            paddle2.y=0;
        }
        if(paddle2.y>=(GAME_HEIGHT-PADDLE_HEIGHT)){
            paddle2.y=GAME_HEIGHT-PADDLE_HEIGHT;
        }


        //bounces the ball of the top and bottom edges of the screen
        if(ball.y<=0){
            ball.setYDirection(-ball.yVelocity);//reverses the direction of the ball after hitting the top edge of screen
        }
        if(ball.y>=GAME_HEIGHT-BALL_DIAMETER){
            ball.setYDirection(-ball.yVelocity);//reverses the direction of the ball after hitting the bottom edge of screen
        }



        //bounces the ball off the paddles
        if(ball.intersects(paddle1)) {//checks if the ball intersect with the paddle1//since the ball class is a subclass of the rectangle superclass it inherits all the properties of rectangle class including the intersects method
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;//optional for more gameplay difficulty//this increases the ball speed after each time it bounces off a paddle
            if (ball.yVelocity > 0) {
                ball.yVelocity++;
            }
            else{
                ball.yVelocity--;
            }
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }



        if(ball.intersects(paddle2)) {//checks if the ball intersect with the paddle2//since the ball class is a subclass of the rectangle superclass it inherits all the properties of rectangle class including the intersects method
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;//optional for more gameplay difficulty//this increases the ball speed after each time it bounces off a paddle

            if (ball.yVelocity > 0) {
                ball.yVelocity++;
            }
            else{
                ball.yVelocity--;
            }
            ball.setXDirection(-ball.xVelocity);//opposite sign is given as after the ball bounces on paddle2, its direction is changed to the opposite side
            ball.setYDirection(ball.yVelocity);
        }


        //Gives player score if ball crosses the boundary of the opponent and creates new paddle and ball
        if(ball.x>=GAME_WIDTH-BALL_DIAMETER){
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1 score is "+score.player1);
        }

        if(ball.x<=0){
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("Player 2 score is "+score.player2);
        }
    }

    public void run(){
        //game loop//creating about 60 frame per second
        long lastTime=System.nanoTime();
        double amountOfTicks=60.0;
        double ns= 1000000000/amountOfTicks;
        double delta=0;
        while(true){
            long now=System.nanoTime();
            delta +=(now-lastTime)/ns;
            lastTime=now;
            if(delta >=1){
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }

    }

    public class ActionListener extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);

        }
        public void keyReleased(KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);

        }
    }

}

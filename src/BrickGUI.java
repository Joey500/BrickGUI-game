import java.awt.event.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.*;
import javax.swing.Timer;


public class BrickGUI extends JPanel implements KeyListener,ActionListener, MouseListener, MouseMotionListener,Runnable {

    //variablen

    // Keys move
    boolean Right = false;
    boolean Left = false;

    int score = 0;


    //ball x en y posities
    int ballX = 250;
    int ballY = 218;

    // variables declaration for ball.................................
    // ===============================================================
    // variables declaration for bat..................................
    int batx = 370;
    int baty = 340;
    // variables declaration for bat..................................
    // ===============================================================
    // variables declaration for brick...............................
    int brickx = 130;
    int bricky = 40;

    int brickBreadth = 70;
    int brickHeight = 20;



    // variables declaration for brick...............................
    // ===============================================================
    // declaring ball, paddle,bricks
    Rectangle Ball = new Rectangle(ballX, ballY, 15, 15);

    Rectangle Bat = new Rectangle(batx, baty, 60, 10);

    // Rectangle Brick;// = new Rectangle(brickx, bricky, 30, 10);
    Rectangle[] Brick = new Rectangle[22];

    //reverses......==>
    int movex = -1;
    int movey = -1;

    boolean ballFallDown = false;
    boolean bricksOver = false;

    int count = 0;
    int lives = 3;

    String status;

    Timer timer;
    BufferedImage bufferedImage;
    static int gameSpeed = 5;
    Time time;

    boolean Running = true;
    boolean paused = false;
    private static Dimension dim;
    Random random = new Random();
    String PlayerName;

    int xpos;
    int ypos;
    boolean rect1Active,rect2Active;
    int rect1xco, rect1yco, rect1width, rect1height;
    int rect2xco, rect2yco, rect2width, rect2height;
    BrickGUI(Container contentPane) {

        timer = new Timer(gameSpeed, this);
        timer.start();
        time = new Time();
        PlayerName = JOptionPane.showInputDialog(null, "Enter your name:", "Name", JOptionPane.INFORMATION_MESSAGE);
        if (PlayerName == null) {
            System.exit(0);

        }
        rect1xco = 620;
        rect1yco = 450;
        rect1width = 100;
        rect1height = 50;

        rect2xco = 480;
        rect2yco = 450;
        rect2width = 100;
        rect2height = 50;

        // Add the MouseMotionListener to yourapplet
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public static void main(String[] args) {
        //JOptionPane.showMessageDialog(null, "Welcome at BrickBreaker!");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Brick Breaker");

        BrickGUI game = new BrickGUI(frame.getContentPane());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(game);

        dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        JButton button = new JButton("restart");

        buttonPanel.add(button);
        JButton PauseButton = new JButton("pause");
        buttonPanel.add(PauseButton);

        buttonPanel.add(button);
        JTextField text = new JTextField(10);
        buttonPanel.add(text);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);


        text.setFont(new Font("Arial", Font.PLAIN, 20));

        //text.setText(PlayerName);


        frame.setLocationRelativeTo(null);

        frame.setResizable(false);
        frame.setVisible(true);
        frame.getSize();


        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.restart();
            }
        });

        PauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                if (button.getText().equals("pause")) {
                    button.setText("play");
                    game.PauseGame();
                } else if (button.getText().equals("play")) {
                    button.setText("pause");
                    game.ResumeGame();
                }
            }
        });


        game.addKeyListener(game);
        game.setFocusable(true);
        Thread t = new Thread(game);
        t.start();

    }

    public void ResumeGame() {
        //restart();
        ballX = 5;
        ballY = 5;

    }


    public void PauseGame() {
        paused = true;
        stop();
    }


    public void stop() {
        ballX = 0;
        ballY = 0;
        movex = 0;
        movey = 0;

    }

    public BrickGUI() {

    }

    // declaring ball, paddle,bricks
    int rectX = 4;
    int rectY = 5;
    int rectWidth = 785;
    int rectHeight = 530;
    int thickness = 5;

    static final float MIN_SAT = 0.8f;
    private static final Color[] colors = {Color.red, Color.orange, Color.yellow,
            Color.green, Color.blue, Color.magenta};

    private int bricksPerRow;
    private int numRows;

    public void paint(Graphics g) {

        Color myColors[];
        myColors = new Color[100];
        g.setColor(new Color(255, 232, 94));
        g.fillRect(0, 0, 800, 600);

        //ball kleur
        g.setColor(Color.blue);
        g.fillOval(Ball.x, Ball.y, Ball.width, Ball.height);
        g.setColor(Color.green);

        //paddle kleur
        g.fill3DRect(Bat.x, Bat.y, Bat.width, Bat.height, true);
        g.setColor(new Color(9, 55, 255));
        g.fillRect(0, 350, 800, 200);

        //lijnen
        g.setColor(Color.red);
        g.draw3DRect(rectX, rectY, rectWidth, rectHeight, false);

        for (int j = 1; j <= thickness; j++) {
            g.draw3DRect(rectX - j, rectY - j, rectWidth + 2 * j - 1, rectHeight + 2 * j - 1, true);
        }
        for (int i = 0; i < Brick.length; i++) {
            if (Brick[i] != null) {
                if (i % 2 == 0) {
                    g.setColor(new Color(0xFA0B0C));
                } else {
                    g.setColor(new Color(0xFF7F00));
                }


                g.fill3DRect(Brick[i].x, Brick[i].y, Brick[i].width,
                        Brick[i].height, true);
            }
        }

        if (ballFallDown == true || bricksOver == true) {
            Font f = new Font("Impact", Font.BOLD, 54);
            g.setFont(f);
            g.setColor(Color.blue);
            g.drawString(status, 180, 260);
            ballFallDown = false;
            bricksOver = false;

        }
        if (rect1Active) {
            g.setColor(Color.red);
            g.drawRect(rect1xco,rect1yco,rect1width,rect1height);

        }
        else {g.setColor(Color.red);
            g.fillRect(rect1xco,rect1yco,rect1width,rect1height);
        }



        if (rect1Active) {
            g.setColor(Color.red);
            g.drawRect(rect2xco,rect2yco,rect2width,rect2height);

        }
        else {g.setColor(Color.red);
            g.fillRect(rect2xco,rect2yco,rect2width,rect2height);
        }






        g.setColor(Color.white);
        Font Score = new Font("Impact", Font.BOLD, 34);
        g.setFont(Score);

        g.drawString(String.valueOf("Score: " + score), 50, 450);
        g.drawString("Player: " + PlayerName, 50, 500);
        g.drawString("Lives: " + lives, 50, 400);


        Font RestartText = new Font("Impact", Font.BOLD, 26);
        g.setFont(RestartText);
        g.drawString("Restart ", rect1xco / 2 + 315, rect1yco /2+260);
        g.drawString("Pause ", rect2xco / 2 + 255, rect2yco /2+260);
        time.paint(g);







    }








    // /...Game Loop...................

    // /////////////////// When ball strikes borders......... it

    public void run() {
        // //////////// =====Creating bricks for the game===>.....
        createBricks();
        // ===========BRICKS created for the game new ready to use===

        // ====================================================
        // == ball reverses when touches the brick=======
//ballFallDown == false && bricksOver == false
        while (Running) {
//   if(gameOver == true){return;}
            for (int i = 0; i < Brick.length; i++) {
                if (Brick[i] != null) {
                    if (Brick[i].intersects(Ball)) {
                        Brick[i] = null;
                        // movex = -movex;
                        movey = -movey;
                        count++;
                        score += 5;


                    }// end of 2nd if..
                }// end of 1st if..
            }// end of for loop..

            // /////////// =================================

            if (count == Brick.length) {// check if ball hits all bricks
                bricksOver = true;
                status = "You win the game!";
                movex = 0;
                movey = 0;

                repaint();

            }
            // /////////// =================================
            repaint();
            Ball.x += movex;
            Ball.y += movey;

            if (Left == true) {

                Bat.x -= 3;
                Right = false;
            }
            if (Right == true) {
                Bat.x += 3;
                Left = false;
            }
            if (Bat.x <= 0) {
                Bat.x = 0;
            } else if (Bat.x >= 735) {
                Bat.x = 735;
            }
            // /===== Ball reverses when strikes the bat
            if (Ball.intersects(Bat)) {
                movey = -movey;
                // if(Ball.y + Ball.width >=Bat.y)
            }
            // //=====================================
            // ....ball reverses when touches left and right boundary
            if (Ball.x <= 3 || Ball.x + Ball.height >= 790) {
                movex = -movex;
            }// if ends here
            if (Ball.y <= 3) {// ////////////////|| bally + Ball.height >= 250
                movey = -movey;
            }// if ends here.....
            //while (lives > 0) {

            if (Ball.y >= 340 && (bricksOver == false) && lives > 0) {

                // when ball falls below bat game is over...
                ballFallDown = true;
                // status = "You lost the game!";

                lives = lives - 1;

                restart();
                repaint();

            }
            if(Ball.y <= 340 && lives==0)
            {
                bricksOver = true;

                // when ball falls below bat game is over...
                ballFallDown = true;
                status = "You lost the game!";


                repaint();

            }
            try {
                Thread.sleep(10);
            } catch (Exception ex) {


            }

        }

    }
    //Key events
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                Left = true;
                System.out.print("left");
                break;


            case KeyEvent.VK_RIGHT:
                Right = true;
                System.out.print("right");
                break;


            case KeyEvent.VK_ESCAPE:
                restart();
                System.out.print("Restart");
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                Left = false;
                System.out.print("left");
                break;

            case KeyEvent.VK_RIGHT:
                Right = false;
                System.out.print("right");
                break;
        }

    }

    @Override
    public void keyTyped(KeyEvent arg0) {

    }



    public void restart() {

        requestFocus(true);
        initializeVariables();
        createBricks();
        repaint();

        timer.start();
    }

    public void initializeVariables(){
        // ..............
        // variables declaration for ball.................................
        ballX = 350;
        ballY = 300;
        // variables declaration for ball.................................
        // ===============================================================
        // variables declaration for bat..................................
        batx = 370;
        baty = 340;
        // variables declaration for bat..................................
        // ===============================================================
        // variables declaration for brick...............................
        brickx = 130;
        bricky = 40;
        // variables declaration for brick...............................
        // ===============================================================
        // declaring ball, paddle,bricks
        Ball = new Rectangle(ballX, ballY, 15, 15);
        Bat = new Rectangle(batx, baty, 60, 10);
        // Rectangle Brick;// = new Rectangle(brickx, bricky, 30, 10);
        Brick = new Rectangle[22];

        movex = 1;
        movey = 1;
        ballFallDown = false;
        bricksOver = false;
        count = 0;
        status = "";
        score = 0;






    }
    public void score(){
        lives =3;
    }

    public void createBricks(){
        ///create Bricks
        for (int i = 0; i < Brick.length; i++) {
            Brick[i] = new Rectangle(brickx, bricky, brickBreadth, brickHeight);

            if (i == 6) {
                brickx = 90;
                bricky = (bricky + brickHeight + 7);

            }
            if (i == 12) {
                brickx = 130;
                bricky = (bricky + brickHeight + 7);

            }
            if (i == 17) {

                brickx = 200;

                bricky = (bricky + brickHeight + 7);

            }

            if (i == 20) {
                brickx = 280;
                bricky = (bricky + brickHeight + 7);
            }

            brickx += (brickBreadth + 5);
        }

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        restart();
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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        xpos = e.getX();
        ypos = e.getY();
        // Check if the mouse is in the rectangle
        if (xpos > rect1xco&& xpos < rect1xco+rect1width && ypos > rect1yco
                && ypos < rect1yco+rect1height) {

            rect1Active = false;
        }
        else {
            rect1Active = true;
            //show the results of the motion
            repaint();
        }
        int x = e.getX();
        int y = e.getY();
        if (x > rect2xco&& x < rect2xco+rect2width && y > rect2yco
                && y < rect1yco+rect1height)

            rect2Active = false;
        else
            rect2Active = true;
        repaint();
    }

}

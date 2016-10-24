import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Random;


public class BrickGUI extends JPanel implements KeyListener,ActionListener, MouseListener, MouseMotionListener,Runnable {

    //variablen
    // Keys moves
    boolean Right = false;
    boolean Left = false;

    //ball x en y posities
    int ballX = 250;
    int ballY = 218;

   //paddle x en y positions
    int padX = 370;
    int padY = 340;

    // bricks formaten
    int BrickX = 130;       int brickBreadth = 70;
    int BrickY = 40;        int brickHeight = 20;


    //Ball object
    Rectangle Ball = new Rectangle(ballX, ballY, 15, 15);

    // Bat object
    Rectangle Bat = new Rectangle(padX, padY, 70, 10);

    // bricks object
    Rectangle[] Brick = new Rectangle[22];

    //reverse
    int movex = -1;
    int movey = -1;

    //booleans
    boolean ballFallDown = false;
    boolean bricksOver = false;

    //levens
    int lives = 3;

    int count = 0;

    //score
    int score = 0;

    //tekst status
    String status;

    Timer timer;
    BufferedImage bufferedImage;

    //game snelheid
    int gameSpeed = 5;
    Time time;

    boolean Running = true;
    boolean paused = false;
    private static Dimension dim;

    //tekst spelernaam
    String PlayerName;

    //variabelen voor muis positie
    int xPos;
    int yPos;

    //booleans
    boolean rect1Active,rect2Active;

    //variablen restart knop
    int RestX = 620;        int RestWidth = 100;
    int RestY = 450;        int RestHeight = 50;

    //variabeln pauze knop
    int PausX = 480;        int PausWidth = 100;
    int PausY = 450;        int PausHeight = 50;


    BrickGUI(Container contentPane) {
        timer = new Timer(gameSpeed, this);
        timer.start();
        time = new Time();
        PlayerName = JOptionPane.showInputDialog(null, "Enter your name:", "Name", JOptionPane.INFORMATION_MESSAGE);
        if (PlayerName == null) {
            System.exit(0);

        }

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
        paused = false;
        start();
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
    public void start(){
        movex = 1;
        movey = 1;
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
                if (i == 7){
                    g.setColor(new Color(0x002FFA));
                }
                if (i == 13){
                    g.setColor(new Color(0xFA472B));
                }
                if (i == 18) {
                    g.setColor(new Color(0x3FFA39));
                }
                if (i == 21) {
                    g.setColor(new Color(0x02A8FA));
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
            g.drawRect(RestX,RestY,RestWidth,RestHeight);
        }
        else {g.setColor(Color.red);
            g.fillRect(RestX,RestY,RestWidth,RestHeight);
        }

        Font Resume = new Font("Impact", Font.BOLD, 26);
        g.setFont(Resume);

        if (rect2Active) {
            g.setColor(Color.white);
            g.drawRect(PausX,PausY,PausWidth,PausHeight);
            g.drawString("Play", PausX / 2 + 255,PausY /2+260);
        }
        else {g.setColor(Color.red);
            g.fillRect(PausX,PausY,PausWidth,PausHeight);
            g.setColor(Color.white);
            g.drawString("Pause", PausX / 2 + 255, PausY /2+260);
        }



        g.setColor(Color.white);
        Font Score = new Font("Impact", Font.BOLD, 34);
        g.setFont(Score);

        g.drawString(String.valueOf("Score: " + score), 50, 450);
        g.drawString("Player: " + PlayerName, 50, 500);
        g.drawString("Lives: " + lives, 50, 400);


        Font RestartText = new Font("Impact", Font.BOLD, 26);
        g.setFont(RestartText);
        g.drawString("Restart ", RestX / 2 + 315, RestY /2+260);

        time.paint(g);

    }
    Graphics g;
    public void menu(){
        g.fillRect(10,10,10,10);

    }
    //game loop
    public void run() {
        //methode bricks
        createBricks();


        while (Running) {

            for (int i = 0; i < Brick.length; i++) {
                if (Brick[i] != null) {
                    if (Brick[i].intersects(Ball)) {
                        Brick[i] = null;
                        // movex = -movex;
                        movey = -movey;
                        count++;
                        score += 5;


                    }
                }
            }


            // check if ball hits all bricks
            if (count == Brick.length) {
                bricksOver = true;
                status = "You win the game!";
                movex = 0;
                movey = 0;

                repaint();

            }

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
            }

            if (Ball.y <= 3) {
                movey = -movey;
            }


            if (Ball.y >= 340 && lives > 0) {

                // Als de bal over de paddle valt, dan is game over.
                ballFallDown = true;
                // status = "You lost the game!";

                lives = lives - 1;

                restart();
                repaint();

            }

            if(Ball.y <= 340 && lives == 0)
            {
                bricksOver = true;
                ballFallDown = true;
                status = "You lost the game!";
                PauseGame();
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
        ballX = 350;
        ballY = 300;

        padX = 370;
        padY = 340;

        BrickX = 130;
        BrickY = 40;

        Ball = new Rectangle(ballX, ballY, 15, 15);
        Bat = new Rectangle(padX, padY, 70, 10);


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

    public void createBricks(){
        ///create Bricks
        for (int i = 0; i < Brick.length; i++) {
            Brick[i] = new Rectangle(BrickX, BrickY, brickBreadth, brickHeight);

            if (i == 6) {
                BrickX = 90;
                BrickY = (BrickY + brickHeight + 7);

            }
            if (i == 12) {
                BrickX = 130;
                BrickY = (BrickY + brickHeight + 7);

            }
            if (i == 17) {

                BrickX = 200;

                BrickY = (BrickY + brickHeight + 7);

            }

            if (i == 20) {
                BrickX = 280;
                BrickY = (BrickY + brickHeight + 7);
            }

            BrickX += (brickBreadth + 5);
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

        xPos = e.getX();
        yPos = e.getY();

        // Check als binnen de vierkant valt
        if (xPos > RestX && xPos < RestX + RestWidth && yPos > RestY
                && yPos < RestY + RestHeight) {

            rect1Active = true;
            lives = 3;
            restart();
        }
        else {
            rect1Active = false;
            //show the results of the motion
            repaint();
        }

        int x = e.getX();
        int y = e.getY();
        if (x > PausX && x < PausX + PausWidth && y > PausY
                && y < PausY + PausHeight) {
            rect2Active = true;
            PauseGame();
        }

        else {
            rect2Active = false;
            repaint();
            ResumeGame();

        }
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

    }

}

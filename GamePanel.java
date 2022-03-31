package snakeGame;

//Imports all the libraries that are needed
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

//Creates the GamePanel class that inherits the properties of the JPanel class and uses the ActionListener interface
public class GamePanel extends JPanel implements ActionListener {

    //Create final variables for the game window size
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;

    //Set the size of the squares in the game
    static final int UNIT_SIZE = 25;

    //Amount of squares in the game panel
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

    //Sets the delay of the timer
    static final int DELAY = 75;

    // Initializes two arrays for the snake body's x and y coordinates
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    //Sets the other class properties
    int bodyParts = 5;
    int applesEaten = 0;
    int appleX;
    int appleY;

    //Sets the direction of the snake
    char direction = 'R';

    //Shows if the game is running for not
    boolean running = false;

    //Create a Random and Timer object
    Timer timer;
    Random random;

    //The main constructor of the class
    public GamePanel() {
        //Declares the random variable to an instance of the Random class
        random = new Random();

        //Sets the preferred size of the panel to the screen width and height.
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        //Sets the background to black
        this.setBackground(Color.black);

        //Sets focusability to true to allow player input
        this.setFocusable(true);

        //Adds a key listener object
        this.addKeyListener(new MyKeyAdapter());

        //runs the start game method
        startGame();
    }

    //Method for starting the game
    public void startGame() {

        //Runs the new apple method
        newApple();

        //sets running to true
        running = true;

        //declares the time with the arguments delay for the delay and the action listener
        timer = new Timer(DELAY, this);

        //Starts the timer
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    //The method for drawing the games visuals
    public void draw(Graphics g) {

        //If the game is running then this code block is executed
        if (running) {

            //Loops to draw the grid lines on the screen
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            //sets the color to red
            g.setColor(Color.red);

            //makes a square at the randomly generated X and Y coordinates for the apple with the dimensions as the unit sizes
            g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //Loops through the snakes bodyparts
            for (int i = 0; i < bodyParts; i++) {

                //if i is 0 then the body part is the head
                if (i == 0) {
                    //makes it green
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                //else it makes the reset a different shade of green
                } else {
                    g.setColor(new Color(133, 255, 2));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            //sets the color to red
            g.setColor(Color.red);

            //sets the font of the text
            g.setFont(new Font("Fixedsys", Font.BOLD, 40));

            //Creates a new FontMetrics object
            FontMetrics metrics = getFontMetrics(g.getFont());

            //Draws the string at the location of the string metrics coordinates
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

        } else {
            //Runs the game over method is the game is not running
            gameOver(g);
        }

    }

    //Method for setting the coordinates of the new apple
    public void newApple() {
        //randomly generates coordinates using the unit sizes
        appleX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt((int) SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;

        //Loops through the body parts of the snake to check if the apple spawned there
        for (int i=0; i < bodyParts; i++){
            if (appleX == x[i] && appleY == y[i]){
                newApple();
            }
        }
    }

    //Method for moving the snake
    public void move() {

        //Loops through the snake body and sets the coordinates of each body part to the last bodypart
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];

        }

        //Sets the coordinates and direction of the head of the snake based on the value of the direction variable
        switch (direction) {
                case 'U':
                    y[0] = y[0] - UNIT_SIZE;
                    break;
                case 'D':
                    y[0] = y[0] + UNIT_SIZE;
                    break;
                case 'L':
                    x[0] = x[0] - UNIT_SIZE;
                    break;
                case 'R':
                    x[0] = x[0] + UNIT_SIZE;
                    break;
            }
    }

    //Checks if the apple is touching the head of the snake
    public void checkApple() {
        if ((x[0] == appleX) && y[0] == appleY){
            //increases the score and body parts variable
            bodyParts++;
            applesEaten++;

            //Runs the new apple method to spawn another apple on the grid
            newApple();
        }
    }

    public void checkCollisions() {
        //check if snake head collides with body
        for (int i = bodyParts; i > 0; i--){
            if ((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        //checks if snake hits left border
        if (x[0] < 0){
            running = false;
        }
         //check if snake head touches right border
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE){
            running = false;
        }
        //check if snake head touches top border
        if (y[0] < 0){
            running = false;
        }
        //checks if snake head touches bottom border
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE){
            running = false;
        }
        //Checks to see if the running is not true to stop timer
        if (!running){
            timer.stop();
        }

    }

    //Method for calling game over
    public void gameOver(Graphics g) {
        //Game over text
        g.setColor(Color.red);
        //Set font
        g.setFont(new Font("Fixedsys", Font.BOLD, 75));
        //Create metrics object for text
        FontMetrics metricsOne = getFontMetrics(g.getFont());
        //Draw the string in the middle of the screen
        g.drawString("GAME OVER", (SCREEN_WIDTH - metricsOne.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);

        //Set the color to red
        g.setColor(Color.red);
        //Sets the font of the text
        g.setFont(new Font("Fixedsys", Font.BOLD, 40));
        //Make a metrics object
        FontMetrics metricsTwo = getFontMetrics(g.getFont());
        //Draw the string onto the board
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metricsTwo.stringWidth("Score: " + applesEaten))/2, (SCREEN_HEIGHT/2) + g.getFont().getSize());
    }

    //Override the action performed method
    @Override
    public void actionPerformed(ActionEvent e) {
        //Checks to see if running is true
        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        //calls the repaint command to update graphics
        repaint();
    }

    //Makes an embeded class that extends the keyadapter super class
    public class MyKeyAdapter extends KeyAdapter {
        //Overrides the keypressed method
        @Override
        public void keyPressed(KeyEvent e) {
            //runs a switch statement for the e key source
            switch (e.getKeyCode()){

                //If the key is the left arrow
                case KeyEvent.VK_LEFT:
                    //Check if direction is not right
                    if (direction != 'R'){
                        //sets direction to left;
                        direction = 'L';
                    }
                    break;
                //If the key is the right arrow
                case KeyEvent.VK_RIGHT:
                    //Checks if direction is not left
                    if (direction != 'L'){
                        //sets direction to right
                        direction = 'R';
                    }
                    break;
                //Checks if key is up arrow
                case KeyEvent.VK_UP:
                    //Checks if key is not down
                    if (direction != 'D'){
                        //Sets the direction to up
                        direction = 'U';
                    }
                    break;
                //Checks if key is down arrow
                case KeyEvent.VK_DOWN:
                    //Checks if direction is not up
                    if (direction != 'U'){
                        //Sets the direction to down
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
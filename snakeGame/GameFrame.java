package snakeGame;

//importing swing
import javax.swing.*;

//GameFrame class that inherits the properties of the JFrame class
public class GameFrame extends JFrame {

    //Constructor of the GameFrame class
    public GameFrame(){

        //Add a new instance of game panel
        this.add(new GamePanel());

        //Sets the properties of the window
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);

        //Makes the window appear in the center
        this.setLocationRelativeTo(null);
    }









}

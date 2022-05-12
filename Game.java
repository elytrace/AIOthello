package AIOthello;
import javax.swing.JFrame;

public class Game {
    public static void main(String[] args) {
        JFrame window = new JFrame("Othello");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new AIGamePanel());
        
        window.pack();

        window.setVisible(true);
        window.setResizable(false);
        
    }
}
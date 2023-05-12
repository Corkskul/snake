import javax.swing.JFrame;

public class GameWindow extends JFrame {
    public GameWindow() {
        setTitle("Snake Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        GameBoard gameBoard = new GameBoard();
        add(gameBoard);
        pack();
        setLocationRelativeTo(null);
    }
}

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameBoard extends JPanel implements ActionListener {
    private static final int BOARD_WIDTH = 600;
    private static final int BOARD_HEIGHT = 600;
    private static final int GRID_SIZE = 20;
    private static final int DELAY = 10;

    private Snake snake;
    private Food food;
    private Timer timer;
    private int score;

    public GameBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (snake.isAlive()) {
                    //snake.processKey(e);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    initGame();
                }
            }
        });
        initGame();
    }

    private void initGame() {
        snake = new Snake(BOARD_WIDTH, BOARD_HEIGHT, GRID_SIZE);
        food = new Food(BOARD_WIDTH, BOARD_HEIGHT, GRID_SIZE);
        timer = new Timer(DELAY, this);
        timer.start();
        score = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Cambiar el color de fondo
        setBackground(Color.DARK_GRAY);

        // Dibujar el borde
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        draw(g);
    }

    private void draw(Graphics g) {
        if (!snake.isAlive()) {
            timer.stop();
            drawGameOver(g);
            return;
        }

        snake.draw(g);
        food.draw(g);
        drawScore(g);
    }
    private void drawScore(Graphics g) {
        String scoreText = "Score: " + score;
        Font font = new Font("Arial", Font.PLAIN, 16);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(scoreText, 10, metrics.getHeight());
    }

    private void drawGameOver(Graphics g) {
        String message = "Game Over";
        String restartMessage = "Press ENTER to restart";
        Font font = new Font("Arial", Font.BOLD, 48);
        Font smallFont = new Font("Arial", Font.BOLD, 24);
        FontMetrics metrics = getFontMetrics(font);
        FontMetrics smallMetrics = getFontMetrics(smallFont);

        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(message, (BOARD_WIDTH - metrics.stringWidth(message)) / 2, BOARD_HEIGHT / 2);

        g.setColor(Color.WHITE);
        g.setFont(smallFont);
        g.drawString(restartMessage, (BOARD_WIDTH - smallMetrics.stringWidth(restartMessage)) / 2, BOARD_HEIGHT / 2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        snake.autoMove(food.getPosition(), BOARD_WIDTH, BOARD_HEIGHT, GRID_SIZE);
        snake.move();
        checkFoodCollision();
        checkSnakeCollision();
        checkWallCollision();
        repaint();
    }

    private void checkFoodCollision() {
        if (snake.getHead().equals(food.getPosition())) {
            snake.grow();
            food.generate(snake.getBody());
            score += 10;
        }
    }

    private void checkSnakeCollision() {
        if (snake.selfCollision()) {
            snake.setAlive(false);
        }
    }
    private void checkWallCollision() {
        Point head = snake.getHead();
        if (head.x < 0 || head.y < 0 || head.x >= BOARD_WIDTH || head.y >= BOARD_HEIGHT) {
            snake.setAlive(false);
        }
    }


}

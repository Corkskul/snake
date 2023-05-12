import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import java.util.Random;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.Graphics2D;

public class Food {
    private static final int DX = 20;
    private static final int DY = 20;

    private int boardWidth;
    private int boardHeight;
    private int gridSize;
    private Point position;
    private Random random;

    public Food(int boardWidth, int boardHeight, int gridSize) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.gridSize = gridSize;
        this.random = new Random();
        this.position = new Point();
    }

    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        //Sirve para suavizar los bordes del dibujo
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Establece un degradado radial para la comida
        GradientPaint gp = new GradientPaint(position.x, position.y, Color.RED, position.x + DX / 2, position.y + DY / 2, Color.RED.darker(), true);
        g2d.setPaint(gp);

        // Dibuja una elipse en lugar de un rectángulo para la comida
        g2d.fillOval(position.x, position.y, DX, DY);
    }

    public void generate(List<Point> snakeBody) {
        int x, y;
        do {
            //aseguramos que las cordenadas sean multiplos  del tamaño de la cuadricula
            //para alinear la comida con la cuadricula del tablero
            x = random.nextInt(boardWidth / gridSize) * gridSize;
            y = random.nextInt(boardHeight / gridSize) * gridSize;
            //Usamos el do-while hasta que la comida no esta en ninguna posicion de la serpiente
        } while (snakeBody.contains(new Point(x, y)));

        position.setLocation(x, y);
    }

    public Point getPosition() {
        return position;
    }
}

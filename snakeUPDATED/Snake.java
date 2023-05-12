import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.GradientPaint;
import java.awt.RenderingHints;

public class Snake {
    private static final int INIT_SIZE = 5;
    private static final int DX = 20;
    private static final int DY = 20;
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_UP = 1;
    public static final int DIRECTION_RIGHT = 2;
    public static final int DIRECTION_DOWN = 3;

    private List<Point> body;
    private int direction;
    private boolean alive;

    public Snake(int boardWidth, int boardHeight, int gridSize) {
        body = new ArrayList<>();
        for (int i = 0; i < INIT_SIZE; i++) {
            body.add(new Point(boardWidth / 2 + i * gridSize, boardHeight / 2));
        }
        direction = KeyEvent.VK_LEFT;
        alive = true;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Point point : body) {
            // Establece un degradado radial para cada segmento de la serpiente
            GradientPaint gp = new GradientPaint(point.x, point.y, Color.GREEN, point.x + DX / 2, point.y + DY / 2, Color.GREEN.darker(), true);
            g2d.setPaint(gp);

            // Dibuja una elipse en lugar de un rectángulo para cada segmento
            g2d.fillRoundRect(point.x, point.y, DX, DY, DX / 2, DY / 2);
        }
    }






      public void move() {
        Point newHead = new Point(body.get(0));

        switch (direction) {
            case DIRECTION_LEFT:
                newHead.x -= DX;
                break;
            case DIRECTION_RIGHT:
                newHead.x += DX;
                break;
            case DIRECTION_UP:
                newHead.y -= DY;
                break;
            case DIRECTION_DOWN:
                newHead.y += DY;
                break;
        }


        body.add(0, newHead);
        body.remove(body.size() - 1);
        for (Point point : body) {
            System.out.println("Point: " + point);
        }
        System.out.println("Body size: " + body.size());
    }


    public void autoMove(Point food, int boardWidth, int boardHeight, int gridSize) {
        //Utilizamos un HashSet porque no podemos permitir duplicados
        Set<Point> obstacles = new HashSet<>(body);
        //eliminamos la cola porque la cola no es un obstaculo
        obstacles.remove(tail());
        //Inicializamos el algoritmo AStar y normalizamos las medidas del tablero para que el algoritmo sea mas eficiente
        AStar aStar = new AStar(boardWidth / gridSize, boardHeight / gridSize, 1);

        Point scaledHead = new Point(head().x / gridSize, head().y / gridSize);
        //System.out.println("Scaled Head: (" + scaledHead.x + ", " + scaledHead.y + ")");

        Point scaledFood = new Point(food.x / gridSize, food.y / gridSize);

        Set<Point> scaledObstacles = new HashSet<>();
        for (Point obstacle : obstacles) {
            scaledObstacles.add(new Point(obstacle.x / gridSize, obstacle.y / gridSize));
        }
        //Buscamos el camino hacia la comida
        List<Point> path = aStar.findPath(scaledHead, scaledFood, scaledObstacles);
        //Determinamos la direccion del movimiento
        //Aseguramos que la ruta existe
        if (path != null && path.size() > 1) {
            //Obtenemos el segundo punto de la ruta para moverse a continuacion
            Point next = path.get(1);

            //Determinamos el proximo punto de la cabeza
            int dx = next.x * gridSize - head().x;
            int dy = next.y * gridSize - head().y;

            if (dx == -gridSize && dy == 0) {
                setDirection(DIRECTION_LEFT);
            } else if (dx == gridSize && dy == 0) {
                setDirection(DIRECTION_RIGHT);
            } else if (dx == 0 && dy == -gridSize) {
                setDirection(DIRECTION_UP);
            } else if (dx == 0 && dy == gridSize) {
                setDirection(DIRECTION_DOWN);
            }

        }

    }


    public Point tail() {
        return body.get(body.size() - 1);
    }
    public Point head() {
        return body.get(0);
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void grow() {
        body.add(body.get(body.size() - 1));
    }

    public List<Point> getBody() {
        return body;
    }


    public boolean selfCollision() {
        for (int i = 1; i < body.size(); i++) {
            if (body.get(0).equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Point getHead() {
        return body.get(0);
    }
}


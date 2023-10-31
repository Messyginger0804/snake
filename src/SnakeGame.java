import java.awt.*;
import java.awt.event.*;
// import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

/**
 * SnakeGame
 */
public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    /* TileSize */
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    // boolean menuScreen = true;

    // snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // food
    Tile food;
    Random random;

    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(150, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        // if (menuScreen) {
        // g.drawString("Welcome to JC's Snake game. Press any arrow key to begin",
        // tileSize - 0,
        // tileSize + 50);
        // }

        // grid
        for (int i = 0; i < boardWidth / tileSize; i++) {
            // (x1, y1, x2, y2)
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // food
        g.setColor(Color.red);
        // g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // snake head
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        // snake body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize,
            // tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        // start screen
        // welomce to JC's snake game use directions arrow keys to start. Press enter to
        // exit start screen

        // if (gameLoop.isRunning()) {
        // g.setColor(Color.blue);
        // g.drawString("Welcome to JC's Snake game" + "... Press any arrow key to
        // begin", tileSize - 0,
        // tileSize + 50);
        // }

        // score
        g.setFont(new Font("Arial", Font.BOLD, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("GAME OVER: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);// 600/25 = 24 random from 0 - 24
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean collide(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {

        // eat food
        if (collide(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // snake body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            // collide with snake head
            if (collide(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || // passed left border or right border
                snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) { // passed top border or bottom
                                                                                      // border
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("KeyEvent: " + e.getKeyCode());

        // if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
        // velocityX = 0;
        // velocityY = -1;
        // } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
        // velocityX = 0;
        // velocityY = 1;
        // } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
        // velocityX = -1;
        // velocityY = 0;
        // } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
        // velocityX = 1;
        // velocityY = 0;
        // }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (velocityY != 1) {
                    velocityX = 0;
                    velocityY = -1;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (velocityY != -1) {
                    velocityX = 0;
                    velocityY = 1;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (velocityX != 1) {
                    velocityX = -1;
                    velocityY = 0;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (velocityX != -1) {
                    velocityX = 1;
                    velocityY = 0;
                }
                break;
            // Handle other key codes if needed
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {

    }

    @Override
    public void keyTyped(KeyEvent arg0) {

    }
}
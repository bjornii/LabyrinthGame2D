package entity;

import main.GamePanel;
import main.KeyHandler;
import main.MySQLConnection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class player{
    public static int x, y, defaultX, defaultY;
    public int speed;
    public String direction;
    public Rectangle bounds = new Rectangle();
    public Rectangle deathBounds = new Rectangle();
    public boolean collisionOn = false;
    GamePanel gp;
    KeyHandler keyH;
    private BufferedImage[] upImages, downImages, leftImages, rightImages, idleImages;
    private int animationFrame = 0;
    private int animationCounter = 0;
    private BufferedImage image;
    public static int score=0, savedscore;
    public static int savedPlayerX,savedPlayerY, savedScore;

    public player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        x = 72;
        y = 40;
        defaultX = x;
        defaultY = y;
        bounds = new Rectangle(10, 36, 52, 36);
        deathBounds = new Rectangle(15, 57, 42, 13);

        setDefaultValues();
        getPlayerImages();
    }

    public static void saveGameState() {
        savedPlayerX = x;
        savedPlayerY = y;
        savedScore = score;
        MySQLConnection msc = new MySQLConnection();
        msc.insertSavedGame(savedPlayerX,savedPlayerY,savedScore);
        msc.insertGameInfo(savedPlayerX,savedPlayerY,savedScore);

        // Debug prints
        System.out.println("Game State Saved:");
        System.out.println("Saved Coordinates: (" + savedPlayerX + ", " + savedPlayerY + ")");
        System.out.println("Saved Score: " + savedScore);
    }

    public void restoreSavedGame() {
        MySQLConnection msc = new MySQLConnection();
        msc.restoreSavedGame();
        x = msc.a[0];
        y = msc.a[1];
        score = msc.a[2];

        // Debug prints
        System.out.println("Game State Restored:");
        System.out.println("Restored Coordinates: (" + x + ", " + y + ")");
        System.out.println("Restored Score: " + score);
    }

    public void updateGameInfoScore(){
        MySQLConnection msc = new MySQLConnection();
        msc.updateScoreInGameInfo(savedscore);
    }

    public void restoreGameInfo() {

        MySQLConnection msc = new MySQLConnection();
        msc.restoreGameInfo();
        x = msc.a[0];
        y = msc.a[1];
        score = msc.a[2];

        // Debug prints
        System.out.println("Game State Restored:");
        System.out.println("Restored Coordinates: (" + x + ", " + y + ")");
        System.out.println("Restored Score: " + score);
    }

    public void resetNewGame(){
        x = defaultX;
        y = defaultY;
        score = 0;
        setDefaultValues();
    }

    public void setDefaultValues() {
        speed = 4;
        direction = "idle";

    }

    public void getPlayerImages() {
        try {
            upImages = loadImages("/player/up/up");
            downImages = loadImages("/player/down/down");
            leftImages = loadImages("/player/left/left");
            rightImages = loadImages("/player/right/right");
            idleImages = loadImages("/player/idle/idle");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage[] loadImages(String pathPrefix) throws IOException {
        BufferedImage[] images = new BufferedImage[8];
        for (int i = 0; i < images.length; i++) {
            images[i] = ImageIO.read(getClass().getResourceAsStream(pathPrefix + (i + 1) + ".png"));
        }
        return images;
    }

    public void update() {
        if (keyH.upPressed) {
            direction = "up";
        } else if (keyH.downPressed) {
            direction = "down";
        } else if (keyH.leftPressed) {
            direction = "left";
        } else if (keyH.rightPressed) {
            direction = "right";
        } else {
            direction = "idle";
        }

        collisionOn = false;

        gp.c.checkWall(this);
        gp.c.checkLava(this);
        gp.c.collectReward(this);
        gp.c.checkPortal(this);

        if(gp.gameover){
            gp.gameState = GamePanel.GameState.GAME_OVER;
        }

        if(score == 1600 && gp.endportal){
            gp.gameState = GamePanel.GameState.YOU_WIN;
        }

        if (!collisionOn) {
            switch (direction) {
                case "up":
                    y -= speed;
                    break;
                case "down":
                    y += speed;
                    break;
                case "left":
                    x -= speed;
                    break;
                case "right":
                    x += speed;
                    break;
            }
        }

        switch (direction) {
            case "up": animate(upImages); break;
            case "down": animate(downImages); break;
            case "left": animate(leftImages); break;
            case "right": animate(rightImages); break;
            default: animate(idleImages); break;
        }
    }

    private void animate(BufferedImage[] images) {
        if (images != null && images.length > 0) {
            int animationSpeed = 6;
            if (animationCounter % animationSpeed == 0) {
                animationFrame = animationFrame % images.length;
                image = images[animationFrame];
                animationFrame = (animationFrame + 1) % images.length;
            }
            animationCounter++;
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image, x-gp.tileSize, y-gp.tileSize, 216, 216, null);

    }
}
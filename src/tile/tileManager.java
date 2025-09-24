package tile;

import main.CollisionChecker;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class tileManager {
    GamePanel gp;
    public tile[] tile;
    CollisionChecker checker;

    public tileManager(GamePanel gp) {
        this.gp = gp;
        tile = new tile[10];


        getTileImage();
    }

    public void getTileImage(){
        try{

            tile[0] = new tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/path3.png"));

            tile[1] = new tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/invis.png"));
            tile[1].death = true;

            tile[2] = new tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/small3.png"));
            tile[2].reward = true;

            tile[3] = new tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/mid2.png"));
            tile[3].reward = true;

            tile[4] = new tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/big2.png"));
            tile[4].reward = true;

            tile[5] = new tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/portal1.png"));

            tile[6] = new tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/tiles/left1.png"));
            tile[6].collision = true;

            tile[7] = new tile();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/tiles/right.png"));
            tile[7].collision = true;

            tile[8] = new tile();
            tile[8].image = ImageIO.read(getClass().getResourceAsStream("/tiles/bottom.png"));
            tile[8].collision = true;

            tile[9] = new tile();
            tile[9].image = ImageIO.read(getClass().getResourceAsStream("/tiles/top.png"));
            tile[9].collision = true;
            /**/
        }catch(IOException | NullPointerException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        int map[][] = gp.map;
        for (int i = 0; i < gp.maxScreenRow; i++) {
            for (int j = 0; j < gp.maxScreenCol; j++) {
                if (map[i][j] == 9 ) {
                    g2.drawImage(tile[9].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }
                else if (map[i][j] == 8) {
                    g2.drawImage(tile[8].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }
                else if (map[i][j] == 7) {
                    g2.drawImage(tile[7].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }
                else if (map[i][j] == 6) {
                    g2.drawImage(tile[6].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }
                else if (map[i][j] == 5) {
                    g2.drawImage(tile[5].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }
                else if (map[i][j] == 4) {
                    g2.drawImage(tile[4].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }
                else if (map[i][j] == 3) {
                    g2.drawImage(tile[3].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }
                else if (map[i][j] == 2 ) {
                    g2.drawImage(tile[2].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }
                else if (map[i][j] == 1) {
                    g2.drawImage(tile[1].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }
                else if (map[i][j] == 0) {
                    g2.drawImage(tile[0].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }
            }
        }
    }
}

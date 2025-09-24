package main;
import entity.player;

import java.awt.*;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkWall(player player) {
        int playerLeftX = player.x + player.bounds.x;
        int playerRightX = player.x + player.bounds.x + player.bounds.width;
        int playerTopY = player.y + player.bounds.y;
        int playerBottomY = player.y + player.bounds.y + player.bounds.height;

        int playerLeftCol  = playerLeftX/gp.tileSize;
        int playerRightCol  = playerRightX/gp.tileSize;
        int playerTopRow  = playerTopY/gp.tileSize;
        int playerBottomRow  = playerBottomY/gp.tileSize;

        int tileNum1, tileNum2;

        switch (player.direction) {
            case "up":
                playerTopRow  = (playerTopY  - player.speed) / gp.tileSize;
                tileNum1 = gp.map[playerTopRow ][playerLeftCol ];
                tileNum2 = gp.map[playerTopRow ][playerRightCol ];
                if (gp.tilem.tile[tileNum1].collision || gp.tilem.tile[tileNum2].collision) {
                    player.collisionOn = true;
                }
                break;
            case "down":
                playerBottomRow  = (playerBottomY + player.speed) / gp.tileSize;
                tileNum1 = gp.map[playerBottomRow ][playerLeftCol ];
                tileNum2 = gp.map[playerBottomRow ][playerRightCol ];
                if (gp.tilem.tile[tileNum1].collision || gp.tilem.tile[tileNum2].collision) {
                    player.collisionOn = true;
                }
                break;
            case "left":
                playerLeftCol  = (playerLeftX  - player.speed) / gp.tileSize ;
                tileNum1 = gp.map[playerTopRow ][playerLeftCol ];
                tileNum2 = gp.map[playerBottomRow ][playerLeftCol ];
                if (gp.tilem.tile[tileNum1].collision || gp.tilem.tile[tileNum2].collision) {
                    player.collisionOn = true;
                }
                break;
            case "right":
                playerRightCol  = (playerRightX + player.speed) / gp.tileSize;
                tileNum1 = gp.map[playerTopRow ][playerRightCol ];
                tileNum2 = gp.map[playerBottomRow ][playerRightCol ];
                if (gp.tilem.tile[tileNum1].collision || gp.tilem.tile[tileNum2].collision) {
                    player.collisionOn = true;
                }
                break;
        }

    }

    public void checkLava(player player) {
        int playerLeftX = player.x + player.deathBounds.x;
        int playerRightX = player.x + player.deathBounds.x + player.deathBounds.width;
        int playerTopY = player.y + player.deathBounds.y;
        int playerBottomY = player.y + player.deathBounds.y + player.deathBounds.height;

        int playerLeftCol  = playerLeftX/72;
        int playerRightCol  = playerRightX/72;
        int playerTopRow  = playerTopY/72;
        int playerBottomRow  = playerBottomY/72;

        int tileNum1, tileNum2;

        switch (player.direction) {
            case "up":
                playerTopRow  = (playerTopY  - player.speed) / 72;
                tileNum1 = gp.map[playerTopRow ][playerLeftCol ];
                tileNum2 = gp.map[playerTopRow ][playerRightCol ];
                if (gp.tilem.tile[tileNum1].death || gp.tilem.tile[tileNum2].death) {
                    player.savedscore = player.score;
                    gp.gameover = true;
                }
                break;
            case "down":
                playerBottomRow  = (playerBottomY + player.speed) / 72;
                tileNum1 = gp.map[playerBottomRow ][playerLeftCol ];
                tileNum2 = gp.map[playerBottomRow ][playerRightCol ];
                if (gp.tilem.tile[tileNum1].death || gp.tilem.tile[tileNum2].death) {
                    player.savedscore = player.score;
                    gp.gameover = true;
                }
                break;
            case "left":
                playerLeftCol  = (playerLeftX  - player.speed) / 72 ;
                tileNum1 = gp.map[playerTopRow ][playerLeftCol ];
                tileNum2 = gp.map[playerBottomRow ][playerLeftCol ];
                if (gp.tilem.tile[tileNum1].death || gp.tilem.tile[tileNum2].death) {
                    player.savedscore = player.score;
                    gp.gameover = true;
                }
                break;
            case "right":
                playerRightCol  = (playerRightX + player.speed) / 72;
                tileNum1 = gp.map[playerTopRow ][playerRightCol ];
                tileNum2 = gp.map[playerBottomRow ][playerRightCol ];
                if (gp.tilem.tile[tileNum1].death || gp.tilem.tile[tileNum2].death) {
                    player.savedscore = player.score;
                    gp.gameover = true;
                }
                break;
        }
    }

    private int checkReward(int tileNum, int row, int col) {
        if (gp.tilem.tile[tileNum].reward) {
            int score = 0;
            switch (tileNum) {
                case 2: score = 100; break;
                case 3: score = 200; break;
                case 4: score = 300; break;
            }
            gp.map[row][col] = 0; // Replace the reward tile with a path tile
            return score;
        }
        return 0;
    }

    public void collectReward(player player) {
        int playerLeftX = player.x + player.deathBounds.x;
        int playerRightX = player.x + player.deathBounds.x + player.deathBounds.width;
        int playerTopY = player.y + player.deathBounds.y;
        int playerBottomY = player.y + player.deathBounds.y + player.deathBounds.height;

        int playerLeftCol  = playerLeftX/gp.tileSize;
        int playerRightCol  = playerRightX/gp.tileSize;
        int playerTopRow  = playerTopY/gp.tileSize;
        int playerBottomRow  = playerBottomY/gp.tileSize;

        int tileNum1, tileNum2, a, b;

        switch (player.direction) {

            case "up":
                playerTopRow  = (playerTopY  - player.speed) / gp.tileSize;
                tileNum1 = gp.map[playerTopRow][playerLeftCol]; //tile number at the player's top left corner
                tileNum2 = gp.map[playerTopRow][playerRightCol]; //tile number at player's top right corner

                a = checkReward(tileNum1, playerTopRow, playerLeftCol);
                b = checkReward(tileNum2, playerTopRow, playerRightCol);
                if (a > 0) {
                    player.score += a;
                }
                if (b > 0 && a != b) {
                    player.score += b;
                }
                break;

            case "down":
                playerBottomRow = (playerBottomY + player.speed) / gp.tileSize;
                tileNum1 = gp.map[playerBottomRow][playerLeftCol]; //tile number at the player's bottom left corner
                tileNum2 = gp.map[playerBottomRow][playerRightCol]; //tile number at the player's bottom right corner
                a = checkReward(tileNum1, playerBottomRow, playerLeftCol);
                b = checkReward(tileNum2, playerBottomRow, playerRightCol);
                if (a > 0) {
                    player.score += a;
                }
                if (b > 0 && a != b) {
                    player.score += b;
                }
                break;

            case "left":
                playerLeftCol = (playerLeftX - player.speed) / gp.tileSize;
                tileNum1 = gp.map[playerTopRow][playerLeftCol]; //tile number at the player's top left corner
                tileNum2 = gp.map[playerBottomRow][playerLeftCol]; //tile number at the player's bottom left corner
                a = checkReward(tileNum1, playerTopRow, playerLeftCol);
                b = checkReward(tileNum2, playerBottomRow, playerLeftCol);
                if (a > 0) {
                    player.score += a;
                }
                if (b > 0 && a != b) {
                    player.score += b;
                }
                break;

            case "right":
                playerRightCol = (playerRightX + player.speed) / gp.tileSize;
                tileNum1 = gp.map[playerTopRow][playerRightCol]; //tile number at player's top right corner
                tileNum2 = gp.map[playerBottomRow][playerRightCol]; //tile number at player's top right corner
                a = checkReward(tileNum1, playerTopRow, playerRightCol);
                b = checkReward(tileNum2, playerBottomRow, playerRightCol);
                if (a > 0) {
                    player.score += a;
                }
                if (b > 0 && a != b) {
                    player.score += b;
                }
                break;
        }
    }

    public void checkPortal(player player) {
        int playerLeftX = player.x + player.deathBounds.x;
        int playerRightX = player.x + player.deathBounds.x + player.deathBounds.width;
        int playerTopY = player.y + player.deathBounds.y;
        int playerBottomY = player.y + player.deathBounds.y + player.deathBounds.height;

        int centerCol = ((playerLeftX + playerRightX) / 2) / gp.tileSize;
        int centerRow = ((playerTopY + playerBottomY) / 2) / gp.tileSize;
        if(player.score == 1600){
            if (centerRow == gp.maxScreenRow - 2 && centerCol == gp.maxScreenCol - 2) {
                gp.endportal = true;
            }
        }
    }
}



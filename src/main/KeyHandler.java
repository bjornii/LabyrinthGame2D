package main;

import entity.player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private final GamePanel gamePanel;


    public boolean upPressed, downPressed, leftPressed, rightPressed, re, pause;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ESCAPE) {
            if (!pause) {
                pause = true;

                // Pause the game only if it's not already paused
                if (!gamePanel.isPaused) {
                    System.out.println("Pausing game...");
                    gamePanel.isPaused = true;
                    gamePanel.resumeMenu();  // Show the resume menu
                }
            }
        }
        if (code == KeyEvent.VK_R) {
            re = true;
        }
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            downPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ESCAPE) {
            pause = false;
        }
        if (code == KeyEvent.VK_R) {
            re = false;
        }
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            downPressed = false;
        }
    }
}

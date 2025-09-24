package main;

import entity.player;
import tile.tileManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel implements Runnable {

    public final int tileSize = 72;
    public static final int maxScreenCol = 18;
    public static final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    int FPS = 60;
    public boolean endportal = false;
    public boolean gameover = false;
    public boolean isPaused = false;
    public BufferedImage backgroundImage, back, back1;
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    player player = new player(this,keyH);
    tileManager tilem = new tileManager(this);
    public CollisionChecker c = new CollisionChecker(this);
    static Labyrinth maze = new Labyrinth(maxScreenRow, maxScreenCol);
    public static int[][] map = maze.getmap();

    public GameState gameState = GameState.PLAYING;

    public enum GameState {
        PLAYING, GAME_OVER, YOU_WIN, PAUSED
    }

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.addKeyListener(keyH);
        this.setFocusable(true);

        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\bjorn\\IdeaProjects\\Labyrinth\\res\\tiles\\lavabackground1.png"));
            back = ImageIO.read(getClass().getResourceAsStream("/tiles/saj.png"));
            back1 = ImageIO.read(getClass().getResourceAsStream("/tiles/yippie.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void mainMenu() {

        JFrame frame = new JFrame("Labyrinth Game");
        frame.setSize(1296, 864);
        frame.setLocationRelativeTo(null); // Center the frame

        // Create a panel for the main menu with background image
        JPanel panel = new JPanel() {
            private final Image backgroundImage = new ImageIcon("C:\\Users\\bjorn\\IdeaProjects\\Labyrinth\\res\\tiles\\mainmenuback.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // Call the parent method
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout()); // Using GridBagLayout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components

        // Title at the top, spanning across columns
        JLabel title = new JLabel("Labyrinth Game", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 60)); // Larger title font
        title.setForeground(Color.orange);
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.insets = new Insets(-350, 10, 10, 10);

        // Set GridBagConstraints for the title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Make title span across both columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the title
        panel.add(title, titleGbc);

        // Panel for buttons, to make them centered
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout()); // Use GridBagLayout inside buttonPanel
        buttonPanel.setOpaque(false);
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 10, 10, 10);
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonGbc.anchor = GridBagConstraints.CENTER; // Center the buttons in their panel

        // Create buttons
        JButton newGameButton = new JButton("New Game");
        JButton loadGameButton = new JButton("Load Game");
        JButton exitButton = new JButton("Exit");
        newGameButton.setFocusPainted(false);
        loadGameButton.setFocusPainted(false);
        exitButton.setFocusPainted(false);

        // Set button sizes
        newGameButton.setPreferredSize(new Dimension(200, 40));
        loadGameButton.setPreferredSize(new Dimension(200, 40));
        exitButton.setPreferredSize(new Dimension(200, 40));

        // Add buttons to buttonPanel with vertical stacking
        buttonPanel.add(newGameButton, buttonGbc);
        buttonGbc.gridy = 1; // Move down for next button
        buttonPanel.add(loadGameButton, buttonGbc);
        buttonGbc.gridy = 2; // Move down for exit button
        buttonPanel.add(exitButton, buttonGbc);

        // Set GridBagConstraints for buttonPanel (center the buttonPanel in main panel)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Ensure buttons span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center button panel in parent
        panel.add(buttonPanel, gbc);

        // Add action listeners to the buttons
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start the game
                MySQLConnection msc = new MySQLConnection();
                msc.insertUserIntoGameInfo(main.LoginSignup.getU());
                msc.insertUserIntoSavedGame(main.LoginSignup.getU());
                msc.deleteflag();
                Main.launchGame();
                frame.setVisible(false);
                frame.dispose();

            }
        });

        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.restoreSavedGame();
                repaint();
                MySQLConnection msc = new MySQLConnection();
                map = msc.loadMapFromDatabase(main.LoginSignup.getU());
                msc.flag();
                Main.launchGame();
                frame.setVisible(false);
                frame.dispose();

            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exit the game
                System.exit(0);
            }
        });

        // Add the main panel to the frame
        frame.add(panel);

        // Make the frame visible
        frame.setVisible(true);
    }

    public void resumeMenu(){
        JFrame dialog = new JFrame ("Game Paused!");
        dialog.setSize(1296, 864);
        dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dialog.setLocationRelativeTo(null); // Center the dialog

        // Create a panel for the main menu with background image
        JPanel panel = new JPanel() {
            private final Image backgroundImage = new ImageIcon("C:\\Users\\bjorn\\IdeaProjects\\Labyrinth\\res\\tiles\\mainmenuback.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout()); // Using GridBagLayout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components

        // Title at the top, spanning across columns
        JLabel title = new JLabel("Labyrinth Game", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 60));
        title.setForeground(Color.orange);
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.insets = new Insets(-350, 10, 10, 10);

        // Set GridBagConstraints for the title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Make title span across both columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the title
        panel.add(title, titleGbc);

        // Panel for buttons, to make them centered
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout()); // Use GridBagLayout inside buttonPanel
        buttonPanel.setOpaque(false);
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 10, 10, 10);
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonGbc.anchor = GridBagConstraints.CENTER; // Center the buttons in their panel

        // Create buttons
        JButton resumeGameButton = new JButton("Resume");
        JButton saveGameButton = new JButton("Save Game");
        JButton exitButton = new JButton("Exit");

        resumeGameButton.setFocusPainted(false);
        saveGameButton.setFocusPainted(false);
        exitButton.setFocusPainted(false);

        // Set button sizes
        resumeGameButton.setPreferredSize(new Dimension(200, 40));
        saveGameButton.setPreferredSize(new Dimension(200, 40));
        exitButton.setPreferredSize(new Dimension(200, 40));

        // Add buttons to buttonPanel with vertical stacking
        buttonPanel.add(resumeGameButton, buttonGbc);
        buttonGbc.gridy = 1; // Move down for next button
        buttonPanel.add(saveGameButton, buttonGbc);
        buttonGbc.gridy = 2; // Move down for exit button
        buttonPanel.add(exitButton, buttonGbc);

        // Set GridBagConstraints for buttonPanel (center the buttonPanel in main panel)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Ensure buttons span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center button panel in parent
        panel.add(buttonPanel, gbc);

        // Add action listeners to the buttons
        resumeGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                gameState = GameState.PLAYING;
                dialog.dispose();
            }
        });

        saveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MySQLConnection msc = new MySQLConnection();
                player.saveGameState();
                msc.saveMapToDatabase();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exit the game
                System.exit(0);
            }
        });

        // Add the main panel to the frame
        dialog.add(panel);

        // Make the frame visible
        dialog.setVisible(true);
    }

    public void drawScore(Graphics2D g2){
        g2.setFont(new Font("Arial", Font.BOLD, 35));
        g2.setColor(Color.WHITE);
        String scoreString = "Score: " + player.score;
        g2.drawString(scoreString, 555, 848);
    }

    public void drawAllTimeScore(Graphics2D g2){
        MySQLConnection msc = new MySQLConnection();
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.setColor(Color.WHITE);
        String scoreString = "All-time wins: " + msc.selectAllTimeScore()/1600;
        g2.drawString(scoreString, 1040, 42);
    }

    public void drawGameOver(Graphics2D g2){
        g2.drawImage(back, 0, 0, getWidth(), getHeight(), this);

        g2.setFont(new Font("Arial Black", Font.BOLD, 140));
        Color custom = new Color(54, 2, 2);
        g2.setColor(custom);

        String message = "GAME OVER";
        int x = getWidth() / 2 - g2.getFontMetrics().stringWidth(message) / 2;
        int y = getHeight() / 2;
        g2.drawString(message, x, y-20);

        g2.setFont(new Font("Arial", Font.BOLD, 30));
        String restartString = "Press R to Restart";
        int restartX = getWidth() / 2 - g2.getFontMetrics().stringWidth(restartString) / 2;
        g2.drawString(restartString, restartX, y+50);
    }

    public void drawWin(Graphics2D g2) {
        Font customFont = null;
        try {
            InputStream fontStream = getClass().getResourceAsStream("/tiles/SuperCereal-owLDo.ttf");
            if (fontStream == null) {
                System.out.println("Font file not found!");
                return;
            }
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(180f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        g2.drawImage(back1, 0, 0, getWidth(), getHeight(), this);
        g2.setFont(customFont);
        Color custom = new Color(30, 129, 5);
        g2.setColor(custom);

        String message = "YIPPIEEE";
        int x = getWidth() / 2 - g2.getFontMetrics().stringWidth(message) / 2;
        int y = getHeight() / 2;
        g2.drawString(message, x, y);

        g2.setFont(new Font("Arial", Font.BOLD, 30));
        String newgame = "Press R to Play Again";
        int newgameX = getWidth() / 2 - g2.getFontMetrics().stringWidth(newgame) / 2;
        g2.drawString(newgame, newgameX, y + 50);
    }

    public void gameReset(){
        maze = new Labyrinth(maxScreenRow, maxScreenCol); // Generate a new map
        map = maze.getmap();
        player.resetNewGame();
        gameState = GameState.PLAYING;
        endportal = false;
        gameover = false;
    }

    public void loadGameInfo(){
        player.restoreGameInfo();
        gameState = GameState.PLAYING;
        endportal = false;
        gameover = false;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while( gameThread != null && gameThread.isAlive() ) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update(){
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        MySQLConnection msc = new MySQLConnection();


        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        }


        if (msc.selectflag()) {

        if (gameState == GameState.PLAYING) {
            tilem.draw(g2); //TILE DRAW
            player.draw(g2);//PLAYER DRAW
            drawScore(g2);
            drawAllTimeScore(g2);


            if (keyH.pause && !isPaused) {
                System.out.println("Pausing game...");
                isPaused = true;
                resumeMenu();
            } else if (!keyH.pause && isPaused) {
                isPaused = false;
            }
        } else if (gameState == GameState.GAME_OVER) {
                drawGameOver(g2);
                player.updateGameInfoScore();
                if (keyH.re) {
                    loadGameInfo();
                }

        } else if (gameState == GamePanel.GameState.YOU_WIN) {
            drawWin(g2);
            if (keyH.re) {
                msc.insertScoreIntoAllTimeScore(1600);
                msc.deleteflag();
                gameReset();
            }
        }
    }


        else if (gameState == GameState.PLAYING) {
            tilem.draw(g2); //TILE DRAW
            player.draw(g2);//PLAYER DRAW
            drawScore(g2);
            drawAllTimeScore(g2);

            if (keyH.re) {
                gameReset();
            }
            if (keyH.pause && !isPaused) {
                System.out.println("Pausing game...");
                isPaused = true;
                resumeMenu();
            }
        }
        else if (!keyH.pause && isPaused) { isPaused = false; }

        else if (gameState == GameState.GAME_OVER) {
            drawGameOver(g2);

            if (keyH.re) {
                gameReset();
            }
        }

        else if (gameState == GameState.YOU_WIN) {
            drawWin(g2);
            if (keyH.re) {
                msc.insertScoreIntoAllTimeScore(1600);
                gameReset();
            }
        }

        g2.dispose();

    }


    // paintComponent FINISH THE MAP TO GENERATE NEW MAP


//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D) g;
//        MySQLConnection msc = new MySQLConnection();
//
//        if (backgroundImage != null) {
//            g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
//        }
//
//        if (gameState == GameState.PLAYING) {
//            tilem.draw(g2); //TILE DRAW
//            player.draw(g2);  //PLAYER DRAW
//            drawScore(g2);
//
//            if (keyH.re) {
//                if (msc.selectflag())loadgameReset();
//                else if (gameState == GameState.YOU_WIN) gameReset();
//            }
//            if (keyH.pause && !isPaused) {
//                System.out.println("Pausing game...");
//                isPaused = true;
//                resumeMenu();
//            }
//            else if (!keyH.pause && isPaused) { isPaused = false; }
//        }
//        else if (gameState == GameState.GAME_OVER) {
//            drawGameOver(g2);
//            if (keyH.re) {
//                if (msc.selectflag()) loadgameReset();
//                else loadgameReset();
//            }
//        }
//        else if (gameState == GameState.YOU_WIN) {
//            drawWin(g2);
//            if (keyH.re) gameReset();
//        }
//        g2.dispose();
//    }

}

package main;

import java.awt.*;
import javax.swing.*;

public class LoginSignup {

public static String u;

    public void LoginSignupMethod() {

        JFrame loginframe = new JFrame("Login / Sign Up");
        loginframe.setSize(1296, 864);
        loginframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginframe.setLocationRelativeTo(null);

        // Create the main panel with a background image
        JPanel mainPanel = new JPanel() {
            private final Image backgroundImage = new ImageIcon("C:\\Users\\bjorn\\IdeaProjects\\Labyrinth\\res\\tiles\\mainmenuback.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        mainPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for main layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components

        // Panel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false); // Make the title panel transparent
        JLabel title = new JLabel("Log in / Sign Up", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 60));
        title.setForeground(Color.orange);
        titlePanel.add(title);

        // Title GridBag Constraints
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;
        titleGbc.gridwidth = 2; // Title spans both columns
        titleGbc.insets = new Insets(-350, 10, 10, 10); // Custom insets for title

        // Add the title panel to the main panel
        mainPanel.add(titlePanel, titleGbc);

        // Panel for the form (username, password, buttons)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for the form layout
        formPanel.setOpaque(false); // Make form panel transparent

        // Username label and text field
        JLabel userlabel = new JLabel("Username:");
        userlabel.setForeground(Color.orange);
        userlabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0; // Username row
        formPanel.add(userlabel, gbc);

        JTextField userText = new JTextField(12);
        userText.setCaretColor(Color.orange);
        gbc.gridx = 1;
        gbc.gridy = 0;
        userText.setPreferredSize(new Dimension(200, 30));
        formPanel.add(userText, gbc);

        // Password label and password field
        JLabel passwordlabel = new JLabel("Password:");
        passwordlabel.setForeground(Color.orange);
        passwordlabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 1; // Password row
        formPanel.add(passwordlabel, gbc);

        JPasswordField passwordText = new JPasswordField(12);
        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordText.setPreferredSize(new Dimension(200, 30));
        formPanel.add(passwordText, gbc);

        // Login button
        JButton loginbutton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2; // Login button row
        loginbutton.setPreferredSize(new Dimension(100, 30));
        formPanel.add(loginbutton, gbc);
        loginbutton.setFocusPainted(false);

        // Register button
        JButton registerbutton = new JButton("Sign Up");
        gbc.gridx = 1;
        gbc.gridy = 2; // Register button row
        registerbutton.setPreferredSize(new Dimension(100, 30));
        formPanel.add(registerbutton, gbc);
        registerbutton.setFocusPainted(false);

        // Form GridBag Constraints
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.gridx = 0;
        formGbc.gridy = 1; // Start after the title panel
        formGbc.gridwidth = 2; // Form spans both columns
        formGbc.insets = new Insets(10, 50, 10, 50); // Padding for the form panel

        // Add the form panel to the main panel
        mainPanel.add(formPanel, formGbc);

        // Add the main panel to the frame
        loginframe.add(mainPanel);

        // Set frame visibility
        loginframe.setVisible(true);

        // Add action listener for the login button
        loginbutton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());

            MySQLConnection msc = new MySQLConnection();

            if (msc.selectCredintials(username, password)) {
                JOptionPane.showMessageDialog(null, "You have successfully logged in!");
                u = username;
                loginframe.dispose();
                GamePanel gamePanel = new GamePanel();
                gamePanel.mainMenu();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password!");
            }
        });

        registerbutton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out both fields!");
                return;
            }

            MySQLConnection msc = new MySQLConnection();

            if (msc.selectUsername(username)) {
                JOptionPane.showMessageDialog(null, "Sign Up failed! Username might already exist!");
            } else {
                // If username doesn't exist, attempt to insert the new user
                if (msc.insertUser(username, password)) {
                    JOptionPane.showMessageDialog(null, "User successfully registered!");
                } else {
                    JOptionPane.showMessageDialog(null, "Sign Up failed! Please try again later.");
                }
            }
        });
    }
    public static String getU() {
            return u;
    }
}

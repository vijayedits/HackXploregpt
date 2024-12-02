package com.hackxplore;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class RegisterScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public RegisterScreen() {
        setTitle("HackXplore - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setResizable(false); // Fixed size
        setLocationRelativeTo(null); // Center window on screen

        // Background Panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set background image
                ImageIcon backgroundImage = new ImageIcon("resources/register_bg.jpg");
                Image image = backgroundImage.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridLayout(4, 1, 10, 10));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("New Username"));
        backgroundPanel.add(usernameField);

        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("New Password"));
        backgroundPanel.add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        backgroundPanel.add(registerButton);

        messageLabel = new JLabel("", JLabel.CENTER);
        messageLabel.setForeground(Color.RED);
        backgroundPanel.add(messageLabel);

        add(backgroundPanel);
        setVisible(true);
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Both fields are required.");
            return;
        }

        try (Connection con = Database.connect("sqlite//users.db")) {
            PreparedStatement pst = con.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            pst.setString(1, username);
            pst.setString(2, password);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "User registered successfully!");
            new LoginScreen(); // Return to login screen
            dispose(); // Close register screen
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setText("Error registering user.");
        }
    }
}

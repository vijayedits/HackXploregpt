package com.hackxplore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JButton registerButton;

    public LoginScreen() {
    	try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        setTitle("HackXplore - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setResizable(false); // Fixed size
        setLocationRelativeTo(null); // Center the window

        // Set app icon
        ImageIcon appIcon = new ImageIcon("resources/logo.JPG");
        setIconImage(appIcon.getImage());

        // Background Panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set background image
                ImageIcon backgroundImage = new ImageIcon("resources/login_bg.jpg");
                Image image = backgroundImage.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Logo Panel
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false); // Transparent to show background

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("resources/logo.JPG");
        Image logoImage = logoIcon.getImage().getScaledInstance(150, 80, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(logoImage));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel appNameLabel = new JLabel("HackXplore", JLabel.CENTER);
        appNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        appNameLabel.setForeground(Color.BLACK); // Adjust text color based on background

        logoPanel.add(logoLabel, BorderLayout.CENTER);
        logoPanel.add(appNameLabel, BorderLayout.SOUTH);
        backgroundPanel.add(logoPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        centerPanel.setOpaque(false); // Transparent to show background
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        centerPanel.add(usernameField);

        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        centerPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this::handleLogin);
        centerPanel.add(loginButton);

        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new GridLayout(2, 1));
        footerPanel.setOpaque(false);

        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        footerPanel.add(errorLabel);

        registerButton = new JButton("Don't have an account? Create New");
        registerButton.setVisible(false); // Initially hidden
        registerButton.addActionListener(e -> {
            new RegisterScreen();
            dispose();
        });
        footerPanel.add(registerButton);

        backgroundPanel.add(footerPanel, BorderLayout.SOUTH);

        add(backgroundPanel);
        setVisible(true);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection con = Database.connect("sqlite//users.db")) {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                new HackathonScreen(); // Open hackathon screen
                dispose(); // Close login screen
            } else {
                errorLabel.setText("Invalid credentials. Please try again.");
                registerButton.setVisible(true); // Show register button
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errorLabel.setText("Error connecting to the database.");
        }
    }

   
}

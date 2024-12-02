package com.hackxplore;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class HackathonScreen extends JFrame {
    public HackathonScreen() {
        setTitle("HackXplore - Upcoming Hackathons");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Fixed size
        setResizable(false); // Prevent resizing
        setLocationRelativeTo(null); // Center on screen

        // Set app icon
        ImageIcon appIcon = new ImageIcon("resources/logo.JPG");
        setIconImage(appIcon.getImage());

        // Background Panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set background image
                ImageIcon backgroundImage = new ImageIcon("resources/bg_image.jpg");
                Image image = backgroundImage.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false); // Transparent to show background

        JLabel headerLabel = new JLabel("Upcoming Hackathons", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.BLACK); // Adjust based on background
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        String[] columnNames = {"S.No", "College Name", "Hackathon Name", "Date", "Place"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        JTable hackathonTable = new JTable(tableModel);
        hackathonTable.setRowHeight(30); // Row height
        hackathonTable.setFont(new Font("Arial", Font.BOLD, 14));
        hackathonTable.setFillsViewportHeight(true);
        hackathonTable.setOpaque(false); // Make table background transparent
        hackathonTable.getTableHeader().setOpaque(false); // Make header transparent

        // Table Header Customization
        JTableHeader tableHeader = hackathonTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 16));
        tableHeader.setBackground(new Color(72, 61, 139)); // Dark Slate Blue
        tableHeader.setForeground(Color.WHITE);

        // Alternate Row Colors
        hackathonTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(240, 240, 240) : Color.WHITE);
                } else {
                    c.setBackground(new Color(135, 206, 250)); // Light Blue for selected row
                }
                return c;
            }
        });

        // Set transparent background for table cells
        hackathonTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(new Color(0, 0, 0, 0)); // Transparent background
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(hackathonTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Populate Table from Database
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:C:/sqlite/hackathons.db")) {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM hackathons");

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("Sno"),
                        rs.getString("college_name"),
                        rs.getString("hackathon_name"),
                        rs.getString("date"),
                        rs.getString("place")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer Panel (Optional: Add a Back Button)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);

        JButton backButton = new JButton("Logout");
        backButton.addActionListener(e -> {
            new LoginScreen();
            dispose();
        });
        footerPanel.add(backButton);

        backgroundPanel.add(footerPanel, BorderLayout.SOUTH);

        add(backgroundPanel);
        setVisible(true);
    }

 
}

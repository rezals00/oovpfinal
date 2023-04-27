/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS;

/**
 *
 * @author erlanggafauzan
 */
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreditsDialog extends JDialog {
    public CreditsDialog() {
        setTitle("About");
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel headerLabel = new JLabel("Credits");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(headerLabel);
        contentPane.add(headerPanel, BorderLayout.PAGE_START);

        JPanel bodyPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        bodyPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        contentPane.add(bodyPanel, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel("Name: Erlangga Fauzan Rezagani");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bodyPanel.add(nameLabel);

        JLabel emailLabel = new JLabel("Email: erlangga.rezagani@president.ac.id");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bodyPanel.add(emailLabel);

        JLabel idLabel = new JLabel("Student ID: 001202200080");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bodyPanel.add(idLabel);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        contentPane.add(footerPanel, BorderLayout.PAGE_END);

        JLabel closeLabel = new JLabel("Close");
        closeLabel.setForeground(new Color(41, 128, 185));
        closeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        closeLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        footerPanel.add(closeLabel);
    }

    public static void main(String[] args) {
        CreditsDialog dialog = new CreditsDialog();
        dialog.setVisible(true);
    }
}


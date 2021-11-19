package co.in.ui;

import co.in.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

@Component
public class CoinFrame extends JFrame {

    @Autowired
    private CoinService coinService;

    /**
     * Creates new form DemoFrame
     */
    public CoinFrame() {
        initComponents();
    }

    private void initComponents() {

        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Coin!");

        /////////Text fields
        JPanel group1 = new JPanel();
        group1.setLayout(new GridLayout(5,3));

        JTextField sessionIdTextField = new JTextField();
        sessionIdTextField.setColumns(30);
        sessionIdTextField.setToolTipText("Ici mettre le SessionID / Authorization token");
        group1.add(sessionIdTextField);

        JTextField positionXTextField = new JTextField();
        sessionIdTextField.setColumns(30);
        positionXTextField.setToolTipText("Pos X");
        group1.add(positionXTextField);

        JTextField positionYTextField = new JTextField();
        sessionIdTextField.setColumns(30);
        positionYTextField.setToolTipText("Pos Y");
        group1.add(positionYTextField);

        JButton button = new JButton();
        button.setText("Coin !");
        button.addActionListener(this::buttonActionPerformed);
        group1.add(button);

        add(group1);
    }

    private void buttonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            coinService.coin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
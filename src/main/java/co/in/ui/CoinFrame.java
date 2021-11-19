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

    JTextField sessionIdTextField;
    JTextField positionYTextField;
    JTextField positionXTextField;
    JTextField color1TextField;
    JTextField color2TextField;

    private void initComponents() {

        setSize(300, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Coin!");

        /////////Text fields
        JPanel group1 = new JPanel();
        group1.setLayout(new GridLayout(12,3));

        sessionIdTextField = new JTextField();
        sessionIdTextField.setColumns(30);
        sessionIdTextField.setToolTipText("Ici mettre le SessionID / Authorization token");
        group1.add(sessionIdTextField);

        positionXTextField = new JTextField();
        sessionIdTextField.setColumns(30);
        positionXTextField.setToolTipText("Pos X (ex : 85");
        group1.add(positionXTextField);

        positionYTextField = new JTextField();
        sessionIdTextField.setColumns(30);
        positionYTextField.setToolTipText("Pos Y (ex : 87");
        group1.add(positionYTextField);

        color1TextField = new JTextField();
        color1TextField.setColumns(30);
        color1TextField.setToolTipText("Couleur du corps du canard (ex : #FFFFFF )");
        group1.add(color1TextField);

        color2TextField = new JTextField();
        color2TextField.setColumns(30);
        color2TextField.setToolTipText("Couleur du bec (ex : #000000 )");
        group1.add(color2TextField);

        JButton button = new JButton();
        button.setText("1 duck !");
        button.addActionListener(this::buttonActionPerformed);
        group1.add(button);

        JButton buttonManyDuckRight = new JButton();
        buttonManyDuckRight.setText("20 ducks ->!");
        buttonManyDuckRight.addActionListener(this::buttonManyDuckRightActionPerformed);
        group1.add(buttonManyDuckRight);

        JButton buttonManyDuckLeft = new JButton();
        buttonManyDuckLeft.setText("20 ducks <- !");
        buttonManyDuckLeft.addActionListener(this::buttonManyDuckLeftActionPerformed);
        group1.add(buttonManyDuckLeft);

        add(group1);
    }

    private void buttonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (sessionIdTextField.getText() != null && !sessionIdTextField.getText().isEmpty() &&
                    positionXTextField.getText() != null && !positionXTextField.getText().isEmpty() &&
                    positionYTextField.getText() != null && !positionYTextField.getText().isEmpty() &&
                    color1TextField.getText() != null && !color1TextField.getText().isEmpty() &&
                    color2TextField.getText() != null && !color2TextField.getText().isEmpty()
            ) {
                coinService.drawOneDuck(sessionIdTextField.getText(), Integer.parseInt(positionXTextField.getText()), Integer.parseInt(positionYTextField.getText()), color1TextField.getText(), color2TextField.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buttonManyDuckRightActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (sessionIdTextField.getText() != null && !sessionIdTextField.getText().isEmpty() &&
                    positionXTextField.getText() != null && !positionXTextField.getText().isEmpty() &&
                    positionYTextField.getText() != null && !positionYTextField.getText().isEmpty() &&
                    color1TextField.getText() != null && !color1TextField.getText().isEmpty() &&
                    color2TextField.getText() != null && !color2TextField.getText().isEmpty()
            ) {
                coinService.draw20DucksToTheRight(sessionIdTextField.getText(), Integer.parseInt(positionXTextField.getText()), Integer.parseInt(positionYTextField.getText()), color1TextField.getText(), color2TextField.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buttonManyDuckLeftActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (sessionIdTextField.getText() != null && !sessionIdTextField.getText().isEmpty() &&
                    positionXTextField.getText() != null && !positionXTextField.getText().isEmpty() &&
                    positionYTextField.getText() != null && !positionYTextField.getText().isEmpty() &&
                    color1TextField.getText() != null && !color1TextField.getText().isEmpty() &&
                    color2TextField.getText() != null && !color2TextField.getText().isEmpty()
            ) {
                coinService.draw20DucksToTheLeft(sessionIdTextField.getText(), Integer.parseInt(positionXTextField.getText()), Integer.parseInt(positionYTextField.getText()), color1TextField.getText(), color2TextField.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
package co.in.ui;

import co.in.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
    JFileChooser fileChooser;
    JButton fileSelector;
    File file;

    private void initComponents() {

        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Coin!");

        /////////Text fields
        JPanel group1 = new JPanel();
        group1.setLayout(new GridLayout(12,6));

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

        fileChooser = new JFileChooser();
        fileSelector = new JButton();
        fileSelector.setText("select image");
        fileSelector.addActionListener(this::buttonFileSelected);
        group1.add(fileSelector);

        JButton drawImage = new JButton();
        drawImage.setText("Draw image");
        drawImage.addActionListener(this::buttonImageActionPerformed);
        group1.add(drawImage);

        JButton buttonManyDuckRight = new JButton();
        buttonManyDuckRight.setText("repeat 20 ->");
        buttonManyDuckRight.addActionListener(this::buttonRepeatImageRight);
        group1.add(buttonManyDuckRight);

        JButton buttonManyDuckLeft = new JButton();
        buttonManyDuckLeft.setText("repeat 20 <- ");
        buttonManyDuckLeft.addActionListener(this::buttonRepeatImageLeft);
        group1.add(buttonManyDuckLeft);

        add(group1);
    }

    private void buttonFileSelected(java.awt.event.ActionEvent evt) {
        int returnVal = fileChooser.showOpenDialog(CoinFrame.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
    }

    private void buttonRepeatImageRight(java.awt.event.ActionEvent evt) {
        try {
            if (sessionIdTextField.getText() != null && !sessionIdTextField.getText().isEmpty() &&
                    positionXTextField.getText() != null && !positionXTextField.getText().isEmpty() &&
                    positionYTextField.getText() != null && !positionYTextField.getText().isEmpty()
            ) {
                BufferedImage image = ImageIO.read(file);
                int imageWidth = image.getWidth();
                int positionX = Integer.parseInt(positionXTextField.getText());
                int positionY = Integer.parseInt(positionYTextField.getText());
                for (int i = 0; i < 20; i++) {
                    coinService.drawOneImage(sessionIdTextField.getText(), positionX, positionY, file);
                    positionX = positionX + imageWidth + 10;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buttonRepeatImageLeft(java.awt.event.ActionEvent evt) {
        try {
            if (sessionIdTextField.getText() != null && !sessionIdTextField.getText().isEmpty() &&
                    positionXTextField.getText() != null && !positionXTextField.getText().isEmpty() &&
                    positionYTextField.getText() != null && !positionYTextField.getText().isEmpty()
            ) {
                BufferedImage image = ImageIO.read(file);
                int imageWidth = image.getWidth();
                int positionX = Integer.parseInt(positionXTextField.getText());
                int positionY = Integer.parseInt(positionYTextField.getText());
                for (int i = 0; i < 20; i++) {
                    coinService.drawOneImage(sessionIdTextField.getText(), positionX, positionY, file);
                    positionX = positionX - imageWidth - 10;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buttonImageActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (sessionIdTextField.getText() != null && !sessionIdTextField.getText().isEmpty() &&
                    positionXTextField.getText() != null && !positionXTextField.getText().isEmpty() &&
                    positionYTextField.getText() != null && !positionYTextField.getText().isEmpty() &&
                    fileChooser.isFileSelectionEnabled()
            ) {
                coinService.drawOneImage(sessionIdTextField.getText(), Integer.parseInt(positionXTextField.getText()), Integer.parseInt(positionYTextField.getText()), file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
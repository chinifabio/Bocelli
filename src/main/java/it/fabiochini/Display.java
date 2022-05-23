package it.fabiochini;

import javax.swing.*;
import java.awt.*;

public class Display {

    private final JFrame frame = new JFrame();
    private final JLabel label = new JLabel();

    public static Display create() {
        final Display display = new Display();

        display.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        display.frame.setAlwaysOnTop(true);
        JPanel content = new JPanel(new GridBagLayout());
        content.setPreferredSize(new Dimension(600, 600));
        content.setBackground(Color.BLACK);

        display.label.setForeground(Color.WHITE);
        display.label.setFont(Font.decode("consolas").deriveFont(Font.BOLD, 30));

        content.add(display.label);
        display.frame.setContentPane(content);

        display.frame.pack();
        display.frame.setVisible(true);
        return display;
    }

    public void updateText(String text) {
        label.setText(text);
    }

}

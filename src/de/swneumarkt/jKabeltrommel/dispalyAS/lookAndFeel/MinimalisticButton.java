package de.swneumarkt.jKabeltrommel.dispalyAS.lookAndFeel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by derduke on 31.05.16.
 */
public class MinimalisticButton extends JButton {
    private final Color defaultColor = Color.lightGray;
    private final Color selectedColor = Color.WHITE;

    public MinimalisticButton(String name) {
        super(name);
        setBackground(defaultColor);
    }

    public void setSelected(boolean selected) {
        if (selected) {
            setBackground(selectedColor);
        } else {
            setBackground(defaultColor);
        }
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return ((MinimalisticButton) obj).getText().equals(getText());
        } catch (Exception e) {
            return false;
        }
    }
}

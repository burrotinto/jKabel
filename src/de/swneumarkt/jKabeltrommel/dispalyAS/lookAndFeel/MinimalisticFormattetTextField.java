package de.swneumarkt.jKabeltrommel.dispalyAS.lookAndFeel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by derduke on 01.06.16.
 */
public class MinimalisticFormattetTextField extends JTextField {
    public MinimalisticFormattetTextField(String text, int anz) {
        super(text, anz);
        init();
    }

    public MinimalisticFormattetTextField(String text) {
        super(text);
        init();
    }

    public MinimalisticFormattetTextField() {
        super();
        init();
    }

    private void init() {
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });
    }

}

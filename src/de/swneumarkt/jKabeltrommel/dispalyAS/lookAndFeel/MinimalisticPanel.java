package de.swneumarkt.jKabeltrommel.dispalyAS.lookAndFeel;

import de.swneumarkt.jKabeltrommel.dispalyAS.DisplayAAS;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Florian Klinger on 29.06.16.
 */
public class MinimalisticPanel extends JPanel {

    public MinimalisticPanel() {
        super();
        setOpaque(false);
        setBackground(DisplayAAS.BACKGROUND);
    }

    public MinimalisticPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        setBackground(DisplayAAS.BACKGROUND);
    }
}

package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.scanAS;

import com.onbarcode.barcode.Code128;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by derduke on 31.05.16.
 */
public class ScanAAS extends JDialog implements ActionListener, AutoCloseable {
    public ScanAAS(int materialnummer, int m, int ba, String lagerplatz) throws Exception {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(new ScanPanel(ba + ""));
        add(new JLabel(m + " m"));
        if (lagerplatz == null || lagerplatz.equals("")) {
            add(new ScanPanel("" + materialnummer));
        } else {
            add(new ScanPanel(lagerplatz + " " + materialnummer));
        }
        pack();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(!isVisible());
    }

    @Override
    public void close() throws Exception {
        dispose();
    }

    private class ScanPanel extends JPanel {
        private String text;

        ScanPanel(String text) {
            this.text = text;
            setPreferredSize(new Dimension(text.length() * 20 + 100, 60));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Code128 baCode = new Code128();
            baCode.setData(text);
            try {
                baCode.drawBarcode((Graphics2D) g, new Rectangle(0, 0, getHeight(), getWidth()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

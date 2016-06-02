package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.lieferantCreateAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.lieferantAuswahlAS.LieferantenAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.lookAndFeel.MinimalisticButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by derduke on 24.05.16.
 */
public class LieferantCreateAAS extends JDialog implements ActionListener {
    private final IDBWrapper db;
    private final LieferantenAuswahlAAS lAAS;

    private JTextField name = new JTextField();
    private JButton cancel = new MinimalisticButton("Abbruch");
    private JButton create = new MinimalisticButton("Erstellen");


    public LieferantCreateAAS(IDBWrapper db, LieferantenAuswahlAAS lAAS) {
        this.db = db;
        this.lAAS = lAAS;
        setLayout(new BorderLayout());
        add(new JLabel("Lieferant hinzufügen"), BorderLayout.NORTH);

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 2));
        p.add(new JLabel("Name:"));
        p.add(name);
        add(p, BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout());
        south.add(cancel);
        south.add(create);
        add(south, BorderLayout.SOUTH);

        cancel.addActionListener(this);
        create.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
        pack();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == create) {
            try {
                db.createLieferant(name.getText());
                lAAS.hastToUpdate();
            } catch (Exception x) {
                //TODO
            }
        }

        dispose();

    }
}
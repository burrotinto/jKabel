package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.kabeltypCreateAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.kabelTypAuswahlAS.KabelTypAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.lookAndFeel.MinimalisticButton;
import de.swneumarkt.jKabeltrommel.dispalyAS.lookAndFeel.MinimalisticFormattetTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by derduke on 22.05.16.
 */
public class KabelTypCreateAAS extends JDialog implements ActionListener {
    private final IDBWrapper db;
    private JTextField matNummer = new MinimalisticFormattetTextField();
    private JTextField typ = new JTextField();
    private JButton cancel = new MinimalisticButton("Abbruch");
    private JButton create = new MinimalisticButton("Erstellen");
    private KabelTypAuswahlAAS kabelTypAuswahlAAS;

    public KabelTypCreateAAS(IDBWrapper db, KabelTypAuswahlAAS kabelTypAuswahlAAS) {
        this.db = db;
        this.kabelTypAuswahlAAS = kabelTypAuswahlAAS;
        setLayout(new BorderLayout());
        add(new JLabel("Kabeltyp erstellen"), BorderLayout.NORTH);

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 2));
        p.add(new JLabel("Materialnummer:"));
        p.add(matNummer);
        p.add(new JLabel("Bezeichnung:"));
        p.add(typ);
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
                db.createKabeltyp(typ.getText(), Integer.parseInt(matNummer.getText()));

            } catch (Exception x) {
                //TODO
            }
        }
        kabelTypAuswahlAAS.repaint();
        kabelTypAuswahlAAS.revalidate();

        dispose();

    }
}

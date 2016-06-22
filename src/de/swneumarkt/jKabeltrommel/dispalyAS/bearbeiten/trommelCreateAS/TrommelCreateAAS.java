package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.trommelCreateAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IKabeltypE;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.lieferantAuswahlAS.LieferantenAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.trommelAuswahlAS.TrommelAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.lookAndFeel.MinimalisticFormattetTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by derduke on 23.05.16.
 */
public class TrommelCreateAAS extends JDialog implements ActionListener {
    private final IDBWrapper db;
    private JTextField trommelNummer = new JTextField();
    private JTextField laenge = new MinimalisticFormattetTextField();
    private JTextField lieferscheinNr = new JTextField();
    private JTextField lagerPlatz = new JTextField();
    private JTextField start = new MinimalisticFormattetTextField();

    private JButton cancel = new JButton("Abbruch");
    private JButton create = new JButton("Erstellen");
    private TrommelAuswahlAAS auswahlAAS;
    private IKabeltypE typ;

    private LieferantenAuswahlAAS lieferantenAuswahlAAS;

    public TrommelCreateAAS(IDBWrapper db, IKabeltypE typ, TrommelAuswahlAAS auswahlAAS) {
        this.db = db;
        this.typ = typ;
        this.auswahlAAS = auswahlAAS;
        setLayout(new BorderLayout());
        createPanel();

        cancel.addActionListener(this);
        create.addActionListener(this);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private void createPanel() {
        add(new JLabel("Trommel erstellen"), BorderLayout.NORTH);

        JPanel auswahl = new JPanel();

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(5, 2));
        p.add(new JLabel("Trommelnummer:"));
        p.add(trommelNummer);

        p.add(new JLabel("Gesamtlänge:"));
        p.add(laenge);

        p.add(new JLabel("Anfang:"));
        p.add(start);
        start.setText("0");

        p.add(new JLabel("Lagerplatz:"));
        p.add(lagerPlatz);
        
        p.add(new JLabel("Lieferscheinnummer:"));
        p.add(lieferscheinNr);

        auswahl.add(p);

        lieferantenAuswahlAAS = new LieferantenAuswahlAAS(db);
        auswahl.add(lieferantenAuswahlAAS);

        add(auswahl, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout());
        south.add(cancel);
        south.add(create);
        add(south, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int startW = 0;
        try {
            startW = Integer.parseInt(start.getText());
        } catch (NumberFormatException nE) {
        }
        try {
            if (!db.createTrommel(typ, trommelNummer.getText(), Integer.parseInt(laenge.getText()), lagerPlatz.getText(), startW, lieferantenAuswahlAAS.getAuswahl(), System.currentTimeMillis(), lieferscheinNr.getText())) {
                throw new Exception();
            } else {
                auswahlAAS.repaint();
                auswahlAAS.revalidate();
                dispose();
            }
        } catch (NumberFormatException nfe) {
            laenge.setSelectionColor(Color.red);
            laenge.setText("ERROR");
            laenge.setSelectionStart(0);
            laenge.setSelectionEnd(laenge.getText().length() - 1);
        } catch (Exception x) {

        }


    }
}

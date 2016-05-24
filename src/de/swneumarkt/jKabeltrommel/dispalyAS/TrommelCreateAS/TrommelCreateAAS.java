package de.swneumarkt.jKabeltrommel.dispalyAS.TrommelCreateAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.GeliefertE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.TrommelE;
import de.swneumarkt.jKabeltrommel.dispalyAS.LieferantAuswahlAS.LieferantenAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.TrommelAuswahlAS.TrommelAuswahlAAS;

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
    private JTextField laenge = new JTextField();
    private JTextField lieferscheinNr = new JTextField();
    private JTextField lagerPlatz = new JTextField();
    private JTextField start = new JTextField();

    private JButton cancel = new JButton("Abbruch");
    private JButton create = new JButton("Erstellen");
    private TrommelAuswahlAAS auswahlAAS;
    private KabeltypE typ;

    private LieferantenAuswahlAAS lieferantenAuswahlAAS;

    public TrommelCreateAAS(IDBWrapper db, KabeltypE typ, TrommelAuswahlAAS auswahlAAS) {
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
        p.add(new JLabel("Lagerplatz:"));
        p.add(lagerPlatz);
        p.add(new JLabel("Lieferscheinnummer:"));
        p.add(lieferscheinNr);

        auswahl.add(p);

        lieferantenAuswahlAAS = new LieferantenAuswahlAAS(db);
        auswahl.add(lieferantenAuswahlAAS);

        add(auswahl,BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout());
        south.add(cancel);
        south.add(create);
        add(south, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == create) {
            try {
//                KabeltypE kabelTyp, String trommelnummer, long date, int gesamtlaenge
                db.create(new TrommelE(typ, trommelNummer.getText(), Integer.parseInt(laenge.getText()),lagerPlatz.getText(),Integer.parseInt(start.getText())), lieferantenAuswahlAAS.getAuswahl(), new GeliefertE(System.currentTimeMillis(),lieferscheinNr.getText(),lieferantenAuswahlAAS.getAuswahl().getId()));
            } catch (Exception x) {
                //TODO
            }
        }
        auswahlAAS.repaint();
        auswahlAAS.revalidate();

        dispose();

    }
}

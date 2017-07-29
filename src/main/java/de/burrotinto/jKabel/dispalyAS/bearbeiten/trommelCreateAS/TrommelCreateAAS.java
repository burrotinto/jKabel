/*
 * jKabel - Ein hochperfomantes, extremstanpassungsfähiges Mehrbenutzersystem zur erfassung von Kabelstrecken
 *
 * Copyright (C) 2016 Florian Klinger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelCreateAS;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.lieferantAuswahlAS.LieferantenAuswahlAAS;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelAuswahlAS.TrommelAuswahlAAS;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticFormattetTextField;
import de.burrotinto.usefull.list.SortedSetAnzahlDerEingefuegtenElemente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.function.Consumer;

/**
 *  Startet den Anwendungsfall zum erstellen einer Trommel
 *
 * Created by Florian Klinger on 23.05.16.
 */
public class TrommelCreateAAS extends JDialog implements ActionListener {
    private final IDBWrapper db;
    private JTextField trommelNummer = new JTextField();
    private JTextField laenge = new MinimalisticFormattetTextField();
    private JTextField lieferscheinNr = new JTextField();
    private JComboBox<String> lagerPlatzComboBox = null;
    private JTextField start = new MinimalisticFormattetTextField();

    private JButton cancel = new JButton("Abbruch");
    private JButton create = new JButton("Erstellen");
    private TrommelAuswahlAAS auswahlAAS;
    private IKabeltypE typ;

    private LieferantenAuswahlAAS lieferantenAuswahlAAS;

    private Vector<String> lagerPlatzVektor = null;

    /**
     * Erstellen des Anwendungsfalles zum erstellen einer neuen Trommel
     *
     * @param db         Datenbank die dazu benutzt werden soll
     * @param typ        Der Kabeltyp der auf die Trommel gespulöt wurde
     * @param auswahlAAS welcher Trommelauswahlanwendungsfall nach dem erstellen neu gezeichnet werden soll
     */
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
        lagerPlatzComboBox = getLagerPlatzComboBox();
        lagerPlatzComboBox.setEditable(true);
        p.add(lagerPlatzComboBox);

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

    private JComboBox<String> getLagerPlatzComboBox() {
        return new JComboBox<String>(getLagerPlaetze());
    }

    private Vector<String> getLagerPlaetze() {
        if (lagerPlatzVektor == null) {
            SortedSetAnzahlDerEingefuegtenElemente<String> sort = new SortedSetAnzahlDerEingefuegtenElemente<>();
            typ.getTrommeln().forEach(new Consumer<ITrommelE>() {
                @Override
                public void accept(ITrommelE iTrommelE) {
                    sort.add(iTrommelE.getLagerPlatz());
                }
            });
            lagerPlatzVektor = new Vector<String>(sort);
            lagerPlatzVektor.add(0, "");
        }
        return lagerPlatzVektor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel) {
            dispose();
        } else {
            int startW;
            try {
                startW = Integer.parseInt(start.getText());
            } catch (NumberFormatException nE) {
                startW = 0;
            }
            try {
                if (db.createTrommel(typ, trommelNummer.getText(), Integer.parseInt(laenge.getText()), (String) lagerPlatzComboBox.getSelectedItem(), startW, lieferantenAuswahlAAS.getAuswahl(), System.currentTimeMillis(), lieferscheinNr.getText())) {
                    auswahlAAS.repaint();
                    auswahlAAS.revalidate();
                    dispose();
                }
            } catch (NumberFormatException nfe) {
                laenge.setSelectionColor(Color.red);
                laenge.setText("ERROR");
                laenge.setSelectionStart(0);
                laenge.setSelectionEnd(laenge.getText().length() - 1);
            }
        }
    }
}

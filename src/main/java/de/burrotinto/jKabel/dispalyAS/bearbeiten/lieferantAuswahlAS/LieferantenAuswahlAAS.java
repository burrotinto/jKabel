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

package de.burrotinto.jKabel.dispalyAS.bearbeiten.lieferantAuswahlAS;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ILieferantE;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.lieferantCreateAS.LieferantCreateAAS;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Startet den Anwendungsfall zum Auswahl eines Liferanten
 * Created by Florian Klinger on 24.05.16.
 */
public class LieferantenAuswahlAAS extends JPanel implements ActionListener {
    private final IDBWrapper db;
    private JComboBox<ILieferantE> cBox;
    private JButton neuButt = new MinimalisticButton("Neuer Lieferant");
    private LieferantenAuswahlK kontroll;

    public LieferantenAuswahlAAS(IDBWrapper db){
        kontroll  = new LieferantenAuswahlK(db);
        this.db = db;
        create();
    }

    private void create(){
        JPanel panel = new JPanel(new GridLayout(2,1));
        JPanel p = new JPanel();
        p.add(new JLabel("Lieferantenauswahl:"));
        cBox = new JComboBox<>(kontroll.getLieferantenSorted());
        cBox.setEditable(true);
        p.add(cBox);
        panel.add(p);
//        panel.add(neuButt);
        neuButt.addActionListener(this);
        add(panel);
    }

    public ILieferantE getAuswahl() {
        if (cBox.getSelectedItem() instanceof String) {
            db.createLieferant(cBox.getSelectedItem().toString());
            return kontroll.getLieferantByName(cBox.getSelectedItem().toString());
        }
        return (ILieferantE) cBox.getSelectedItem();
    }

    public void hastToUpdate() {
        removeAll();
        create();
        repaint();
        revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new LieferantCreateAAS(db,this);
    }
}

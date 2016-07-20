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

package de.burrotinto.jKabel.dispalyAS.bearbeiten.kabeltypCreateAS;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.kabelTypAuswahlAS.KabelTypAuswahlAAS;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticButton;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticFormattetTextField;

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
                kabelTypAuswahlAAS.typSelected(db.getTypByMaterialnummer(Integer.parseInt(matNummer.getText())));
            } catch (Exception x) {
                //TODO
            }
        } else {

        }
        dispose();


    }
}

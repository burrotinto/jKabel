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

package de.burrotinto.jKabel.dispalyAS.bearbeiten.lieferantCreateAS;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.lieferantAuswahlAS.LieferantenAuswahlAAS;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticButton;

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
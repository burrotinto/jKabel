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

package de.burrotinto.jKabel.dispalyAS.search;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS.StreckenAAS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by derduke on 25.05.16.
 */
public class SearchAAS extends JPanel implements ActionListener {
    private final SearchK kontroller;
    private final IDBWrapper db;
    private JButton searchButt = new JButton("suchen");
    private JTextField baFied;
    private JComboBox<IKabeltypE> cBox;
    private JScrollPane ergebnis = new JScrollPane();

    public SearchAAS(IDBWrapper db) {
        this.kontroller = new SearchK(db);
        searchButt.addActionListener(this);
        this.db = db;
        setLayout(new BorderLayout());
        add(getEingabePanel(), BorderLayout.WEST);

    }

    private JPanel getEingabePanel() {
        JPanel s = new JPanel(new GridLayout(3, 2));
        s.add(new JLabel("Suchen:"));
        s.add(searchButt);
        s.add(new JLabel("BA:"));
        baFied = new JTextField(8);
        s.add(baFied);
        s.add(new JLabel("Kabeltyp"));
        cBox = new JComboBox<>(kontroller.getAllKAbelTypen());
        s.add(cBox);
        JPanel p = new JPanel();
        p.add(s);
        return p;
    }

    private JPanel getErgebnisPanel(List<ITrommelE> trommeln) {
        JPanel s = new JPanel(new GridLayout(trommeln.size(), 1));
        for (ITrommelE t : trommeln) {
            StreckenAAS nachweis = new StreckenAAS(db);
            s.add(new JScrollPane(nachweis));
            nachweis.trommelAusgewaehlt(t);
        }
        return s;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ergebnis = new JScrollPane(getErgebnisPanel(kontroller.getAllTrommelWithBA(Integer.parseInt(baFied.getText()), (IKabeltypE) cBox.getSelectedItem())));
        add(ergebnis, BorderLayout.CENTER);
        repaint();
        revalidate();
    }
}

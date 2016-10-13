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

package de.burrotinto.jKabel.dispalyAS.search.trommelByNummer;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS.StreckenAAS;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticButton;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * Created by derduke on 13.10.16.
 */
public class SearchTrommelNrAAS extends MinimalisticPanel implements KeyListener, ActionListener {
    private SearchTrommelNrK kontroll;
    private JTextField tf = new JTextField(9);
    private JPanel ergebnis = new JPanel();
    private StreckenAAS streckenPanel;

    public SearchTrommelNrAAS(StreckenAAS streckenPanel, IDBWrapper db) {
        super();
        kontroll = new SearchTrommelNrK(db);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        this.streckenPanel = streckenPanel;
        JPanel p = new JPanel();
        p.add(new JLabel("Trommelnummer"));
        p.add(tf);
        tf.addKeyListener(this);
        add(p);
        add(ergebnis);
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        ergebnis.removeAll();
        List<ITrommelE> trommeln = kontroll.getListOfTrommeln(tf.getText());
        for (ITrommelE t : trommeln) {
            JButton b = new MinimalisticButton(t.getTrommelnummer());
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    streckenPanel.trommelAusgewaehlt(t.getId());
                }
            });
            ergebnis.add(b);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        setVisible(!isVisible());
    }
}

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

package de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelAuswahlAS;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.kabelTypAuswahlAS.IKabelTypListner;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelCreateAS.TrommelCreateAAS;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticButton;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by derduke on 22.05.16.
 */
public class TrommelAuswahlAAS extends JPanel implements IKabelTypListner, ActionListener {
    private TommelAuswahlK kontroll;
    private MinimalisticButton addNewButt = new MinimalisticButton("Neue Trommel");
    private IKabeltypE typ = null;
    private IDBWrapper db;
    private boolean zeiheAlle;
    private MinimalisticButton ausgewaehlt = null;

    private HashMap<MinimalisticButton, Integer> buttonTrommelMap = new HashMap<>();

    private Set<ITrommelListner> trommelListners = new HashSet<>();

    public TrommelAuswahlAAS(IDBWrapper db, boolean zeiheAlle) {
        kontroll = new TommelAuswahlK(db);
        this.db = db;
        addNewButt.addActionListener(this);
        this.zeiheAlle = zeiheAlle;
    }

    public void addTrommelListner(ITrommelListner listner) {
        trommelListners.add(listner);
    }

    private void buildPanel(IKabeltypE typ) {
        if (typ != null) {
            this.typ = typ;
            JPanel panel = new MinimalisticPanel(new GridLayout(kontroll.getAllTrommelForTyp(typ).size() + 1, 1));
            JPanel p = new MinimalisticPanel();

            p.add(addNewButt);
            panel.add(p);

            for (ITrommelE t : kontroll.getAllTrommelForTyp(typ)) {
                if (zeiheAlle || !(t.isFreigemeldet() && kontroll.getRestMeter(t) == 0)) {
                    p = new MinimalisticPanel(new FlowLayout(FlowLayout.LEFT));
                    MinimalisticButton b = new MinimalisticButton(t.getTrommelnummer() + "");
                    b.setSelected(b.equals(ausgewaehlt));
                    buttonTrommelMap.put(b, t.getId());
                    p.add(b);
                    JLabel label;
                    if (t.isFreigemeldet() && kontroll.getRestMeter(t) == 0) {
                        label = new JLabel("Beendet");
                    } else if (!kontroll.isAusserHaus(t)) {
                        label = new JLabel("Noch: " + kontroll.getRestMeter(t) + " m" + (t.isFreigemeldet() ? " - Bund" : ""));
                        if (kontroll.getRestMeter(t) == 0) {
                            p.setBackground(Color.GRAY);
                            p.setOpaque(true);
                        }

                    } else {
                        label = new JLabel("Bei: " + kontroll.getBaustelle(t));
                        p.setBackground(Color.ORANGE);
                        p.setOpaque(true);
                    }
                    p.add(label);
                    if (t.isFreigemeldet()) {
                        p.setBackground(Color.RED);
                        p.setOpaque(true);
                    }


                    b.addActionListener(this);
                    panel.add(p);
                }
            }
            add(panel);
        }
    }


    @Override
    public void typSelected(IKabeltypE typ) {
        if (this.typ == null || !this.typ.equals(typ)) {
            removeAll();
            buildPanel(typ);
            repaint();
            revalidate();
            buttonTrommelMap.get(ausgewaehlt);
            trommelListners.forEach(iTrommelListner -> iTrommelListner.trommelAusgewaehlt(buttonTrommelMap.get(ausgewaehlt)));
        }
    }

    @Override
    public void revalidate() {
        removeAll();
        if (kontroll != null) {
            buildPanel(typ != null ? kontroll.getNewTypCopy(typ) : kontroll.getNewTypCopy(buttonTrommelMap.get(ausgewaehlt)));
        }
        repaint();
        super.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (buttonTrommelMap.containsKey(e.getSource())) {
            ausgewaehlt = (MinimalisticButton) e.getSource();
            buttonTrommelMap.keySet().forEach(minimalisticButton -> minimalisticButton.setSelected(minimalisticButton == ausgewaehlt));
            trommelListners.forEach(iTrommelListner -> iTrommelListner.trommelAusgewaehlt(buttonTrommelMap.get(e.getSource())));
        } else {
            if (e.getSource() == addNewButt) {
                new TrommelCreateAAS(db, typ, this);
            }
        }
    }


    public void setZeiheAlle(boolean zeiheAlle) {
        this.zeiheAlle = zeiheAlle;
    }

//    @Override
//    public void removeAll() {
//        super.removeAll();
//        ausgewaehlt = null;
//    }
}

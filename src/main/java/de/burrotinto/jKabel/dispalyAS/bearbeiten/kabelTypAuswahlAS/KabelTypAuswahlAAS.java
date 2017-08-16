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

package de.burrotinto.jKabel.dispalyAS.bearbeiten.kabelTypAuswahlAS;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dispalyAS.UpdateSet;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.kabeltypCreateAS.KabelTypCreateAAS;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticButton;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticPanel;
import de.burrotinto.jKabel.eventDriven.events.TrommelSelectEvent;
import reactor.bus.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@org.springframework.stereotype.Component
public class KabelTypAuswahlAAS extends JPanel implements ActionListener, IKabelTypListner, reactor.fn.Consumer<reactor.bus.Event<TrommelSelectEvent>> {
    private final IDBWrapper db;
    private KabelTypAuswahlK kontroll;
    private HashMap<MinimalisticButton, Integer> buttonsMatNr = new HashMap<>();
    private Set<IKabelTypListner> kabelTypLIstners = new HashSet<IKabelTypListner>();
    private MinimalisticButton addNewButt = new MinimalisticButton("Neuer Kabeltyp");
    private IKabeltypE selected = null;


    public KabelTypAuswahlAAS(IDBWrapper db, UpdateSet updateSet, KabelTypAuswahlK kontroll) {
        super();
        updateSet.getSet().add(this);
        this.kontroll = kontroll;
        addKabelTypListner(this);
        buildPanel();
        this.db = db;
        addNewButt.addActionListener(this);

    }

    private void buildPanel() {
        if (kontroll != null) {
            List<IKabeltypE> list = kontroll.getTypen();
            buttonsMatNr = new HashMap<>();
            JPanel panel = new MinimalisticPanel(new FlowLayout(FlowLayout.RIGHT));

            panel.setLayout(new GridLayout(list.size() + 1, 1));
            KabelTypAuswahlAAS kta = this;


            list.forEach(new Consumer<IKabeltypE>() {
                @Override
                public void accept(IKabeltypE iKabeltypE) {
                    JPanel p = new MinimalisticPanel(new FlowLayout(FlowLayout.RIGHT));

                    MinimalisticButton b = new MinimalisticButton(iKabeltypE.getMaterialNummer() + "");
                    buttonsMatNr.put(b, iKabeltypE.getMaterialNummer());
                    p.add(new JLabel(iKabeltypE.getTyp()));
                    p.add(b);
                    b.addActionListener(kta);

                    // Buttonfarbe wählen
                    b.setSelected(iKabeltypE.equals(selected));


                    panel.add(p);
                }
            });
            panel.add(addNewButt);
            add(panel);
        }
    }


    public void addKabelTypListner(IKabelTypListner listner) {
        kabelTypLIstners.add(listner);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addNewButt) {
            new KabelTypCreateAAS(db, this);
        } else {
            IKabeltypE typ = kontroll.getTyp(buttonsMatNr.get(e.getSource()));
            buttonsMatNr.keySet().forEach(new Consumer<MinimalisticButton>() {
                @Override
                public void accept(MinimalisticButton jButton) {
                    jButton.setSelected(false);
                }
            });
            ((MinimalisticButton) e.getSource()).setSelected(true);

            kabelTypLIstners.forEach(new Consumer<IKabelTypListner>() {
                @Override
                public void accept(IKabelTypListner IKabelTypListner) {
                    IKabelTypListner.typSelected(typ);
                }
            });
        }
    }


    @Override
    public void typSelected(IKabeltypE typ) {
        selected = typ;
        removeAll();
        buildPanel();
        repaint();
        revalidate();
    }

    public IKabeltypE getSelected() {
        return selected;
    }

    @Override
    public void accept(Event<TrommelSelectEvent> trommelSelectEventEvent) {
        for (Map.Entry<MinimalisticButton, Integer> entry : buttonsMatNr.entrySet()) {
            entry.getKey().setSelected(entry.getValue() == db.getTrommelByID(trommelSelectEventEvent.getData()
                    .getTrommelId()).getMaterialNummer());
        }
    }
}

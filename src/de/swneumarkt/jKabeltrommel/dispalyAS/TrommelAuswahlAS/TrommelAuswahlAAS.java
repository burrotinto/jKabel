package de.swneumarkt.jKabeltrommel.dispalyAS.TrommelAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.TrommelE;
import de.swneumarkt.jKabeltrommel.dispalyAS.KabelTypAuswahlAS.IKabelTypListner;
import de.swneumarkt.jKabeltrommel.dispalyAS.TrommelCreateAS.TrommelCreateAAS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by derduke on 22.05.16.
 */
public class TrommelAuswahlAAS extends JPanel implements IKabelTypListner, ActionListener {
    private TommelAuswahlK kontroll;
    private JButton addNewButt = new JButton("Neue Trommel");
    private KabeltypE typ = null;
    private IDBWrapper db;

    private HashMap<JButton, TrommelE> buttonTrommelMap;

    private Set<ITrommelListner> trommelListners = new HashSet<>();

    public void addTrommelListner(ITrommelListner listner) {
        trommelListners.add(listner);
    }

    public TrommelAuswahlAAS(IDBWrapper db) {
        kontroll = new TommelAuswahlK(db);
        this.db = db;
        addNewButt.addActionListener(this);
    }

    private void buildPanel(KabeltypE typ) {
        this.typ = typ;
        buttonTrommelMap = new HashMap<>();
        JPanel panel = new JPanel(new GridLayout(kontroll.getAllTrommelForMatNr(typ).size() + 1,1));
        JPanel p = new JPanel();
        p.add(addNewButt);
        panel.add(p);

        for (TrommelE t : kontroll.getAllTrommelForMatNr(typ)) {
            p = new JPanel();
            JButton b = new JButton(t.getTrommelnummer() + "");
            buttonTrommelMap.put(b, t);
            p.add(b);
            if(!kontroll.isAusserHaus(t)) {
                p.add(new JLabel("Noch: " + kontroll.getRestMeter(t) + " m"));
            } else {
                p.add(new JLabel("Bei: " + kontroll.getBaustelle(t)) );
            }

            b.addActionListener(this);
            panel.add(p);
        }
        add(panel);
    }


    @Override
    public void typSelected(KabeltypE typ) {
        removeAll();
        buildPanel(typ);
        repaint();
        revalidate();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (buttonTrommelMap.containsKey(e.getSource())) {
            trommelListners.forEach(new Consumer<ITrommelListner>() {
                @Override
                public void accept(ITrommelListner iTrommelListner) {
                    iTrommelListner.trommelAusgewaehlt(buttonTrommelMap.get(e.getSource()));
                }
            });
        } else {
            if (e.getSource() == addNewButt) {
                new TrommelCreateAAS(db, typ, this);
            }
        }
    }


}

package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.TrommelAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IKabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.ITrommelE;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.KabelTypAuswahlAS.IKabelTypListner;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.TrommelCreateAS.TrommelCreateAAS;

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
    private IKabeltypE typ = null;
    private IDBWrapper db;
    private boolean auchFreigemeldete;

    private HashMap<JButton, ITrommelE> buttonTrommelMap;

    private Set<ITrommelListner> trommelListners = new HashSet<>();

    public TrommelAuswahlAAS(IDBWrapper db, boolean auchFreigemeldete) {
        kontroll = new TommelAuswahlK(db);
        this.db = db;
        addNewButt.addActionListener(this);
        this.auchFreigemeldete = auchFreigemeldete;
    }

    public void addTrommelListner(ITrommelListner listner) {
        trommelListners.add(listner);
    }

    private void buildPanel(IKabeltypE typ) {
        if (typ != null) {
            this.typ = typ;
            buttonTrommelMap = new HashMap<>();
            JPanel panel = new JPanel(new GridLayout(kontroll.getAllTrommelForMatNr(typ).size() + 1, 1));
            JPanel p = new JPanel();

//            JPanel tLP = new JPanel(new FlowLayout(FlowLayout.CENTER));
//            JLabel tL = new JLabel(typ.toString());
//            tLP.setBorder(new LineBorder(Color.BLACK));
//            tLP.add(tL);
//            tLP.setBackground(Color.BLUE);
//            panel.add(tLP);

            p.add(addNewButt);
            panel.add(p);

            for (ITrommelE t : kontroll.getAllTrommelForMatNr(typ)) {
                if (auchFreigemeldete || !t.isFreigemeldet()) {
                    p = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JButton b = new JButton(t.getTrommelnummer() + "");
                    buttonTrommelMap.put(b, t);
                    p.add(b);
                    if (!kontroll.isAusserHaus(t)) {
                        p.add(new JLabel("Noch: " + kontroll.getRestMeter(t) + " m"));
                    } else {
                        p.add(new JLabel("Bei: " + kontroll.getBaustelle(t)));
                        p.setBackground(Color.ORANGE);
                    }
                    if (t.isFreigemeldet()) {
                        p.setBackground(Color.RED);
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

    @Override
    public void repaint() {
        super.repaint();
        removeAll();
        buildPanel(typ);
    }

    public void setAuchFreigemeldete(boolean auchFreigemeldete) {
        this.auchFreigemeldete = auchFreigemeldete;
    }
}

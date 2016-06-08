package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.trommelAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IKabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.ITrommelE;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.kabelTypAuswahlAS.IKabelTypListner;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.trommelCreateAS.TrommelCreateAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.lookAndFeel.MinimalisticButton;

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
    private MinimalisticButton addNewButt = new MinimalisticButton("Neue Trommel");
    private IKabeltypE typ = null;
    private IDBWrapper db;
    private boolean zeiheAlle;
    private MinimalisticButton ausgewaehlt = null;

    private HashMap<MinimalisticButton, ITrommelE> buttonTrommelMap;

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
            buttonTrommelMap = new HashMap<>();
            JPanel panel = new JPanel(new GridLayout(kontroll.getAllTrommelForMatNr(typ).size() + 1, 1));
            JPanel p = new JPanel();

            p.add(addNewButt);
            panel.add(p);

            for (ITrommelE t : kontroll.getAllTrommelForMatNr(typ)) {
                if (zeiheAlle || !(t.isFreigemeldet() && kontroll.getRestMeter(t) == 0)) {
                    p = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    MinimalisticButton b = new MinimalisticButton(t.getTrommelnummer() + "");
                    b.setSelected(b.equals(ausgewaehlt));
                    buttonTrommelMap.put(b, t);
                    p.add(b);
                    JLabel label;
                    if (t.isFreigemeldet() && kontroll.getRestMeter(t) == 0) {
                        label = new JLabel("Beendet");
                    } else if (!kontroll.isAusserHaus(t)) {
                        label = new JLabel("Noch: " + kontroll.getRestMeter(t) + " m" + (t.isFreigemeldet() ? " - Bund" : ""));
                        if (kontroll.getRestMeter(t) == 0) {
                            p.setBackground(Color.GRAY);
                        }

                    } else {
                        label = new JLabel("Bei: " + kontroll.getBaustelle(t));
                        p.setBackground(Color.ORANGE);
                    }
                    p.add(label);
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
        if (this.typ == null || !this.typ.equals(typ)) {
            removeAll();
            buildPanel(typ);
            repaint();
            revalidate();
        }
    }

    @Override
    public void revalidate() {
        removeAll();
        buildPanel(typ);
        repaint();
        super.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (buttonTrommelMap.containsKey(e.getSource())) {
            ausgewaehlt = (MinimalisticButton) e.getSource();
            buttonTrommelMap.keySet().forEach(new Consumer<MinimalisticButton>() {
                @Override
                public void accept(MinimalisticButton minimalisticButton) {
                    minimalisticButton.setSelected(minimalisticButton == ausgewaehlt);
                }
            });
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


    public void setZeiheAlle(boolean zeiheAlle) {
        this.zeiheAlle = zeiheAlle;
    }

    @Override
    public void removeAll() {
        super.removeAll();
        ausgewaehlt = null;
    }
}

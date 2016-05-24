package de.swneumarkt.jKabeltrommel.dispalyAS.KabelTypAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;
import de.swneumarkt.jKabeltrommel.dispalyAS.KabeltypCreateAS.KabelTypCreateAAS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by derduke on 21.05.16.
 */
public class KabelTypAuswahlAAS extends JPanel implements ActionListener, IKabelTypListner {
    private KabelTypAuswahlK kontroll;
    private HashMap<JButton, Integer> buttonsMatNr = new HashMap<>();
    private Set<IKabelTypListner> kabelTypLIstners = new HashSet<IKabelTypListner>();
    private JButton addNewButt = new JButton("Neuer Kabeltyp");
    private final IDBWrapper db;

    public KabelTypAuswahlAAS(IDBWrapper db) {
        kontroll = new KabelTypAuswahlK(db);
        addKabelTypListner(this);
        buildPanel();
        this.db = db;
        addNewButt.addActionListener(this);
    }

    private void buildPanel() {
        if(kontroll != null) {
            HashMap<Integer, String> namen = kontroll.getTypenMap();
            buttonsMatNr = new HashMap<>();
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(namen.size() + 1, 1));
            KabelTypAuswahlAAS kta = this;
            namen.forEach(new BiConsumer<Integer, String>() {
                @Override
                public void accept(Integer integer, String s) {
                    JPanel p = new JPanel();

                    JButton b = new JButton(integer + "");
                    buttonsMatNr.put(b, integer);
                    p.add(new JLabel(s));
                    p.add(b);
                    b.addActionListener(kta);
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
            KabeltypE typ = kontroll.getTyp(buttonsMatNr.get(e.getSource()));
            kabelTypLIstners.forEach(new Consumer<IKabelTypListner>() {
                @Override
                public void accept(IKabelTypListner IKabelTypListner) {
                    IKabelTypListner.typSelected(typ);
                }
            });
        }
    }


    @Override
    public void typSelected(KabeltypE typ) {

    }

    @Override
    public void revalidate() {
        removeAll();
        buildPanel();
        super.revalidate();
    }
}

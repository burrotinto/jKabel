package de.swneumarkt.jKabeltrommel.dispalyAS.KabelTypAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;

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

    public KabelTypAuswahlAAS(IDBWrapper db) {
        kontroll = new KabelTypAuswahlK(db);
        addKabelTypListner(this);
        buildPanel();
    }

    private void buildPanel() {
        HashMap<Integer, String> namen = kontroll.getTypenMap();
        buttonsMatNr = new HashMap<>();
        setLayout(new GridLayout(namen.size() + 1, 1));
        KabelTypAuswahlAAS panel = this;
        namen.forEach(new BiConsumer<Integer, String>() {
            @Override
            public void accept(Integer integer, String s) {
                JPanel p = new JPanel();

                JButton b = new JButton(integer + "");
                buttonsMatNr.put(b, integer);
                p.add(new JLabel(s));
                p.add(b);
                b.addActionListener(panel);
                panel.add(p);
            }
        });

        JPanel p =  new JPanel();
        p.add(addNewButt);
        add(p);
    }

    @Override
    public void print(Graphics g) {
        super.print(g);


    }

    public void addKabelTypListner(IKabelTypListner listner) {
        kabelTypLIstners.add(listner);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addNewButt) {
            //TODO
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
        public void typSelected (KabeltypE typ){

        }
    }

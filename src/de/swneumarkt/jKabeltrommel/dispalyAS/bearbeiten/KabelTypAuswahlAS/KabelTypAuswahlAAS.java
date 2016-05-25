package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.KabelTypAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IKabeltypE;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.KabeltypCreateAS.KabelTypCreateAAS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by derduke on 21.05.16.
 */
public class KabelTypAuswahlAAS extends JPanel implements ActionListener, IKabelTypListner {
    private final IDBWrapper db;
    private KabelTypAuswahlK kontroll;
    private HashMap<JButton, Integer> buttonsMatNr = new HashMap<>();
    private Set<IKabelTypListner> kabelTypLIstners = new HashSet<IKabelTypListner>();
    private JButton addNewButt = new JButton("Neuer Kabeltyp");

    public KabelTypAuswahlAAS(IDBWrapper db) {
        kontroll = new KabelTypAuswahlK(db);
        addKabelTypListner(this);
        buildPanel();
        this.db = db;
        addNewButt.addActionListener(this);
    }

    private void buildPanel() {
        if(kontroll != null) {
            List<IKabeltypE> list = kontroll.getTypen();
            buttonsMatNr = new HashMap<>();
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panel.setLayout(new GridLayout(list.size() + 1, 1));
            KabelTypAuswahlAAS kta = this;


            Collections.sort(list, new Comparator<IKabeltypE>() {
                @Override
                public int compare(IKabeltypE o1, IKabeltypE o2) {
                    return o1.getMaterialNummer() - o2.getMaterialNummer();
                }
            });

            list.forEach(new Consumer<IKabeltypE>() {
                @Override
                public void accept(IKabeltypE iKabeltypE) {
                    JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));

                    JButton b = new JButton(iKabeltypE.getMaterialNummer() + "");
                    buttonsMatNr.put(b, iKabeltypE.getMaterialNummer());
                    p.add(new JLabel(iKabeltypE.getTyp()));
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
            IKabeltypE typ = kontroll.getTyp(buttonsMatNr.get(e.getSource()));
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

    }

    @Override
    public void repaint() {
        super.repaint();
        removeAll();
        buildPanel();
    }
}

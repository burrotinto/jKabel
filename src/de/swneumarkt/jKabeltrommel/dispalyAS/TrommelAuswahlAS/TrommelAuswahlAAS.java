package de.swneumarkt.jKabeltrommel.dispalyAS.TrommelAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.TrommelE;
import de.swneumarkt.jKabeltrommel.dispalyAS.KabelTypAuswahlAS.IKabelTypListner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created by derduke on 22.05.16.
 */
public class TrommelAuswahlAAS extends JPanel implements IKabelTypListner, ActionListener {
    private TommelAuswahlK kontroll;
    private JButton addNewButt = new JButton("Neue Trommel");

    private HashMap<JButton, TrommelE> buttonTrommelMap;

    public TrommelAuswahlAAS(IDBWrapper db) {
        kontroll = new TommelAuswahlK(db);
    }

    private void buildPanel(KabeltypE typ) {
        buttonTrommelMap = new HashMap<>();
        setLayout(new GridLayout(buttonTrommelMap.size() + 1, 1));
        JPanel p = new JPanel();
        p.add(addNewButt);
        add(p);
        for (TrommelE t : kontroll.getAllTrommelForMatNr(typ)) {
            p = new JPanel();
            JButton b = new JButton(t.getTrommelnummer() + " (" + kontroll.getRestMeter(t) + ")");
            buttonTrommelMap.put(b, t);
            p.add(b);
            b.addActionListener(this);
            add(p);
        }

    }


    @Override
    public void typSelected(KabeltypE typ) {
        System.out.println(typ.toString());
        removeAll();
        buildPanel(typ);
        repaint();
        revalidate();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(buttonTrommelMap.get(e.getSource()).getTrommelnummer());
    }
}

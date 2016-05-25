package de.swneumarkt.jKabeltrommel.dispalyAS.LieferantAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.ILieferantE;
import de.swneumarkt.jKabeltrommel.dispalyAS.LieferantCreateAS.LieferantCreateAAS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by derduke on 24.05.16.
 */
public class LieferantenAuswahlAAS extends JPanel implements ActionListener {
    private final IDBWrapper db;
    private JComboBox<ILieferantE> cBox;
    private JButton neuButt = new JButton("Neuer Lieferant");
    private LieferantenAuswahlK kontroll;

    public LieferantenAuswahlAAS(IDBWrapper db){
        kontroll  = new LieferantenAuswahlK(db);
        this.db = db;
        create();
    }

    private void create(){
        JPanel panel = new JPanel(new GridLayout(2,1));
        JPanel p = new JPanel();
        p.add(new JLabel("Lieferantenauswahl:"));
        cBox = new JComboBox<>(kontroll.getLieferanten());
        p.add(cBox);
        panel.add(p);
        panel.add(neuButt);
        neuButt.addActionListener(this);
        add(panel);
    }

    public ILieferantE getAuswahl() {
        return (ILieferantE) cBox.getSelectedItem();
    }

    public void hastToUpdate() {
        removeAll();
        create();
        repaint();
        revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new LieferantCreateAAS(db,this);
    }
}

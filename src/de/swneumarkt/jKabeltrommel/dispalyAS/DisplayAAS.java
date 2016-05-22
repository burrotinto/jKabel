package de.swneumarkt.jKabeltrommel.dispalyAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.DBAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dispalyAS.KabelTypAuswahlAS.KabelTypAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.TrommelAuswahlAS.TrommelAuswahlAAS;

import javax.swing.*;
import java.awt.*;

/**
 * Created by derduke on 22.05.16.
 */
public class DisplayAAS {


    public static void main(String[] args) {
        JFrame f = new JFrame();
        IDBWrapper db = new DBAuswahlAAS().getDBWrapper();
        KabelTypAuswahlAAS k = new KabelTypAuswahlAAS(db);
        TrommelAuswahlAAS t = new TrommelAuswahlAAS(db);

        k.addKabelTypListner(t);
        f.getContentPane().add(new JScrollPane(k), BorderLayout.WEST);
        f.getContentPane().add(new JScrollPane(t), BorderLayout.CENTER);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }
}

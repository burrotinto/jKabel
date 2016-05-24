package de.swneumarkt.jKabeltrommel.dispalyAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.DBAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.TrommelE;
import de.swneumarkt.jKabeltrommel.dispalyAS.KabelTypAuswahlAS.IKabelTypListner;
import de.swneumarkt.jKabeltrommel.dispalyAS.KabelTypAuswahlAS.KabelTypAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.StreckenAS.StreckenAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.TrommelAuswahlAS.ITrommelListner;
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
        StreckenAAS s = new StreckenAAS(db);

        k.addKabelTypListner(t);
        k.addKabelTypListner(s);

        t.addTrommelListner(s);

        k.addKabelTypListner(new IKabelTypListner() {
            @Override
            public void typSelected(KabeltypE typ) {
                f.repaint();
                f.revalidate();
            }
        });
        t.addTrommelListner(new ITrommelListner() {
            @Override
            public void trommelAusgewaehlt(TrommelE trommel) {
                f.repaint();
                f.revalidate();
            }
        });

        f.getContentPane().setLayout(new GridLayout(1,2));
        JPanel l = new JPanel(new GridLayout(1,2));
        l.add(new JScrollPane(k));
        l.add(new JScrollPane(t));
        f.getContentPane().add(l);
        f.getContentPane().add(new JScrollPane(s));

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setVisible(true);


    }


}

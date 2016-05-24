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
public class DisplayAAS extends JFrame{


    private  IDBWrapper db;

    public void setDb(IDBWrapper db) {
        this.db = db;
    }

    public static void main(String[] args) {
        DisplayAAS f = new DisplayAAS();
        f.setTitle("jKabeltrommel");
        IDBWrapper db = new DBAuswahlAAS().getDBWrapper();
        if(db == null){
            f.setLayout(new FlowLayout());
            f.add(new JLabel("Es wurde bereits eine Instanz auf einem anderen Rechner geöffnet. Mehrbenutzerbetrieb ist aktuell nicht möglich.(Es steht kein Server zur verfügung)"));
            f.add(new JLabel("Wenn !!!sicher!!! ist das kein anderer auf dedr DB arbeitet die lock.lck Datei löschen"));
            f.pack();
        }else {
            f.setDb(db);
            f.getContentPane().setLayout(new BorderLayout());
            f.getContentPane().add(f.getBearbeitenPanel(),BorderLayout.CENTER);
            f.getContentPane().add(new JLabel("proudly made by Florian Klinger"),BorderLayout.SOUTH);
            f.getContentPane().add(new JLabel(";-)"),BorderLayout.NORTH);
            f.setSize(1400, 640);
            f.setMinimumSize(new Dimension(480, 480));
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        }
    }

    public JPanel getBearbeitenPanel(){
        KabelTypAuswahlAAS k = new KabelTypAuswahlAAS(db);
        TrommelAuswahlAAS t = new TrommelAuswahlAAS(db);
        StreckenAAS s = new StreckenAAS(db);

        k.addKabelTypListner(t);
        k.addKabelTypListner(s);

        t.addTrommelListner(s);

        k.addKabelTypListner(new IKabelTypListner() {
            @Override
            public void typSelected(KabeltypE typ) {
                repaint();
                revalidate();
            }
        });
        t.addTrommelListner(new ITrommelListner() {
            @Override
            public void trommelAusgewaehlt(TrommelE trommel) {
                repaint();
                revalidate();
            }
        });

        JPanel l = new JPanel(new GridLayout(1, 2));
        JPanel all =new JPanel(new GridLayout(1, 2));
        l.add(new JScrollPane(k));
        l.add(new JScrollPane(t));
        all.add(l);
        all.add(new JScrollPane(s));
        return all;
    }
}

package de.swneumarkt.jKabeltrommel.dispalyAS.StreckenAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.StreckeE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.TrommelE;
import de.swneumarkt.jKabeltrommel.dispalyAS.KabelTypAuswahlAS.IKabelTypListner;
import de.swneumarkt.jKabeltrommel.dispalyAS.TrommelAuswahlAS.ITrommelListner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by derduke on 22.05.16.
 */
public class StreckenAAS extends JPanel implements ITrommelListner, ActionListener, IKabelTypListner {
    private final StreckenK kontroller;
    private JTextField trommelnummerField, datumField, laengeField, typField, matNrField, baField, startField, endField, ortField, lagerplatzField,trommelstartField;
    private List<Abgang> abgaenge;
    private JButton create = new JButton("Eintragen");
    private JButton update = new JButton("Ändern");
    private TrommelE trommel = null;
    private KabeltypE typ = null;


    public StreckenAAS(IDBWrapper db) {
        kontroller = new StreckenK(db);
        create.addActionListener(this);
        update.addActionListener(this);
    }

    @Override
    public void trommelAusgewaehlt(TrommelE trommel) {
        removeAll();
        buildPanel(trommel);
        repaint();
        revalidate();
    }

    private void buildPanel(TrommelE trommel) {
        this.trommel = trommel;
        List<StreckeE> strecken = kontroller.getStreckenForTrommel(trommel);
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(strecken.size() + 12, 1));

        // Überschrift
        JPanel uebers = new JPanel(new FlowLayout());
        uebers.add(new JLabel("Kabelnachweiß für Trommel:"));
        trommelnummerField = new JTextField(trommel.getTrommelnummer(), 16);
        uebers.add(trommelnummerField);
        p.add(uebers);

        //Datum
        JPanel date = new JPanel(new FlowLayout());
        date.add(new JLabel("Datum:"));
        datumField = new JTextField(kontroller.getTimeString(kontroller.getLieferDate(trommel)));
        datumField.setEditable(false);
        date.add(datumField);
        p.add(date);

        //Lieferant
        JPanel liefer = new JPanel(new FlowLayout());
        liefer.add(new JLabel("Lieferant:"));
        JTextField ltf = new JTextField(kontroller.getLieferant(trommel));
        ltf.setEditable(false);
        liefer.add(ltf);
        p.add(liefer);

        //Lieferscheinnummer
        JPanel lNR = new JPanel(new FlowLayout());
        lNR.add(new JLabel("Lieferscheinnummer:"));
        JTextField lnTF = new JTextField(kontroller.getLieferscheinNR(trommel));
        lnTF.setEditable(false);
        lNR.add(lnTF);
        p.add(lNR);


        //Länge
        JPanel laenge = new JPanel(new FlowLayout());
        laenge.add(new JLabel("Gesamtlänge:"));
        laengeField = new JTextField(trommel.getGesamtlaenge() + "", 6);
        laenge.add(laengeField);
        p.add(laenge);

        //Trommelanfang
        JPanel trommelP = new JPanel(new FlowLayout());
        trommelP.add(new JLabel("Trommelanfang:"));
        trommelstartField = new JTextField(trommel.getStart() + "", 6);
        trommelP.add(trommelstartField);
        p.add(trommelP);


        //Typ
        JPanel typ = new JPanel(new FlowLayout());
        typ.add(new JLabel("Kabelart:"));
        typField = new JTextField(kontroller.getTyp(trommel).getTyp(), 16);
        typ.add(typField);
        p.add(typ);

        //Lagerplatz
        JPanel lP = new JPanel(new FlowLayout());
        lP.add(new JLabel("Lagerplatz:"));
        lagerplatzField = new JTextField(trommel.getLagerPlatz(), 12);
        lP.add(lagerplatzField);
        p.add(lP);

        //MatNr
        JPanel matNr = new JPanel(new FlowLayout());
        matNr.add(new JLabel("MaterialNummer:"));
        matNrField = new JTextField(kontroller.getTyp(trommel).getMaterialNummer() + "");
        matNr.add(matNrField);
        matNrField.setEditable(false);
        p.add(matNr);

        // Beschriftung
        JPanel bes = new JPanel();
        JTextField tf = new JTextField("Datum", 10);
        tf.setEditable(false);
        bes.add(tf);
        tf = new JTextField("m", 4);
        tf.setEditable(false);
        bes.add(tf);
        tf = new JTextField("BA", 8);
        tf.setEditable(false);
        bes.add(tf);
        tf = new JTextField("Start", 4);
        tf.setEditable(false);
        bes.add(tf);
        tf = new JTextField("Ende", 4);
        tf.setEditable(false);
        bes.add(tf);
        tf = new JTextField("Ort", 16);
        tf.setEditable(false);
        bes.add(tf);
        bes.add(new JLabel("                          "));
        p.add(bes);

        String last = trommel.getStart()+"";
        abgaenge = new ArrayList<>();

        for (StreckeE s : strecken) {
            JPanel panel = new JPanel(new FlowLayout());
            JButton del = new JButton("Löschen");
            del.addActionListener(this);
            Abgang a = new Abgang(s, del);
            abgaenge.add(a);
            JTextField dT = new JTextField(kontroller.getTimeString(s.getVerlegedatum()), 10);
            dT.setEditable(false);
            panel.add(dT);
            JTextField tF = new JTextField(s.getMeter() + "", 4);
            tF.setEditable(false);
            panel.add(tF);
            panel.add(a.bA);
            panel.add(a.start);
            panel.add(a.ende);
            panel.add(a.text);
            panel.add(del);
            p.add(panel);
            last = s.getEnde() + "";
        }

        // Neuer Eintrag
        JPanel panel = new JPanel(new FlowLayout());
        JTextField dT = new JTextField(kontroller.getTimeString(System.currentTimeMillis()), 10);
        dT.setEditable(false);
        panel.add(dT);
        JTextField tF = new JTextField(4);
        tF.setEditable(false);
        panel.add(tF);
        baField = new JTextField("", 8);
        startField = new JTextField(last, 4);
        endField = new JTextField("", 4);
        ortField = new JTextField("", 16);
        panel.add(baField);
        panel.add(startField);
        panel.add(endField);
        panel.add(ortField);
        panel.add(create);
        p.add(panel);

        JPanel butt = new JPanel();
        butt.add(update);
        p.add(butt);

        add(p);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == create && trommel != null) {
            int ba = -1;
            int start = -1;
            int ende = -1;
            try {
                ba = Integer.parseInt(baField.getText());
            } catch (NumberFormatException ex) {
            }
            try {
                start = Integer.parseInt(startField.getText());
            } catch (NumberFormatException ex) {
            }
            try {
                ende = Integer.parseInt(endField.getText());
            } catch (NumberFormatException ex) {
            }
            if (!kontroller.richtigeRichtung(trommel, start, ende)) {

                // Dialog
                JDialog dialog = new JDialog();
                dialog.setLayout(new FlowLayout());
                dialog.add(new JLabel("Fasche Größen"));
                JButton butt = new JButton("OK");
                dialog.add(butt);
                dialog.setLocationRelativeTo(null);
                butt.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });
                dialog.pack();
                dialog.setVisible(true);

            } else {
                StreckeE strecke = new StreckeE(ba, ortField.getText(), System.currentTimeMillis(), start, ende, trommel);
                kontroller.eintragen(strecke);
            }
        } else {
            // Updates der Einträge
            if (e.getSource() == update) {
                for (Abgang a : abgaenge) {
                    StreckeE s = a.strecke;
                    s.setBa(Integer.parseInt(a.bA.getText()));
                    try {
                        s.setEnde(Integer.parseInt(a.ende.getText()));
                    } catch (NumberFormatException ex) {
                        s.setEnde(Integer.parseInt(a.start.getText()));
                    }
                    s.setStart(Integer.parseInt(a.start.getText()));
                    s.setOrt(a.text.getText());
                    kontroller.update(s);
                }

                trommel.setGesamtlaenge(Integer.parseInt(laengeField.getText()));
                trommel.setTrommelnummer(trommelnummerField.getText());
                trommel.setLagerPlatz(lagerplatzField.getText());
                trommel.setStart(Integer.parseInt(trommelstartField.getText()));
                kontroller.update(trommel);

                typ.setTyp(typField.getText());
                kontroller.update(typ);
            } else {
                StreckeE s = null;
                for (Abgang a : abgaenge) {
                    if (e.getSource() == a.butt) {
                        kontroller.remove(a.strecke);
                        break;
                    }
                }
            }

        }
        removeAll();
        buildPanel(trommel);
        repaint();
        revalidate();
    }

    @Override
    public void typSelected(KabeltypE typ) {
        removeAll();
        repaint();
        revalidate();

        this.typ = typ;
        kontroller.setTyp(typ);
    }

    private class Abgang {
        final StreckeE strecke;
        JTextField bA, start, ende, text;
        JButton butt;

        private Abgang(StreckeE strecke, JButton butt) {
            this.butt = butt;
            this.strecke = strecke;
            bA = new JTextField(strecke.getBa() + "", 8);
            start = new JTextField(strecke.getStart() + "", 4);
            ende = new JTextField(strecke.getEnde() + "", 4);
            text = new JTextField(strecke.getOrt() + "", 16);
        }
    }
}

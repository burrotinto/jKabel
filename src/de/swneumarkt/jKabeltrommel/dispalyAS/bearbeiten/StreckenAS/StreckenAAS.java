package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.StreckenAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.*;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.KabelTypAuswahlAS.IKabelTypListner;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.TrommelAuswahlAS.ITrommelListner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by derduke on 22.05.16.
 */
public class StreckenAAS extends JPanel implements ITrommelListner, ActionListener, IKabelTypListner, KeyListener {
    private final StreckenK kontroller;
    private final Set<JPanel> updateOnChange;
    private JTextField trommelnummerField, datumField, laengeField, typField, matNrField, baField, startField, endField, ortField, lagerplatzField,trommelstartField;
    private JCheckBox freiCheckBox;
    private List<Abgang> abgaenge;
    private JButton create = new JButton("Eintragen");
    private JButton update = new JButton("Ändern");
    private ITrommelE trommel = null;
    private IKabeltypE typ = null;
    private JComboBox<ILieferantE> cBox;


    public StreckenAAS(IDBWrapper db, Set<JPanel> updateOnChange) {
        this.updateOnChange = updateOnChange == null ? new HashSet<>() : updateOnChange;
        kontroller = new StreckenK(db);
        create.addActionListener(this);
        update.addActionListener(this);
//        setPreferredSize(new Dimension(680,680));

    }

    public StreckenAAS(IDBWrapper db) {
        this(db, null);
    }

    @Override
    public void trommelAusgewaehlt(ITrommelE trommel) {
        removeAll();
        buildPanel(trommel);
        repaint();
        revalidate();
    }

    private void buildPanel(ITrommelE trommel) {
        this.trommel = trommel;
        List<IStreckeE> strecken = kontroller.getStreckenForTrommel(trommel);
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(strecken.size() + 13, 1));

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
        Vector<ILieferantE> v = kontroller.getLieferanten();
        int pos = 0;
        for (int i = 0; i< v.size();i++){
            if (v.get(i).getId() == kontroller.getLiefer(trommel).getLieferant().getId()) {
                pos = i;
            }
        }
        cBox = new JComboBox<>(v);
        cBox.setSelectedIndex(pos);
        liefer.add(cBox);
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

        //Freimeldung
        JPanel frei = new JPanel(new FlowLayout());
        frei.add(new JLabel("Freimeldung:"));
        freiCheckBox = new JCheckBox("", trommel.isFreigemeldet());
        frei.add(freiCheckBox);
        p.add(frei);

        // Beschriftung
        JPanel bes = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        p.add(bes);

        String last = trommel.getStart()+"";
        abgaenge = new ArrayList<>();

        for (IStreckeE s : strecken) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        baField.addKeyListener(this);
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
                kontroller.eintragenStrecke(ba, ortField.getText(), System.currentTimeMillis(), start, ende, trommel);
            }
        } else {
            // Updates der Einträge
            if (e.getSource() == update) {
                for (Abgang a : abgaenge) {
                    IStreckeE s = a.strecke;
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
                trommel.setFreimeldung(freiCheckBox.isSelected());
                kontroller.update(trommel);

                IGeliefertE g = kontroller.getLiefer(trommel);
                g.setLieferantID(((ILieferantE) cBox.getSelectedItem()));
                kontroller.update(g);


                typ.setTyp(typField.getText());
                kontroller.update(typ);
            } else {
                IStreckeE s = null;
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
        updateOnChange.forEach(new Consumer<JPanel>() {
            @Override
            public void accept(JPanel jPanel) {
                jPanel.repaint();
                jPanel.revalidate();
            }
        });
    }

    @Override
    public void typSelected(IKabeltypE typ) {
        removeAll();
        repaint();
        revalidate();

        this.typ = typ;
        kontroller.setTyp(typ);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (ortField.getText().equals("")) {
            try {
                ortField.setText(kontroller.getTextForBA(Integer.parseInt(((JTextField) e.getSource()).getText())));
            } catch (Exception ex) {
            }
        }
    }

    private class Abgang {
        final IStreckeE strecke;
        JTextField bA, start, ende, text;
        JButton butt;

        private Abgang(IStreckeE strecke, JButton butt) {
            this.butt = butt;
            this.strecke = strecke;
            bA = new JTextField(strecke.getBa() + "", 8);
            start = new JTextField(strecke.getStart() + "", 4);
            ende = new JTextField(strecke.getEnde() + "", 4);
            text = new JTextField(strecke.getOrt() + "", 16);
        }
    }
}

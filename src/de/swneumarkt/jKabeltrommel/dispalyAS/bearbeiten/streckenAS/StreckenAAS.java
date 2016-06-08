package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.streckenAS;

import com.onbarcode.barcode.Code128;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.*;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.kabelTypAuswahlAS.IKabelTypListner;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.scanAS.ScanAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.trommelAuswahlAS.ITrommelListner;
import de.swneumarkt.jKabeltrommel.dispalyAS.lookAndFeel.MinimalisticButton;
import de.swneumarkt.jKabeltrommel.dispalyAS.lookAndFeel.MinimalisticFormattetTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by derduke on 22.05.16.
 */
public class StreckenAAS extends JPanel implements ITrommelListner, ActionListener, IKabelTypListner, KeyListener {
    private final StreckenK kontroller;
    private final Set<JPanel> updateOnChange;
    private JTextField trommelnummerField, datumField, laengeField, typField, matNrField, baField, startField, endField, ortField, lagerplatzField, trommelstartField;
    private JCheckBox freiCheckBox;
    private List<Abgang> abgaenge;
    private MinimalisticButton create = new MinimalisticButton("Eintragen");
    private MinimalisticButton update = new MinimalisticButton("Ändern");
    private ITrommelE trommel = null;
    private IKabeltypE typ = null;
    private JComboBox<ILieferantE> cBox;
    private List<JDialog> scanDialoge = new ArrayList<>();


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
        p.setLayout(new GridLayout(strecken.size() + (kontroller.istAusserHaus(trommel) ? 12 : 13), 1));

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
        for (int i = 0; i < v.size(); i++) {
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
        laengeField = new MinimalisticFormattetTextField(trommel.getGesamtlaenge() + "", 6);
        laenge.add(laengeField);
        p.add(laenge);

        //Trommelanfang
        JPanel trommelP = new JPanel(new FlowLayout());
        trommelP.add(new JLabel("Trommelanfang:"));
        trommelstartField = new MinimalisticFormattetTextField(trommel.getStart() + "", 6);
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
        matNrField = new MinimalisticFormattetTextField(kontroller.getTyp(trommel).getMaterialNummer() + "");
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
        bes.setBackground(Color.DARK_GRAY);
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

        String last = trommel.getStart() + "";
        abgaenge = new ArrayList<>();

        boolean next = false;
        for (IStreckeE s : strecken) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            if (next) panel.setBackground(Color.GRAY);

            next = !next;
            MinimalisticButton del = new MinimalisticButton("Löschen");
            del.addActionListener(this);
            Abgang a = new Abgang(s, del);
            abgaenge.add(a);
            JTextField dT = new JTextField(kontroller.getTimeString(s.getVerlegedatum()), 10);
            panel.add(dT);
            JTextField tF = new JTextField(s.getMeter() + "", 4);
            panel.add(tF);
            panel.add(a.bA);
            panel.add(a.start);
            panel.add(a.ende);
            panel.add(a.text);
            panel.add(del);
            try (ScanAAS scanAAS = new ScanAAS(trommel.getMaterialNummer(), s.getMeter(), s.getBa(), trommel.getLagerPlatz())) {
                scanAAS.setLocationRelativeTo(null);
                JButton button = new MinimalisticButton("scan");
                button.addActionListener(scanAAS);
                panel.add(button);
                scanDialoge.add(scanAAS);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Wenn Trommel ausser haus
            if (a.ende.getText().equals(Abgang.platzHalter)) {
                panel.setBackground(Color.ORANGE);
                tF.setText(Abgang.platzHalter);
            }

            dT.setEditable(false);
            tF.setEditable(false);

            p.add(panel);
            last = a.ende.getText();
        }

        // Neuer Eintrag
        if (!kontroller.istAusserHaus(trommel) && kontroller.getRestMeter(trommel) > 0) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setBackground(Color.white);
            JTextField dT = new JTextField(kontroller.getTimeString(System.currentTimeMillis()), 10);
            dT.setEditable(false);
            panel.add(dT);
            JTextField tF = new JTextField(4);
            tF.setEditable(false);
            panel.add(tF);
            baField = new MinimalisticFormattetTextField("", 8);
            startField = new MinimalisticFormattetTextField(last, 4);
            endField = new MinimalisticFormattetTextField("", 4);
            ortField = new JTextField("", 16);
            baField.addKeyListener(this);
            panel.add(baField);
            panel.add(startField);
            panel.add(endField);
            panel.add(ortField);
            panel.add(create);
            p.add(panel);
        }
        JPanel butt = new JPanel();
        butt.add(update);
        p.add(butt);

        add(p);
//        printBarcode((Graphics2D) p.getGraphics());
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
            if (ende >= 0 && !kontroller.richtigeRichtung(trommel, start, ende)) {

                // Dialog
                JDialog dialog = new JDialog();
                dialog.setTitle("Da stimmt was nicht");
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
                    try {
                        kontroller.update(a);
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }

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

    @Override
    public void removeAll() {
        super.removeAll();
        scanDialoge.forEach(new Consumer<JDialog>() {
            @Override
            public void accept(JDialog jDialog) {
                jDialog.dispose();
            }
        });
        scanDialoge = new ArrayList<>();
    }

    private void printBarcode(Graphics2D g) {
        Code128 baCode = new Code128();
        String s = matNrField.getText();
        if (!(lagerplatzField.getText() == null || lagerplatzField.getText().equals(""))) {
            s = lagerplatzField.getText() + s;
        }
        baCode.setData(s);
        try {
            baCode.drawBarcode(g, new Rectangle(matNrField.getX() + matNrField.getWidth(), matNrField.getY(), 20, 20));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Abgang implements FocusListener, IStreckeE, KeyListener {
        public static final String platzHalter = "---";
        private final IStreckeE strecke;
        private MinimalisticFormattetTextField bA, start, ende;
        private JTextField text;
        private MinimalisticButton butt;

        private Abgang(IStreckeE strecke, MinimalisticButton butt) {
            this.butt = butt;
            this.strecke = strecke;
            bA = new MinimalisticFormattetTextField((strecke.getBa() < 0 ? "" : strecke.getBa()) + "", 8);
            start = new MinimalisticFormattetTextField((strecke.getStart() < 0 ? "" : strecke.getStart()) + "", 4);
            ende = new MinimalisticFormattetTextField((strecke.getEnde() < 0 ? platzHalter : strecke.getEnde()) + "", 4);
            text = new JTextField(strecke.getOrt() + "", 16);
            ende.addFocusListener(this);
            bA.addKeyListener(this);
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (ende.getText().equals(platzHalter)) {
                ende.setText("");
            }
        }

        @Override
        public void focusLost(FocusEvent e) {

        }

        @Override
        public int getBa() {
            try {
                return Integer.parseInt(bA.getText());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        @Override
        public void setBa(int ba) {
            strecke.setBa(ba);
            bA.setText(ba + "");
        }

        @Override
        public int getEnde() {
            try {
                return Integer.parseInt(ende.getText());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        @Override
        public void setEnde(int ende) {
            strecke.setEnde(ende);
            this.ende.setText(ende + "");

        }

        @Override
        public int getStart() {
            try {
                return Integer.parseInt(start.getText());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        @Override
        public void setStart(int start) {
            this.start.setText(start + "");
            strecke.setStart(start);
        }

        @Override
        public long getVerlegedatum() {
            return strecke.getVerlegedatum();
        }

        @Override
        public void setVerlegedatum(long verlegedatum) {
            strecke.setVerlegedatum(verlegedatum);
        }

        @Override
        public String getOrt() {
            return text.getText();
        }

        @Override
        public void setOrt(String ort) {
            text.setText(ort);
            strecke.setOrt(ort);
        }

        @Override
        public int getTrommelID() {
            return strecke.getTrommelID();
        }

        @Override
        public int getMeter() {
            return Math.abs(getStart() - getEnde());
        }

        @Override
        public int getId() {
            return strecke.getId();
        }


        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (text.getText().equals("")) {
                try {
                    text.setText(kontroller.getTextForBA(Integer.parseInt(((JTextField) e.getSource()).getText())));
                } catch (Exception ex) {
                }
            }
        }
    }
}

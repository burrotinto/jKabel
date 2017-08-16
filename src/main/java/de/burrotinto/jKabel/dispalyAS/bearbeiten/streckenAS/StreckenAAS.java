/*
 * jKabel - Ein hochperfomantes, extremstanpassungsfähiges Mehrbenutzersystem zur erfassung von Kabelstrecken
 *
 * Copyright (C) 2016 Florian Klinger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IGeliefertE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ILieferantE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IStreckeE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.kabelTypAuswahlAS.IKabelTypListner;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.scanAS.ScanAAS;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelAuswahlAS.ITrommelListner;
import de.burrotinto.jKabel.eventDriven.events.TrommelSelectEvent;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticButton;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticFormattetTextField;
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticPanel;
import de.burrotinto.jKabel.eventDriven.events.UpdateEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.bus.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

/**
 * Created by derduke on 22.05.16.
 */
@Component
public class StreckenAAS extends JPanel implements ITrommelListner, ActionListener, IKabelTypListner, KeyListener{
    private final StreckenK kontroller;
    private final ApplicationEventPublisher eventPublisher;
    private JTextField trommelnummerField, datumField, laengeField, typField, matNrField, baField, startField, endField, trommelstartField;
    private JComboBox<String> lagerplatzBox, ortBox;
    private JCheckBox freiCheckBox;
    private List<Abgang> abgaenge;
    private MinimalisticButton create = new MinimalisticButton("Eintragen");
    private MinimalisticButton update = new MinimalisticButton("Ändern");
    private ITrommelE trommel = null;
    private JComboBox<ILieferantE> cBox;
    private List<JDialog> scanDialoge = new ArrayList<>();
    private BufferedImage logo = null;


    public StreckenAAS(IDBWrapper db, ApplicationEventPublisher eventPublisher) {
        kontroller = new StreckenK(db);
        this.eventPublisher = eventPublisher;
        create.addActionListener(this);
        update.addActionListener(this);
//        setPreferredSize(new Dimension(680,680));

    }

    @Override
    public void trommelAusgewaehlt(Integer trommelID) {
        removeAll();
        if (trommelID != null && kontroller != null && kontroller.getTrommelByID(trommelID) != null)
            buildPanel(kontroller.getTrommelByID(trommelID));
        repaint();
        revalidate();
    }

    private void buildPanel(ITrommelE trommel) {
        this.trommel = trommel;
        kontroller.setTyp(trommel.getTyp());
        List<IStreckeE> strecken = kontroller.getStreckenForTrommel(trommel);
        JPanel p = new MinimalisticPanel();
        p.setLayout(new GridLayout(strecken.size() + (kontroller.istAusserHaus(trommel) ? 12 : 13), 1));

        // Überschrift
        JPanel uebers = new MinimalisticPanel(new FlowLayout());
        uebers.add(new JLabel("Kabelnachweiß für Trommel:"));
        trommelnummerField = new JTextField(trommel.getTrommelnummer(), 16);
        uebers.add(trommelnummerField);
        p.add(uebers);

        //Datum
        JPanel date = new MinimalisticPanel(new FlowLayout());
        date.add(new JLabel("Datum:"));
        datumField = new JTextField(kontroller.getTimeString(kontroller.getLieferDate(trommel)));
        datumField.setEditable(false);
        date.add(datumField);
        p.add(date);

        //Lieferant
        JPanel liefer = new MinimalisticPanel(new FlowLayout());
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
        JPanel lNR = new MinimalisticPanel(new FlowLayout());
        lNR.add(new JLabel("Lieferscheinnummer:"));
        JTextField lnTF = new JTextField(kontroller.getLieferscheinNR(trommel));
        lnTF.setEditable(false);
        lNR.add(lnTF);
        p.add(lNR);


        //Länge
        JPanel laenge = new MinimalisticPanel(new FlowLayout());
        laenge.add(new JLabel("Gesamtlänge:"));
        laengeField = new MinimalisticFormattetTextField(trommel.getGesamtlaenge() + "", 6);
        laenge.add(laengeField);
        p.add(laenge);

        //Trommelanfang
        JPanel trommelP = new MinimalisticPanel(new FlowLayout());
        trommelP.add(new JLabel("Trommelanfang:"));
        trommelstartField = new MinimalisticFormattetTextField(trommel.getStart() + "", 6);
        trommelP.add(trommelstartField);
        p.add(trommelP);


        //Typ
        JPanel typ = new MinimalisticPanel(new FlowLayout());
        typ.add(new JLabel("Kabelart:"));
        typField = new JTextField(kontroller.getTyp(trommel).getTyp(), 16);
        typ.add(typField);
        p.add(typ);

        //Lagerplatz
        JPanel lP = new MinimalisticPanel(new FlowLayout());
        lP.add(new JLabel("Lagerplatz:"));
        lagerplatzBox = new JComboBox<String>(kontroller.getLagerPlaetze());
        lagerplatzBox.setEditable(true);
        lagerplatzBox.setSelectedItem(trommel.getLagerPlatz());
        lP.add(lagerplatzBox);
        p.add(lP);

        //MatNr
        JPanel matNr = new MinimalisticPanel(new FlowLayout());
        matNr.add(new JLabel("MaterialNummer:"));
        matNrField = new MinimalisticFormattetTextField(kontroller.getTyp(trommel).getMaterialNummer() + "");
        matNr.add(matNrField);
        matNrField.setEditable(false);
        p.add(matNr);

        //Freimeldung
        JPanel frei = new MinimalisticPanel(new FlowLayout());
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
            Abgang a = new Abgang(s, del, trommel);
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
            try (ScanAAS scanAAS = new ScanAAS(trommel, s)) {
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
                int free = trommel.getGesamtlaenge();
                for (IStreckeE iStreckeE : trommel.getStrecken()) {
                    if (iStreckeE.getEnde() != -1) {
                        free -= iStreckeE.getMeter();
                    }
                }
                tF.setText(free + "");
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

            ortBox = new JComboBox<String>();
            ortBox.setEditable(true);
            ortBox.setPreferredSize(new Dimension(180, 21));

            baField.addKeyListener(this);
            panel.add(baField);
            panel.add(startField);
            panel.add(endField);
            panel.add(ortBox);
            panel.add(create);
            p.add(panel);
        }
        JPanel butt = new MinimalisticPanel();
        butt.add(update);
        p.add(butt);
        add(p);
    }

    public void setLogo(BufferedImage img) {
        logo = img;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (logo != null) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.drawImage(logo, null, getWidth() - logo.getWidth() - 20, 20);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == create && trommel != null) {
            int ba = -1;
            int start = -1;
            int ende = -1;
            try {
                ba = Integer.parseInt(baField.getText());
            } catch (NumberFormatException ignored) {
            }
            try {
                start = Integer.parseInt(startField.getText());
            } catch (NumberFormatException ignored) {
            }
            try {
                ende = Integer.parseInt(endField.getText());
            } catch (NumberFormatException ignored) {
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
                kontroller.eintragenStrecke(ba, ortBox.getSelectedItem() == null ? "" : (String) ortBox.getSelectedItem(), System.currentTimeMillis(), start, ende, trommel);
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
                trommel.setLagerPlatz((String) lagerplatzBox.getSelectedItem());
                trommel.setStart(Integer.parseInt(trommelstartField.getText()));
                trommel.setFreimeldung(freiCheckBox.isSelected());
                kontroller.update(trommel);

                IGeliefertE g = kontroller.getLiefer(trommel);
                g.setLieferantID(((ILieferantE) cBox.getSelectedItem()));
                kontroller.update(g);


                IKabeltypE typ = trommel.getTyp();
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

        buildPanel(kontroller.getNewCopy(trommel));
        repaint();
        revalidate();
        eventPublisher.publishEvent(new UpdateEvent(trommel));
    }

    @Override
    public void typSelected(IKabeltypE typ) {
//        removeAll();
//        repaint();
//        revalidate();
//
//        this.typ = typ;
//        kontroller.setTyp(typ);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            if (!kontroller.getTextForBA(Integer.parseInt(((JTextField) e.getSource()).getText())).isEmpty()) {
                String old = (String) ortBox.getSelectedItem();
                ortBox.removeAllItems();
                ortBox.addItem(old);
                Vector<String> v = kontroller.getTextForBA(Integer.parseInt(((JTextField) e.getSource()).getText()));
                v.forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        ortBox.addItem(s.equals("null") ? "" : s);
                    }
                });
                if (!v.isEmpty()) {
                    ortBox.setSelectedItem(v.firstElement());
                } else {
                    ortBox.setSelectedItem(old);
                }
                ortBox.updateUI();
            }
        } catch (Exception ex) {
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

    @EventListener
    public void handle(TrommelSelectEvent trommelSelectEvent) {
        trommelAusgewaehlt(trommelSelectEvent.getTrommelId());
    }

    private class Abgang implements FocusListener, IStreckeE, KeyListener {
        public static final String platzHalter = "---";
        private final IStreckeE strecke;
        private MinimalisticFormattetTextField bA, start, ende;
        private JTextField text;
        private MinimalisticButton butt;
        private ITrommelE trommel;

        private Abgang(IStreckeE strecke, MinimalisticButton butt, ITrommelE trommel) {
            this.butt = butt;
            this.strecke = strecke;
            bA = new MinimalisticFormattetTextField((strecke.getBa() < 0 ? "" : strecke.getBa()) + "", 8);
            start = new MinimalisticFormattetTextField((strecke.getStart() < 0 ? "" : strecke.getStart()) + "", 4);
            ende = new MinimalisticFormattetTextField((strecke.getEnde() < 0 ? platzHalter : strecke.getEnde()) + "", 4);
            text = new JTextField(strecke.getOrt() + "", 16);
            ende.addFocusListener(this);
            bA.addKeyListener(this);
            this.trommel = trommel;
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
        public ITrommelE getTrommel() {
            return trommel;
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
                    text.setText(kontroller.getTextForBA(Integer.parseInt(((JTextField) e.getSource()).getText())).firstElement());
                } catch (Exception ex) {
                }
            }
        }
    }
}

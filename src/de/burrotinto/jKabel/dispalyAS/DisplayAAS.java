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

package de.burrotinto.jKabel.dispalyAS;

import de.burrotinto.jKabel.config.Reader;
import de.burrotinto.jKabel.dbauswahlAS.DBAuswahlAAS;
import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.serverStatus.IStatusClient;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.kabelTypAuswahlAS.KabelTypAuswahlAAS;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS.StreckenAAS;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelAuswahlAS.TrommelAuswahlAAS;
import de.burrotinto.jKabel.dispalyAS.search.SearchAAS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created by derduke on 22.05.16.
 */
public class DisplayAAS extends JFrame implements ItemListener, ActionListener {
    public static Color BACKGROUND = Color.WHITE;
    private JPanel north = new JPanel();
    private JMenuItem edit = new JMenuItem("Bearbeiten");
    private JMenuItem search = new JMenuItem("Suchen");
    private JMenuItem exit = new JMenuItem("Ende");
    private JMenuItem auchf = new JCheckBoxMenuItem("Zeige alle Trommeln");
    private JPanel center = new JPanel();
    private JPanel south = new JPanel();

    private TrommelAuswahlAAS tommelAAs = null;

    private DBAuswahlAAS dbAuswahlAAS = new DBAuswahlAAS();
    private IDBWrapper db = null;
    private IStatusClient sClient = null;
    private JLabel anZClients = new JLabel("Insgesamt 0 angemeldet");

    public DisplayAAS() {
        setTitle("jKabel");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(south = getSouth(), BorderLayout.SOUTH);

        // MenueBar
        JMenuBar menuBar = new JMenuBar();
        JMenu menue = new JMenu("File");
        menue.add(edit);
        menue.add(search);

        menue.add(auchf);
        menue.add(exit);

        edit.addActionListener(this);
        search.addActionListener(this);
        exit.addActionListener(this);
        auchf.addActionListener(this);
        menuBar.add(menue);
        setJMenuBar(menuBar);


        IDBWrapper db = dbAuswahlAAS.getDBWrapper();

        if (db == null) {
            center.add(new JLabel("Es konnte keine Verbindung zur DB hergestellt werden."));
            center.add(new JLabel("Wenn !!!sicher!!! ist das kein anderer auf dedr DB arbeitet die lock.lck Datei löschen"));
            DisplayAAS d = this;
            getContentPane().add(center, BorderLayout.CENTER);
        } else {
            setDb(db);
        }


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        DisplayAAS f = new DisplayAAS();


    }

    public void setDb(IDBWrapper db) {
        if (this.db != db) {
            this.db = db;
            // Zuerst mal Bearbeiten öffnen
            if (db != null) {
                //StatusClient
                try {
                    sClient = dbAuswahlAAS.getStatusClient();
                } catch (IOException e) {
                    e.printStackTrace();
                    sClient = null;
                }

                center.removeAll();
                south.removeAll();
                remove(center);
                remove(south);
                getContentPane().add(center = getBearbeitenPanel(), BorderLayout.CENTER);
                getContentPane().add(south = getSouth(), BorderLayout.SOUTH);
                revalidate();
                new Thread(new DBConectionTester(this, db, dbAuswahlAAS)).start();

            } else {

            }
        }
    }

    public JPanel getBearbeitenPanel() {
        KabelTypAuswahlAAS k = new KabelTypAuswahlAAS(db);
        tommelAAs = new TrommelAuswahlAAS(db, auchf.isSelected());
        HashSet<JPanel> updateSet = new HashSet<>();
        updateSet.add(k);
        updateSet.add(tommelAAs);
        StreckenAAS s = new StreckenAAS(db, updateSet);

        k.addKabelTypListner(tommelAAs);
        k.addKabelTypListner(s);

        tommelAAs.addTrommelListner(s);

        JPanel l = new JPanel(new GridLayout(1, 2));
        JPanel all = new JPanel(new GridLayout(1, 2));

        JScrollPane kSP = new JScrollPane(k);
        l.add(kSP);
        l.add(new JScrollPane(tommelAAs));
        all.add(l);
        JScrollPane sc = new JScrollPane(s);
        sc.setPreferredSize(new Dimension(740, 740));
        all.add(sc);

        setBackground(BACKGROUND);

        kSP.setOpaque(false);
        sc.setOpaque(false);

        setBackground(BACKGROUND);
        l.setBackground(BACKGROUND);
        k.setBackground(BACKGROUND);
        tommelAAs.setBackground(BACKGROUND);
        s.setBackground(BACKGROUND);

        try {
            s.setLogo(ImageIO.read(new File(Reader.getInstance().getPath() + "logo.jpg")));
        } catch (IOException e) {
            s.setLogo(null);
        }
        all.setBackground(BACKGROUND);
        return all;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == edit) {
            System.out.println("EDIT");
        } else if (e.getSource() == search) {
            System.out.println("Suchen");

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit) {
            System.exit(0);
        }
        if (db != null) {
            if (e.getSource() == edit) {
                remove(center);
                center = getBearbeitenPanel();
                getContentPane().add(center, BorderLayout.CENTER);

            } else if (e.getSource() == search) {
                remove(center);
                center = new SearchAAS(db);
                getContentPane().add(center, BorderLayout.CENTER);
            } else if (e.getSource() == auchf && tommelAAs != null) {
                tommelAAs.setZeiheAlle(auchf.isSelected());
            }
            repaint();
            revalidate();
        }
    }

    private boolean isRemoteDB() {
        return !dbAuswahlAAS.hasServer();
    }

    /**
     * Erstellt den "Footer"
     *
     * @return ein Panel...-
     */
    private JPanel getSouth() {
        JPanel p = new JPanel();
        JPanel rPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (dbAuswahlAAS.getServerIP() != null) {
            if (isRemoteDB()) {
                rPanel.add(new JLabel("Running as Client. Connected with Server: " + dbAuswahlAAS.getServerIP().getHostAddress() + " \"" + dbAuswahlAAS.getServerIP().getHostName() + "\" |"));
            } else {
                rPanel.add(new JLabel("Running as Server |"));
            }
        }
        p.add(rPanel);

        if (sClient != null) {
            p.add(anZClients);
        }
        JPanel prodlyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        prodlyPanel.add(new JLabel("| proudly made by Florian Klinger"));
        p.add(prodlyPanel);
        return p;

    }

    /**
     * Soll die Verbindung zur Datenbank prüfen. Wenn diese geschlossen wurde soll eine neue Verbindung hergestellt werden.
     */
    private class DBConectionTester implements Runnable {
        private final DisplayAAS display;
        private final IDBWrapper db;
        private final DBAuswahlAAS dbAuswahlAAS;

        DBConectionTester(DisplayAAS display, IDBWrapper db, DBAuswahlAAS dbAuswahlAAS) {
            this.display = display;
            this.db = db;
            this.dbAuswahlAAS = dbAuswahlAAS;
        }

        @Override
        public void run() {
            while (!db.isClosed()) {
                try {
                    Thread.sleep(1000);
                    try {
                        anZClients.setText("| Insgesamt " + sClient.getAnzahlClients() + " angemeldet |");
                        anZClients.revalidate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            display.setDb(dbAuswahlAAS.getDBWrapper());
        }
    }
}

package de.swneumarkt.jKabeltrommel.dispalyAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.DBAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.KabelTypAuswahlAS.KabelTypAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.StreckenAS.StreckenAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.TrommelAuswahlAS.TrommelAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.search.SearchAAS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;

/**
 * Created by derduke on 22.05.16.
 */
public class DisplayAAS extends JFrame implements ItemListener, ActionListener {
    private JPanel north = new JPanel();
    private JMenuItem edit = new JMenuItem("Bearbeiten");
    private JMenuItem search = new JMenuItem("Suchen");
    private JMenuItem exit = new JMenuItem("Ende");
    private JMenuItem auchf = new JCheckBoxMenuItem("Zeige freigemeldete");
    private JScrollPane center = new JScrollPane();

    private TrommelAuswahlAAS tommelAAs = null;

    private IDBWrapper db = null;

    public DisplayAAS() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JLabel("proudly made by Florian Klinger"), BorderLayout.SOUTH);

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

    }

    public static void main(String[] args) {
        DisplayAAS f = new DisplayAAS();
        f.setTitle("jKabeltrommel");
        IDBWrapper db = new DBAuswahlAAS().getDBWrapper();
        if (db == null) {
            JPanel p = new JPanel();
            p.add(new JLabel("Es wurde bereits eine Instanz auf einem anderen Rechner geöffnet. Mehrbenutzerbetrieb ist aktuell nicht möglich.(Es steht kein Server zur verfügung)."));
            p.add(new JLabel("Wenn !!!sicher!!! ist das kein anderer auf dedr DB arbeitet die lock.lck Datei löschen"));
            f.getContentPane().add(p, BorderLayout.CENTER);
        } else {
            f.setDb(db);

        }
        f.setSize(1400, 640);
        f.setMinimumSize(new Dimension(480, 480));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public void setDb(IDBWrapper db) {
        this.db = db;

        // Zuerst mal Bearbeiten öffnen
        remove(center);
        center = new JScrollPane(getBearbeitenPanel());
        getContentPane().add(center, BorderLayout.CENTER);
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
        l.add(new JScrollPane(k));
        l.add(new JScrollPane(tommelAAs));
        all.add(l);
        JScrollPane sc = new JScrollPane(s);
        sc.setPreferredSize(new Dimension(680, 680));
        all.add(new JScrollPane(sc));
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
                center = new JScrollPane(getBearbeitenPanel());
                getContentPane().add(center, BorderLayout.CENTER);

            } else if (e.getSource() == search) {
                remove(center);
                center = new JScrollPane(new SearchAAS(db));
                getContentPane().add(center, BorderLayout.CENTER);
            } else if (e.getSource() == auchf && tommelAAs != null) {
                tommelAAs.setAuchFreigemeldete(auchf.isSelected());
            }
            repaint();
            revalidate();
        }
    }
}

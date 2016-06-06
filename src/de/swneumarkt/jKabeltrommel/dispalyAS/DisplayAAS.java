package de.swneumarkt.jKabeltrommel.dispalyAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.DBAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.kabelTypAuswahlAS.KabelTypAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.streckenAS.StreckenAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.trommelAuswahlAS.TrommelAuswahlAAS;
import de.swneumarkt.jKabeltrommel.dispalyAS.search.SearchAAS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * Created by derduke on 22.05.16.
 */
public class DisplayAAS extends JFrame implements ItemListener, ActionListener {
    private JPanel north = new JPanel();
    private JMenuItem edit = new JMenuItem("Bearbeiten");
    private JMenuItem search = new JMenuItem("Suchen");
    private JMenuItem exit = new JMenuItem("Ende");
    private JMenuItem auchf = new JCheckBoxMenuItem("Zeige alle Trommeln");
    private JPanel center = new JPanel();

    private TrommelAuswahlAAS tommelAAs = null;

    private DBAuswahlAAS dbAuswahlAAS = new DBAuswahlAAS();
    private IDBWrapper db = null;

    public DisplayAAS() {

        setTitle("jKabel");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getSouth(), BorderLayout.SOUTH);

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
            JTextField tf = new JTextField(20);
            center.add(tf);
            DisplayAAS d = this;
            tf.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        IDBWrapper db = dbAuswahlAAS.connectRemoteDB(InetAddress.getByName(tf.getText()));
                        if (db != null) {
                            d.setDb(db);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                    }
                }
            });
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
        this.db = db;
        // Zuerst mal Bearbeiten öffnen
        if (db != null) {
            center.removeAll();
            remove(center);
            center = getBearbeitenPanel();
            getContentPane().add(center, BorderLayout.CENTER);
            getContentPane().add(getSouth(), BorderLayout.SOUTH);
            revalidate();
        } else {

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
        kSP.setBackground(Color.WHITE);
        l.add(kSP);
        l.add(new JScrollPane(tommelAAs));
        all.add(l);
        JScrollPane sc = new JScrollPane(s);
        sc.setPreferredSize(new Dimension(680, 680));
        all.add(sc);
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

    private JPanel getSouth() {
        JPanel p = new JPanel();
        JPanel rPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (dbAuswahlAAS.getServerIP() != null) {
            if (isRemoteDB()) {
                rPanel.add(new JLabel("Running as Client. Connected with Server: " + dbAuswahlAAS.getServerIP().getHostAddress() + " \"" + dbAuswahlAAS.getServerIP().getHostName() + "\""));
            } else {
                rPanel.add(new JLabel("Running as Server"));
            }
        }
        p.add(rPanel);

        JPanel prodlyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        prodlyPanel.add(new JLabel("proudly made by Florian Klinger"));
        p.add(prodlyPanel);
        return p;

    }


}

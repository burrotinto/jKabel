package de.swneumarkt.jKabeltrommel.dispalyAS.search;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.HSQLDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.OnlyOneUserExeption;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IKabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.ITrommelE;
import de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.StreckenAS.StreckenAAS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by derduke on 25.05.16.
 */
public class SearchAAS extends JPanel implements ActionListener {
    private final SearchK kontroller;
    private final IDBWrapper db;
    private JButton searchButt = new JButton("suchen");
    private JTextField baFied;
    private JComboBox<IKabeltypE> cBox;
    private JPanel ergebnis = new JPanel();

    public SearchAAS(IDBWrapper db) {
        this.kontroller = new SearchK(db);
        searchButt.addActionListener(this);
        this.db = db;
        add(getEingabePanel());

    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, OnlyOneUserExeption, IOException {
        JFrame f = new JFrame();
        f.add(new SearchAAS(new HSQLDBWrapper("/home/derduke/GIT-Projekte/jKabeltrommel/")));
        f.setVisible(true);
    }

    private JPanel getEingabePanel() {
        JPanel s = new JPanel(new GridLayout(3, 2));
        s.add(new JLabel("Suchen:"));
        s.add(searchButt);
        s.add(new JLabel("BA:"));
        baFied = new JTextField(8);
        s.add(baFied);
        s.add(new JLabel("Kabeltyp"));
        cBox = new JComboBox<>(kontroller.getAllKAbelTypen());
        s.add(cBox);

        return s;
    }

    private JPanel getErgebnisPanel(List<ITrommelE> trommeln) {
        JPanel s = new JPanel(new GridLayout(trommeln.size(), 1));
        for (ITrommelE t : trommeln) {
            StreckenAAS nachweis = new StreckenAAS(db);
            s.add(new JScrollPane(nachweis));
            nachweis.trommelAusgewaehlt(t);
        }
        return s;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        remove(ergebnis);
        ergebnis = getErgebnisPanel(kontroller.getAllTrommelWithBA(Integer.parseInt(baFied.getText()), (IKabeltypE) cBox.getSelectedItem()));
        add(ergebnis);
        repaint();
        revalidate();
    }
}

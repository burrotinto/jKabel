package de.swneumarkt.jKabeltrommel.dispalyAS.adminShell;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;

import javax.swing.*;

/**
 * Created by derduke on 25.05.16.
 */
public class AdminShellAAS extends JPanel {

    private IDBWrapper db;

    public AdminShellAAS(IDBWrapper db) {
        this.db = db;
        setName("AdminShell");
    }

    void buildPanel() {
        JPanel panel = new JPanel();

    }
}

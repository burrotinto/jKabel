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

package de.burrotinto.jKabel.dispalyAS

import de.burrotinto.jKabel.SplashScreen
import de.burrotinto.jKabel.dbauswahlAS.DBAuswahlAAS
import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper
import de.burrotinto.jKabel.dbauswahlAS.serverStatus.IStatusClient
import de.burrotinto.jKabel.dispalyAS.help.GPLAAS
import de.burrotinto.jKabel.dispalyAS.help.HelpAAS
import de.burrotinto.jKabel.dispalyAS.search.trommelByBA.SearchAAS
import de.burrotinto.jKabel.dispalyAS.search.trommelByNummer.SearchTrommelNrAAS
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import java.io.IOException
import javax.swing.*


/**
 * Created by derduke on 22.05.16.
 */
@org.springframework.stereotype.Component
class DisplayAAS(private val kontroll: DisplayK,
                 private val dbAuswahlAAS: DBAuswahlAAS,
                 private val searchTrommelNrAAS: SearchTrommelNrAAS,
                 private val searchAAS: SearchAAS,
                 private val splashScreen: SplashScreen) : JFrame(), ItemListener {

    private val bearbeitenPanel: JPanel

    private val north = JPanel()
    private val edit = JMenuItem("Bearbeiten")
    private val search = JMenuItem("Suchen")
    private val exit = JMenuItem("Ende")
    private val auchf = JCheckBoxMenuItem("Zeige alle Trommeln")
    private val help = JMenuItem("Hilfe")
    private val gpl = JMenuItem("Über")
    private var center = JPanel()
    private var south = JPanel()
    private var db: IDBWrapper? = null
    private var sClient: IStatusClient? = null
    private val anZClients = JLabel("Insgesamt 0 angemeldet")


    // MenueBar
    private val menuBar = JMenuBar()


    init {

        title = "jKabel"

        contentPane.layout = BorderLayout()
        south = getSouth()
        contentPane.add(south, BorderLayout.SOUTH)

        //File menue
        val menue = JMenu("File")
        menue.add(edit)
        menue.add(search)

        menue.add(auchf)

        menue.addSeparator()
        //        menue.add(getjTypSortMenu());
        //        menue.add(getjTrommelSortMenu());
        menue.addSeparator()
        menue.add(exit)


        edit.addActionListener { this.edit(it) }
        search.addActionListener { this.search(it) }
        exit.addActionListener { this.exit(it) }
        //        auchf.addActionListener(new ActionListener() {
        //            @Override
        //            public void actionPerformed(ActionEvent actionEvent) {
        //                try {
        //                    ConfigReader.getInstance().setZeigeAlle(auchf.isSelected());
        //                } catch (IOException e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //        });

        //License and more
        val lMenue = JMenu("Hilfe")
        lMenue.add(help)
        lMenue.add(gpl)

        help.addActionListener { HelpAAS() }
        gpl.addActionListener { GPLAAS() }

        // und zusammenbauen
        menuBar.add(menue)
        menuBar.add(lMenue)
        menuBar.add(Version())

        // Trommelsuchband
        val sbt = JMenuItem("Suchen nach Trommelnummer")
        menuBar.add(sbt)
        sbt.addActionListener(searchTrommelNrAAS)
        searchTrommelNrAAS.isVisible = false
        contentPane.add(searchTrommelNrAAS, BorderLayout.NORTH)


        jMenuBar = menuBar


        val db = this.dbAuswahlAAS.dbWrapper


        bearbeitenPanel = kontroll.getBearbeitenPanel()

        if (db == null) {
            center.add(JLabel("Es konnte keine Verbindung zur DB hergestellt werden."))
            center.add(JLabel("Wenn !!!sicher!!! ist das kein anderer auf der DB arbeitet die lock.lck Datei löschen"))
            val d = this
            contentPane.add(center, BorderLayout.CENTER)
        } else {
            setDb(db)
        }


        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        pack()
        isVisible = true

        splashScreen.dispose()
    }

    private fun edit(actionEvent: ActionEvent) {
        remove(center)
        center = bearbeitenPanel
        menuBar.updateUI()
        contentPane.add(center, BorderLayout.CENTER)

        repaint()
        revalidate()
    }

    private fun search(actionEvent: ActionEvent) {
        remove(center)
        center = searchAAS
        contentPane.add(center, BorderLayout.CENTER)

        repaint()
        revalidate()
    }

    fun exit(e: ActionEvent) {
        System.exit(0)
    }


    fun setDb(db: IDBWrapper?) {
        if (this.db !== db) {
            this.db = db
            // Zuerst mal Bearbeiten öffnen
            if (db != null) {
                //StatusClient
                try {
                    sClient = dbAuswahlAAS.statusClient
                } catch (e: IOException) {
                    e.printStackTrace()
                    sClient = null
                }

                center.removeAll()
                south.removeAll()
                remove(center)
                remove(south)
                center = bearbeitenPanel
                south = getSouth()
                contentPane.add(center, BorderLayout.CENTER)
                contentPane.add(south, BorderLayout.SOUTH)
                revalidate()
                Thread(DBConectionTester(this, db, dbAuswahlAAS)).start()

            }
        }
    }


    override fun itemStateChanged(e: ItemEvent) {
        if (e.source === edit) {
            println("EDIT")
        } else if (e.source === search) {
            println("Suchen")

        }
    }


    private val isRemoteDB: Boolean
        get() = !dbAuswahlAAS.hasServer()

    /**
     * Erstellt den "Footer"
     *
     * @return ein Panel...-
     */
    private fun getSouth(): JPanel {
        val p = JPanel()
        val rPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        if (dbAuswahlAAS.serverIP != null) {
            if (isRemoteDB) {
                rPanel.add(JLabel("Running as Client. Connected with Server: " + dbAuswahlAAS.serverIP.hostAddress + " \"" + dbAuswahlAAS.serverIP.hostName + "\" |"))
            } else {
                rPanel.add(JLabel("Running as Server |"))
            }
        }
        p.add(rPanel)

        if (sClient != null) {
            p.add(anZClients)
        }
        val prodlyPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        prodlyPanel.add(JLabel("| proudly made by Florian Klinger"))
        p.add(prodlyPanel)
        return p

    }

    //    private JMenu getjTrommelSortMenu() {
    //        JMenu trommelSortMenu = new JMenu("Trommel Sortierung");
    //
    //        JRadioButtonMenuItem inOrder = new JRadioButtonMenuItem("Aufsteigend Sortieren");
    ////        inOrder.setSelected(ConfigReader.getInstance().isTypeInOrder());
    //        inOrder.addActionListener(new ActionListener() {
    //            @Override
    //            public void actionPerformed(ActionEvent actionEvent) {
    //                try {
    //                    ConfigReader.getInstance().setTrommelInOrder(inOrder.isSelected());
    ////                    if (kabelTypAuswahlAAS != null) {
    ////                        kabelTypAuswahlAAS.typSelected(kabelTypAuswahlAAS.getSelected());
    ////                    }
    //                } catch (IOException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        });
    //        trommelSortMenu.add(inOrder);
    //
    //        trommelSortMenu.addSeparator();
    //
    //        ButtonGroup group = new ButtonGroup();
    //        for (AbstractTrommelSort aTS : ConfigReader.getInstance().getAllTrommelSort()) {
    //
    //            JRadioButtonMenuItem sw = new JRadioButtonMenuItem(aTS.getName());
    //            sw.setSelected(aTS.equals(ConfigReader.getInstance().getTrommelSort()));
    //            group.add(sw);
    //            trommelSortMenu.add(sw);
    //
    //            sw.addActionListener(aTS);
    //        }
    //        return trommelSortMenu;
    //    private JMenu getjTypSortMenu() {
    //        JMenu typSortMenu = new JMenu("Typ Sortierung");
    //
    //        JRadioButtonMenuItem inOrder = new JRadioButtonMenuItem("Aufsteigend Sortieren");
    //        inOrder.setSelected(ConfigReader.getInstance().isTypeInOrder());
    //        inOrder.addActionListener(new ActionListener() {
    //            @Override
    //            public void actionPerformed(ActionEvent actionEvent) {
    //                try {
    //                    ConfigReader.getInstance().setTypeInOrder(inOrder.isSelected());
    ////                    if (kabelTypAuswahlAAS != null) {
    ////                        kabelTypAuswahlAAS.typSelected(kabelTypAuswahlAAS.getSelected());
    ////                    }
    //                } catch (IOException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        });
    //        typSortMenu.add(inOrder);
    //        typSortMenu.addSeparator();
    //
    //        ButtonGroup group = new ButtonGroup();
    //        for (AbstractTypeSort aTS : ConfigReader.getInstance().getAllTypSort()) {
    //
    //            JRadioButtonMenuItem sw = new JRadioButtonMenuItem(aTS.getName());
    //            sw.setSelected(aTS.equals(ConfigReader.getInstance().getKabeltypSort()));
    //            group.add(sw);
    //            typSortMenu.add(sw);
    //
    //            sw.addActionListener(aTS);
    //        }
    //        return typSortMenu;


    /**
     * Soll die Verbindung zur Datenbank prüfen. Wenn diese geschlossen wurde soll eine neue Verbindung hergestellt werden.
     */
    private inner class DBConectionTester internal constructor(private val display: DisplayAAS, private val db: IDBWrapper, private val dbAuswahlAAS: DBAuswahlAAS) : Runnable {

        override fun run() {
            try {
                while (true) {
                    Thread.sleep(500)

                    anZClients.text = "| Insgesamt " + sClient!!.anzahlClients + " angemeldet |"
                    anZClients.revalidate()

                }
            } catch (e: Exception) {
                val dialog = JOptionPane.showMessageDialog(display,
                        "Ungespeicherte Daten aufschreiben und jKabel neu starten ",
                        "Datenbank wurde geschlossen",
                        JOptionPane.ERROR_MESSAGE)
//                display.setDb(dbAuswahlAAS.dbWrapper)
            }

        }
    }

}


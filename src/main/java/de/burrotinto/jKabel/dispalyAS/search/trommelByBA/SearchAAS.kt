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

package de.burrotinto.jKabel.dispalyAS.search.trommelByBA

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE
import de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS.StreckenAAS

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by derduke on 25.05.16.
 */
@org.springframework.stereotype.Component
class SearchAAS(private val db: IDBWrapper,val kontroller: SearchK) : JPanel(), ActionListener {
    private val searchButt = JButton("suchen")
    private var baFied: JTextField? = null
    private var cBox: JComboBox<IKabeltypE>? = null
    private var ergebnis = JScrollPane()

    init {
        searchButt.addActionListener(this)
        layout = BorderLayout()
        add(eingabePanel, BorderLayout.WEST)
    }

    private val eingabePanel: JPanel
        get() {
            val s = JPanel(GridLayout(3, 2))
            s.add(JLabel("Suchen:"))
            s.add(searchButt)
            s.add(JLabel("BA:"))
            baFied = JTextField(8)
            s.add(baFied)
            s.add(JLabel("Kabeltyp"))
            cBox = JComboBox(kontroller.allKAbelTypen)
            s.add(cBox)
            val p = JPanel()
            p.add(s)
            return p
        }

    private fun getErgebnisPanel(ba: Int, typ: IKabeltypE): JPanel {
        val trommeln = kontroller.getAllTrommelWithBA(ba,typ)
        val s = JPanel(GridLayout(trommeln.size+1, 1))

        s.add(JLabel("Insgesamt: ${trommeln.flatMap { it.strecken }.filter { it.ba == ba }.sumBy { it.meter }} m"))
        for (t in trommeln) {
            val nachweis = StreckenAAS(db, null)
            s.add(JScrollPane(nachweis))
            nachweis.trommelAusgewaehlt(t.id)
        }
        return s
    }

    override fun actionPerformed(e: ActionEvent) {
        remove(ergebnis)
        ergebnis = JScrollPane(getErgebnisPanel(Integer.parseInt(baFied!!.text), cBox!!.selectedItem as IKabeltypE))
        add(ergebnis, BorderLayout.CENTER)
        repaint()
        revalidate()
    }
}

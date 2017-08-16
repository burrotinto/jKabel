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

package de.burrotinto.jKabel.dispalyAS.search.trommelByNummer

import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticButton
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticPanel
import de.burrotinto.jKabel.eventDriven.events.TrommelSelectEvent
import de.burrotinto.jKabel.eventDriven.events.TypSelectEvent
import org.springframework.context.ApplicationEventPublisher
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * Created by derduke on 13.10.16.
 */
@org.springframework.stereotype.Service
open class SearchTrommelNrAAS(val kontroll: SearchTrommelNrK,
                              val eventPublisher: ApplicationEventPublisher) : MinimalisticPanel(), KeyListener, ActionListener {

    private val tf = JTextField(9)
    private val ergebnis = JPanel()

    init {
        layout = FlowLayout(FlowLayout.LEFT)
        init()
    }

    private fun init() {
        val p = JPanel()
        p.add(JLabel("Trommelnummer"))
        p.add(tf)
        tf.addKeyListener(this)
        add(p)
        add(ergebnis)
    }

    override fun keyTyped(keyEvent: KeyEvent) {

    }

    override fun keyPressed(keyEvent: KeyEvent) {}

    override fun keyReleased(keyEvent: KeyEvent) {
        ergebnis.removeAll()
        kontroll.getListOfTrommeln(tf.text).forEach {
            val b = MinimalisticButton(it.trommelnummer)

            val id = it.id
            val typId = it.typ.materialNummer
            b.addActionListener {
               eventPublisher.publishEvent(TrommelSelectEvent(id))
                eventPublisher.publishEvent(TypSelectEvent(typId))
            }

            ergebnis.add(b)
        }
    }

    override fun actionPerformed(actionEvent: ActionEvent) {
        isVisible = !isVisible
    }
}

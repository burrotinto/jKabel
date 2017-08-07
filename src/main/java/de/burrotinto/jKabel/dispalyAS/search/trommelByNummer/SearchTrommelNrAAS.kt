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

import de.burrotinto.jKabel.eventDriven.events.TrommelSelectEvent
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticButton
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticPanel
import de.burrotinto.jKabel.eventDriven.EventDrivenConf
import de.burrotinto.jKabel.eventDriven.EventDrivenWire
import reactor.bus.Event
import reactor.bus.EventBus

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

/**
 * Created by derduke on 13.10.16.
 */
@org.springframework.stereotype.Service
open class SearchTrommelNrAAS(val kontroll: SearchTrommelNrK,
                              val eventBus: EventBus) : MinimalisticPanel(), KeyListener, ActionListener {

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
            b.addActionListener {eventBus.notify(EventDrivenWire.TROMMEL_SELECTED_REGISTRATION, Event.wrap(TrommelSelectEvent(id))) }
            ergebnis.add(b)
        }
    }

    override fun actionPerformed(actionEvent: ActionEvent) {
        isVisible = !isVisible
    }
}

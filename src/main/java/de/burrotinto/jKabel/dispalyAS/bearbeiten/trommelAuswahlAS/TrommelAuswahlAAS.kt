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

package de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelAuswahlAS

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE
import de.burrotinto.jKabel.dispalyAS.UpdateSet
import de.burrotinto.jKabel.dispalyAS.bearbeiten.kabelTypAuswahlAS.IKabelTypListner
import de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelCreateAS.TrommelCreateAAS
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticButton
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.MinimalisticPanel
import de.burrotinto.jKabel.dispalyAS.lookAndFeel.PercendBarMinimalisticPanel
import de.burrotinto.jKabel.eventDriven.EventDrivenWire
import de.burrotinto.jKabel.eventDriven.events.TrommelSelectEvent
import reactor.bus.Event
import reactor.bus.EventBus
import java.awt.BorderLayout
import java.awt.Color
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * Created by derduke on 22.05.16.
 */
@org.springframework.stereotype.Component
class TrommelAuswahlAAS(private val db: IDBWrapper, updateSet: UpdateSet, val kontroll: TommelAuswahlK, val eventBus: EventBus) :
        JPanel(), IKabelTypListner {
    private val addNewButt = MinimalisticButton("Neue Trommel")
    private var typ: IKabeltypE? = null
    private var ausgewaehlt: MinimalisticButton? = null

    init {
        updateSet.set.add(this)
        addNewButt.addActionListener { this.create(it) }
        layout = BorderLayout()
    }

    private fun create(actionEvent: ActionEvent) {
        TrommelCreateAAS(db, typ, this)
    }

    private fun buildPanel(typ: IKabeltypE?) {
        if (typ != null) {
            this.typ = typ
            val panel = MinimalisticPanel(GridLayout(kontroll!!.getAllTrommelForTyp(typ).size, 1))
            var p: JPanel = MinimalisticPanel()

            p.add(addNewButt)
            add(p, BorderLayout.NORTH)


            for (t in kontroll.getAllTrommelForTyp(typ)) {
                //                if (ConfigReader.getInstance().isZeigeAlle() || !(t.isFreigemeldet() && kontroll.getRestMeter(t) == 0)) {
                p = MinimalisticPanel(FlowLayout(FlowLayout.LEFT))

                val label: JLabel
                if (kontroll.isBeendet(t)) {
                    label = JLabel("Beendet")
                } else if (!kontroll.isAusserHaus(t)) {
                    p = PercendBarMinimalisticPanel(FlowLayout(FlowLayout.LEFT), kontroll.getRestMeter(t).toDouble() / t.gesamtlaenge.toDouble(), if (t.isFreigemeldet) Color.RED else Color(221, 160, 221), Color.white)
                    label = JLabel("Noch: " + kontroll.getRestMeter(t) + " m" + if (t.isFreigemeldet) " - Bund" else "")
                    if (kontroll.getRestMeter(t) == 0) {
                        p.setBackground(Color.GRAY)
                        p.setOpaque(true)
                    }

                } else {
                    val sb = StringBuilder(">> ")
                    sb.append(kontroll.getBaustelle(t)).append(" <<>> ")
                    if (kontroll.getAusleihtage(t) == 0) {
                        if (kontroll.getAusleihStunden(t) == 0) {
                            sb.append(kontroll.getAusleihMinuten(t)).append(" min")
                        } else {
                            sb.append(kontroll.getAusleihStunden(t)).append(" h")
                        }
                    } else {
                        sb.append(kontroll.getAusleihtage(t)).append(" Tag")
                        if (kontroll.getAusleihtage(t) != 1) {
                            sb.append("e")
                        }

                    }
                    sb.append(" <<")
                    label = JLabel(sb.toString())
                    p.setBackground(Color.ORANGE)
                    p.setOpaque(true)
                }

                if (kontroll.isBeendet(t)) {
                    p.background = Color.RED
                    p.isOpaque = true
                }

                val b = MinimalisticButton(t.trommelnummer + "")
                b.isSelected = b == ausgewaehlt
                p.add(b)
                p.add(label)

                b.addActionListener {
                    ausgewaehlt = b
                    eventBus.notify(EventDrivenWire.TROMMEL_SELECTED_REGISTRATION, Event.wrap(TrommelSelectEvent(t.id)))
                }

                panel.add(p)
            }
            //            }
            val pp = MinimalisticPanel()
            pp.add(panel)
            add(pp, BorderLayout.CENTER)

            val min = kontroll.getMinMeter(typ)
            val max = kontroll.getMaxMeter(typ)

            add(JLabel("Verfügbar: " + (if (min == max) max else min.toString() + " - " + max) + " m"), BorderLayout.SOUTH)

        }
    }


    override fun typSelected(typ: IKabeltypE) {
        removeAll()
        buildPanel(typ)
        repaint()
        revalidate()
    }

    override fun revalidate() {
        removeAll()
        buildPanel(if (typ != null) kontroll.getNewTypCopy(typ!!) else null)
        repaint()
        super.revalidate()
    }
}

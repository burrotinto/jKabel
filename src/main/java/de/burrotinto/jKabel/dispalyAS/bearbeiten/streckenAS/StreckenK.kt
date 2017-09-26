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

package de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper
import de.burrotinto.jKabel.dbauswahlAS.enitys.*
import de.burrotinto.usefull.list.SortedSetAnzahlDerEingefuegtenElemente
import org.springframework.stereotype.Component

import java.util.Date
import java.util.Vector
import java.util.function.Consumer

/**
 * Created by derduke on 22.05.16.
 */
@Component
internal class StreckenK(private val db: IDBWrapper) {
    private var typ: IKabeltypE? = null

    fun getStreckenForTrommel(trommel: ITrommelE): List<IStreckeE> = trommel.strecken.sortedBy { it.verlegedatum }


    fun getTyp(trommel: ITrommelE): IKabeltypE = trommel.typ


    fun setTyp(typ: IKabeltypE) {
        this.typ = typ
    }

    fun update(s: IStreckeE) {
        db.update(s)
    }

    fun update(trommel: ITrommelE) {
        db.update(trommel)
    }

    fun update(typ: IKabeltypE) {
        db.update(typ)
    }

    fun getTimeString(t: Long): String {
        val sb = StringBuilder()
        val d = Date(t)
        if (d.date < 10) sb.append("0")
        sb.append(d.date).append(".")
        if (d.month + 1 < 10) sb.append("0")
        sb.append(d.month + 1).append(".").append(d.year + 1900)
        return sb.toString()
    }

    fun remove(strecke: IStreckeE) {
        db.remove(strecke)
    }

    fun richtigeRichtung(trommel: ITrommelE, start: Int, ende: Int): Boolean {
        val list = getStreckenForTrommel(trommel)
        if (list.size == 0) {
            return true
        } else {
            val x = list[0].start - list[0].ende
            val y = start - ende
            return x <= 0 && y <= 0 || x >= 0 && y >= 0
        }

    }

    fun getLiefer(trommel: ITrommelE): IGeliefertE = trommel.geliefert


    fun getLieferDate(trommel: ITrommelE): Long = getLiefer(trommel).datum


    fun getLieferant(trommel: ITrommelE): ILieferantE = getLiefer(trommel).lieferant


    fun getLieferscheinNR(trommel: ITrommelE): String =getLiefer(trommel).lieferscheinNr


    val lieferanten: Vector<ILieferantE>
        get() = Vector(db.allLieferanten)

    fun update(g: IGeliefertE) {
        db.update(g)
    }

    fun eintragenStrecke(ba: Int, text: String, l: Long, start: Int, ende: Int, trommel: ITrommelE) {
        db.createStrecke(ba, text, l, start, ende, trommel)
    }

    fun getTextForBA(ba: Int): Vector<String> {
        val set = SortedSetAnzahlDerEingefuegtenElemente<String>()
        set.addAll(db.getAllTexteForBA(ba))
        return Vector(set)
    }

    fun istAusserHaus(trommel: ITrommelE): Boolean = getStreckenForTrommel(trommel).any { it.ende < 0 || it.start < 0 }


    fun getRestMeter(trommel: ITrommelE): Int {
        var laenge = trommel.gesamtlaenge
        for (s in trommel.strecken) {
            laenge -= s.meter
        }
        return laenge
    }

    fun getNewCopy(trommel: ITrommelE): ITrommelE? {
        return getTrommelByID(trommel.id)
    }

    fun getTrommelByID(trommelID: Int?): ITrommelE? {
        return if (trommelID == null) null else db.getTrommelByID(trommelID)
    }


    val lagerPlaetze: Vector<String>
        get() {
            val sort = SortedSetAnzahlDerEingefuegtenElemente<String>()
            typ!!.trommeln.forEach(Consumer { iTrommelE -> sort.add(iTrommelE.lagerPlatz) })
            return Vector(sort)
        }
}

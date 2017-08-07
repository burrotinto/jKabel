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

import de.burrotinto.jKabel.config.ConfigReader
import de.burrotinto.jKabel.config.trommelSort.AbstractTrommelSort
import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by derduke on 22.05.16.
 */
@Component
class TommelAuswahlK(private val db: IDBWrapper, val configReader: ConfigReader, val abstractTrommelSort: AbstractTrommelSort) {

    fun getAllTrommelForTyp(typ: IKabeltypE): List<ITrommelE> {
        val list = typ.trommeln
        Collections.sort(list, abstractTrommelSort)
        return list
    }

    fun getRestMeter(trommel: ITrommelE): Int {
        var laenge = trommel.gesamtlaenge
        for (s in trommel.strecken) {
            laenge -= s.meter
        }
        return laenge
    }

    fun isAusserHaus(t: ITrommelE): Boolean {
        return getBaustelle(t) != null
    }

    fun getBaustelle(t: ITrommelE?): String? {
        if (t != null)
            for (s in t.strecken) {
                if (s.ende < 0 || s.start < 0) {
                    return s.ort
                }
            }
        return null
    }

    fun getNewTypCopy(typ: IKabeltypE): IKabeltypE {
        return db.getTypByMaterialnummer(typ.materialNummer)
    }

    fun getNewTypCopy(materialnummer: Int): IKabeltypE {
        return db.getTypByMaterialnummer(materialnummer)
    }

    fun getMaxMeter(typ: IKabeltypE): Int {
        var x = 0
        for (t in getAllTrommelForTyp(typ)) {
            if (!isAusserHaus(t)) {
                x += getRestMeter(t)
            } else {
                var i = 0
                for (s in t.strecken) {
                    if (s.ende >= 0) {
                        i += s.meter
                    }
                }
                x += t.gesamtlaenge - i
            }
        }
        return x
    }

    fun getMinMeter(typ: IKabeltypE): Int {
        var x = 0
        for (t in getAllTrommelForTyp(typ)) {
            if (!isAusserHaus(t)) {
                x += getRestMeter(t)
            }

        }
        return x
    }

    fun getAusleihStunden(t: ITrommelE): Int {
        return getAusleihMinuten(t) / 60
    }

    fun getAusleihMinuten(t: ITrommelE): Int {
        val strecken = t.strecken
        Collections.sort(strecken) { iStreckeE, t1 -> -iStreckeE.verlegedatum.toLong().compareTo(t1.verlegedatum) }
        if (strecken.size == 0) {
            return 0
        } else {
            return ((System.currentTimeMillis() - strecken[0].verlegedatum) / (1000 * 60).toLong()).toInt()
        }
    }

    fun getAusleihtage(t: ITrommelE): Int {
        return getAusleihStunden(t) / 24
    }

    fun isBeendet(t: ITrommelE): Boolean {
        return t.isFreigemeldet && getRestMeter(t) == 0
    }
}

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
import de.burrotinto.jKabel.dbauswahlAS.enitys.IStreckeE
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE

import java.util.ArrayList
import java.util.Vector

/**
 * Created by derduke on 25.05.16.
 */
@org.springframework.stereotype.Component
class SearchK(private val db: IDBWrapper) {

    fun getAllTrommelWithBA(ba: Int, typ: IKabeltypE): List<ITrommelE> {
        val list = ArrayList<ITrommelE>()
        for (t in typ.trommeln) {
            for (s in t.strecken) {
                if (s.ba == ba) {
                    list.add(t)
                    break
                }
            }
        }
        return list
    }

    fun getAllStrecken(trommel: ITrommelE): List<IStreckeE> {
        return trommel.strecken
    }

    val allKAbelTypen: Vector<IKabeltypE>
        get() = Vector(db.allKabeltypen)
}

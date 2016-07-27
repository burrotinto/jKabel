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

package de.burrotinto.jKabel.config.trommelSort;

import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;

/**
 * Created by Florian Klinger on 27.07.16.
 */
public class TrommelIntelligentSort extends AbstractTrommelSort {

    @Override
    public int compare(ITrommelE o1, ITrommelE o2) {
        int i = 0;
        if ((!isBeendet(o1) && isBeendet(o2)) || (!isBeendet(o2) && isBeendet(o1))) {
            i = isBeendet(o1) ? 1 : -1;
        } else if ((!isAusserHaus(o1) && isAusserHaus(o2)) || (!isAusserHaus(o2) && isAusserHaus(o1))) {
            i = isAusserHaus(o1) ? 1 : -1;
        } else if (isAusserHaus(o1) && isAusserHaus(o2)) {
            i = getAusleihMinuten(o2) - getAusleihMinuten(o1);
        } else {
            i = ((Long) o1.getGeliefert().getDatum()).compareTo(o2.getGeliefert().getDatum());
        }
        return wendeAusgewaehlteOrderreihenfolgeAn(i);
    }

    @Override
    public String getName() {
        return "Intelligente Sortierung";
    }
}

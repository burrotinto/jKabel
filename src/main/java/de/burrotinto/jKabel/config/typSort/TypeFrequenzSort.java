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

package de.burrotinto.jKabel.config.typSort;

import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Created by Florian Klinger on 21.07.16.
 */
@Primary
@Component
public class TypeFrequenzSort extends AbstractTypeSort {

    @Override
    public int compare(IKabeltypE t1, IKabeltypE t2) {
        int x1 = 0;
        for (ITrommelE t : t1.getTrommeln()) {
            x1 += t.getStrecken().size();
        }

        int x2 = 0;
        for (ITrommelE t : t2.getTrommeln()) {
            x2 += t.getStrecken().size();
        }

        return wendeAusgewaehlteOrderreihenfolgeAn(x2 - x1);
    }

    @Override
    public String getName() {
        return "Häufigkeit";
    }
}

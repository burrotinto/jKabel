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

import de.burrotinto.jKabel.config.ConfigReader;
import de.burrotinto.jKabel.config.ISort;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Florian Klinger on 27.07.16.
 */
public abstract class AbstractTypeSort implements ISort<IKabeltypE> {
    private boolean inOrder = true;


    public boolean isInOrder() {
        return inOrder;
    }

    public void setInOrder(boolean inOrder) {
        this.inOrder = inOrder;
    }

    protected int wendeAusgewaehlteOrderreihenfolgeAn(int x) {
        return (isInOrder() ? 1 : -1) * x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractTypeSort)) return false;

        AbstractTypeSort that = (AbstractTypeSort) o;

        return getName().equals(that.getName());

    }
}

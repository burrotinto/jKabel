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

import de.burrotinto.jKabel.config.ConfigReader;
import de.burrotinto.jKabel.config.ISort;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IStreckeE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Florian Klinger on 27.07.16.
 */
public abstract class AbstractTrommelSort implements ISort<ITrommelE>, ActionListener {
    private boolean inOrder = true;

    protected boolean isInOrder() {
        return inOrder;
    }

    @Override
    public void setInOrder(boolean inOrder) {
        this.inOrder = inOrder;
    }

    protected int wendeAusgewaehlteOrderreihenfolgeAn(int x) {
        return (isInOrder() ? 1 : -1) * x;
    }

    protected boolean isAusserHaus(ITrommelE t) {
        return getBaustelle(t) != null;
    }

    protected String getBaustelle(ITrommelE t) {
        if (t != null)
            for (IStreckeE s : t.getStrecken()) {
                if (s.getEnde() < 0 || s.getStart() < 0) {
                    return s.getOrt();
                }
            }
        return null;
    }

    protected boolean isBeendet(ITrommelE t) {
        return t.isFreigemeldet() && getRestMeter(t) == 0;
    }

    protected int getAusleihMinuten(ITrommelE t) {
        List<IStreckeE> strecken = t.getStrecken();
        Collections.sort(strecken, new Comparator<IStreckeE>() {
            @Override
            public int compare(IStreckeE iStreckeE, IStreckeE t1) {
                return -((Long) iStreckeE.getVerlegedatum()).compareTo(t1.getVerlegedatum());
            }
        });
        if (strecken.size() == 0) {
            return 0;
        } else {
            return (int) ((System.currentTimeMillis() - strecken.get(0).getVerlegedatum()) / ((long) (1000 * 60)));
        }
    }

    protected int getRestMeter(ITrommelE trommel) {
        int laenge = trommel.getGesamtlaenge();
        for (IStreckeE s : trommel.getStrecken()) {
            if (s.getStart() >= 0 && s.getEnde() >= 0)
                laenge -= s.getMeter();
        }
        return laenge;
    }

    abstract public String getName();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractTrommelSort)) return false;

        AbstractTrommelSort that = (AbstractTrommelSort) o;

        return getName().equals(that.getName());

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            ConfigReader.getInstance().setTrommelSort(this.getClass().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

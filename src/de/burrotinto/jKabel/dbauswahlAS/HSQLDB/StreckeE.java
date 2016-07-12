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

package de.burrotinto.jKabel.dbauswahlAS.HSQLDB;

import de.burrotinto.jKabel.dbauswahlAS.enitys.IStreckeE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;

/**
 * Created by derduke on 20.05.16.
 */
class StreckeE implements IStreckeE {
    //(id IDENTITY, trommelid integer not null, ba INTEGER, ort VARCHAR(64), verlegedatum DATE , start INTEGER , ende INTEGER)
//     id, trommelid, ba, ort,verlegedatum,start,ende
    private int id, ba,start,ende;
    private String ort;
    private long verlegedatum;
    private ITrommelE trommel;

    StreckeE(int id, int ba, String ort, long verlegedatum, int start, int ende, ITrommelE trommel) {
        this.ba = ba;
        this.ende = ende;
        this.id = id;
        this.ort = ort;
        this.start = start;
        this.verlegedatum = verlegedatum;
        this.trommel=trommel;
    }

    StreckeE(int ba, String ort, long verlegedatum, int start, int ende, ITrommelE trommel) {
        this(-1, ba, ort, verlegedatum, start, ende, trommel);
    }

    @Override
    public String toString() {
        return "StreckeE{" +
                "id=" + id +
                ", ba=" + ba +
                ", start=" + start +
                ", ende=" + ende +
                ", ort='" + ort + '\'' +
                ", verlegedatum=" + verlegedatum +
                '}';
    }

    @Override
    public int getBa() {
        return ba;
    }

    @Override
    public void setBa(int ba) {
        this.ba = ba;
    }

    @Override
    public int getEnde() {
        return ende;
    }

    @Override
    public void setEnde(int ende) {
        this.ende = ende;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public long getVerlegedatum() {
        return verlegedatum;
    }

    @Override
    public void setVerlegedatum(long verlegedatum) {
        this.verlegedatum = verlegedatum;
    }

    @Override
    public String getOrt() {
        return ort;
    }

    @Override
    public void setOrt(String ort) {
        this.ort = ort;
    }

    @Override
    public int getTrommelID(){
        return trommel.getId();
    }

    @Override
    public int getMeter() {
        return Math.max(start,ende) - Math.min(start,ende);
    }

    @Override
    public int getId() {
        return id;
    }
}

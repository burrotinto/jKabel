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

package de.burrotinto.jKabel.dbauswahlAS.enitys;

/**
 * Created by derduke on 20.05.16.
 */
public abstract class AbstractTrommelE implements ITrommelE {
    private final IKabeltypE kabelTyp;
    private final int id;
    private int start;
    private String lagerPlatz;
    private String trommelnummer;
    private int gesamtlaenge;
    private boolean freigemeldet;

    protected AbstractTrommelE(int id, IKabeltypE kabelTyp) {
        this.kabelTyp = kabelTyp;
        this.id = id;
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
    public String getLagerPlatz() {
        return lagerPlatz;
    }

    @Override
    public void setLagerPlatz(String lagerPlatz) {
        this.lagerPlatz = lagerPlatz;
    }

    @Override
    public int getMaterialNummer() {
        return kabelTyp.getMaterialNummer();
    }

    @Override
    public int getGesamtlaenge() {
        return gesamtlaenge;
    }

    @Override
    public void setGesamtlaenge(int gesamtlaenge) {
        this.gesamtlaenge = gesamtlaenge;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTrommelnummer() {
        return trommelnummer;
    }

    @Override
    public void setTrommelnummer(String trommelnummer) {
        this.trommelnummer = trommelnummer;
    }

    @Override
    public boolean isFreigemeldet() {
        return freigemeldet;
    }

    @Override
    public String toString() {
        return "TrommelE{" +
            "id=" + id +
            ", trommelnummer='" + trommelnummer + '\'' +
            ", gesamtlaenge=" + gesamtlaenge +
            ", kabelTyp=" + kabelTyp.toString() +
            '}';
    }

    @Override
    public void setFreimeldung(boolean freigemeldet) {
        this.freigemeldet = freigemeldet;
    }

    @Override
    public IKabeltypE getTyp() {
        return kabelTyp;
    }

    @Override
    public int hashCode() {
        return getId();
    }
}

package de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IKabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.ITrommelE;

/**
 * Created by derduke on 20.05.16.
 */
class TrommelE implements ITrommelE {
    private final IKabeltypE kabelTyp;
    private  int start;
    private String lagerPlatz;
    private int id;
    private String trommelnummer;
    private int gesamtlaenge;
    private boolean freigemeldet;

    TrommelE(IKabeltypE kabelTyp, int id, String trommelnummer, int gesamtlaenge, String lagerPlatz, int start, boolean freigemeldet) {
        this.kabelTyp = kabelTyp;
        this.lagerPlatz = lagerPlatz;
        this.id = id;
        this.start = start;
        this.trommelnummer = trommelnummer;
        this.gesamtlaenge = gesamtlaenge;
        this.freigemeldet = freigemeldet;
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
}

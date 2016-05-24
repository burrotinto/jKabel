package de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis;

/**
 * Created by derduke on 23.05.16.
 */
public class GeliefertE {
    private  LieferantE lieferant;
    private  long datum;
    private String lieferscheinNr;
    private int lieferantID, trommelID;
    public GeliefertE(long datum, String lieferscheinNr,int lieferantID, int trommelID) {
        this.lieferantID = lieferantID;
        this.trommelID = trommelID;
        this.lieferscheinNr = lieferscheinNr;
        this.datum = datum;
    }

    public GeliefertE(long datum, String lieferscheinNr,int lieferantID) {
        this.lieferantID = lieferantID;
        this.trommelID = -1;
        this.lieferscheinNr = lieferscheinNr;
        this.datum = datum;
    }
    public long getDatum() {
        return datum;
    }

    public String getLieferscheinNr() {
        return lieferscheinNr;
    }

    public int getLieferantID() {
        return lieferantID;
    }

    public int getTrommelID() {
        return trommelID;
    }

    public void setLieferantID(int lieferantID) {
        this.lieferantID = lieferantID;
    }
}
